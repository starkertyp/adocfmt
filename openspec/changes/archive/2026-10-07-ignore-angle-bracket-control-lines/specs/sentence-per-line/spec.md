## MODIFIED Requirements

### Requirement: Prose paragraph detection

The formatter SHALL treat a run of consecutive non-blank "prose" lines as a single prose paragraph. A line SHALL be considered prose only when it is not any of the following: a heading (`=+ `), a list item, a table fence or cell line (leading `|`), a block macro (`name::`), an attribute entry (`:name:`), a block attribute line (`[...]`), an anchor (`[[...]]`), a block title or caption (leading `.`), a control line consisting only of one or more `<` characters, or a line comment (`//`). Lines inside delimited `----` blocks are already withheld by the pipeline and are never prose.

#### Scenario: Consecutive prose lines form one paragraph

- **WHEN** two or more consecutive non-blank prose lines are given
- **THEN** they are treated as one paragraph for sentence splitting

#### Scenario: A blank line ends a paragraph

- **WHEN** a blank line separates two runs of prose lines
- **THEN** each run is treated as a separate paragraph

#### Scenario: Structural lines are not prose

- **WHEN** a line is a heading, list item, table line, block macro, attribute entry, block attribute, anchor, block title, control line, or line comment
- **THEN** the line is emitted unchanged

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
