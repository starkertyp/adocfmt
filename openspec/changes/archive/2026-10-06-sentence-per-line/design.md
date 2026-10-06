## Context

The pipeline (`Formatter`) normalizes line endings, withholds delimited `----` blocks, and applies an ordered list of `FormatRule`s over the whole text, with no shared block context. The existing `HeadingBlankLineRule` and `TableCellSpacingRule` are both line-based and carry `ponytail:` notes that block-sensitive rules need a segmentation upgrade. This change adds `SentencePerLineRule`, whose expected behavior is fixed by `src/test/resources/spec/sentence-per-line/{before,after}.adoc`.

## Goals / Non-Goals

**Goals:**

- Implement one rule: split plain prose paragraphs into one sentence per line and guarantee a trailing period.
- Keep the rule pure and idempotent.
- Tie the rule to the `before.adoc`/`after.adoc` fixtures with an automated test.
- Restrict the rule to plain prose so structural AsciiDoc is untouched.

**Non-Goals:**

- Handling line endings, delimited blocks, tables, or lists (other rules/capabilities own these).
- Detecting abbreviations such as `z.B.` or `u.s.w.` followed by an uppercase word.
- Rewriting prose that contains no period at all (no sentence boundary to split on) beyond appending a trailing period.
- Reflowing wrapped non-sentence line breaks inside a sentence beyond joining and re-splitting at sentence boundaries.
- Any CLI or configuration surface.

## Decisions

### Prose-line classifier, not a full AsciiDoc parser

The rule classifies each line by exclusion: a line is prose unless it matches a known structural prefix (heading, list item, table line, block macro, attribute entry, block attribute, anchor, line comment). This mirrors the existing line-based rules and keeps block awareness local to the rule. Alternatives considered:

- **Parse AsciiDoc blocks properly** — out of reach for the current line-based pipeline and far more code than the feature needs. Rejected.
- **Treat every non-blank line as prose** — would mangle headings and list items. Rejected per the agreed scope.

The classifier MUST run before any splitting, so structural lines pass through untouched.

### Paragraph as consecutive prose lines, joined then split

A paragraph is a maximal run of consecutive prose lines. The rule joins the run with single spaces, detects sentence boundaries, and emits one sentence per line. Joining handles sentences wrapped across source lines; splitting handles multiple sentences on one line. Alternative considered: **split each line independently without joining** — simpler, but leaves a wrapped sentence split across lines, violating "one sentence per line". Rejected.

### Sentence boundary regex

A boundary exists after a period that is followed by one or more spaces and an uppercase letter (`\.(?=\s+[A-ZÄÖÜ])`), or after a period at the end of the paragraph. Splitting preserves the period. Periods followed by a lowercase word, by a digit sequence (`3.14`), or by a quote/bracket (`."`, `.)`) do not split.

### Trailing period

After splitting, if the final emitted sentence does not end with `.`, append one. This is applied to the paragraph's last sentence only, so an already-terminated paragraph is unchanged.

### Idempotency

After formatting, each line holds one sentence ending in `.` and paragraphs end in `.`. Joining such lines again reproduces the same logical text, and the boundary regex finds no new interior boundary to split (each line ends at a period followed by a line break, not whitespace + uppercase). Therefore `format(format(x)) == format(x)` holds.

### Capability tests run their own rule, not the default list

A global prose rule changes the output of existing fixtures and unit tests (for example `spec/headings` contains `more text`, which the new rule termintes with a period). To avoid coupling, each capability test class constructs the `Formatter` with only the rule it verifies: `HeadingBlankLineTest` with `HeadingBlankLineRule`, `TableCellSpacingTest` with `TableCellSpacingRule`, and `DelimitedBlockProtectionTest` with `HeadingBlankLineRule` + `TableCellSpacingRule` (the rules that would otherwise mutate block content). Delimited-block protection lives in `Formatter` itself and continues to apply. Alternative considered: **update the existing fixtures to the new global output** — more diff and mixes sentence behavior into the heading/verbatim specs. Rejected.

### Fixture-driven test

`SentencePerLineTest` reads `before.adoc`/`after.adoc` from the classpath, formats `before` with a `Formatter` holding only `SentencePerLineRule`, and asserts equality with `after`. Unit scenarios cover: multi-sentence single line, wrapped sentence, missing trailing period, abbreviation, decimal, and each structural prefix left unchanged.

## Risks / Trade-offs

- **Abbreviation followed by an uppercase word** (`z.B. Der`) splits incorrectly. → Intentional ceiling of the regex; documented and left to a future abbreviation list.
- **Sentence end followed by a quote or bracket** (`."`) is not split. → Regex currently requires whitespace directly after the period; documented.
- **Structural prefixes missing from the classifier** (e.g. admonition prefixes) may be treated as prose. → Classifier list is explicit and additive; extend when a real document needs it. Marked with a `ponytail:` comment naming the ceiling.
- **Joining reflows wrapped prose**. → Intended: it is what produces "one sentence per line"; lines without an internal sentence boundary are still joined into one line.

## Migration Plan

None — additive rule; no existing formatted output to migrate.

## Open Questions

- Should an abbreviation list (or a "single period only at end" heuristic) be added? Not covered by the fixture; out of scope, add when a real document needs it.
