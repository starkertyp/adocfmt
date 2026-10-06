## Context

`Formatter.format` normalizes line endings and then feeds the entire text through each `FormatRule` in sequence. Rules are line-based and have no block context, so a `----`-fenced listing block is just more lines: `HeadingBlankLineRule` will insert blank lines around `= `-looking lines and `TableCellSpacingRule` will respace `|` markers inside code samples. Both rules carry `ponytail:` comments anticipating a block-segmentation pre-pass.

## Goals / Non-Goals

**Goals:**

- Guarantee that no registered rule can change content between `----` fences.
- Keep the existing `FormatRule` interface and rule implementations untouched.
- Preserve line-ending and trailing-newline behavior exactly.

**Non-Goals:**

- Detecting block attributes (`[source,java]`) or any delimiter other than hyphen runs (`====`, `....`, `--`).
- Matching opening/closing fence length or nesting depth.
- A general block model exposed to rules.

## Decisions

**Segment in `Formatter`, not in a rule.** Add a pre-pass to `Formatter.format` that partitions the normalized line list into alternating protected and unprotected runs, applies the rule chain only to unprotected runs, and reassembles. This is where the `Formatter.java` comment already points; it keeps the public `FormatRule` contract (`String -> String`) and needs no rule changes.

**Toggle on any hyphen-run line.** A line whose content, after stripping trailing whitespace, is four or more `-` characters toggles protected state. This mirrors the existing `|===` toggle in `TableCellSpacingRule` and is trivially idempotent. An unclosed fence therefore protects to EOF naturally.

**Segmentation granularity is the line.** After applying rules to one unprotected run, split the result back into lines and append to the shared output list; append protected runs verbatim. Final assembly is a single `String.join("\n", lines)`, which keeps the current split/join semantics and thus leaves trailing newlines untouched.

Alternative considered: a decorator `FormatRule` that replaces protected blocks with placeholders, runs the rest, then restores. Rejected — placeholder collision and whole-pipeline wrapping add complexity for no benefit over a line-run partition.

## Risks / Trade-offs

- [A `----` line that is literal content rather than a fence will open a block] → Acceptable: four-plus hyphens on their own line is the listing-block delimiter in AsciiDoc; same line-based heuristic already accepted for `|===`.
- [A shorter hyphen-run delimiter nested inside a block closes early] → Matches the existing table toggle behavior; documented as a known ceiling, upgrade only if a real document needs it.
- [Rules that rely on seeing the whole document are now given runs] → All current rules are line-local; the `FormatRule` contract makes no whole-document guarantee.
