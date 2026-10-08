# Tabellen

Tabellen sind immer zwischen zwei `|===`-Blöcken eingefasst.
Zellen werden mit einem `|`markiert.
Einem `|` zum Start der Zeile soll immer ein Leerzeichen folgen.
Mehrere `|` in einer Zeile sind zulässig, in dem Fall soll vor und hinter dem `|` ein Leerzeichen sein.
Eine Zelle kann am Zeilenanfang mit dem `a`-Zellen-Spezifizierer beginnen (`a|`), statt nur mit `|`.
In dem Fall steht kein Leerzeichen zwischen dem `a` und dem `|`, nach dem `|` folgt genau ein Leerzeichen.
Leerzeilen innerhalb der Tabelle sind zulässig.
Ein mit Backslash escapeter Pipe (`\|`) ist kein Zellentrenner und bleibt unverändert (z. B. Shell-Pipes in Befehlen: `cmd \| grep`).
