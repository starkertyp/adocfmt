## MODIFIED Requirements

### Requirement: Leading cell marker spacing

Inside a table, a cell line whose first character is `|` SHALL have exactly one space after that leading `|`, except when the line consists only of `|` followed by nothing or whitespace, in which case the line SHALL be emitted as `|` with no space after the marker.

#### Scenario: Leading marker without a space

- **WHEN** a table line begins with `|text`
- **THEN** it becomes `| text`

#### Scenario: Leading marker already spaced

- **WHEN** a table line already begins with `| text`
- **THEN** it is left unchanged

#### Scenario: Empty cell line has no trailing space

- **WHEN** a table line consists only of `|` with no content
- **THEN** it is emitted as `|` with no trailing space

#### Scenario: Empty cell line with only whitespace has no trailing space

- **WHEN** a table line consists of `|` followed only by spaces
- **THEN** it is emitted as `|` with no trailing space
