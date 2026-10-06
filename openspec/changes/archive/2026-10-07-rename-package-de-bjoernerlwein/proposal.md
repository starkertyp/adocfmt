## Why

The Java sources use the top-level package `adocfmt` and the Maven coordinates
`groupId adocfmt`. The project belongs to the `bjoernerlwein` namespace
(see the Nexus host `nexus.local.bjoernerlwein.de`), so the package and group id
should reflect that ownership.

## What Changes

- Rename the Java package `adocfmt` to `de.bjoernerlwein.adocfmt` for all main
  and test sources (move files under `src/main/java/de/bjoernerlwein/adocfmt/`
  and `src/test/java/de/bjoernerlwein/adocfmt/`, update `package` declarations).
- Update `pom.xml`: `groupId` becomes `de.bjoernerlwein`, `artifactId` stays
  `adocfmt`, and `main.class` becomes `de.bjoernerlwein.adocfmt.Main`.
- No formatting behavior, CLI flags, exit codes, or fixtures change. The CLI
  command name (`adocfmt`) and the shaded jar `finalName` (`adocfmt`) stay.

## Capabilities

### New Capabilities
- `project-namespace`: the Maven group id and the Java base package under which
  the formatter classes are published.

### Modified Capabilities
<!-- none -->

## Impact

- `src/main/java/adocfmt/**` → `src/main/java/de/bjoernerlwein/adocfmt/**`
- `src/test/java/adocfmt/**` → `src/test/java/de/bjoernerlwein/adocfmt/**`
- `pom.xml` (`groupId`, `main.class`)
- No changes to `openspec/specs/`, resources, or CLI behavior.
