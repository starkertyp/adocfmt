## Why

Prose paragraphs in AsciiDoc are often written as one long line or wrapped mid-sentence, which produces noisy diffs and makes it hard to edit or review individual sentences. The formatter should normalize prose so that each sentence occupies exactly one line and every paragraph ends with a period.

## What Changes

- Add a `SentencePerLineRule` that reformats plain prose paragraphs:
  - a paragraph is a run of consecutive prose lines (no blank line between them);
  - sentences are split at sentence-ending periods (a `.` followed by whitespace and an uppercase letter, or a `.` at the end of the paragraph);
  - each sentence is emitted on its own line, with no blank line inserted between them;
  - if the paragraph does not already end with a period, one is appended.
- Apply the rule only to plain prose paragraphs. Headings, list items, tables, block macros (`image::`, `include::`), attribute entries (`:name:`), block attribute lines (`[...]`), anchors (`[[...]]`), and line comments (`//`) are left unchanged.
- Abbreviations (`z.B.`), decimal numbers (`3.14`), URLs, and sentence endings followed by a quote or bracket are not treated as sentence boundaries.
- Wire the rule fixtures (`src/test/resources/spec/sentence-per-line/{README.md,before.adoc,after.adoc}`) into a fixture-driven test; formatting `before.adoc` must produce exactly `after.adoc`.
- Register the rule in `FormatRules.defaults()`.

## Capabilities

### New Capabilities

- `sentence-per-line`: the rule that splits plain prose paragraphs into one sentence per line and guarantees a trailing period, plus the fixture that defines its expected behavior.

### Modified Capabilities

<!-- none: `formatting-pipeline` semantics (ordered pipeline, pass-through, idempotency, line endings, final newline, delimited-block protection) are unchanged; only the default rule list grows. -->

## Impact

- Code: new rule implementation (`SentencePerLineRule`) and one more entry in `FormatRules.defaults()`.
- Tests: a fixture-driven test reading `src/test/resources/spec/sentence-per-line/{before,after}.adoc`, plus unit tests for the rule's edge cases and idempotency.
- No new dependencies; no CLI flag changes.
- Out of scope: abbreviations with a period followed by an uppercase word, content inside delimited `----` blocks (protected by the pipeline before rules run), tables, lists, and reflowing text that has no period at all.
