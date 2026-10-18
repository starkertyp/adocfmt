## MODIFIED Requirements

### Requirement: Table block detection

The formatter SHALL treat lines as being inside a table only when they lie between a `|===` opening fence and the next `|===` closing fence. The fence lines themselves SHALL NOT be modified. Table state SHALL persist across delimited blocks protected by `----` or `****` fences nested inside the table: a protected line SHALL neither open nor close a table, and a `|===` line inside a protected block SHALL NOT be interpreted as a fence. Lines after the closing `|===` SHALL NOT be treated as table content.

#### Scenario: Cell spacing inside a table

- **WHEN** a `|`-containing line lies between two `|===` lines
- **THEN** the cell-spacing rules are applied to that line

#### Scenario: Cell-spacing characters outside a table

- **WHEN** a line containing `|` is not between two `|===` lines
- **THEN** the line is left unchanged

#### Scenario: Fence lines are left untouched

- **WHEN** a line is exactly a `|===` fence
- **THEN** the fence line is emitted unchanged

#### Scenario: Protected block nested inside a table

- **WHEN** a `----` block lies between two cells of a table
- **THEN** the block content is emitted unchanged and cells after the block are still spaced as table cells

#### Scenario: Closing fence after a protected block closes the table

- **WHEN** a table contains a protected block before its closing `|===` and a `|`-containing line follows that fence
- **THEN** the table is closed by that fence and the following line is not spaced as a cell
