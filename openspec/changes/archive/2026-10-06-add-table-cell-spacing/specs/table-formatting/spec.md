## ADDED Requirements

### Requirement: Table block detection

The formatter SHALL treat lines as being inside a table only when they lie between a `|===` opening fence and the next `|===` closing fence. The fence lines themselves SHALL NOT be modified.

#### Scenario: Cell spacing inside a table

- **WHEN** a `|`-containing line lies between two `|===` lines
- **THEN** the cell-spacing rules are applied to that line

#### Scenario: Cell-spacing characters outside a table

- **WHEN** a line containing `|` is not between two `|===` lines
- **THEN** the line is left unchanged

#### Scenario: Fence lines are left untouched

- **WHEN** a line is exactly a `|===` fence
- **THEN** the fence line is emitted unchanged

### Requirement: Leading cell marker spacing

Inside a table, a cell line whose first character is `|` SHALL have exactly one space after that leading `|`.

#### Scenario: Leading marker without a space

- **WHEN** a table line begins with `|text`
- **THEN** it becomes `| text`

#### Scenario: Leading marker already spaced

- **WHEN** a table line already begins with `| text`
- **THEN** it is left unchanged

### Requirement: Inline cell marker spacing

Inside a table, every `|` cell marker that is not the first character of the line SHALL have exactly one space before and after it. A line MAY contain multiple inline markers.

#### Scenario: Inline marker without spaces

- **WHEN** a table line contains `a|b`
- **THEN** it becomes `a | b`

#### Scenario: Mixed leading and inline markers

- **WHEN** a table line is `|heading|1|2|3`
- **THEN** it becomes `| heading | 1 | 2 | 3`

#### Scenario: Inline marker already spaced

- **WHEN** a table line already contains ` | `
- **THEN** the spacing around that marker is left unchanged

### Requirement: Non-cell table content untouched

Inside a table, blank lines and lines that contain no cell marker SHALL be emitted unchanged, so multi-line cell content and blank separators are preserved.

#### Scenario: Blank line inside a table

- **WHEN** a blank line lies between two `|===` fences
- **THEN** the blank line is emitted unchanged

#### Scenario: Continuation line without a marker

- **WHEN** a non-blank line inside a table contains no `|`
- **THEN** the line is emitted unchanged

### Requirement: Fixture conformance

Formatting the file `src/test/resources/spec/table/before.adoc` SHALL produce exactly the content of `src/test/resources/spec/table/after.adoc`.

#### Scenario: Before fixture formats to after fixture

- **WHEN** the formatter formats the contents of `before.adoc`
- **THEN** the result equals the contents of `after.adoc`

#### Scenario: Table fixture is idempotent

- **WHEN** the formatter formats the result of formatting `before.adoc` a second time
- **THEN** both results are equal
