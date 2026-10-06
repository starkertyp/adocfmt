# sentence-per-line Specification

## Purpose

Detect prose paragraphs and split each into one sentence per line, with a trailing period guaranteed.

## Requirements

### Requirement: Prose paragraph detection

The formatter SHALL treat a run of consecutive non-blank "prose" lines as a single prose paragraph. A line SHALL be considered prose only when it is not any of the following: a heading (`=+ `), a list item, a table fence or cell line (leading `|` or the `a|` cell specifier), a block macro (`name::`), an attribute entry (`:name:`), a block attribute line (`[...]`), an anchor (`[[...]]`), a block title or caption (leading `.`), a control line consisting only of one or more `<` characters, or a line comment (`//`). Lines inside delimited `----` blocks are already withheld by the pipeline and are never prose.

#### Scenario: Consecutive prose lines form one paragraph

- **WHEN** two or more consecutive non-blank prose lines are given
- **THEN** they are treated as one paragraph for sentence splitting

#### Scenario: A blank line ends a paragraph

- **WHEN** a blank line separates two runs of prose lines
- **THEN** each run is treated as a separate paragraph

#### Scenario: Structural lines are not prose

- **WHEN** a line is a heading, list item, table line, block macro, attribute entry, block attribute, anchor, block title, control line, or line comment
- **THEN** the line is emitted unchanged

#### Scenario: Leading a cell specifier line is not prose

- **WHEN** a table line begins with `a| include::../version.txt[]`
- **THEN** the line is emitted unchanged with no period appended

#### Scenario: Leading a cell specifier stays separate from prose

- **WHEN** a line beginning with `a|` is adjacent to a prose paragraph
- **THEN** the `a|` line is emitted unchanged and is not merged into, split with, or given a trailing period as part of the paragraph

#### Scenario: Caption line stays separate from prose

- **WHEN** a line beginning with `.` is followed by a prose paragraph
- **THEN** the caption line is emitted unchanged and is not merged into, split with, or given a trailing period as part of the paragraph

#### Scenario: Caption line is idempotent

- **WHEN** the rule is applied to an already-formatted caption line
- **THEN** the line is emitted unchanged

#### Scenario: Control line stays separate from prose

- **WHEN** a line consisting only of `<` characters is followed by a prose paragraph
- **THEN** the control line is emitted unchanged and is not merged into, split with, or given a trailing period as part of the paragraph

#### Scenario: Control line is idempotent

- **WHEN** the rule is applied to an already-formatted control line
- **THEN** the line is emitted unchanged

### Requirement: Sentence boundary detection

A sentence boundary SHALL be a period immediately followed by whitespace and then an uppercase letter, or a period at the end of the paragraph. A period that is part of a common German abbreviation SHALL NOT create a boundary, even when it is followed by whitespace and an uppercase letter. Periods not matching this rule (for example in `3.14`, a URL, or before a quote or bracket) SHALL NOT create a boundary either.

Common German abbreviations are the period-bearing entries of the CharLingua reference list (<https://www.charlingua.de/post/abkuerzungen>) and SHALL be recognized case-insensitively. This includes both single-token abbreviations (`usw.`, `bzw.`, `ca.`, `evtl.`, `ggf.`, `inkl.`, `Abs.`, `Art.`, `Dr.`, `Jan.`, ...) and multi-part abbreviations of the form letter-dot-letter (`z.B.`, `d.h.`, `i.D.`, `m.a.W.`, `o.ä.`, ...).

#### Scenario: Period followed by an uppercase word

- **WHEN** a paragraph contains `Erster Satz. Zweiter Satz`
- **THEN** the boundary is placed between `Satz.` and `Zweiter`

#### Scenario: Period before a lowercase word is not a boundary

- **WHEN** a paragraph contains `z.B. ein Beispiel`
- **THEN** no boundary is placed and the text stays on one line

#### Scenario: Multi-part abbreviation before an uppercase word is not a boundary

- **WHEN** a paragraph contains `z.B. Der folgende Text`
- **THEN** no boundary is placed after `z.B.` and the text stays on one line

#### Scenario: Single-token abbreviation before an uppercase word is not a boundary

- **WHEN** a paragraph contains `usw. Das gilt weiter`
- **THEN** no boundary is placed after `usw.` and the text stays on one line

#### Scenario: Abbreviation is matched case-insensitively

- **WHEN** a paragraph contains `D.h. Das gilt`
- **THEN** no boundary is placed after `D.h.` and the text stays on one line

#### Scenario: Abbreviation does not prevent a boundary after the next real sentence

- **WHEN** a paragraph contains `usw. Der Satz endet hier. Ein neuer Satz`
- **THEN** the only boundary is after `hier.` between `hier.` and `Ein`

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

A prose paragraph that does not already end with a period SHALL have a period appended to its final sentence, except when the paragraph's final character is a colon (`:`), in which case the paragraph SHALL be emitted unchanged. When deciding whether the paragraph already ends with a period or colon, trailing whitespace SHALL be ignored, including Unicode whitespace that `String.trim()` does not remove (for example `U+00A0` and `U+2003`).

#### Scenario: Paragraph without a final period

- **WHEN** the paragraph ends with `kein Punkt hier`
- **THEN** the last sentence is emitted as `kein Punkt hier.`

#### Scenario: Paragraph already ending with a period

- **WHEN** the paragraph already ends with a period
- **THEN** no additional period is appended

#### Scenario: Paragraph already ending with a period plus trailing Unicode whitespace

- **WHEN** the paragraph ends with `.` followed by a non-breaking space (`U+00A0`) or an em space (`U+2003`)
- **THEN** no additional period is appended and the trailing whitespace is removed

#### Scenario: Paragraph ending with a colon

- **WHEN** the paragraph ends with `Folgende Punkte:`
- **THEN** the line is emitted as `Folgende Punkte:` with no period appended

#### Scenario: Colon-terminated intro before a list

- **WHEN** a line reading `Folgende Punkte:` is followed by list items
- **THEN** the intro line is emitted as `Folgende Punkte:` with no period appended

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
