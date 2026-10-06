## Context

`adocfmt` is a greenfield Maven/Java 21 project. The dev shell provides JDK 21 and Maven 3.9 only (no Gradle). The formatting rules are intentionally undefined; this change builds only the stable surrounding machinery so rules can be added as isolated units later. See `proposal.md` for motivation and `specs/` for requirements.

## Goals / Non-Goals

**Goals:**

- A runnable CLI that reads AsciiDoc files and emits formatted text.
- A stable `FormatRule` extension point and an ordered pipeline.
- Predictable, non-destructive defaults (nothing is written unless asked).
- A testable, pure core with no framework boot.

**Non-Goals:**

- Defining any concrete formatting rule.
- A document/AST model for AsciiDoc.
- Configuration files, plugin discovery, DI container, native image.
- Encoding detection beyond UTF-8; final-newline normalization.

## Decisions

### Build: Maven

Maven is the only build tool present in the dev shell. Gradle would require adding it to the Nix flake for no benefit.

### CLI: picocli, no DI container

A formatter is a pure function from text to text. Rules are injected as an ordered `List<FormatRule>` via constructor. This keeps the core unit-testable without a container and keeps startup trivial.

Alternatives considered:
- **picocli + Weld SE**: a CDI container boot for a millisecond tool; only justified if rules must be discovered/injected at runtime. Rejected as YAGNI.
- **Quarkus CLI**: attractive for a native single binary, but that is a distribution concern, not a skeleton concern. Rejected for now; revisit if a native binary is required.

### Rule model: line-based `String -> String`

```java
@FunctionalInterface
public interface FormatRule {
    String apply(String input);
}
```

The `Formatter` applies rules in a fixed order and returns the result. With an empty list it is a pass-through.

Alternatives considered:
- **Document/block model** (`parse blocks -> visit -> render`): needed only once a context-sensitive rule appears (e.g. formatting must skip listing/literal blocks). Deferred; premature without a known rule.

### I/O: stdout by default, `--write` opt-in

```
adocfmt [OPTIONS] <paths>...
  -w, --write      rewrite files in place
  -c, --check      exit 1 if any file would change
      --diff       print a unified diff of changes
      --stdin      read from standard input
```

Follows the prettier convention (non-destructive by default) rather than gofmt's in-place default.

### Line endings: normalize to `\n`

Input CRLF/CR is normalized to LF. Final newline is left untouched (spec-level: not enforced). UTF-8 is assumed for read and write.

### Diff output: java-diff-utils

`--diff` prints a unified diff via `io.github.java-diff-utils:java-diff-utils`. A correct unified diff (LCS + hunks) is not a few lines of code; the library is tiny and avoids a hand-rolled diff.

Diff is computed on **raw lines split on `\n`, preserving `\r`** — matching Unix `diff`. This makes a CRLF→LF change visible as changed lines; `String.lines()` would strip `\r` and produce an empty diff.

### Exit codes

`0` success; `1` when `--check` finds files that would change; `2` for usage/IO errors (unknown flag, missing file). Chosen for gofmt/ci familiarity.

## Risks / Trade-offs

- **Line-based rules can corrupt block content** (e.g. indentation inside a `----` listing block). → Marked with a `ponytail:` comment on the pipeline; upgrade to a block-segmentation pre-pass as soon as a context-sensitive rule is added.
- **Arbitrary rules may not be idempotent.** → The test harness asserts `format(format(x)) == format(x)` for any rule under test; the guarantee is documented in the spec.
- **Whole-file in-memory processing** on very large files. → Acceptable for source documents; revisit streaming only if it becomes a problem.

## Migration Plan

None — new project, no existing users.

## Open Questions

- Should final-newline behavior be normalized (enforce single trailing `\n`) later? Currently out of scope.
- Exact `--diff` output format (unified context lines) unspecified beyond "unified diff".
