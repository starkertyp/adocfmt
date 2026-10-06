## MODIFIED Requirements

### Requirement: Delimited block detection

The formatter SHALL treat a line as a delimited-block fence when the line, ignoring trailing whitespace, consists solely of four or more `-` characters or four or more `*` characters. A fence line opens a protected block; the next fence line using the same delimiter character closes it.

#### Scenario: Opening fence is recognized

- **WHEN** a line consists solely of four or more `-` characters
- **THEN** the formatter treats every following line as protected until the next `-` fence line

#### Scenario: Asterisk fence is recognized

- **WHEN** a line consists solely of four or more `*` characters
- **THEN** the formatter treats every following line as protected until the next `*` fence line

#### Scenario: Closing fence ends protection

- **WHEN** a protected block is open and another fence line with the same delimiter is encountered
- **THEN** that fence closes the block and following lines are protected again only by a later fence

#### Scenario: Different delimiter inside a block stays protected

- **WHEN** a block opened by `****` contains a line of four or more `-`
- **THEN** the `-` line is treated as protected content and does not close the `****` block

#### Scenario: Shorter hyphen run is not a fence

- **WHEN** a line consists of three or fewer `-` characters
- **THEN** the line is treated as ordinary content and does not open a block

#### Scenario: Shorter asterisk run is not a fence

- **WHEN** a line consists of three or fewer `*` characters
- **THEN** the line is treated as ordinary content and does not open a block
