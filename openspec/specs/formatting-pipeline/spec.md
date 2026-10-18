# formatting-pipeline Specification

## Purpose

The formatting rule model: ordered rule application, pass-through default, and idempotency guarantees.

## Requirements

### Requirement: Ordered rule pipeline

The formatter SHALL apply an ordered collection of `FormatRule` instances to the whole normalized document and return the resulting text. Before the rules run, every line of a delimited block protected by `----` or `****` fences SHALL be replaced by an opaque, content-free token line; after the rules run, each token line SHALL be replaced by its original line. Rules SHALL therefore observe protected lines as non-blank, inert neighbours rather than as document boundaries.

#### Scenario: Rules apply in order

- **WHEN** the formatter is given two rules and an input
- **THEN** the output equals applying the first rule to the input and the second rule to that result

#### Scenario: Protected content bypasses every rule

- **WHEN** the input contains a `----`-fenced or `****`-fenced block
- **THEN** the rule output for a protected line never reaches the output, regardless of rule order

#### Scenario: Protected lines are neighbours, not edges

- **WHEN** an unprotected line directly precedes or follows a protected block
- **THEN** rules observe the protected line as a non-blank line at the document's position, not as the start or end of the document

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

### Requirement: Protected line restoration

The formatter SHALL restore each masked protected line from the pre-format original, matched by its token index. If the number of restored lines does not equal the number of masked lines, the formatter SHALL fail with an error instead of emitting a document missing protected content.

#### Scenario: Rule output cannot alter protected content

- **WHEN** a rule modifies a token line's text
- **THEN** the original protected line is emitted unchanged

#### Scenario: Lost protected line is reported

- **WHEN** a rule removes or duplicates a token line
- **THEN** the formatter throws an error rather than returning a document with missing or extra protected content
