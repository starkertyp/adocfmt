# blank-line-collapsing Specification

## Purpose

Collapse runs of consecutive empty lines into a single empty line, while leaving whitespace-only lines and delimited block content untouched.

## Requirements

### Requirement: Consecutive empty lines collapse to one

The formatter SHALL replace every run of two or more consecutive empty lines with exactly one empty line. A single empty line SHALL be preserved unchanged, and non-empty lines SHALL be emitted unchanged.

An "empty line" is a line with zero characters. A line containing only whitespace is not an empty line and SHALL be emitted unchanged.

#### Scenario: Two empty lines become one

- **WHEN** two empty lines separate two non-empty lines
- **THEN** the output contains exactly one empty line between them

#### Scenario: A long run becomes one

- **WHEN** five consecutive empty lines are given
- **THEN** the output contains exactly one empty line

#### Scenario: A single empty line is preserved

- **WHEN** a single empty line separates two non-empty lines
- **THEN** the output is unchanged

#### Scenario: Whitespace-only line is untouched

- **WHEN** a line contains only spaces or tabs
- **THEN** the line is emitted unchanged and does not participate in collapsing

### Requirement: Trailing empty lines

A run of empty lines at the end of the input SHALL collapse to at most one empty line, and the presence or absence of a final trailing newline SHALL be preserved.

#### Scenario: Two trailing empty lines become one

- **WHEN** the input ends with two empty lines after the last non-empty line
- **THEN** the output ends with a single empty line

#### Scenario: Final newline preserved

- **WHEN** the input ends with a newline
- **THEN** the output ends with a newline

#### Scenario: No trailing newline preserved

- **WHEN** the input does not end with a newline
- **THEN** the output does not end with a newline

### Requirement: Delimited block content is not collapsed

Lines inside delimited `----` blocks SHALL pass through the pipeline unchanged, so an empty-line run inside such a block SHALL NOT be collapsed.

#### Scenario: Empty lines inside a listing block

- **WHEN** a `----` block contains two consecutive empty lines
- **THEN** the output block still contains both empty lines

### Requirement: Fixture conformance

Formatting the file `src/test/resources/spec/blank-lines/before.adoc` SHALL produce exactly the content of `src/test/resources/spec/blank-lines/after.adoc`.

#### Scenario: Before fixture formats to after fixture

- **WHEN** the formatter formats the contents of `before.adoc`
- **THEN** the result equals the contents of `after.adoc`

#### Scenario: Blank-line fixture is idempotent

- **WHEN** the formatter formats the result of formatting `before.adoc` a second time
- **THEN** both results are equal

### Requirement: Rule idempotency

Applying the rule twice SHALL produce the same result as applying it once. After the first application no run of two or more consecutive empty lines remains, so the second application SHALL make no change.

#### Scenario: Second application is a no-op

- **WHEN** the rule is applied to already-collapsed text
- **THEN** the output equals the input
