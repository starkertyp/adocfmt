# Mehrfache Leerzeilen zusammenfassen

Eine Leerzeile ist eine Zeile ohne Inhalt. Zwei oder mehr aufeinanderfolgende
Leerzeilen werden zu genau einer Leerzeile zusammengefasst. Eine einzelne Leerzeile
bleibt unverändert.

Zeilen, die nur aus Leerzeichen bestehen, gelten als Inhalt und bleiben unverändert.
Sie nehmen am Zusammenfassen nicht teil.

Leerzeilen innerhalb eines abgegrenzten `----`-Blocks werden von der Pipeline
geschützt und nicht zusammengefasst.

Die An- oder Abwesenheit eines abschließenden Zeilenumbruchs bleibt erhalten.
