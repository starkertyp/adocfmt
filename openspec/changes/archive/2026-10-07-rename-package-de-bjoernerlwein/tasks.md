## 1. Move sources

- [x] 1.1 `git mv src/main/java/adocfmt src/main/java/de/bjoernerlwein/adocfmt`
- [x] 1.2 `git mv src/test/java/adocfmt src/test/java/de/bjoernerlwein/adocfmt`
- [x] 1.3 Update the `package` declaration in every moved Java source to `de.bjoernerlwein.adocfmt`

## 2. Maven coordinates

- [x] 2.1 Set `<groupId>de.bjoernerlwein</groupId>` in `pom.xml`
- [x] 2.2 Set `<main.class>de.bjoernerlwein.adocfmt.Main</main.class>` in `pom.xml`
- [x] 2.3 Confirm `artifactId`/`finalName` remain `adocfmt` and the CLI command name is unchanged

## 3. Verify

- [x] 3.1 Confirm no remaining `adocfmt` package declarations or `adocfmt.Main` references outside archives/AGENTS docs
- [x] 3.2 Run `google-java-format --replace` on changed Java files
- [x] 3.3 Run `mvn -q clean verify`
