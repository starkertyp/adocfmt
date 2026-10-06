## ADDED Requirements

### Requirement: Heading detection

The formatter SHALL treat a line as a heading when it starts with one or more `=` characters immediately followed by a space (for example `= Title`, `== Section`, `=== Subsection`).

#### Scenario: Caret starts with equals and a space

- **WHEN** a line begins with `= `, `== `, or more `=` characters followed by a space
- **THEN** the line is treated as a heading

#### Scenario: Equals without a following space

- **WHEN** a line begins with `=` but the character after the equals run is not a space
- **THEN** the line is treated as ordinary text and the rule leaves it unchanged

### Requirement: Blank line after every heading

The formatter SHALL ensure that a heading line is directly followed by a blank line. If the next line is not blank, the formatter SHALL insert a blank line between the heading and the next line.

#### Scenario: Heading followed by text

- **WHEN** a heading is directly followed by a non-blank line
- **THEN** a blank line is inserted between the heading and that line

#### Scenario: Heading followed by another heading

- **WHEN** a heading is directly followed by another heading
- **THEN** a blank line is inserted between the two headings

#### Scenario: Heading already followed by a blank line

- **WHEN** a heading is already followed by a blank line
- **THEN** the text is left unchanged at that position

#### Scenario: Heading is the last line

- **WHEN** a heading is the final line of the document
- **THEN** no blank line is appended after it

### Requirement: Fixture conformance

Formatting the file `src/test/resources/spec/headings/before.adoc` SHALL produce exactly the content of `src/test/resources/spec/headings/after.adoc`.

#### Scenario: Before fixture formats to after fixture

- **WHEN** the formatter formats the contents of `before.adoc`
- **THEN** the result equals the contents of `after.adoc`
