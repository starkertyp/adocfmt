## 1. Fixture

- [x] 1.1 Add an empty cell line (`|`) and a whitespace-only empty cell line to `src/test/resources/spec/table/before.adoc` and `after.adoc`, with `after` showing a bare `|`

## 2. Rule

- [x] 2.1 In `TableCellSpacingRule`, emit a bare `|` for a table line matching `^\|\s*$` before applying the general marker spacing

## 3. Tests

- [x] 3.1 Add a unit test asserting a lone `|` line stays `|` with no trailing space
- [x] 3.2 Add a unit test asserting a whitespace-only `|   ` line becomes `|`
- [x] 3.3 Add a unit test asserting idempotency of the empty cell line

## 4. Verify

- [x] 4.1 Run `mvn -q verify`
- [x] 4.2 Run `google-java-format --replace` on changed Java files
