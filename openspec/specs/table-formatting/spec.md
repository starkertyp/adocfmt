# table-formatting Specification

## Purpose

Spacing normalization of `|` cell markers inside AsciiDoc table blocks delimited by `|===` fences.

## Requirements

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
