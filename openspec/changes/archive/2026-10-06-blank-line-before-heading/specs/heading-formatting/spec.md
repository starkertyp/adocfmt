## ADDED Requirements

### Requirement: Blank line before every heading

The formatter SHALL ensure that a heading is separated from any preceding content by a blank line; if the line directly before the heading is not blank, it SHALL insert a blank line between that line and the heading. If the heading is the first line of the document, no blank line SHALL be added. Existing blank lines SHALL be left as they are (the rule does not collapse multiple blanks into one).

#### Scenario: Text directly before a heading

- **WHEN** a heading is directly preceded by a non-blank, non-anchor line
- **THEN** a blank line is inserted before the heading

#### Scenario: Heading directly before a heading

- **WHEN** a heading is directly preceded by another heading
- **THEN** a blank line is inserted before the second heading

#### Scenario: Heading already has a blank line before it

- **WHEN** a heading is already preceded by a blank line
- **THEN** the text is left unchanged at that position

#### Scenario: Heading is the first line

- **WHEN** a heading is the first line of the document
- **THEN** no blank line is inserted before it

### Requirement: Anchor stays attached to its heading

When a heading is directly preceded by one or more anchor lines (`[[xxx]]`), the formatter SHALL place the separating blank line before the topmost anchor and SHALL NOT leave a blank line between the last anchor and the heading. If several anchor lines are stacked, the run SHALL remain contiguous with no blank lines between the anchors.

#### Scenario: Anchor directly above a heading

- **WHEN** a heading is directly preceded by an anchor line that is itself preceded by non-blank content
- **THEN** a blank line is inserted before the anchor and the anchor remains directly adjacent to the heading

#### Scenario: Blank line between anchor and heading

- **WHEN** a blank line sits between an anchor and the heading below it
- **THEN** that blank line is removed, leaving the anchor directly adjacent to the heading

#### Scenario: Blank line already before the anchor

- **WHEN** an anchor directly above a heading already has a blank line before it
- **THEN** the text is left unchanged at that position

#### Scenario: Multiple stacked anchors

- **WHEN** two anchor lines are stacked directly above a heading
- **THEN** a single blank line is placed before the first anchor and no blank line separates the anchors from each other or from the heading
