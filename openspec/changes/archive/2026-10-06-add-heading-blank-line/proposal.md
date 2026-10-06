## Why

`adocfmt` ships with zero formatting rules, so it currently changes nothing but line endings. Headings are the first rule to implement, and a rule that is written down with before/after examples is the natural starting point for a rule-driven formatter.

## What Changes

- Add the first concrete formatting rule: a blank line is always inserted after an AsciiDoc heading.
- A heading is any line starting with one or more `=` characters followed by a space.
- If the line after a heading is already blank (or the heading is the last line), nothing changes.
- Wire the existing `src/test/resources/spec/headings` fixtures (`README.md`, `before.adoc`, `after.adoc`) into an automated test: formatting `before.adoc` must produce exactly `after.adoc`.
- The rule is attached to the default rule list, so the CLI now changes documents.

## Capabilities

### New Capabilities

- `heading-formatting`: the rule that every AsciiDoc heading is followed by a blank line, and the fixture that defines its expected behavior.

### Modified Capabilities

<!-- none: `formatting-pipeline` semantics (ordered pipeline, pass-through, idempotency, line endings, final newline) are unchanged; only the default rule list is populated. -->

## Impact

- Code: new rule implementation (e.g. `HeadingBlankLineRule`) and a non-empty `FormatRules.defaults()`.
- Tests: a fixture-driven test reading `src/test/resources/spec/headings/{before,after}.adoc`, plus unit tests for the rule.
- No new dependencies; no CLI flag changes.
- Out of scope: skipping headings inside listing/literal blocks (the pipeline is still line-based).
