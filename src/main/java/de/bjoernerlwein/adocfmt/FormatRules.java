package de.bjoernerlwein.adocfmt;

import java.util.List;

public final class FormatRules {

  private FormatRules() {}

  public static List<FormatRule> defaults() {
    return List.of(
        new CollapseBlankLinesRule(),
        new HeadingBlankLineRule(),
        new TableCellSpacingRule(),
        new SentencePerLineRule());
  }
}
