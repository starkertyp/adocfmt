# AGENTS.md

`adocfmt` — a Maven/Java 21 AsciiDoc formatter CLI. Toolchain (JDK 21, Maven, google-java-format, openspec) is provided by the Nix devshell via direnv; it is already on `PATH` inside the project shell.

## Commands

- Build + test: `mvn -q verify`
- Single test class: `mvn -q test -Dtest=FormatterTest`
- Format Java before committing: `google-java-format --replace <file>` (the pre-commit hook runs `--replace --set-exit-if-changed` and will fail unformatted code)
- OpenSpec: `openspec new change <kebab-name>`, `openspec status --change <name>`, `openspec validate <name>`. Slash commands are in `.opencode/commands/` (`/opsx-propose`, `/opsx-apply`, ...).

## Conventions

- Every formatting rule is a `FormatRule` (`String -> String`) registered in `FormatRules.defaults()`. The pipeline is line-based and normalizes line endings to `\n` before rules run; it has **no block context** (verbatim/listing blocks are not detected — note this ceiling when adding rules).
- Every rule MUST be idempotent: `format(format(x)) == format(x)`.
- Rule behavior is specified by fixtures, not prose: `src/test/resources/spec/<rule>/{README.md,before.adoc,after.adoc}`. A fixture test formats `before.adoc` and must equal `after.adoc`. Add new rules here first.
- CLI (`FormatCommand`, picocli): default prints to stdout, `--write`, `--check` (exit `1` if changes needed), `--diff`, `--stdin`; exit `2` on usage/IO error.
- `README.md` is the user-facing GitHub entry point. Keep it current whenever behavior, CLI options, or rules change (it also notes the project is AI-assisted).

## Tests

Tests are not optional here: no rule or feature ships without a test. A new rule needs a fixture under `src/test/resources/spec/<rule>/` plus unit tests for its edge cases, and every rule test asserts idempotency. Run `mvn -q verify` before considering work done.

## Workflow

This repo uses OpenSpec (schema `spec-driven`). Plan changes under `openspec/changes/<name>/` with proposal → specs → design → tasks, then implement. Main specs live in `openspec/specs/` and are updated at archive time.

## Do not touch

- `.pre-commit-config.yaml` is a Nix-store symlink, gitignored, and generated — never edit it.
- `target/` and `dependency-reduced-pom.xml` are build artifacts.
