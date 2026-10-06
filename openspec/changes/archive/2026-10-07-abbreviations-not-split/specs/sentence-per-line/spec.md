## MODIFIED Requirements

### Requirement: Sentence boundary detection

A sentence boundary SHALL be a period immediately followed by whitespace and then an uppercase letter, or a period at the end of the paragraph. A period that is part of a common German abbreviation SHALL NOT create a boundary, even when it is followed by whitespace and an uppercase letter. Periods not matching this rule (for example in `3.14`, a URL, or before a quote or bracket) SHALL NOT create a boundary either.

Common German abbreviations are the period-bearing entries of the CharLingua reference list (<https://www.charlingua.de/post/abkuerzungen>) and SHALL be recognized case-insensitively. This includes both single-token abbreviations (`usw.`, `bzw.`, `ca.`, `evtl.`, `ggf.`, `inkl.`, `Abs.`, `Art.`, `Dr.`, `Jan.`, ...) and multi-part abbreviations of the form letter-dot-letter (`z.B.`, `d.h.`, `i.D.`, `m.a.W.`, `o.ä.`, ...).

#### Scenario: Period followed by an uppercase word

- **WHEN** a paragraph contains `Erster Satz. Zweiter Satz`
- **THEN** the boundary is placed between `Satz.` and `Zweiter`

#### Scenario: Period before a lowercase word is not a boundary

- **WHEN** a paragraph contains `z.B. ein Beispiel`
- **THEN** no boundary is placed and the text stays on one line

#### Scenario: Multi-part abbreviation before an uppercase word is not a boundary

- **WHEN** a paragraph contains `z.B. Der folgende Text`
- **THEN** no boundary is placed after `z.B.` and the text stays on one line

#### Scenario: Single-token abbreviation before an uppercase word is not a boundary

- **WHEN** a paragraph contains `usw. Das gilt weiter`
- **THEN** no boundary is placed after `usw.` and the text stays on one line

#### Scenario: Abbreviation is matched case-insensitively

- **WHEN** a paragraph contains `D.h. Das gilt`
- **THEN** no boundary is placed after `D.h.` and the text stays on one line

#### Scenario: Abbreviation does not prevent a boundary after the next real sentence

- **WHEN** a paragraph contains `usw. Der Satz endet hier. Ein neuer Satz`
- **THEN** the only boundary is after `hier.` between `hier.` and `Ein`

#### Scenario: Decimal number is not a boundary

- **WHEN** a paragraph contains `3.14 ist Pi`
- **THEN** no boundary is placed at the decimal point
