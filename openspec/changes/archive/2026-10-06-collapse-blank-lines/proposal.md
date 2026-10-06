## Why

Documents often accumulate runs of two or more consecutive empty lines (accidental extra returns while editing), which produce noisy diffs and inconsistent spacing. The formatter should normalize every run of consecutive empty lines down to a single empty line so vertical whitespace is deterministic.

## What Changes

- Add a `CollapseBlankLinesRule` that replaces every run of two or more consecutive empty lines with exactly one empty line.
- An empty line is a line with no content (a truly empty line). Whitespace-only lines are left untouched.
- Runs inside delimited `----` blocks are protected by the pipeline and are never collapsed.
- Exact behavior is fixed by the fixtures `src/test/resources/spec/blank-lines/{README.md,before.adoc,after.adoc}`; formatting `before.adoc` must produce exactly `after.adoc`.
- Register the rule in `FormatRules.defaults()` (first, so the other rules start from normalized vertical spacing).

## Capabilities

### New Capabilities

- `blank-line-collapsing`: the rule that collapses runs of consecutive empty lines into a single empty line, plus the fixture that defines its expected behavior.

### Modified Capabilities

<!-- none: `formatting-pipeline` semantics (ordered pipeline, pass-through, idempotency, line endings, final newline, delimited-block protection) are unchanged; only the default rule list grows. -->

## Impact

- Code: new rule implementation (`CollapseBlankLinesRule`) and one more entry in `FormatRules.defaults()`.
- Tests: a fixture-driven test reading `src/test/resources/spec/blank-lines/{before,after}.adoc`, plus unit tests for the rule's edge cases and idempotency.
- No new dependencies; no CLI flag changes.
- Out of scope: blank-line insertion or removal at block boundaries (headings own the blank line before a heading), and content inside delimited `----` blocks (protected by the pipeline before rules run).
