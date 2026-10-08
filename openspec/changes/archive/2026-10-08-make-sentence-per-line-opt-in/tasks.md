## 1. Rule selection

- [x] 1.1 In `FormatRules`, remove `SentencePerLineRule` from `defaults()` and add an overload `defaults(boolean sentencePerLine)` that appends the rule last when true

## 2. CLI flag

- [x] 2.1 In `FormatCommand`, add a `--sentence-per-line` boolean `@Option` and build the `Formatter` from `FormatRules.defaults(sentencePerLine)` after parsing (in `call()`)

## 3. Tests

- [x] 3.1 Add CLI tests: prose paragraph is NOT split by default (stdout mode), and IS split with `--sentence-per-line` (stdin mode); assert `--check --sentence-per-line` exits 1 on a prose-only difference

## 4. Docs

- [x] 4.1 Update `README.md`: document the `--sentence-per-line` flag and mark the rule as opt-in in the rule table

## 5. Verify

- [x] 5.1 Run `mvn -q verify` and format touched Java files with google-java-format
