package de.bjoernerlwein.adocfmt;

import java.util.ArrayList;
import java.util.List;

public final class FormatRules {

  private FormatRules() {}

  public static List<FormatRule> defaults() {
    return defaults(false);
  }

  public static List<FormatRule> defaults(boolean sentencePerLine) {
    List<FormatRule> rules =
        new ArrayList<>(
            List.of(
                new CollapseBlankLinesRule(),
                new HeadingBlankLineRule(),
                new TableCellSpacingRule()));
    if (sentencePerLine) {
      rules.add(new SentencePerLineRule());
    }
    return List.copyOf(rules);
  }
}
