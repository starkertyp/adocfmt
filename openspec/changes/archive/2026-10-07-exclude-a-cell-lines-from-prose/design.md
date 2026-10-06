## Context

`SentencePerLineRule` decides what is prose line-by-line using the `NON_PROSE` pattern. Table rows are excluded by `^\s*\|`, but the `a|` cell specifier (a valid cell start per the table-formatting rules) is not covered, so `a| ...` lines are collected into a paragraph and get a sentence period. The rule has no table/block context and classifies purely by line prefix.

## Goals / Non-Goals

**Goals:**
- Treat a table line beginning with the `a|` cell specifier as a structural, non-prose line, so it is emitted unchanged (no period, no split/join), symmetric with a line beginning with `|`.

**Non-Goals:**
- `a|` appearing later in a line (inline), or `|a|b`-style content.
- Multi-line cell continuation lines (e.g. a second line of a default cell) — they remain governed by existing prose detection.
- Full `|===` block-awareness in the sentence rule; other cell specifiers (`h|`, `m|`, ...).
- Changing table-fence or bare-`|` handling.

## Decisions

- Extend the existing table prefix in `NON_PROSE` from `^\s*\|` to `^\s*a?\|`, making the leading `a` optional. This reuses the established prefix heuristic and stays one regex change; it is symmetric with the `a|` handling recently added to `TableCellSpacingRule`.
- Rejected: teaching `SentencePerLineRule` to track `|===` fences and skip all interior lines. That would also change multi-line default-cell behavior, which is out of the agreed scope.
- Rejected: detecting a block macro (`include::`) after a cell prefix. That would only fix macro cells, not prose-looking `a|` content, and would add complexity for no benefit over the prefix check.

## Risks / Trade-offs

- [A genuine prose line starting with `a|` would no longer be split/period-appended] → acceptable and faithful: `a` directly before `|` at a cell start is the AsciiDoc cell specifier, and such a line is not normal prose.
