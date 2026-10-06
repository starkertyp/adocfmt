## 1. Fixture

- [x] 1.1 Add an `a|`-prefixed cell line (e.g. `a| include::../version.txt[]`) to `src/test/resources/spec/sentence-per-line/before.adoc` and the identical unchanged line to `after.adoc`
- [x] 1.2 Document the leading `a|` cell specifier as a structural line in `src/test/resources/spec/sentence-per-line/README.md`

## 2. Rule

- [x] 2.1 In `SentencePerLineRule`, extend the table-line prefix in `NON_PROSE` from `^\s*\|` to also match a leading `a|` cell specifier (`^\s*a?\|`)

## 3. Tests

- [x] 3.1 Add a unit test asserting `|===\na| include::../version.txt[]\n|===\n` is emitted unchanged (no period)
- [x] 3.2 Add a unit test asserting an `a|` line adjacent to a prose paragraph stays separate and unmodified while the paragraph is still split/period-appended
- [x] 3.3 Add a unit test asserting idempotency of an `a|` line
- [x] 3.4 Confirm the existing `|`-cell and structural-line tests still pass

## 4. Verify

- [x] 4.1 Run `mvn -q verify`
- [x] 4.2 Run `google-java-format --replace` on the changed Java file
- [x] 4.3 Run `openspec validate exclude-a-cell-lines-from-prose`
