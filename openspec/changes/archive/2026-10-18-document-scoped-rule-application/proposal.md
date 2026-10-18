## Why

`Formatter` hides protected delimited blocks by splitting the document into runs and applying every rule per run. Rules therefore cannot tell a run boundary from a document boundary: a heading directly above or below a `----` block loses its required blank line, and a table containing a listing block loses its table state — the closing `|===` is then read as an opening fence and lines *after* the table are reformatted as cells.

## What Changes

- Rules are applied to the whole normalized document instead of per run. Protected lines are replaced by opaque, uniquely identified tokens before the rules run and restored from their originals afterwards.
- A protected line is therefore visible to rules only as a non-blank, content-free neighbour: it keeps blank-line decisions and block state correct, but can never be modified.
- Protected content is restored from the pre-format copy, not from rule output, so verbatim safety no longer depends on each rule remembering to skip it.
- Heading blank lines are now also enforced where a heading touches a delimited block, on either side, including its anchors.
- Table cell formatting continues across a protected block nested in a table; lines after the closing `|===` are no longer treated as table content.
- `SentencePerLineRule` gains the token guard so masked lines stay opaque to prose detection.
- Extend the existing `spec/headings/` and `spec/table/` fixtures and add formatter-level boundary tests (including idempotency).

## Capabilities

### New Capabilities

(none)

### Modified Capabilities

- `formatting-pipeline`: rules are applied document-scoped over masked protected lines; protected lines are restored from their originals.
- `heading-formatting`: blank-line separation applies when a heading is adjacent to a delimited block; a fence is not a document boundary.
- `table-formatting`: table detection spans protected blocks inside the table and cannot open a phantom table at a closing fence.

## Impact

- `src/main/java/de/bjoernerlwein/adocfmt/Formatter.java`: run partition replaced by mask/restore around the whole-document rule chain.
- `src/main/java/de/bjoernerlwein/adocfmt/SentencePerLineRule.java`: token guard in `NON_PROSE`.
- Tests/fixtures: `FormatterTest`, `HeadingBlankLineTest`, `TableCellSpacingTest`, `src/test/resources/spec/headings/`, `src/test/resources/spec/table/`.
- No CLI, rule-signature, dependency, or output-format changes. Additional AsciiDoc delimiters (`....`, `++++`, `////`) remain out of scope.
