## 1. Fixtures

- [x] 1.1 Create `src/test/resources/spec/sentence-per-line/README.md` describing the rule in prose (one sentence per line, trailing period, structural lines untouched)
- [x] 1.2 Create `src/test/resources/spec/sentence-per-line/before.adoc` covering: a two-sentence single line, a sentence wrapped across source lines, a paragraph missing a trailing period, and structural lines (heading, list item, table line, macro, attribute, anchor, comment) that must stay unchanged
- [x] 1.3 Create `src/test/resources/spec/sentence-per-line/after.adoc` as the expected formatted output

## 2. Rule implementation

- [x] 2.1 Add `SentencePerLineRule` implementing `FormatRule`: split input on `\n` (`-1` limit)
- [x] 2.2 Implement the prose-line classifier (exclude headings, list items, table lines, block macros, attribute entries, block attributes, anchors, line comments)
- [x] 2.3 Group consecutive prose lines into paragraphs and join each with single spaces
- [x] 2.4 Split each paragraph at sentence boundaries (`\.(?=\s+[A-ZÄÖÜ])` plus a period at the end of the paragraph)
- [x] 2.5 Emit one sentence per line with no blank line between them
- [x] 2.6 Append a period to the final sentence when the paragraph does not already end with one
- [x] 2.7 Emit non-prose lines and blank lines unchanged
- [x] 2.8 Add a `ponytail:` comment naming the classifier/regex ceilings (abbreviations, quote/bracket endings) and the upgrade path
- [x] 2.9 Register the rule in `FormatRules.defaults()`

## 3. Tests

- [x] 3.1 Add a fixture test (`SentencePerLineTest`) that formats `src/test/resources/spec/sentence-per-line/before.adoc` with only `SentencePerLineRule` and asserts equality with `after.adoc`
- [x] 3.2 Add unit scenarios: `Erster Satz. Zweiter Satz.` splits into two lines, wrapped sentence re-emitted on one line, missing trailing period appended, `z.B.` not split, `3.14` not split, heading/list/table/macro/attribute/comment lines unchanged
- [x] 3.3 Assert idempotency (`format(format(x)) == format(x)`) for the sentence-per-line fixture

## 4. Isolate existing capability tests

- [x] 4.1 Change `HeadingBlankLineTest` to construct the `Formatter` with only `HeadingBlankLineRule`
- [x] 4.2 Change `TableCellSpacingTest` to construct the `Formatter` with only `TableCellSpacingRule`
- [x] 4.3 Change `DelimitedBlockProtectionTest` to construct the `Formatter` with `HeadingBlankLineRule` + `TableCellSpacingRule` (the rules that would otherwise mutate block content)

## 5. Verification

- [x] 5.1 Run `mvn -q verify` and confirm all tests pass
- [x] 5.2 Smoke test: `adocfmt --check src/test/resources/spec/sentence-per-line/after.adoc` exits `0`, and `--check before.adoc` exits `1`
- [x] 5.3 Run `google-java-format --replace` on new/changed Java files
