## 1. Fix

- [x] 1.1 In `SentencePerLineRule`, replace `line.trim()` with `line.strip()` when collecting paragraph lines
- [x] 1.2 In `SentencePerLineRule.flush`, replace `sentence.trim()` with `sentence.strip()`

## 2. Tests

- [x] 2.1 Add a `sentence-per-line` fixture case (before/after) covering a paragraph ending in `.` followed by Unicode whitespace (`U+00A0`/`U+2003`)
- [x] 2.2 Add a unit test asserting a line ending in `.` plus trailing Unicode whitespace is not given a second period
- [x] 2.3 Assert idempotency for the new case

## 3. Verify

- [x] 3.1 Run `google-java-format --replace` on changed Java files
- [x] 3.2 Run `mvn -q verify`
