## Why

The sentence-per-line rule splits a sentence after any period followed by
whitespace and an uppercase letter. When a common German abbreviation ends a
clause and the next word is uppercase, the abbreviation is wrongly split onto
its own line, e.g. `z.B. Der folgende Text` becomes:

```
z.B.
Der folgende Text
```

The `z.B.` abbreviation should stay attached to the sentence that follows it,
and no new line should be inserted after it. The rule currently only avoids
splitting an abbreviation when the following word is lowercase.

## What Changes

- Recognize common German abbreviations (reference:
  <https://www.charlingua.de/post/abkuerzungen>) that end with or contain a
  period, such as `z.B.`, `d.h.`, `usw.`, `bzw.`, `ca.`, `evtl.`, `i.D.`.
- Suppress a sentence boundary when the text before the period ends with a known
  abbreviation, regardless of whether the following word is upper- or lowercase.
- Keep abbreviations on the same line as the following text (no new line after
  them).

## Capabilities

### New Capabilities
<!-- none -->

### Modified Capabilities
- `sentence-per-line`: The "Sentence boundary detection" requirement is refined
  so a period that is part of a common German abbreviation does not create a
  sentence boundary even when followed by an uppercase word.

## Impact

- `src/main/java/adocfmt/SentencePerLineRule.java` (adds an abbreviation set and
  a boundary-suppression check)
- `src/test/resources/spec/sentence-per-line/` fixtures and README
- `src/test/java/adocfmt/SentencePerLineTest.java`
