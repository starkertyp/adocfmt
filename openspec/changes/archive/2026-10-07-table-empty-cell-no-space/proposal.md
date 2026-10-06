## Why

Inside a table, a line consisting solely of `|` (an empty cell row, with optional trailing whitespace) currently has a trailing space appended (`| `). That trailing whitespace conflicts with other formatters that strip it, producing churn.

## What Changes

- In a table block, a line that consists only of `|` followed by nothing or whitespace is emitted as `|` with no trailing space.
- All other cell-spacing behavior is unchanged.
- Update the `table` fixture and unit tests to cover the empty-cell line.

## Capabilities

### New Capabilities

<!-- none -->

### Modified Capabilities

- `table-formatting`: leading cell marker spacing gains an exception — an empty cell line (`|` plus optional whitespace) is emitted as `|` with no space after it.

## Impact

- `src/main/java/adocfmt/TableCellSpacingRule.java`.
- `src/test/resources/spec/table/` fixtures and `TableCellSpacingTest`.
- No CLI, API, or dependency changes.
