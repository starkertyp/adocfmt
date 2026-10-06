## Why

There is no tooling to normalize AsciiDoc files in this project yet. Formatting rules are not defined at this point, but the surrounding machinery (CLI, file handling, rule application) is stable and independent of the rules. Building the skeleton now lets rules be added later as small, isolated units.

## What Changes

- New Maven project (Java 21) producing an `adocfmt` CLI.
- CLI accepts one or more file paths (and `-` / `--stdin`) and prints formatted output to stdout by default.
- `--write` edits files in place; `--check` reports whether files are already formatted and exits `1` if not; `--diff` prints a unified diff.
- A `FormatRule` extension point plus an ordered rule pipeline in a pure `String -> String` formatter.
- Line endings are always normalized to Unix (`\n`).
- The pipeline ships with **zero concrete formatting rules**; the behavior is pass-through until rules are added.

## Capabilities

### New Capabilities

- `format-cli`: command-line contract — arguments, flags, stdin/stdout/in-place behavior, and exit codes.
- `formatting-pipeline`: the rule model, ordered application, pass-through default, and idempotency guarantees.

### Modified Capabilities

<!-- none -->

## Impact

- New build files: `pom.xml`, `src/main/java`, `src/test/java`.
- New dependencies: `picocli` (CLI), `java-diff-utils` (unified diff for `--diff`), `junit-jupiter` (test, test scope).
- Build: Maven (the only build tool available in the dev shell). No DI container, no config file, no native image.
- Open follow-ups (out of scope): concrete formatting rules, `--check`/`--diff` output formatting details beyond exit codes, final-newline handling, encoding detection beyond UTF-8.
