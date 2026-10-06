## Context

`SentencePerLineRule` classifies each line as prose or structural via the `NON_PROSE` regex. Structural lines are emitted unchanged; prose runs are joined, split into sentences, and given a trailing period. A line starting with `.` is an AsciiDoc block title (caption) but is currently classified as prose, so it is absorbed into the following paragraph and corrupted.

## Goals / Non-Goals

**Goals:**
- Exclude caption lines (`^.`) from prose handling so paragraph rules leave them untouched.

**Non-Goals:**
- Parsing AsciiDoc block titles in full (e.g. `.` with attached block attributes or `[caption]` syntax).
- Applying caption behavior to any rule other than the prose/paragraph rule; no other rule treats prose specially.

## Decisions

- Add `^\\.` as an alternative in the existing `NON_PROSE` pattern in `SentencePerLineRule`. Minimal, line-based, consistent with the other structural prefixes already there.
- Keep the pipeline/Formatter unchanged: caption protection is a rule-level concern, matching the established pattern for headings, lists, and block macros.

## Risks / Trade-offs

- [A line legitimately beginning with `.` that is not a caption (e.g. an ellipsis) will be left unformatted] → acceptable: AsciiDoc treats a leading `.` as a block title; the rule errs toward pass-through, which is always idempotent.
