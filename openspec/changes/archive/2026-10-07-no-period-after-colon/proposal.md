## Why

The sentence-per-line rule guarantees a trailing period on every prose paragraph. When a line introduces a list or block and ends with `:`, the rule appends a period, producing `Folgende Punkte:.` — a visible formatting error that appears in normal AsciiDoc documents.

## What Changes

- The trailing-period guarantee no longer appends a period when the paragraph's final character is a colon (`:`).
- Add a fixture and unit coverage for colon-terminated lines, including idempotency.

## Capabilities

### New Capabilities
<!-- none -->

### Modified Capabilities

- `sentence-per-line`: the "Trailing period guaranteed" requirement is narrowed so a paragraph ending in `:` is emitted unchanged.

## Impact

- `src/main/java/adocfmt/SentencePerLineRule.java` (trailing-period logic in `flush`)
- `src/test/resources/spec/sentence-per-line/` fixture (before/after/README)
- `src/test/java/adocfmt/SentencePerLineRuleTest.java` edge-case unit tests
