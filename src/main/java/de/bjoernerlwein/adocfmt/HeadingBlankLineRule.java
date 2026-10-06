package de.bjoernerlwein.adocfmt;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class HeadingBlankLineRule implements FormatRule {

  // Line-based. Delimited `----` blocks are segments the Formatter never
  // exposes to this rule, so listing content cannot be reformatted here.

  private static final Pattern HEADING = Pattern.compile("^=+ ");
  private static final Pattern ANCHOR = Pattern.compile("^\\s*\\[\\[.*\\]\\]\\s*$");

  @Override
  public String apply(String input) {
    String[] lines = input.split("\n", -1);
    List<String> out = new ArrayList<>(lines.length);
    for (int i = 0; i < lines.length; i++) {
      String line = lines[i];
      if (HEADING.matcher(line).find()) {
        separateBeforeHeading(out);
        out.add(line);
        boolean nextExists = i + 1 < lines.length;
        if (nextExists && !lines[i + 1].isBlank()) {
          out.add("");
        }
      } else {
        out.add(line);
      }
    }
    return String.join("\n", out);
  }

  private static void separateBeforeHeading(List<String> out) {
    int anchorTop = -1;
    for (int j = out.size() - 1; j >= 0; j--) {
      String candidate = out.get(j);
      if (candidate.isBlank()) {
        continue;
      }
      if (ANCHOR.matcher(candidate).matches()) {
        anchorTop = j;
        continue;
      }
      break;
    }
    if (anchorTop >= 0) {
      for (int j = out.size() - 1; j > anchorTop; j--) {
        if (out.get(j).isBlank()) {
          out.remove(j);
        }
      }
      if (anchorTop > 0 && !out.get(anchorTop - 1).isBlank()) {
        out.add(anchorTop, "");
      }
    } else if (!out.isEmpty() && !out.get(out.size() - 1).isBlank()) {
      out.add("");
    }
  }
}
