## Why

The formatter only inserts a blank line after a heading, so a heading jammed
against the paragraph above it is left untouched. AsciiDoc relies on blank-line
separation for stable rendering, and the same separation is needed before a
heading. Anchors (`[[xxx]]`) attached to a heading complicate this: a blank line
between the anchor and its heading detaches the anchor from the heading.

## What Changes

- Insert a blank line before every heading when the preceding line is not blank.
- When a heading is immediately preceded by an anchor line (`[[xxx]]`), place the
  blank line before the anchor instead, leaving the anchor directly adjacent to
  the heading.
- Leave the text unchanged when the heading is already separated, when it is the
  first line of the document, or when the anchor already has a blank line above it.
- Keep the existing "blank line after every heading" behavior.

## Capabilities

### New Capabilities

None.

### Modified Capabilities

- `heading-formatting`: add a requirement for blank-line separation *before* a
  heading, including the anchor exception.

## Impact

- `src/main/java/adocfmt/HeadingBlankLineRule.java` (extended, no longer only
  "after").
- `src/test/resources/spec/headings/{before,after}.adoc` fixture and
  `src/test/java/adocfmt/HeadingBlankLineTest.java` edge-case tests.
- No CLI, API, or dependency changes.
