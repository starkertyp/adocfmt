## Context

The pipeline (`Formatter`) is line-based, receives the whole text as a `String`, and applies an ordered list of `FormatRule`s. Line endings are already normalized to `\n` before rules run. `FormatRules.defaults()` currently returns an empty list, so the CLI is a pass-through. This change adds the first concrete rule and populates the default list. The expected behavior is fixed by the fixtures under `src/test/resources/spec/headings/`.

## Goals / Non-Goals

**Goals:**

- Implement one rule: a blank line directly after every heading.
- Keep the rule pure, idempotent, and consistent with the existing final-newline guarantee.
- Tie the rule to the existing `before.adoc`/`after.adoc` fixtures with an automated test.

**Non-Goals:**

- Skipping headings inside listing/literal/comment blocks (pipeline is line-based; see Risks).
- Normalizing heading levels, spacing inside headings, or the `=` run.
- Any CLI or configuration surface.

## Decisions

### Rule shape: a `FormatRule` over normalized text

`HeadingBlankLineRule` implements `FormatRule` and is added to `FormatRules.defaults()`. It splits on `\n` with a limit of `-1` (`String.split("\n", -1)`) so a missing final newline is preserved, builds the output line by line, and inserts a blank line after a heading when the following line exists and is not blank.

A heading is matched with `^=+ ` (start-of-line, one or more `=`, one space).

Alternatives considered:
- **Regex whole-text replacement** (`(?m)^(=+ .*)\n(?!\n|$)`) — clever and short, but harder to reason about regarding final newline and EOF; the line loop is boring and directly testable. Rejected.
- **Block segmentation pre-pass** — only needed when a rule must skip verbatim blocks. Not required by this rule. Rejected as YAGNI.

### Idempotency

The rule emits a blank line only when the next line is non-blank; running it again sees the inserted blank and changes nothing. Heading-at-EOF is left untouched, so `format(format(x)) == format(x)` holds.

### Fixture-driven test

`HeadingBlankLineTest` (or an extension of the rule test) reads `before.adoc` and `after.adoc` from the test resources via the classpath/resource path, formats `before`, and asserts equality with `after`. Unit scenarios (heading followed by text, by heading, already blank, last line, `=` without space) cover the edge cases the fixture does not.

## Risks / Trade-offs

- **Headings inside verbatim blocks** (e.g. a line starting with `= ` inside a `----` listing block would get a spurious blank line). → Known ceiling: the pipeline has no block context. Marked with a `ponytail:` comment on the rule; upgrade to block segmentation when such a rule is needed.
- **Heading detection false positives** (`= `-prefixed text that is not meant as a heading). → Same root cause; accepted for now, consistent with the documented README rule.
- **Line splitting cost on large files.** → Irrelevant for source documents; same order as the existing pipeline.

## Migration Plan

None — additive rule; no existing formatted output to migrate.

## Open Questions

- Should a heading at end-of-file with no trailing newline be treated specially (currently left untouched)? The fixture does not cover it; documented as a scenario.
