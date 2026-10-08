## 1. Rule-Fix

- [x] 1.1 `TableCellSpacingRule`: `MARKER` um negatives Lookbehind erweitern (`\s*(?<!\\)\|\s*`), mit `ponytail:`-Kommentar zum `\\|`-Ceiling an der Pattern-Definition

## 2. Tests

- [x] 2.1 Fixture `src/test/resources/spec/table/`: escaped-Pipe-Zeilen in `before.adoc`/`after.adoc` ergänzen (Inline-Command-Beispiel aus dem Bugreport, `\|` zwischen echten Markern), README um Escaping-Hinweis erweitern
- [x] 2.2 `TableCellSpacingTest`: Unit-Tests — `\|` unverändert, unescape Marker derselben Zeile gespacet, Idempotenz

## 3. Verifikation

- [x] 3.1 `mvn -q verify` grün; `google-java-format --replace` auf geänderten Java-Dateien
