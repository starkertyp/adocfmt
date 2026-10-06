## Context

The pipeline (`Formatter`) normalizes line endings, withholds delimited `----` blocks, and applies an ordered list of `FormatRule`s over each unprotected run, with no shared block context. Existing rules (`HeadingBlankLineRule`, `TableCellSpacingRule`, `SentencePerLineRule`) are line-based and carry `ponytail:` notes that block-sensitive rules need a segmentation upgrade. This change adds `CollapseBlankLinesRule`, whose expected behavior is fixed by `src/test/resources/spec/blank-lines/{before,after}.adoc`. Vertical whitespace is currently left as-is, so accidental runs of empty lines survive formatting.

## Goals / Non-Goals

**Goals:**

- Implement one rule: collapse every run of consecutive empty lines to a single empty line.
- Keep the rule pure and idempotent.
- Preserve whitespace-only lines and the presence/absence of the final newline.
- Tie the rule to the `before.adoc`/`after.adoc` fixtures with an automated test.

**Non-Goals:**

- Adding or removing a blank line at block boundaries (the blank line before a heading is owned by `HeadingBlankLineRule`).
- Trimming trailing whitespace or normalizing whitespace-only lines.
- Touching content inside delimited `----` blocks (the pipeline protects it before rules run).
- Any CLI or configuration surface.

## Decisions

### Empty line means zero characters

A line counts as empty only when it has zero characters. Whitespace-only lines are treated as ordinary content and pass through unchanged. This matches the AsciiDoc notion of a blank line and avoids changing the presence of a final newline when the last line is whitespace-only. Alternative considered: **treat whitespace-only lines as blank and normalize them to empty** — rejected because it silently rewrites trailing whitespace and can turn a no-trailing-newline file into one with a trailing newline.

### Strip the trailing newline before splitting

`String.split("\n", -1)` produces a trailing empty element when the text ends with a newline. Naively collapsing runs of empty elements would consume that final-newline artifact (for example `"\n"` would become `""`), violating the pipeline's "final newline untouched" requirement. The rule therefore remembers whether the input ends with `\n`, strips that one trailing newline, splits, collapses runs, rejoins, and re-appends the newline when it was present.

### Collapse by run, single pass

The rule walks the lines and, for each maximal run of empty lines, emits exactly one empty line. All other lines are emitted as-is. This is a single pass, no regex, and O(n) in the number of lines.

### Idempotency

After one application no run of two or more consecutive empty lines remains; the trailing-newline handling is a no-op on already-normalized text. Therefore `format(format(x)) == format(x)` holds.

### Rule ordering

The rule is registered first in `FormatRules.defaults()`, so every later rule observes normalized vertical spacing. `HeadingBlankLineRule` already removes extra blank lines immediately before a heading, so it never reintroduces a run the new rule would have to collapse on a later pass.

### Capability test runs only its own rule

Following the existing pattern, `CollapseBlankLinesTest` builds a `Formatter` holding only `CollapseBlankLinesRule`, so the test is independent of the other rules.

## Risks / Trade-offs

- **Whitespace-only lines are not collapsed.** A run of lines containing only spaces is left untouched. → Intentional: avoids rewriting trailing whitespace and breaking the final-newline guarantee. Documented with a `ponytail:` comment naming the ceiling.
- **Empty lines adjacent to a protected block boundary.** Blank lines just outside a `----` fence are part of the unprotected run and collapse normally; blank lines inside are preserved. → Covered by the pipeline's existing run-splitting and asserted by a scenario.
- **Tabs/CR handling.** Line endings are normalized to `\n` by the pipeline before rules run. → No action needed.

## Migration Plan

None — additive rule; no existing formatted output to migrate.

## Open Questions

None.
