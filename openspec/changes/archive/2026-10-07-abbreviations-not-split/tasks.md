## 1. Implementation

- [x] 1.1 Add an abbreviation set (lowercased, from the CharLingua reference) and a multi-part pattern `\p{L}(?:\.\p{L})+\.` to `SentencePerLineRule`
- [x] 1.2 Add a helper that reports whether a sentence's trailing token is an abbreviation
- [x] 1.3 In `flush`, merge a split sentence into the previous one when the previous sentence ends with an abbreviation
- [x] 1.4 Add a `ponytail:` comment noting list completeness as the ceiling

## 2. Tests

- [x] 2.1 Extend `sentence-per-line` fixture (`before.adoc`/`after.adoc` and README) with abbreviation-before-uppercase cases (`z.B. Der ...`, `usw. Das ...`)
- [x] 2.2 Add unit tests for multi-part (`z.B. Der`), single-token (`usw. Das`), case-insensitive (`D.h. Das`), and a following real boundary (`usw. Der Satz endet hier. Ein neuer Satz`)
- [x] 2.3 Assert idempotency for the abbreviation cases

## 3. Verify

- [x] 3.1 Run `google-java-format --replace` on changed Java files
- [x] 3.2 Run `mvn -q verify`
