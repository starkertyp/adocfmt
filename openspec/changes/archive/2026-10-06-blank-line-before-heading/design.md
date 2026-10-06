## Context

`HeadingBlankLineRule` currently only guarantees a blank line *after* a heading.
The new requirement adds symmetric separation *before* a heading, with an anchor
(`[[xxx]]`) exception. The pipeline is line-based with no block context
(verbatim/listing blocks are not detected), a ceiling already documented in the
rule and the formatter.

## Goals / Non-Goals

**Goals:**
- Guarantee a blank line before every heading that has preceding content.
- When one or more anchor lines sit directly above a heading, put the blank line
  before the anchor run and keep the last anchor directly adjacent to the heading.
- Preserve existing after-heading behavior and idempotency.

**Non-Goals:**
- Block context (headings/anchors inside listing or literal blocks are still
  treated as ordinary lines). Unchanged ceiling.
- Anchors written on the same line as a heading (`[[id]] = Title`).
- Collapsing multiple pre-existing blank lines into exactly one.

## Decisions

**Extend `HeadingBlankLineRule` instead of adding a new rule.** The before- and
after-separation of a heading interact (a heading followed by an anchor run that
is followed by another heading), so they must be coordinated in a single pass.
Keeping one rule also preserves the single `headings` fixture and the
`FormatRules.defaults()` wiring. Alternative: a second rule ordered before the
existing one — rejected because the anchor lookback and the after-insertion can
both touch the same gap.

**Anchor line = a line whose trimmed content is `[[...]]`.** Matches `[[id]]`
and `[[id,reftext]]` anchors on their own line. This is the common AsciiDoc
form and keeps the rule line-based; no parser.

**Anchor lookback skips existing blanks.** When handling a heading, collect the
maximal run of anchor lines above it, ignoring blank lines in between. This lets
the rule both (a) place the separating blank above the topmost anchor and
(b) remove a stray blank that would detach the anchor from the heading, satisfying
"no blank line between anchor and heading". Anchors stay contiguous.

**Idempotency.** After normalization the line above the heading is the anchor
(or the line above a blank is content), so a second run inserts nothing and
removes nothing. A run over the new fixture asserts `format(format(x)) == format(x)`.

## Risks / Trade-offs

- Blank/heading lines inside listing blocks still get a spurious blank line →
  pre-existing ceiling, unchanged; documented in the rule and out of scope here.
- Removing a pre-existing blank between anchor and heading is destructive if a
  user deliberately wrote it → intended by the requirement ("no blank line
  between anchor and heading"); noted as deliberate.
