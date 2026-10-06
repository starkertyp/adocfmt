## MODIFIED Requirements

### Requirement: Trailing period guaranteed

A prose paragraph that does not already end with a period SHALL have a period appended to its final sentence, except when the paragraph's final character is a colon (`:`), in which case the paragraph SHALL be emitted unchanged.

#### Scenario: Paragraph without a final period

- **WHEN** the paragraph ends with `kein Punkt hier`
- **THEN** the last sentence is emitted as `kein Punkt hier.`

#### Scenario: Paragraph already ending with a period

- **WHEN** the paragraph already ends with a period
- **THEN** no additional period is appended

#### Scenario: Paragraph ending with a colon

- **WHEN** the paragraph ends with `Folgende Punkte:`
- **THEN** the line is emitted as `Folgende Punkte:` with no period appended

#### Scenario: Colon-terminated intro before a list

- **WHEN** a line reading `Folgende Punkte:` is followed by list items
- **THEN** the intro line is emitted as `Folgende Punkte:` with no period appended
