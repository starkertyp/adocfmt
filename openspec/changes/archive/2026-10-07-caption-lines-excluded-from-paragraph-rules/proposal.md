## Why

A line starting with `.` is an AsciiDoc block title (caption). The sentence-per-line rule currently treats it as prose, so a caption gets merged into the following paragraph, re-split, and given a trailing period — corrupting the caption and its block.

## What Changes

- Treat a line beginning with `.` as a structural (non-prose) line in the prose classifier, so paragraph/flowing-text rules leave it unchanged.
- Update the `sentence-per-line` fixture and unit tests to cover caption lines.

## Capabilities

### New Capabilities

<!-- none -->

### Modified Capabilities

- `sentence-per-line`: the prose-line classifier gains block-title (caption) lines (`^.`) as a structural line that is never treated as prose.

## Impact

- `src/main/java/adocfmt/SentencePerLineRule.java` (prose classifier).
- `src/test/resources/spec/sentence-per-line/` fixtures and `SentencePerLineTest`.
- No CLI, API, or dependency changes.
