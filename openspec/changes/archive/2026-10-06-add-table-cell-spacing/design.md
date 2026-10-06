## Context

The pipeline (`Formatter`) normalizes line endings and applies an ordered list of `FormatRule`s over the whole text, with no shared block context. The existing `HeadingBlankLineRule` is purely per-line and carries a `ponytail:` note that block-sensitive rules need a segmentation upgrade. This change adds the second rule, `TableCellSpacingRule`, whose expected behavior is fixed by `src/test/resources/spec/table/{before,after}.adoc`.

## Goals / Non-Goals

**Goals:**

- Implement one rule: normalize spacing around `|` cell markers inside `|===` table blocks.
- Keep the rule pure and idempotent.
- Tie the rule to the existing `before.adoc`/`after.adoc` fixtures with an automated test.

**Non-Goals:**

- Column alignment or padding of cell contents.
- Handling `|` inside verbatim/listing blocks, or tables nested in block delimiters.
- Normalizing the `|===` fence itself, `cols`/options lines, or row-span syntax (`2+|`).
- Any CLI or configuration surface.

## Decisions

### Rule-local fence scan, no shared block context

`TableCellSpacingRule` walks the lines once, toggling an `inTable` flag when it sees a `|===` fence, and only rewrites lines while `inTable` is `true`. This keeps the block-awareness local to the rule and avoids adding a shared segmentation pre-pass to `Formatter`. The pipeline stays line-based; the `HeadingBlankLineRule` ceiling note remains valid for rules that cannot self-detect their blocks.

### Marker normalization

For a cell line (inside a table, not blank, not a fence):

1. Collapse spacing around each `|` marker with the regex `\s*\|\s*` replaced by ` | `.
2. If the original line started with `|` (optionally after leading whitespace), strip the leading space so the result starts with `| `.

Alternatives considered:
- **Per-boundary lookarounds** — correct but harder to read than a single substitution plus a leading fix. Rejected.
- **Splitting on `|` and rejoining** — would need to preserve multi-line/continuation semantics and leading indentation; more code, same result. Rejected.

The fence check MUST happen before the regex, because `|===` itself contains `|` and would otherwise be rewritten to ` | ===`.

### Idempotency

After formatting, every marker already sits in exactly one space (` | `) or line-start (`| `), which the regex reproduces unchanged. Blank lines and marker-free lines pass through, so `format(format(x)) == format(x)` holds.

### Fixture-driven test

`TableCellSpacingTest` reads `before.adoc`/`after.adoc` from the classpath, formats `before`, and asserts equality with `after`, mirroring `HeadingBlankLineTest`. Unit scenarios cover: leading marker without a space, inline marker, mixed line, already-spaced line, blank line inside a table, continuation line without a marker, and a `|`-line outside any table left unchanged.

## Risks / Trade-offs

- **`|` inside cell text** (e.g. a literal pipe that is not a cell marker) gets spaced. → Same root cause as all marker-based formatting: AsciiDoc uses `\|` to escape a literal pipe; escaped pipes are out of scope for this rule and documented as a known ceiling.
- **Fence detection false positives** (a `|===` line inside a listing block toggles state). → Known ceiling of the line-based pipeline; marked with a `ponytail:` comment on the rule, upgrade to block segmentation when needed.
- **Multiple spaces around a marker collapse to one.** → Intentional per the README ("ein Leerzeichen"); documented as a scenario.

## Migration Plan

None — additive rule; no existing formatted output to migrate.

## Open Questions

- Should escaped pipes (`\|`) be skipped? Not covered by the fixture; documented as out of scope, add when a real document needs it.
