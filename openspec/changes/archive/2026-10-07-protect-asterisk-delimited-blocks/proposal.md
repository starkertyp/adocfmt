## Why

A line consisting of four or more `*` characters is an AsciiDoc special-block fence (sidebar), analogous to `----`. The pipeline only protects `----` blocks today, so content inside a `****` block is reformatted and corrupted.

## What Changes

- Recognize `****` (four or more `*`, ignoring trailing whitespace) as a delimited-block fence, exactly like `----`.
- Protect all lines inside such a block from every rule, and emit the fence lines unchanged.
- Track the opening delimiter so a fence only closes a block opened by the same delimiter; mixed/nested `----` and `****` blocks stay protected.
- Extend the `verbatim` fixture and unit tests to cover `****` blocks.

## Capabilities

### New Capabilities

<!-- none -->

### Modified Capabilities

- `delimited-block-protection`: fence detection broadened from `-` runs to `-` or `*` runs, with same-delimiter closing.
- `formatting-pipeline`: protected-segment wording broadened from `----` fences to `----` or `****` fences.

## Impact

- `src/main/java/adocfmt/Formatter.java` (fence detection and block state).
- `src/test/resources/spec/verbatim/` fixtures and `DelimitedBlockProtectionTest`.
- No CLI, API, or dependency changes.
