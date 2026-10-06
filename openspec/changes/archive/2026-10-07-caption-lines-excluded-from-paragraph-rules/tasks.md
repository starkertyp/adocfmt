## 1. Fixture

- [x] 1.1 Extend `src/test/resources/spec/sentence-per-line/before.adoc` and `after.adoc` with caption lines (a caption followed by prose, and a caption above a table/listing)
- [x] 1.2 Update `src/test/resources/spec/sentence-per-line/README.md` to document caption lines as structural

## 2. Rule

- [x] 2.1 Add `^\.` (block title / caption) to the `NON_PROSE` pattern in `SentencePerLineRule`

## 3. Tests

- [x] 3.1 Add a unit test asserting a caption line followed by prose is left unchanged while the prose is still formatted
- [x] 3.2 Add a unit test asserting caption handling is idempotent

## 4. Verify

- [x] 4.1 Run `mvn -q verify`
- [x] 4.2 Run `google-java-format --replace` on changed Java files
