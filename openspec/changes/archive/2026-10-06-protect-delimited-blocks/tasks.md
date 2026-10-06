## 1. Fixture

- [x] 1.1 Create `src/test/resources/spec/verbatim/README.md` describing the `----` protection rule.
- [x] 1.2 Create `before.adoc` with an unprotected heading/table plus a `----` block containing heading-like and table-like lines.
- [x] 1.3 Create `after.adoc` where only the unprotected content is formatted and the block is byte-for-byte identical.

## 2. Implementation

- [x] 2.1 Add a block-segmentation pre-pass to `Formatter.format` that partitions normalized lines into protected/unprotected runs, toggling on lines of four or more `-`.
- [x] 2.2 Apply the rule chain only to unprotected runs and reassemble with the existing line split/join semantics.
- [x] 2.3 Update the `ponytail:` comments in `Formatter.java`, `HeadingBlankLineRule.java`, and `TableCellSpacingRule.java` to reflect that block context now exists.

## 3. Tests

- [x] 3.1 Add `DelimitedBlockProtectionTest` covering: heading-like line inside a block untouched, table-like line inside a block untouched, fences unchanged, and unclosed block protects to EOF.
- [x] 3.2 Add a fixture test asserting `before.adoc` formats to `after.adoc`.
- [x] 3.3 Assert idempotency (`format(format(x)) == format(x)`) for the fixture and each edge case.

## 4. Verification

- [x] 4.1 Run `mvn -q verify`.
- [x] 4.2 Run `openspec validate protect-delimited-blocks`.
