## 1. Pipeline

- [x] 1.1 Replace run partitioning in `Formatter.format` with whole-document rule application over masked protected lines (`\u0000<index>\u0000`), with a `ponytail:` note on the NUL-token ceiling.
- [x] 1.2 Restore protected lines from the pre-format originals by token index and throw `IllegalStateException` when the restored count differs.
- [x] 1.3 Add the `^\u0000` guard to `SentencePerLineRule.NON_PROSE` so masked lines stay opaque to prose detection.

## 2. Fixtures

- [x] 2.1 Extend `src/test/resources/spec/headings/{before,after}.adoc` with a heading directly above an opening fence, a heading directly below a closing fence, and an anchor directly below a closing fence; note the block-boundary behaviour in the fixture README.
- [x] 2.2 Extend `src/test/resources/spec/table/{before,after}.adoc` with a `----` listing block nested inside a table and a line after the closing `|===`; note it in the fixture README.

## 3. Tests

- [x] 3.1 `HeadingBlankLineTest`: heading before opening fence, heading after closing fence, anchor after closing fence, and idempotency for each.
- [x] 3.2 `TableCellSpacingTest`: cell after a nested protected block is spaced, listing content untouched, line after the closing fence unchanged, idempotency.
- [x] 3.3 `DelimitedBlockProtectionTest`: a rule that mutates token lines cannot alter protected output; a token-consuming rule fails instead of losing content.
- [x] 3.4 `SentencePerLineTest`: a protected block stays opaque to prose detection (no splitting or period appended).

## 4. Verification

- [x] 4.1 Run `mvn -q verify` and `google-java-format --replace` on changed Java files.
- [x] 4.2 Run `openspec validate document-scoped-rule-application`.
- [x] 4.3 Sync delta specs to main specs and archive the change.
