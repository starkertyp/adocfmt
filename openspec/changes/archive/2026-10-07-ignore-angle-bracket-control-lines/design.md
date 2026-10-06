## Context

`SentencePerLineRule` classifies each line as prose or structural via the `NON_PROSE` regex. Structural lines are emitted unchanged; prose runs are joined, split into sentences, and given a trailing period. A line of only `<` characters is a control line but is currently classified as prose.

## Goals / Non-Goals

**Goals:**
- Exclude control lines made only of `<` characters from prose handling.

**Non-Goals:**
- Matching `<` inside other content (e.g. `<<anchor>>`); only lines consisting solely of `<` are affected.
- Changing any rule other than the prose/paragraph rule.

## Decisions

- Add `^<+$` as an alternative in the existing `NON_PROSE` pattern in `SentencePerLineRule`. Minimal, line-based, consistent with the other structural prefixes already there.

## Risks / Trade-offs

- [A legitimate all-`<` line (e.g. a decorative separator) is left unformatted] → acceptable: pass-through preserves content and is always idempotent.
