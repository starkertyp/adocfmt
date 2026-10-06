## MODIFIED Requirements

### Requirement: Leading cell marker spacing

Inside a table, a cell line that begins with `|`, or that begins with the `a` cell specifier written as `a|`, SHALL have exactly one space after the marker. For a bare leading marker this SHALL be `| `; for an `a` specifier the `a` SHALL remain directly attached to the `|` with no space between them, and exactly one space SHALL follow the `|`. Exception: when the line consists only of `|` (or `a|`) followed by nothing or whitespace, the line SHALL be emitted as `|` (or `a|`) with no space after the marker.

#### Scenario: Leading marker without a space

- **WHEN** a table line begins with `|text`
- **THEN** it becomes `| text`

#### Scenario: Leading marker already spaced

- **WHEN** a table line already begins with `| text`
- **THEN** it is left unchanged

#### Scenario: Leading a specifier without a space

- **WHEN** a table line begins with `a|text`
- **THEN** it becomes `a| text` with the `a` attached to the `|`

#### Scenario: Leading a specifier already spaced

- **WHEN** a table line already begins with `a| text`
- **THEN** it is left unchanged

#### Scenario: Empty cell line has no trailing space

- **WHEN** a table line consists only of `|` with no content
- **THEN** it is emitted as `|` with no trailing space

#### Scenario: Empty cell line with only whitespace has no trailing space

- **WHEN** a table line consists of `|` followed only by spaces
- **THEN** it is emitted as `|` with no trailing space

#### Scenario: Empty a specifier cell line has no trailing space

- **WHEN** a table line consists only of `a|` with no content
- **THEN** it is emitted as `a|` with no trailing space

### Requirement: Inline cell marker spacing

Inside a table, every `|` cell marker that is not the first character of the line SHALL have exactly one space before and after it. A line MAY contain multiple inline markers. A `|` that forms part of a leading `a` cell specifier at the start of the line is exempt and is governed by the leading cell marker spacing requirement instead.

#### Scenario: Inline marker without spaces

- **WHEN** a table line contains `a|b`
- **THEN** it becomes `a | b`

#### Scenario: Mixed leading and inline markers

- **WHEN** a table line is `|heading|1|2|3`
- **THEN** it becomes `| heading | 1 | 2 | 3`

#### Scenario: Inline marker already spaced

- **WHEN** a table line already contains ` | `
- **THEN** the spacing around that marker is left unchanged

#### Scenario: Leading a specifier is not treated as an inline marker

- **WHEN** a table line begins with `a|text |more`
- **THEN** it becomes `a| text | more`, leaving the leading `a|` attached and spacing only the subsequent marker
