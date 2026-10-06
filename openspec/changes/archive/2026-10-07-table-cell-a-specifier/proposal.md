## Why

In AsciiDoc a table cell can be introduced by the `a|` cell specifier (AsciiDoc cell content) instead of a bare `|`. The formatter currently inserts a space before every inline marker, so `a|text` becomes `a | text`, which silently turns the specifier into ordinary cell text and breaks the table.

## What Changes

- Inside a table, an `a` immediately followed by `|` (an `a|` cell specifier) SHALL keep the `a` attached to the `|`; only a single space is ensured after the `|`.
- This applies both when the line starts with `a|` and when `a|` appears after another cell marker within a row.
- Other letters directly before `|` keep the current behavior (`jayjay|` still becomes `jayjay |`); only the `a` specifier is recognized.
- All other cell-spacing behavior is unchanged.
- Update the `table` fixture and unit tests to cover the `a|` specifier.

## Capabilities

### New Capabilities

<!-- none -->

### Modified Capabilities

- `table-formatting`: marker spacing must not insert a space before a `|` that is immediately preceded by the `a` cell specifier.

## Impact

- `src/main/java/adocfmt/TableCellSpacingRule.java`.
- `src/test/resources/spec/table/` fixtures and `TableCellSpacingTest`.
- No CLI, API, or dependency changes.
