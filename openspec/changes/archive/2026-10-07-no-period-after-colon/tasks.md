## 1. Fixture

- [x] 1.1 Add a colon-terminated intro line (e.g. `Folgende Punkte:`) to `src/test/resources/spec/sentence-per-line/before.adoc`
- [x] 1.2 Add the expected unchanged line to `after.adoc` (no trailing period)
- [x] 1.3 Update `README.md` to document colon-terminated lines

## 2. Rule

- [x] 2.1 In `SentencePerLineRule.flush`, skip the trailing-period append when the final sentence ends with `:`
- [x] 2.2 Run `google-java-format --replace` on the changed Java file

## 3. Unit tests

- [x] 3.1 Add an edge-case test asserting a colon-ending paragraph is unchanged
- [x] 3.2 Add an idempotency test for colon-ending prose

## 4. Verify

- [x] 4.1 Run `mvn -q verify` and confirm all tests pass
