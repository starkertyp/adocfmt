## Why

A prose line that already ends with `.` but carries trailing whitespace that
`String.trim()` does not remove (for example a non-breaking space `U+00A0` or an
em space `U+2003`) is not recognized as period-terminated, so the
sentence-per-line rule appends a second period. Rendering the result shows
`. .` instead of `.`.

## What Changes

- The sentence-per-line rule strips Unicode whitespace (not just ASCII
  whitespace) when detecting the end of a paragraph, so a line ending in `.`
  followed by any whitespace character is recognized as already terminated and
  is not given a second period.

## Capabilities

### New Capabilities
<!-- none -->

### Modified Capabilities
- `sentence-per-line`: The "Trailing period guaranteed" requirement is refined
  so trailing Unicode whitespace is ignored when deciding whether the paragraph
  already ends with a period (or colon).

## Impact

- `src/main/java/adocfmt/SentencePerLineRule.java`
- `src/test/resources/spec/sentence-per-line/` fixtures and unit tests.
