## 1. Fixture

- [x] 1.1 Add an `a|`-prefixed cell line (both `a|text` unspaced and `a| text` already spaced, plus a bare `a|` empty cell) to `src/test/resources/spec/table/before.adoc` and `after.adoc`, with `after` showing the `a` attached to `|` and one space after
- [x] 1.2 Document the `a|` cell specifier in `src/test/resources/spec/table/README.md`

## 2. Rule

- [x] 2.1 In `TableCellSpacingRule`, add a line-leading `a|` recognition pattern and a fixup symmetric to the existing `LEADING` handling that emits `a| ` with the `a` attached
- [x] 2.2 Emit a bare `a|` (no trailing space) for an empty `a|` line, mirroring the existing empty-`|` exception

## 3. Tests

- [x] 3.1 Add a unit test asserting `a|text` becomes `a| text`
- [x] 3.2 Add a unit test asserting an already spaced `a| text` is unchanged
- [x] 3.3 Add a unit test asserting a bare `a|` line stays `a|` with no trailing space
- [x] 3.4 Add a unit test asserting idempotency of the `a|` line
- [x] 3.5 Confirm the existing `|a|b` → `| a | b` behavior still holds (inline `a` is not a specifier)

## 4. Verify

- [x] 4.1 Run `mvn -q verify`
- [x] 4.2 Run `google-java-format --replace` on the changed Java file
- [x] 4.3 Run `openspec validate table-cell-a-specifier`
