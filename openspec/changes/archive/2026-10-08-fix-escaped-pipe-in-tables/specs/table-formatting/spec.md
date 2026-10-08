## ADDED Requirements

### Requirement: Escaped pipe is not a cell marker

Inside a table, a `|` that is directly preceded by a backslash (`\|`) SHALL be treated as an escaped literal pipe, not as a cell marker. Escaped pipes SHALL be emitted unchanged: the rule SHALL NOT insert, remove, or normalize whitespace between the backslash and the pipe. The backslash preceding the pipe SHALL also be left unchanged. Unescaped `|` markers on the same line SHALL still be spaced normally.

#### Scenario: Inline command with escaped pipe in a cell

- **WHEN** a table line contains `` `oc get configmap -o yaml \| grep ...` ``
- **THEN** the escaped `\|` is left unchanged and no space is inserted between `\` and `|`

#### Scenario: Escaped pipe between real markers

- **WHEN** a table line contains `\|` between unescaped `|` markers, e.g. `|a \| b|c`
- **THEN** the unescaped markers are spaced (`| a \| b | c`) and the `\|` remains untouched

#### Scenario: Escaped pipe is idempotent

- **WHEN** a line containing `\|` is formatted a second time
- **THEN** both results are equal

#### Scenario: Unescaped markers on the same line are still spaced

- **WHEN** a table line contains both an escaped pipe and an unescaped cell marker, e.g. `|cmd \| grep|next`
- **THEN** the unescaped markers are spaced normally while `\|` remains unchanged

## MODIFIED Requirements

### Requirement: Inline cell marker spacing

Inside a table, every `|` cell marker that is not the first character of the line SHALL have exactly one space before and after it. A line MAY contain multiple inline markers. A `|` that forms part of a leading `a` cell specifier at the start of the line is exempt and is governed by the leading cell marker spacing requirement instead. A `|` preceded by a backslash (`\|`) is not a cell marker and is exempt; it is governed by the escaped-pipe requirement instead.

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
