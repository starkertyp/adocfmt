## Context

All main and test sources live in the flat package `adocfmt`, and `pom.xml`
declares `groupId adocfmt` and `main.class adocfmt.Main`. The project is owned by
the `bjoernerlwein` namespace. There are no cross-package imports (all classes
are in one package), so the rename is purely mechanical: package declarations,
directory locations, and the `pom.xml` coordinates.

## Goals / Non-Goals

**Goals:**
- Move sources to `de.bjoernerlwein.adocfmt` and update `pom.xml` coordinates.
- Keep the build, tests, fixtures, CLI name, and output byte-identical.

**Non-Goals:**
- Renaming the CLI command (`adocfmt`) or the shaded jar `finalName`.
- Splitting classes into sub-packages or introducing modules.
- Changing any formatting rule or spec behavior.

## Decisions

- Use `git mv` to relocate files so history is preserved:
  `src/main/java/adocfmt/` → `src/main/java/de/bjoernerlwein/adocfmt/` and the
  same for tests.
- Update only the `package` line in each Java file. Since there are no
  `import adocfmt...` statements, no import rewrites are needed.
- `pom.xml`: set `groupId` to `de.bjoernerlwein`, keep `artifactId` `adocfmt`
  (and therefore `finalName`), set `main.class` to `de.bjoernerlwein.adocfmt.Main`.
- Tests load fixtures via absolute classpath paths (`/spec/...`), which do not
  include the package, so they need no change.

**Alternatives considered:**
- `de.bjoernerlwein` as the package with no `adocfmt` sub-package: rejected by
  the user in favor of `de.bjoernerlwein.adocfmt`.
- Rewriting via `sed`/script without `git mv`: rejected to preserve history.

## Risks / Trade-offs

- [Stale compiled classes under `target/` or IDE indexes] → run `mvn clean verify`
  and rely on the build to recompile from the new sources.
- [Missed reference to the old `main.class`/package] → covered by a full-repo
  search plus `mvn verify`; a test asserts the entry point still runs.

## Migration Plan

1. `git mv` both source trees into the namespaced directory.
2. Update `package` declarations in every moved file.
3. Update `pom.xml` (`groupId`, `main.class`).
4. `mvn -q clean verify`.
5. Rollback: revert the single commit; no data or external interface involved.
