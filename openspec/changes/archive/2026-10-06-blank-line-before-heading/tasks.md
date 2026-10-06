## 1. Fixtures

- [x] 1.1 Extend `src/test/resources/spec/headings/before.adoc` with cases for a heading jammed against prior text and for anchor(s) above a heading
- [x] 1.2 Update `src/test/resources/spec/headings/after.adoc` to the expected formatted output for the new `before.adoc`
- [x] 1.3 Update `src/test/resources/spec/headings/README.md` to describe the blank line before a heading and the anchor exception

## 2. Rule Implementation

- [x] 2.1 Extend `HeadingBlankLineRule` to insert a blank line before a heading when the preceding line is non-blank
- [x] 2.2 Add anchor handling: recognize `[[...]]` lines, place the blank above the topmost anchor, and remove blanks between anchor and heading
- [x] 2.3 Keep the existing after-heading behavior and the documented no-block-context comment

## 3. Tests

- [x] 3.1 Update `HeadingBlankLineTest` edge cases: text before heading, heading before heading, first-line heading, anchor above heading, blank between anchor and heading, multiple stacked anchors
- [x] 3.2 Keep the fixture test and add/retain an idempotency assertion over the fixture

## 4. Verify

- [x] 4.1 Run `mvn -q verify` and fix any failures
- [x] 4.2 Run `google-java-format --replace` on changed Java files
