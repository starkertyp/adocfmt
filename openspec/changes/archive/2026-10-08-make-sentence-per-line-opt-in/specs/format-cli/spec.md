## ADDED Requirements

### Requirement: Default rule set excludes sentence-per-line

By default the CLI SHALL format with the always-on rules only (blank line collapsing, heading blank lines, table cell spacing) and SHALL NOT apply the sentence-per-line rule. A prose paragraph that would be split into one sentence per line, or a missing final period, SHALL pass through unchanged by default.

#### Scenario: Prose is not split by default

- **WHEN** the CLI formats a document containing a multi-sentence prose paragraph on a single line
- **THEN** the paragraph is emitted unchanged (no sentence splitting, no appended period)

#### Scenario: Flag enables sentence-per-line

- **WHEN** the CLI is invoked with `--sentence-per-line` on the same document
- **THEN** the prose paragraph is split into one sentence per line

### Requirement: Opt-in sentence-per-line flag

The CLI SHALL accept a `--sentence-per-line` flag that enables the sentence-per-line rule for the run. The flag SHALL combine with all output modes (`--write`, `--check`, `--diff`, `--stdin`).

#### Scenario: Flag with stdin

- **WHEN** the CLI is invoked with `--stdin --sentence-per-line` and prose is piped in
- **THEN** the formatted result has one sentence per line

#### Scenario: Flag with check mode

- **WHEN** the CLI is invoked with `--check --sentence-per-line` and a file whose only difference is prose reformatting
- **THEN** the CLI exits with code `1`
