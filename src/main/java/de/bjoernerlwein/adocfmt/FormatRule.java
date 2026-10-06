package de.bjoernerlwein.adocfmt;

@FunctionalInterface
public interface FormatRule {
  String apply(String input);
}
