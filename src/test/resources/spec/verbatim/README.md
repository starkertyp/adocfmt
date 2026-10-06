# Verbatim-Blöcke

Blöcke, die durch `----`- oder `****`-Linien eingefasst sind, werden vom Formatter
unverändert durchgelassen. Innerhalb eines solchen Blocks wird keine Regel
angewendet, auch wenn der Inhalt gültiges AsciiDoc ist (Überschriften,
Tabellenzellen). Die ein- und auszäunenden `----`- bzw. `****`-Zeilen bleiben
ebenfalls unverändert. Ein Block wird nur von einer Zaunzeile desselben
Zeichens (`-` oder `*`) geschlossen, sodass verschachtelte Blöcke geschützt
bleiben.
