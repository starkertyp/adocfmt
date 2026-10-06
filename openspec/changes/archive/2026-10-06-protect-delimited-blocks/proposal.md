## Why

The formatter currently applies every rule line-by-line with no block context, so AsciiDoc source shown inside a listing block fenced by `----` is reformatted too. A code sample about headings or tables would be silently mutated, corrupting the exact content the block is meant to preserve verbatim.

## What Changes

- Detect listing/delimited blocks fenced by lines of four or more `-` characters.
- Pass the content of a delimited block through completely unformatted, even when it contains valid AsciiDoc (headings, tables, cell markers).
- Emit the opening and closing fence lines unchanged.
- Protect an unclosed opening fence through the end of the document.
- Add a `spec/verbatim/` fixture (`before.adoc`/`after.adoc`/`README.md`) and unit tests for edge cases and idempotency.

## Capabilities

### New Capabilities

- `delimited-block-protection`: detection of `----`-fenced blocks and the guarantee that no rule modifies their content or fence lines.

### Modified Capabilities

- `formatting-pipeline`: rule application is segmented so registered rules only observe text outside protected delimited blocks.

## Impact

- `src/main/java/adocfmt/Formatter.java` gains a block-segmentation pre-pass around rule application (the upgrade already noted in its `ponytail:` comment).
- `HeadingBlankLineRule` and `TableCellSpacingRule` stop seeing protected content; their local `ponytail:` caveats about listing blocks are resolved.
- New fixture under `src/test/resources/spec/verbatim/` plus a corresponding test class.
- No CLI, API, or dependency changes.
