## 1. Fixture

- [x] 1.1 Add a `****` block (containing heading-like and table-like lines) and a `****` block wrapping a `----` block to `src/test/resources/spec/verbatim/before.adoc` and `after.adoc`, showing the content is left unchanged
- [x] 1.2 Update `src/test/resources/spec/verbatim/README.md` to document `****` as a protected fence

## 2. Pipeline

- [x] 2.1 Broaden `DELIMITED_FENCE` in `Formatter` to also match four or more `*`
- [x] 2.2 Track the opening delimiter character so a fence only closes a block opened by the same delimiter

## 3. Tests

- [x] 3.1 Add a unit test asserting a `****` block protects heading-like content
- [x] 3.2 Add a unit test asserting a `****` block nesting a `----` block stays fully protected
- [x] 3.3 Add a unit test asserting a short `***` run is not a fence
- [x] 3.4 Add a unit test asserting a `*`-only line inside a `----` block does not close it

## 4. Verify

- [x] 4.1 Run `mvn -q verify`
- [x] 4.2 Run `google-java-format --replace` on changed Java files
