package de.bjoernerlwein.adocfmt;

import java.util.ArrayList;
import java.util.List;

public final class CollapseBlankLinesRule implements FormatRule {

  // Line-based. Delimited `----` blocks are withheld by the Formatter, so empty
  // lines inside listing/verbatim content are never reached by this rule.
  // ponytail: a whitespace-only line counts as content, not blank, so a run of
  // spaces is left untouched. Upgrade to trim-then-collapse when trailing
  // whitespace normalization is wanted.

  @Override
  public String apply(String input) {
    boolean trailingNewline = input.endsWith("\n");
    String body = trailingNewline ? input.substring(0, input.length() - 1) : input;
    String[] lines = body.split("\n", -1);
    List<String> out = new ArrayList<>(lines.length);
    boolean previousEmpty = false;
    for (String line : lines) {
      if (line.isEmpty()) {
        if (!previousEmpty) {
          out.add(line);
        }
        previousEmpty = true;
      } else {
        out.add(line);
        previousEmpty = false;
      }
    }
    String result = String.join("\n", out);
    return trailingNewline ? result + "\n" : result;
  }
}
