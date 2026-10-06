## 1. Rule implementation

- [x] 1.1 Add `HeadingBlankLineRule` implementing `FormatRule`: split input on `\n` (`-1` limit), detect headings with `^=+ `, insert a blank line after a heading when the next line exists and is non-blank
- [x] 1.2 Add a `ponytail:` comment noting the lack of block context (verbatim blocks) and the upgrade path
- [x] 1.3 Register the rule in `FormatRules.defaults()`

## 2. Tests

- [x] 2.1 Add a fixture test that formats `src/test/resources/spec/headings/before.adoc` and asserts equality with `after.adoc`
- [x] 2.2 Add unit scenarios: heading followed by text, heading followed by heading, heading already followed by a blank line, heading as last line, `=` without a following space left unchanged
- [x] 2.3 Assert idempotency (`format(format(x)) == format(x)`) for the heading fixture

## 3. Verification

- [x] 3.1 Run `mvn -q verify` and confirm all tests pass
- [x] 3.2 Smoke test: `adocfmt --check src/test/resources/spec/headings/after.adoc` exits `0`, and `--check before.adoc` exits `1`
