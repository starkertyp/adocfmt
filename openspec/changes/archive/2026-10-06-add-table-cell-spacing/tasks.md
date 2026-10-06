## 1. Rule implementation

- [x] 1.1 Add `TableCellSpacingRule` implementing `FormatRule`: split input on `\n` (`-1` limit), toggle an `inTable` flag on `|===` fences, and rewrite only lines inside a table
- [x] 1.2 Normalize each `|` marker via `\s*\|\s*` → ` | `, then strip the leading space for lines that start with `|` so they begin with `| `
- [x] 1.3 Skip fence lines, blank lines, and lines without a `|` (leave them unchanged)
- [x] 1.4 Add a `ponytail:` comment noting that fence detection has no shared block context (listing blocks) and the upgrade path
- [x] 1.5 Register the rule in `FormatRules.defaults()`

## 2. Tests

- [x] 2.1 Add a fixture test (`TableCellSpacingTest`) that formats `src/test/resources/spec/table/before.adoc` and asserts equality with `after.adoc`
- [x] 2.2 Add unit scenarios: `|text` → `| text`, `a|b` → `a | b`, `|heading|1|2|3` → `| heading | 1 | 2 | 3`, already-spaced line unchanged, blank line inside a table unchanged, continuation line without a marker unchanged, `|`-line outside a table unchanged
- [x] 2.3 Assert idempotency (`format(format(x)) == format(x)`) for the table fixture

## 3. Verification

- [x] 3.1 Run `mvn -q verify` and confirm all tests pass
- [x] 3.2 Smoke test: `adocfmt --check src/test/resources/spec/table/after.adoc` exits `0`, and `--check before.adoc` exits `1`
- [x] 3.3 Run `google-java-format --replace` on new/changed Java files
