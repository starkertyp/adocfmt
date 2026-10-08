## Why

Ein Bug wurde entdeckt: In Tabellenzellen wird ein escapter Pipe (`\|`) als Zellen-Trennzeichen behandelt. Beispiel: `oc get configmap ... -o yaml \| grep ...` in einer Zelle wird zu `... yaml \ | grep ...` — die Rule schiebt Whitespace zwischen `\` und `|` und zerstört damit das Escaping; der resultierende `|` ist wieder ein echter Zellentrenner.

## What Changes

- `TableCellSpacingRule` erkennt `\|` (Backslash direkt vor Pipe) nicht mehr als Zellenmarker und lässt das Zeichenpaar unverändert.
- Bestehende Fixture `src/test/resources/spec/table/` (before/after/README) um escaped-Pipe-Zeilen erweitert, plus Unit-Tests für Edge-Cases inkl. Idempotenz.

## Capabilities

### New Capabilities

(keine)

### Modified Capabilities

- `table-formatting`: Inline-Zellenmarker-Spacing gilt nur für Pipes, die **nicht** durch einen Backslash escaped sind; `\|` bleibt unverändert.

## Impact

- `src/main/java/de/bjoernerlwein/adocfmt/TableCellSpacingRule.java` (MARKER-Pattern)
- `src/test/java/de/bjoernerlwein/adocfmt/TableCellSpacingTest.java`, `src/test/resources/spec/table/`
- Keine CLI-/API-Änderungen.
