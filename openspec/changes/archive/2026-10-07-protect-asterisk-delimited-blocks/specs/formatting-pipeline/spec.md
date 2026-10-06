## MODIFIED Requirements

### Requirement: Ordered rule pipeline

The formatter SHALL apply an ordered collection of `FormatRule` instances and return the resulting text. Rules SHALL be applied only to text outside delimited blocks protected by `----` or `****` fences; protected segments (including their fence lines) SHALL pass through unchanged.

#### Scenario: Rules apply in order

- **WHEN** the formatter is given two rules and an input
- **THEN** the output equals applying the first rule to the input and the second rule to that result

#### Scenario: Protected segments bypass every rule

- **WHEN** the input contains a `----`-fenced or `****`-fenced block
- **THEN** no rule is applied to the lines between the fences, regardless of rule order
