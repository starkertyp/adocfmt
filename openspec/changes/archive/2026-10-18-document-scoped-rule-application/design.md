## Context

`Formatter` currently partitions the normalized document into alternating unprotected and protected runs and applies the whole rule chain to each unprotected run. Protected runs are emitted verbatim. This was the cheapest way to give rules a fence-free `String` without changing `FormatRule`, but it introduces a boundary that rules cannot see:

- `HeadingBlankLineRule` treats the first and last line of its input as the document edges, so a heading touching a fence gets no separating blank line and an anchor above such a heading is treated as the document start.
- `TableCellSpacingRule` keeps `inTable` in local state, so a listing block inside a table resets it; the closing `|===` is then read as an opening fence and subsequent lines are reformatted as cells.

Both are symptoms of one contract mismatch: `FormatRule` receives "the text", but the Formatter passes a fragment.

## Goals / Non-Goals

**Goals:**

- Rules see the whole document, including protected lines as opaque neighbours.
- Protected content is byte-for-byte preserved without every rule having to know about fences.
- Keep `FormatRule` as `String -> String` and keep all existing rule-level tests valid.
- Fix heading/anchor block-boundary behaviour and table state across protected blocks.
- Preserve output semantics: line endings, trailing newline, diff/check behaviour.

**Non-Goals:**

- Expanding protection to `....`, `++++`, `////`, `====`, or `--` delimiters.
- Table column alignment or any other cross-line layout.
- A block/AST model or a changed rule interface.
- Changing fence detection rules (four or more `-` or `*`, same delimiter closes).

## Decisions

**Mask protected lines instead of splitting runs.** Before rules run, each protected line (fence lines and block content) is replaced by a content-free token line `\u0000<index>\u0000`. After the rule chain, each token line is replaced by its original line. Rules now see one continuous document where a protected block is a run of non-blank, inert neighbour lines.

Alternatives considered:

- *Segment model* (`List<Segment>` or a `Document` type): correct by construction and the natural endpoint for block-aware formatting, but changes every rule and every test. Not worth it for four rules.
- *Per-rule context flags* (`atDocStart`/`atDocEnd`, initial table state): two special cases for the same root cause, and it forces `TableCellSpacingRule` to re-implement fence tracking. Rejected as the option that looks small but accumulates.
- *Placeholder block replaced by a single line*: loses per-line positions and confuses blank-run handling; per-line tokens are simpler.

**Tokens are content-free and restored from originals.** The token carries only an index, not the line content, so a rule cannot leak or corrupt protected text through it. Restoration ignores whatever a rule did to the token line and substitutes the original. This keeps verbatim safety by construction even if a rule forgets to be fence-aware.

**Only `SentencePerLineRule` needs an explicit guard.** `CollapseBlankLinesRule` only treats empty lines specially, `HeadingBlankLineRule` matches `^=+ ` / anchors, and `TableCellSpacingRule` only acts on lines containing `|`; a token line matches none of them. `SentencePerLineRule` would classify a token as prose and merge it into a paragraph, so `NON_PROSE` gets a `^\u0000` alternative.

**Fail on token loss.** If the number of restored tokens differs from the number of masked lines, a rule has dropped or duplicated a protected line. The formatter throws `IllegalStateException` instead of silently emitting an incomplete document. This is a guard at the rule boundary, not a user-facing error path.

**No changes to the heading or table rules.** Their existing lookback/scan logic is correct once they see the document rather than a fragment; the fix is deliberately confined to the pipeline.

## Risks / Trade-offs

- [A rule could mutate a token line's surrounding text (e.g. split it) and lose the index] → Only `SentencePerLineRule` can, and its guard prevents it; the restore-count check catches regressions.
- [Input text containing U+0000 collides with the token format] → Not valid content in real AsciiDoc; documented as a `ponytail:` ceiling at the mask definition.
- [Adding a future rule that treats any non-blank line as content] → It will see inert non-blank lines, which is the intended document-scoped semantics; it must not assume every non-blank line has text.
- [Masked lines keep blank-line runs from spanning a block] → Correct: blank lines inside a block are protected content, and rules already never collapsed across a protected run.
