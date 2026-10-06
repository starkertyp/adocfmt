# format-cli Specification

## Purpose

The command-line contract of the `adocfmt` CLI: arguments, flags, stdin/stdout/in-place behavior, and exit codes.

## Requirements

### Requirement: Accept document paths

The CLI SHALL accept one or more file path arguments in a single invocation.

#### Scenario: Multiple paths

- **WHEN** the CLI is invoked with two valid `.adoc` paths
- **THEN** both files are processed in the same run

#### Scenario: No paths and no stdin

- **WHEN** the CLI is invoked without any path and without `--stdin`
- **THEN** the CLI reports a usage error and exits with code `2`

### Requirement: Write result to stdout by default

By default the CLI SHALL print the formatted result to standard output and SHALL NOT modify any file on disk.

#### Scenario: Default invocation is non-destructive

- **WHEN** the CLI is invoked with a path and no `--write`
- **THEN** the formatted content is written to stdout and the source file is unchanged

### Requirement: Read from standard input

The CLI SHALL read document content from standard input when `--stdin` is given.

#### Scenario: Stdin input

- **WHEN** the CLI is invoked with `--stdin` and content is piped in
- **THEN** the formatted result of that input is written to stdout

### Requirement: In-place writing

The CLI SHALL rewrite each input file with the formatted content when `--write` is given.

#### Scenario: Write flag rewrites file

- **WHEN** the CLI is invoked with `--write` and a path whose formatting would change
- **THEN** the file on disk is replaced with the formatted content

### Requirement: Check mode

The CLI SHALL provide a `--check` mode that does not modify files and reports whether any input would change.

#### Scenario: Check reports a difference

- **WHEN** the CLI is invoked with `--check` and at least one input would change
- **THEN** the CLI exits with code `1`

#### Scenario: Check with no differences

- **WHEN** the CLI is invoked with `--check` and every input is already formatted
- **THEN** the CLI exits with code `0`

### Requirement: Diff output

The CLI SHALL provide a `--diff` mode that prints a unified diff of the proposed changes instead of the full formatted content.

#### Scenario: Diff mode shows changes

- **WHEN** the CLI is invoked with `--diff` on a file that would change
- **THEN** a unified diff of the changes is printed to stdout

### Requirement: Exit codes

The CLI SHALL exit with `0` on success, `1` when `--check` finds unformatted files, and `2` for usage or I/O errors.

#### Scenario: Missing file

- **WHEN** the CLI is invoked with a path that does not exist
- **THEN** the CLI reports an error and exits with code `2`

#### Scenario: Unknown option

- **WHEN** the CLI is invoked with an unrecognized option
- **THEN** the CLI reports a usage error and exits with code `2`
