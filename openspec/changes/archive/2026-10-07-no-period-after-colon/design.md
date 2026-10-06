## Context

`SentencePerLineRule.flush` joins a prose paragraph, splits it into sentences, and appends a period to the final sentence when it does not already end with `.`. A paragraph whose text ends with `:` (typically a line introducing a list or block) therefore becomes `...:.`.

## Goals / Non-Goals

**Goals:**
- Suppress the trailing period when the paragraph's final character is `:`.
- Preserve idempotency and all existing behavior.

**Non-Goals:**
- Handling other terminal punctuation (`?`, `!`, `;`, `…`) or abbreviations.
- Parsing AsciiDoc block context; the rule stays line-based.

## Decisions

- **Guard in the existing append check.** Change the condition at `SentencePerLineRule.java:58` from `!sentence.endsWith(".")` to also require that the sentence does not end with `:`. One-line change, no new abstraction.
- Alternative considered: a general "terminal punctuation" set. Rejected — YAGNI; only `:` was reported and any wider set needs its own spec.

## Risks / Trade-offs

- A genuine sentence ending in a colon but not followed by a list stays without a period → acceptable; the user's reported rule is exactly this.
