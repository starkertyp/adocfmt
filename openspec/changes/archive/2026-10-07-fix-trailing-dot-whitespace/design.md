## Context

`SentencePerLineRule` collects prose lines with `line.trim()` and decides
whether to append a period with `sentence.endsWith(".")`. `String.trim()`
removes only characters `<= U+0020`, so trailing Unicode whitespace such as a
non-breaking space (`U+00A0`) or em space (`U+2003`) survives. The final
`sentence` then ends with that whitespace, `endsWith(".")` is false, and a
period is appended, producing `. .` in the rendered output.

## Goals / Non-Goals

**Goals:**
- Recognize a paragraph as period-terminated when it ends in `.` followed by
  any whitespace, ASCII or Unicode.

**Non-Goals:**
- Changing sentence boundary detection for whitespace *between* sentences.
  `BOUNDARY` uses ASCII `\s`; Unicode whitespace between two sentences is a
  separate concern and not part of this fix.

## Decisions

- Replace `String.trim()` with `String.strip()` for per-line normalization and
  for the final sentence check. `strip()` is Unicode-aware (`Character.isWhitespace`)
  and is a drop-in replacement, so no regex or custom logic is needed.
- `trim()` on the joined paragraph remains; `strip()` at line collection already
  removes the offending trailing characters before joining.

## Risks / Trade-offs

- [Behavior change for lines with Unicode whitespace] → The change is strictly
  a fix: the characters removed were unintended trailing whitespace and the
  prior output (`. .`) was malformed. Fixtures and idempotency tests cover it.
