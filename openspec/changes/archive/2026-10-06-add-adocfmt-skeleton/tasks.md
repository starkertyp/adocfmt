## 1. Project Setup

- [x] 1.1 Create `pom.xml` (Java 21, packaging jar) with dependencies: `info.picocli:picocli`, `org.junit.jupiter:junit-jupiter` (test)
- [x] 1.2 Configure `maven-compiler-plugin` (release 21) and `maven-surefire-plugin`
- [x] 1.3 Add `maven-shade-plugin` to produce a runnable fat jar with `Main-Class` and picocli annotation processing

## 2. Core Formatter

- [x] 2.1 Create `FormatRule` functional interface (`String apply(String)`)
- [x] 2.2 Create `Formatter` holding an ordered `List<FormatRule>`; normalize line endings to `\n`; apply rules in order
- [x] 2.3 Add `ponytail:` comment on the pipeline noting the line-based context ceiling and the upgrade path to block segmentation
- [x] 2.4 Create an empty default rule list wiring (pass-through behavior)

## 3. CLI

- [x] 3.1 Create `Main` as picocli `@Command` entry point
- [x] 3.2 Implement positional `<paths>...` argument accepting one or more files
- [x] 3.3 Implement default behavior: read UTF-8 and print formatted result to stdout
- [x] 3.4 Implement `--stdin` to read from standard input
- [x] 3.5 Implement `--write` to rewrite files in place
- [x] 3.6 Implement `--check` (no writes; exit `1` if any input would change)
- [x] 3.7 Implement `--diff` to print a unified diff instead of full content
- [x] 3.8 Implement exit codes: `0` success, `1` check-diff, `2` usage/IO error (missing file, unknown option)

## 4. Tests

- [x] 4.1 `FormatterTest`: pass-through default, ordered rule application, line-ending normalization (CRLF -> LF)
- [x] 4.2 `FormatterTest`: idempotency harness asserting `format(format(x)) == format(x)`
- [x] 4.3 `FormatterTest`: final newline preserved (with and without trailing newline)
- [x] 4.4 `FormatCommandTest`: default invocation writes to stdout and leaves file unchanged
- [x] 4.5 `FormatCommandTest`: `--write` rewrites the file
- [x] 4.6 `FormatCommandTest`: `--check` exit codes (`0` clean, `1` dirty)
- [x] 4.7 `FormatCommandTest`: missing path and unknown option exit with `2`

## 5. Verification

- [x] 5.1 Run `mvn -q verify` and confirm all tests pass
- [x] 5.2 Smoke test the fat jar: format a sample `.adoc` to stdout, then with `--check`
