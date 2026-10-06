## Context

`TableCellSpacingRule` rewrites `|` markers inside `|===` tables. The `MARKER` regex surrounds every marker with spaces, and the `LEADING` fixup collapses a line-leading marker back to `| `. An AsciiDoc cell may be introduced by the `a` style operator written directly before the separator (`a|`), which selects block (AsciiDoc) parsing for the cell. Today the formatter turns a leading `a|text` into `a | text`, splitting the operator from the separator and changing the cell's meaning.

The `a` operator appears at the beginning of a cell. In this line-based, block-context-free pipeline a cell beginning that is reliably detectable is a line beginning, so the operator is recognized only when it leads a table line.

## Goals / Non-Goals

**Goals:**
- Preserve the `a` operator attached to the `|` (`a|`) when it starts a table line, while ensuring exactly one space after the `|`.
- Keep an empty `a|` line free of trailing whitespace, mirroring the existing empty `|` behavior.
- Preserve all existing marker behavior and idempotency.

**Non-Goals:**
- Other style operators (`d|`, `e|`, `h|`, `l|`, `m|`, `s|`).
- Span/dup operators (`2+|`, `.3+`, `2*>`) and alignment operators.
- Inline `a|` occurring after a bare leading `|` (e.g. `|a|b`): it is ambiguous with cell content and existing behavior treats it as content.
- Any real block/table parsing beyond the current line-based fence toggle.

## Decisions

- Detect the operator as a line-leading pattern `^\s*a\|` and, after the general marker spacing, apply a fixup symmetric to the existing `LEADING` one: replace the spaced `a | ` at line start with `a| `. This reuses the existing normalization path instead of inserting a pre-pass.
- Handle an empty `a|` line with a dedicated `^\s*a\|\s*$` check emitted as bare `a|`, matching the existing empty-`|` exception, so no trailing space is introduced.
- Recognize only the `a` letter (the block-cell operator named in the request), not the full style set. This keeps `jayjay|` and `|a|b` behaving exactly as before.

## Risks / Trade-offs

- [A line-leading word ending in `a` immediately before `|` (e.g. `a|x`) is treated as the operator] → acceptable and faithful: per the AsciiDoc cell grammar an `a` directly before a cell separator at a cell start *is* the operator.
- [Inline `a|` mid-line is still split] → out of scope; documented as a non-goal, and ambiguous with content-ending-in-`a`.
