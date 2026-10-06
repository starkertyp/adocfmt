## Context

`Formatter` splits input into protected and unprotected runs. `DELIMITED_FENCE` matches `^-{4,}\s*$`; a single boolean `inBlock` toggles on every fence line. Rules run only on unprotected runs.

## Goals / Non-Goals

**Goals:**
- Recognize `****` (four or more `*`) as a fence and protect its content exactly like `----`.
- Keep mixed/nested `----` and `****` blocks correctly protected.

**Non-Goals:**
- Supporting other AsciiDoc delimiters (`====`, `....`, etc.).
- Parsing AsciiDoc block attributes or titles on fence lines.

## Decisions

- Broaden `DELIMITED_FENCE` to `^(-{4,}|\*{4,})\s*$`.
- Replace the single `inBlock` boolean with `inBlock` plus the opening delimiter character; a fence opens a block when none is open, and closes it only when the line's delimiter character equals the opening one. A different delimiter inside a block is treated as protected content.
  - Why: a plain toggle would let a `****` line inside a `----` block (or vice versa) close it prematurely, corrupting content. AsciiDoc sidebars commonly nest listing blocks, so this matters.
  - Alternative rejected: single boolean toggle with the broadened regex — simpler but wrong for mixed blocks.

## Risks / Trade-offs

- [An unclosed inner delimiter now never closes the outer block either] → acceptable: the whole remainder is protected, which errs toward preserving content.
