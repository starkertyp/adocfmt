# Ein Satz pro Zeile

Ein Fließtext-Absatz besteht aus aufeinanderfolgenden Nicht-Leerzeilen, die keine
strukturellen AsciiDoc-Zeilen sind. Überschriften, Listenpunkte, Tabellenzeilen
(beginnend mit `|` oder dem `a|`-Zellen-Spezifizierer),
Block-Makros, Attribut-Einträge, Block-Attribute (`[...]`), Anker (`[[...]]`),
Block-Titel bzw. Captions (eine Zeile, die mit `.` beginnt), Steuerzeilen (eine
Zeile, die nur aus `<` besteht) und Zeilenkommentare (`//`) sind keine Absätze und
bleiben unverändert.

Innerhalb eines Absatzes beginnt nach jedem Satzende eine neue Zeile. Ein Satzende
ist ein Punkt, auf den Leerzeichen und ein Großbuchstabe folgen, sowie der Punkt am
Absatzende. Abkürzungen (`z.B.`), Dezimalzahlen (`3.14`) und URLs lösen keinen
Umbruch aus. Zwischen den Sätzen steht keine Leerzeile.

Gängige deutsche Abkürzungen (Referenz:
<https://www.charlingua.de/post/abkuerzungen>) lösen auch dann keinen Umbruch aus,
wenn danach ein Großbuchstabe folgt. Das gilt sowohl für mehrteilige Abkürzungen
(`z.B.`, `d.h.`, `m.a.W.`) als auch für einteilige (`usw.`, `bzw.`, `ggf.`). Die
Abkürzung bleibt dabei in derselben Zeile wie der folgende Text.

Endet ein Absatz nicht mit einem Punkt, wird am letzten Satz ein Punkt ergänzt.
Eine Ausnahme bildet eine Zeile, die mit `:` endet (z.B. eine Einleitung vor
einer Liste): hier wird kein Punkt ergänzt.

Über mehrere Quellzeilen umgebrochene Sätze werden zu einer Zeile zusammengefügt.
