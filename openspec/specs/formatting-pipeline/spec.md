# formatting-pipeline Specification

## Purpose

The formatting rule model: ordered rule application, pass-through default, and idempotency guarantees.

## Requirements

### Requirement: Ordered rule pipeline

The formatter SHALL apply an ordered collection of `FormatRule` instances and return the resulting text. Rules SHALL be applied only to text outside delimited blocks protected by `----` or `****` fences; protected segments (including their fence lines) SHALL pass through unchanged.

#### Scenario: Rules apply in order

- **WHEN** the formatter is given two rules and an input
- **THEN** the output equals applying the first rule to the input and the second rule to that result

#### Scenario: Protected segments bypass every rule

- **WHEN** the input contains a `----`-fenced or `****`-fenced block
- **THEN** no rule is applied to the lines between the fences, regardless of rule order

### Requirement: Pass-through default

With no rules configured, the formatter SHALL return the input unchanged except for line-ending normalization.

#### Scenario: No rules configured

- **WHEN** the formatter has an empty rule list and receives input text
- **THEN** the output equals the input with line endings normalized to `\n`

### Requirement: Idempotency

Formatting an already-formatted document SHALL produce the same document. Every rule SHALL satisfy `format(format(x)) == format(x)`.

#### Scenario: Second run changes nothing

- **WHEN** the formatter formats a document and then formats the result again
- **THEN** both results are equal

### Requirement: Line ending normalization

The formatter SHALL normalize all line endings to Unix `\n` (LF).

#### Scenario: CRLF input

- **WHEN** the input contains `\r\n` line endings
- **THEN** the output contains only `\n` line endings

### Requirement: Final newline untouched

The formatter SHALL NOT add or remove a final trailing newline.

#### Scenario: Input without trailing newline

- **WHEN** the input does not end with a newline
- **THEN** the output does not end with a newline

#### Scenario: Input with trailing newline

- **WHEN** the input ends with a single newline
- **THEN** the output ends with a single newline
