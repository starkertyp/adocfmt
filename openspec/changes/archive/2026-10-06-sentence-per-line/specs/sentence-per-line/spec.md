## ADDED Requirements

### Requirement: Prose paragraph detection

The formatter SHALL treat a run of consecutive non-blank "prose" lines as a single prose paragraph. A line SHALL be considered prose only when it is not any of the following: a heading (`=+ `), a list item, a table fence or cell line (leading `|`), a block macro (`name::`), an attribute entry (`:name:`), a block attribute line (`[...]`), an anchor (`[[...]]`), or a line comment (`//`). Lines inside delimited `----` blocks are already withheld by the pipeline and are never prose.

#### Scenario: Consecutive prose lines form one paragraph

- **WHEN** two or more consecutive non-blank prose lines are given
- **THEN** they are treated as one paragraph for sentence splitting

#### Scenario: A blank line ends a paragraph

- **WHEN** a blank line separates two runs of prose lines
- **THEN** each run is treated as a separate paragraph

#### Scenario: Structural lines are not prose

- **WHEN** a line is a heading, list item, table line, block macro, attribute entry, block attribute, anchor, or line comment
- **THEN** the line is emitted unchanged

### Requirement: Sentence boundary detection

A sentence boundary SHALL be a period immediately followed by whitespace and then an uppercase letter, or a period at the end of the paragraph. Periods not matching this rule (for example in `z.B.`, `3.14`, a URL, or before a quote or bracket) SHALL NOT create a boundary.

#### Scenario: Period followed by an uppercase word

- **WHEN** a paragraph contains `Erster Satz. Zweiter Satz`
- **THEN** the boundary is placed between `Satz.` and `Zweiter`

#### Scenario: Period before a lowercase word is not a boundary

- **WHEN** a paragraph contains `z.B. ein Beispiel`
- **THEN** no boundary is placed and the text stays on one line

#### Scenario: Decimal number is not a boundary

- **WHEN** a paragraph contains `3.14 ist Pi`
- **THEN** no boundary is placed at the decimal point

### Requirement: One sentence per line

Each detected sentence within a prose paragraph SHALL be emitted on its own line. No blank line SHALL be inserted between the sentence lines, and the lines SHALL remain part of the same paragraph.

#### Scenario: Single-line paragraph with two sentences

- **WHEN** the paragraph `Erster Satz. Zweiter Satz.` is given
- **THEN** the output is two lines `Erster Satz.` and `Zweiter Satz.` with no blank line between

#### Scenario: Sentence wrapped across source lines

- **WHEN** a paragraph is split across source lines in the middle of a sentence
- **THEN** the sentences are re-emitted each on a single line

#### Scenario: Already one sentence per line

- **WHEN** each line of a paragraph already contains exactly one sentence
- **THEN** the lines are emitted unchanged

### Requirement: Trailing period guaranteed

A prose paragraph that does not already end with a period SHALL have a period appended to its final sentence.

#### Scenario: Paragraph without a final period

- **WHEN** the paragraph ends with `kein Punkt hier`
- **THEN** the last sentence is emitted as `kein Punkt hier.`

#### Scenario: Paragraph already ending with a period

- **WHEN** the paragraph already ends with a period
- **THEN** no additional period is appended

### Requirement: Fixture conformance

Formatting the file `src/test/resources/spec/sentence-per-line/before.adoc` SHALL produce exactly the content of `src/test/resources/spec/sentence-per-line/after.adoc`.

#### Scenario: Before fixture formats to after fixture

- **WHEN** the formatter formats the contents of `before.adoc`
- **THEN** the result equals the contents of `after.adoc`

#### Scenario: Sentence fixture is idempotent

- **WHEN** the formatter formats the result of formatting `before.adoc` a second time
- **THEN** both results are equal

### Requirement: Rule idempotency

Applying the rule twice SHALL produce the same result as applying it once. After the first application each sentence already occupies its own line and the paragraph already ends with a period, so the second application SHALL make no change.

#### Scenario: Second application is a no-op

- **WHEN** the rule is applied to already-formatted prose
- **THEN** the output equals the input
