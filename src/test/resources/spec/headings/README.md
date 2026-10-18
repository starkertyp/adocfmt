# Behandlung von Überschriften

Überschriften beginnen immer mit einem oder mehreren =-Zeichen.
Auf eine Überschrift folgt *immer* eine Leerzeile.
Vor eine Überschrift gehört ebenfalls eine Leerzeile, sobald davor Inhalt steht.

Eine Ausnahme gilt für Anker (`[[xxx]]`) direkt über einer Überschrift:
Die Leerzeile steht dann *vor* dem Anker, und zwischen Anker und Überschrift
steht keine Leerzeile. Mehrere aufeinanderfolgende Anker bleiben zusammen.

Ein `----`- oder `****`-Blockzaun zählt als Inhalt: Steht eine Überschrift direkt davor oder dahinter, wird die Leerzeile genauso gesetzt wie bei normalem Text. Ein Anker direkt hinter einem schließenden Zaun bekommt die Leerzeile vor dem Anker.
