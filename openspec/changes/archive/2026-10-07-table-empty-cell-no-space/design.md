## Context

`TableCellSpacingRule` rewrites `|` markers inside `|===` tables. The global `MARKER` regex surrounds each marker with spaces and the `LEADING` fixup collapses the start of the line to `| `. For a line that is only `|` (empty cell row), this yields `| ` with a trailing space.

## Goals / Non-Goals

**Goals:**
- Emit a whole-line empty cell (`|` plus optional whitespace) as `|` with no trailing space.

**Non-Goals:**
- Changing spacing for inline empty cells (e.g. `| a || b`); it keeps current behavior.
- Parsing table structure beyond the existing line-based fence toggle.

## Decisions

- Before the general spacing branch, detect a line matching `^\|\s*$` while inside a table and emit `|` directly.

## Risks / Trade-offs

- [A line that is `|` plus meaningful trailing whitespace is normalized] → acceptable: the lines are semantically identical and the whitespace was the conflict source.
