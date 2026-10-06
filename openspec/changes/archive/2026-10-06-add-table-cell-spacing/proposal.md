## Why

Tables are the first structured AsciiDoc construct the formatter handles, and the existing `src/test/resources/spec/table` fixtures already document the expected cell spacing. Today no rule touches table cells, so the fixtures fail and the documented rule is unimplemented.

## What Changes

- Add a `TableCellSpacingRule` that operates inside `|===` table blocks:
  - a `|` at the start of a cell line is always followed by exactly one space;
  - an inline `|` (not at the start of the line) has a space before and after it.
- Leave the `|===` fence lines, blank lines within the table, and content outside table blocks untouched.
- Wire the existing `src/test/resources/spec/table` fixtures (`README.md`, `before.adoc`, `after.adoc`) into an automated test: formatting `before.adoc` must produce exactly `after.adoc`.
- Register the rule in `FormatRules.defaults()`.

## Capabilities

### New Capabilities

- `table-formatting`: the rule that table cell markers (`|`) inside `|===` blocks get consistent surrounding spaces, plus the fixture that defines its expected behavior.

### Modified Capabilities

<!-- none: `formatting-pipeline` semantics (ordered pipeline, pass-through, idempotency, line endings, final newline) are unchanged; only the default rule list grows. -->

## Impact

- Code: new rule implementation (`TableCellSpacingRule`) and one more entry in `FormatRules.defaults()`.
- Tests: a fixture-driven test reading `src/test/resources/spec/table/{before,after}.adoc`, plus unit tests for the rule edge cases and idempotency.
- No new dependencies; no CLI flag changes.
- Out of scope: column alignment/padding, row span handling, cell content reflow, and tables nested in verbatim/listing blocks.
