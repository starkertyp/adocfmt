## 1. Fixtures

- [x] 1.1 Create `src/test/resources/spec/blank-lines/README.md` describing the rule in prose (empty lines only, runs collapse to one, whitespace-only lines untouched, final newline preserved)
- [x] 1.2 Create `src/test/resources/spec/blank-lines/before.adoc` covering: a run of three empty lines between paragraphs, a single empty line (preserved), a whitespace-only line (preserved), two empty lines inside a `----` block (preserved), and two trailing empty lines
- [x] 1.3 Create `src/test/resources/spec/blank-lines/after.adoc` as the expected formatted output

## 2. Rule implementation

- [x] 2.1 Add `CollapseBlankLinesRule` implementing `FormatRule`: remember whether the input ends with `\n`, strip that one newline, then split with `input.split("\n", -1)`
- [x] 2.2 Walk the lines and emit a maximal run of consecutive empty lines (`line.isEmpty()`) as exactly one empty line
- [x] 2.3 Emit non-empty and whitespace-only lines unchanged
- [x] 2.4 Rejoin with `\n` and re-append the trailing newline when it was present
- [x] 2.5 Add a `ponytail:` comment naming the ceiling (whitespace-only lines are not collapsed) and the upgrade path
- [x] 2.6 Register the rule first in `FormatRules.defaults()`

## 3. Tests

- [x] 3.1 Add a fixture test (`CollapseBlankLinesTest`) that formats `src/test/resources/spec/blank-lines/before.adoc` with only `CollapseBlankLinesRule` and asserts equality with `after.adoc`
- [x] 3.2 Add unit scenarios: three empty lines become one, a single empty line is unchanged, a whitespace-only line is unchanged, `"\n"` stays `"\n"`, input without a trailing newline keeps none, and an empty-line run inside a `----` block is preserved through a full `Formatter` with the rule
- [x] 3.3 Assert idempotency (`format(format(x)) == format(x)`) for the blank-line fixture and for each unit scenario

## 4. Verification

- [x] 4.1 Run `mvn -q verify` and confirm all tests pass
- [x] 4.2 Smoke test: `adocfmt --check src/test/resources/spec/blank-lines/after.adoc` exits `0`, and `--check before.adoc` exits `1`
- [x] 4.3 Run `google-java-format --replace` on new/changed Java files
