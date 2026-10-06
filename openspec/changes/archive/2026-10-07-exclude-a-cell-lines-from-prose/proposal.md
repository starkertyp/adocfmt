## Why

A table cell may be introduced by the AsciiDoc `a|` cell specifier. The sentence-per-line rule recognizes table cells only by a leading `|`, so an `a|` line is misclassified as prose and gets a trailing period: `a| include::../version.txt[]` becomes `a| include::../version.txt[].`, corrupting the directive. This is a consistency gap introduced when `a|` cells gained table-formatting support.

## What Changes

- A table line beginning with the `a|` cell specifier SHALL be treated as a structural (non-prose) line, symmetric with a line beginning with `|`, and SHALL be emitted unchanged (no period added, no sentence splitting or joining).
- Only a leading `a|` is recognized; an `a|` appearing later in a line is unchanged behavior.
- All other prose detection and sentence behavior is unchanged.
- Update the `sentence-per-line` fixture, README, and unit tests to cover a leading `a|` line.

## Capabilities

### New Capabilities

<!-- none -->

### Modified Capabilities

- `sentence-per-line`: prose paragraph detection gains the leading `a|` cell specifier as a structural line prefix (alongside leading `|`).

## Impact

- `src/main/java/adocfmt/SentencePerLineRule.java`.
- `src/test/resources/spec/sentence-per-line/` fixtures and `SentencePerLineTest`.
- No CLI, API, or dependency changes.
