# project-namespace Specification

## Purpose

The published namespace of the `adocfmt` project: Maven coordinates, Java package structure, and application entry point.

## Requirements

### Requirement: Namespace of published classes

The project SHALL publish its Maven artifact and Java classes under the
`bjoernerlwein` namespace: the Maven `groupId` SHALL be `de.bjoernerlwein`, the
`artifactId` SHALL be `adocfmt`, and every Java source SHALL declare a package
under `de.bjoernerlwein.adocfmt`. The application entry point SHALL be
`de.bjoernerlwein.adocfmt.Main`.

#### Scenario: Maven coordinates use the namespace

- **WHEN** the project `pom.xml` is read
- **THEN** its `groupId` is `de.bjoernerlwein` and its `artifactId` is `adocfmt`

#### Scenario: Sources declare the namespaced package

- **WHEN** any Java source under `src/main/java` or `src/test/java` is read
- **THEN** it declares a package starting with `de.bjoernerlwein.adocfmt`

#### Scenario: Entry point is the namespaced Main

- **WHEN** the build is run
- **THEN** the configured main class is `de.bjoernerlwein.adocfmt.Main`

#### Scenario: CLI behavior is unchanged by the rename

- **WHEN** the formatter is run on any fixture before and after the rename
- **THEN** the output, flags, and exit codes are identical
