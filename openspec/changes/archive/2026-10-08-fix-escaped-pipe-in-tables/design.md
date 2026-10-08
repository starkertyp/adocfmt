## Context

`TableCellSpacingRule` spacet alle `|` in Tabellenzeilen via `MARKER = \s*\|\s*` (replaceAll mit `" | "`). Ein escapter Pipe `\|` (z. B. Shell-Pipe in Inline-Code innerhalb einer Zelle) wird mitgematcht: aus `yaml \| grep` wird `yaml \ | grep` — Whitespace landet zwischen `\` und `|`, das Escape zerbricht und der `|` wird wieder zum Zellentrenner.

Die Rule ist line-based und sieht keinen Block-Kontext; Inline-Code (Backticks) wird nicht erkannt. Das ist auch das Design-Ceiling der Pipeline — der einzige verlässliche Marker ist der Backslash.

## Goals / Non-Goals

**Goals:**
- `\|` wird nicht als Zellenmarker behandelt, Zeichenpaar bleibt byte-genau unverändert.
- Unescape Pipes auf derselben Zeile werden weiterhin normal gespacet.
- Idempotenz bleibt erhalten.

**Non-Goals:**
- Vollständiges Backslash-Unescaping (`\\|` = escaped Backslash + echter Marker) — rare Edge Case.
- Erkennen von Inline-Code/Backticks oder anderem Block-Kontext.
- Änderungen an anderen Rules (`SentencePerLineRule` matched `\|` am Zeilenanfang bereits nicht).

## Decisions

**Negative Lookbehind statt Scanner/Refactor.** `MARKER` wird zu `\s*(?<!\\)\|\s*`. Java-Lookbehind ist fixed-width, ein Backslash-Zähler ist darin nicht ausdrückbar — ein manueller Scanner wäre mehr Code für einen Edge Case ohne realen Nutzen. Ein-Zeilen-Fix am Ursprung (alle Caller matchen über dasselbe Pattern). Alternativen verworfen: Split-basierter Parser (overkill), Lookbehind mit Backslash-Zählung (in Java-Regex nicht möglich).

**Fix nur in MARKER.** `LEADING`, `A_LEADING`, `EMPTY_CELL`, `EMPTY_A_CELL` ankern am Zeilenanfang; eine Zeile, die mit `\|` beginnt, matcht dort nicht (Backslash blockt). Keine Änderung nötig.

**Escaped-Pipe-Fälle in die bestehende `table`-Fixture.** Der Fixture-Test lädt hartkodiert `/spec/table/{before,after}.adoc`; neue Zeilen dort + README-Eintrag statt zweiter Fixture und Loader-Generalisierung.

## Risks / Trade-offs

- [`\\|` (escaped Backslash + echter Marker) wird nicht gespacet] → Absicht, mit `ponytail:`-Kommentar an der Pattern-Definition dokumentiert; aufwändige Lösung nur bei Bedarf.
- [Regex-Lookbehind übersieht Pipe nach ungerader Backslash-Kette] → Gleiches Ceiling wie oben, gleiche Stelle dokumentiert.
