## Context

`SentencePerLineRule` splits a paragraph with
`BOUNDARY = (?<=\.)\s+(?=[A-ZÄÖÜ])`. That matches the final period of any
abbreviation, so `z.B. Der ...` is split after `z.B.`. The existing code comment
already flags this limitation. Abbreviations that contain an internal period
(`z.B.`, `d.h.`, `m.a.W.`) are distinguishable by shape, but single-token
abbreviations (`usw.`, `bzw.`, `ca.`) are shaped exactly like an ordinary word
ending a sentence, so they need an explicit list.

## Goals / Non-Goals

**Goals:**
- Do not split after a common German abbreviation, even before an uppercase word.
- Recognize the period-bearing abbreviations of the CharLingua reference list.
- Keep the rule idempotent and keep formatting otherwise unchanged.

**Non-Goals:**
- Full German tokenizer, sentence segmentation library, or language detection.
- Perfect coverage of every German abbreviation; the list can grow when a real
  document needs more.
- Changing non-prose classification or block handling.

## Decisions

- After applying `BOUNDARY.split`, merge a sentence back into the previous one
  when the previous sentence ends with a known abbreviation. This keeps the
  existing, well-tested split logic untouched and confines the change to one
  post-processing step. Re-joining uses a single space, matching the original
  `String.join(" ", paragraph)`.
- Recognize two abbreviation forms:
  1. A curated `Set<String>` (lowercased) of single-token acronyms from the
     CharLingua reference (`usw`, `bzw`, `ca`, `evtl`, `ggf`, `inkl`, `abs`,
     `art`, `dr`, `jan`, ...).
  2. A pattern `\p{L}(?:\.\p{L})+\.` for multi-part abbreviations
     (`z.B.`, `d.h.`, `i.D.`, `m.a.W.`, `o.ä.`), so variants not in the list are
     still handled.
  Matching is case-insensitive via `Locale.ROOT` lowercasing.
- Check the trailing whitespace-delimited token of the previous sentence for
  membership. After the boundary split that token is exactly the abbreviation
  plus its final period.

**Alternatives considered:**
- Heuristic "short lowercase token ending in a period" (e.g. <= 4 letters):
  rejected because it would also suppress real boundaries after short words such
  as `gut.` or `Haus.`.
- Replacing `BOUNDARY.split` with a hand-written scanner: rejected as a larger
  rewrite of working code for the same effect.

## Risks / Trade-offs

- [A real sentence ending in a word identical to an abbreviation is not split]
  → Inherent to abbreviation handling and generally desirable (e.g. `Std.` is
  the abbreviation for Stunde). The list is intentionally limited to period-
  bearing abbreviations.
- [Incomplete list] → Multi-part regex covers the unbounded class; the curated
  list can be extended. A `ponytail:` comment will name this ceiling.
- [Idempotency] → Merging only removes boundaries; a second pass sees no
  abbreviation-induced boundary, so the output is stable.
