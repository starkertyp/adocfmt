## ADDED Requirements

### Requirement: Delimited block detection

The formatter SHALL treat a line as a delimited-block fence when the line, ignoring trailing whitespace, consists solely of four or more `-` characters. A fence line opens a protected block; the next fence line closes it.

#### Scenario: Opening fence is recognized

- **WHEN** a line consists solely of four or more `-` characters
- **THEN** the formatter treats every following line as protected until the next fence line

#### Scenario: Closing fence ends protection

- **WHEN** a protected block is open and another fence line is encountered
- **THEN** that fence closes the block and following lines are protected again only by a later fence

#### Scenario: Shorter hyphen run is not a fence

- **WHEN** a line consists of three or fewer `-` characters
- **THEN** the line is treated as ordinary content and does not open a block

### Requirement: Block content left unformatted

The formatter SHALL NOT modify any line inside a delimited block, even when that line would otherwise be changed by an active rule (headings, table cell markers, or any future rule).

#### Scenario: Heading-like line stays untouched

- **WHEN** a line beginning with `= ` lies inside a delimited block
- **THEN** the line is emitted exactly as written, with no blank line inserted around it

#### Scenario: Table-like line stays untouched

- **WHEN** a line containing `|` lies inside a delimited block
- **THEN** the line is emitted exactly as written, with cell spacing unchanged

#### Scenario: Blank lines inside the block are preserved

- **WHEN** the block contains blank lines
- **THEN** those blank lines are emitted unchanged

### Requirement: Fence lines emitted unchanged

The formatter SHALL emit each opening and closing fence line exactly as written and SHALL NOT apply any rule to it.

#### Scenario: Fence line is byte-for-byte identical

- **WHEN** the input contains a fence line
- **THEN** the output contains the same line with the same number of hyphens and spacing

### Requirement: Unclosed block protects to end of document

When a fence opens a block that is never closed, the formatter SHALL protect every remaining line through the end of the document.

#### Scenario: Missing closing fence

- **WHEN** the input contains an opening fence and no further fence line
- **THEN** every line after the opening fence is emitted unchanged

### Requirement: Fixture conformance

Formatting the file `src/test/resources/spec/verbatim/before.adoc` SHALL produce exactly the content of `src/test/resources/spec/verbatim/after.adoc`.

#### Scenario: Before fixture formats to after fixture

- **WHEN** the formatter formats the contents of `before.adoc`
- **THEN** the result equals the contents of `after.adoc`

#### Scenario: Verbatim fixture is idempotent

- **WHEN** the formatter formats the result of formatting `before.adoc` a second time
- **THEN** both results are equal
