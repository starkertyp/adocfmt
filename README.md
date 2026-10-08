# adocfmt

Ein Formatierer für [AsciiDoc](https://asciidoc.org/)-Dateien als
Kommandozeilen-Werkzeug (Java 21, Maven). `adocfmt` vereinheitlicht
wiederkehrende Schreibweisen in AsciiDoc-Quellen — Leerzeilen, Überschriften,
Tabellenzellen und Absätze —, damit Dokumente unabhängig vom Autor konsistent
bleiben.

> [!NOTE]
> Dieses Projekt wird **KI-unterstützt** umgesetzt: Konzeption, Code und Tests
> entstehen im Zusammenspiel eines Menschen mit einem KI-Assistenten
> ([opencode](https://opencode.ai)). Änderungen werden dennoch reviewed und durch
> Tests abgesichert.

## Verhalten

Die Pipeline ist zeilenbasiert und normalisiert zunächst die Zeilenenden auf
`\n`. Danach laufen die Regeln in folgender Reihenfolge:

| Regel | Wirkung |
| --- | --- |
| `CollapseBlankLinesRule` | Fasst mehrere aufeinanderfolgende Leerzeilen zu einer zusammen. |
| `HeadingBlankLineRule` | Erzwingt genau eine Leerzeile um Überschriften (Anker ausgenommen). |
| `TableCellSpacingRule` | Vereinheitlicht Leerzeichen um `|` in Tabellenzeilen. Escapte Pipes (`\|`) gelten nicht als Zelltrenner und bleiben unverändert. |
| `SentencePerLineRule` | Setzt Fließtext auf einen Satz pro Zeile, ergänzt fehlende Punkte. **Opt-in** via `--sentence-per-line`. |

In `----`- und `****`-Blöcken (Verbatim/Listing) wird nichts verändert. Jede
Regel ist idempotent: `format(format(x)) == format(x)`.

Die genaue Spezifikation jeder Regel steht als Fixture unter
`src/test/resources/spec/<regel>/README.md` (mit `before.adoc` / `after.adoc`).

## Voraussetzungen

- JDK 21
- Maven 3.9+

Das Repository bringt einen Nix-Devshell (`flake.nix`) mit, der JDK, Maven,
`google-java-format` und `openspec` bereitstellt. Mit `direnv` (oder
`nix develop`) landen sie automatisch auf dem `PATH`.

## Bauen und testen

```bash
mvn -q verify               # Kompilieren, Tests, Paket bauen
mvn -q test -Dtest=FormatterTest   # eine einzelne Testklasse
```

Das baut ein ausführbares JAR: `target/adocfmt.jar`.

## Verwendung

```bash
# Ausgabe nach stdout (Standard, ohne Änderung an der Datei)
java -jar target/adocfmt.jar docs/handbuch.adoc

# Mehrere Dateien, Ausgabe für alle nach stdout
java -jar target/adocfmt.jar docs/*.adoc

# Dateien in-place schreiben
java -jar target/adocfmt.jar --write docs/handbuch.adoc

# Prüfen (Exit-Code 1, wenn eine Datei geändert würde)
java -jar target/adocfmt.jar --check docs/*.adoc

# Unified Diff ausgeben
java -jar target/adocfmt.jar --diff docs/handbuch.adoc

# Von stdin lesen
cat docs/handbuch.adoc | java -jar target/adocfmt.jar --stdin
```

### Optionen

| Option | Bedeutung |
| --- | --- |
| `-w`, `--write` | Dateien direkt überschreiben. |
| `-c`, `--check` | Nur prüfen; Exit-Code `1`, falls Änderungen nötig wären. |
| `--diff` | Statt des Ergebnisses einen Unified Diff ausgeben. |
| `--stdin` | Eingabe von der Standardeingabe lesen. |
| `--sentence-per-line` | Opt-in: die `SentencePerLineRule` für diesen Lauf aktivieren. |
| `-h`, `--help` | Hilfe anzeigen (via picocli). |
| `-V`, `--version` | Version anzeigen. |

Exit-Codes: `0` Erfolg, `1` Änderungen nötig (`--check`), `2` Nutzungs- oder
E/A-Fehler.

## Projektstruktur

```
src/main/java/de/bjoernerlwein/adocfmt/
  FormatRule.java        # String -> String
  FormatRules.java       # Registry der Standardregeln
  Formatter.java         # Pipeline (Zeilenenden normalisieren, Regeln anwenden)
  *Rule.java             # einzelne Formatierungsregeln
  FormatCommand.java     # CLI (picocli)
  Main.java              # Einstiegspunkt
src/test/resources/spec/ # Fixtures je Regel (README, before.adoc, after.adoc)
openspec/                # Spec-getriebene Änderungsplanung
```

## Mitwirken

- Jede neue Regel ist eine `FormatRule` und wird in `FormatRules.defaults()`
  registriert.
- Zu jeder Regel gehört ein Fixture unter `src/test/resources/spec/<regel>/`
  plus Tests für Randfälle; jeder Test prüft die Idempotenz.
- Java-Code vor dem Commit formatieren:
  `google-java-format --replace <datei>` (der Pre-Commit-Hook erzwingt das).

Konzeptionelle Details und aktuell gültige Spezifikationen liegen unter
`openspec/` (schema-getrieben). Einstiegspunkte für Mitwirkende und Assistenten:
`AGENTS.md`.

## Lizenz

Lizenziert unter der [EUPL-1.2](LICENSE).
