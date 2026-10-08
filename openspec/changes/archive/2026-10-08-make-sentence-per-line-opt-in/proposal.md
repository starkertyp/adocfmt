## Why

Real-world use shows `SentencePerLineRule` misfires on too many edge cases (prose classification, sentence splitting, forced periods); a rule that rewrites prose paragraphs must not run silently by default. Making it opt-in keeps the default formatter safe while keeping the rule available for those who want it.

## What Changes

- **BREAKING**: Remove `SentencePerLineRule` from the default rule set. By default the CLI no longer splits prose into one sentence per line and no longer appends missing periods.
- Add CLI flag `--sentence-per-line` that enables `SentencePerLineRule` for the run (works with all modes: default stdout, `--write`, `--check`, `--diff`, `--stdin`).
- Rule behavior itself is unchanged; only its activation changes.
- Update `README.md` (rule table note + new flag).

## Capabilities

### New Capabilities

None.

### Modified Capabilities

- `format-cli`: The default rule set excludes the sentence-per-line rule (**BREAKING**), and a new `--sentence-per-line` flag enables it.

## Impact

- `src/main/java/de/bjoernerlwein/adocfmt/FormatRules.java` — default rule list no longer contains `SentencePerLineRule`; parameterized variant for opt-in.
- `src/main/java/de/bjoernerlwein/adocfmt/FormatCommand.java` — new `--sentence-per-line` option; formatter built from the flag.
- `src/test/java/de/bjoernerlwein/adocfmt/FormatCommandTest.java` — tests for default-off and opt-in-on.
- `README.md` — flag documentation and rule table note.
- Existing `SentencePerLineTest` and fixtures stay valid (they test the rule directly, still enabled).
- Users' scripts/pre-commit hooks that relied on prose reformatting must pass the new flag.
