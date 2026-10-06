## Why

A line consisting only of `<` characters (e.g. `<<<<`) is a control line. The sentence-per-line rule currently treats it as prose, so it can be merged into a surrounding paragraph, re-split, or given a trailing period, corrupting it.

## What Changes

- Treat a line consisting only of one or more `<` characters as a structural (non-prose) line, so paragraph/flowing-text rules leave it unchanged.
- Update the `sentence-per-line` fixture and unit tests to cover such control lines.

## Capabilities

### New Capabilities

<!-- none -->

### Modified Capabilities

- `sentence-per-line`: the prose-line classifier gains lines made only of `<` characters as a structural line that is never treated as prose.

## Impact

- `src/main/java/adocfmt/SentencePerLineRule.java` (prose classifier).
- `src/test/resources/spec/sentence-per-line/` fixtures and `SentencePerLineTest`.
- No CLI, API, or dependency changes.
