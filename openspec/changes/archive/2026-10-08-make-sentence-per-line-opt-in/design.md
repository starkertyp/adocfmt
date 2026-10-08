## Context

`FormatRules.defaults()` (FormatRules.java:9-15) returns the fixed rule list, with `SentencePerLineRule` last. `FormatCommand` holds one `final` formatter built from that list at construction (FormatCommand.java:49), before picocli has parsed options. The rule's behavior itself is unchanged and stays covered by `SentencePerLineTest` + fixtures; only its activation moves.

## Goals / Non-Goals

**Goals:**

- Sentence-per-line off by default, on via `--sentence-per-line`.
- Works across all CLI modes (stdout, `--write`, `--check`, `--diff`, `--stdin`).
- Rule order preserved: sentence-per-line still runs last when enabled.

**Non-Goals:**

- No generic per-rule flag mechanism (`--rule <name>`) — one flag for one rule; add the generic mechanism when a second opt-in rule appears.
- No config file support.
- No changes to `SentencePerLineRule` behavior or its fixtures/spec.

## Decisions

- **Parameterize `FormatRules` instead of filtering in the CLI.** `FormatRules.defaults()` becomes the always-on list (sentence rule removed); a new overload `defaults(boolean sentencePerLine)` appends `SentencePerLineRule` last when true. The CLI knows about rule selection only through this one call; keeping the knowledge in `FormatRules` avoids leaking rule wiring into `FormatCommand`.
  - Alternative considered: `FormatRules.defaults()` stays full and the CLI removes the rule when the flag is absent — rejected; a "remove" filter implies opt-out and puts selection logic in the wrong place.
- **Build the formatter lazily in `call()`.** The `final` field initializer runs before option parsing, so the formatter must be constructed after parsing (e.g., assign the field at the top of `call()`). No `CommandLine` plumbing or provider needed.
- **Boolean flag, not value-taking.** `--sentence-per-line` present = enabled. Matches picocli `@Option` boolean semantics, one line.

## Risks / Trade-offs

- [Breaking default behavior] Users relying on prose reformatting see different output → mitigated by README note and the explicit `--sentence-per-line` escape hatch; `--check`/`--diff` users will notice smaller diffs, not corruption.
- [Future second opt-in rule makes booleans clumsy] → at that point add a generic mechanism; not now (YAGNI).
