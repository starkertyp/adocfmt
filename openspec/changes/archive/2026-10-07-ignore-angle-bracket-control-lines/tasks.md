## 1. Fixture

- [x] 1.1 Add a control line (`<<<<`) between prose lines in `src/test/resources/spec/sentence-per-line/before.adoc` and `after.adoc` so it stays unchanged while the prose is still formatted
- [x] 1.2 Update `src/test/resources/spec/sentence-per-line/README.md` to document control lines as structural

## 2. Rule

- [x] 2.1 Add `^<+$` (control line) to the `NON_PROSE` pattern in `SentencePerLineRule`

## 3. Tests

- [x] 3.1 Add a unit test asserting a `<`-only line between prose lines is left unchanged while the prose is formatted
- [x] 3.2 Add a unit test asserting control-line handling is idempotent

## 4. Verify

- [x] 4.1 Run `mvn -q verify`
- [x] 4.2 Run `google-java-format --replace` on changed Java files
