package de.bjoernerlwein.adocfmt;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class Formatter {

  private static final Pattern DELIMITED_FENCE = Pattern.compile("^(-{4,}|\\*{4,})\\s*$");

  // Rules run over the whole document, so protected lines are masked by an
  // index-only token instead of being cut out: rules still see a non-blank,
  // inert neighbour, which keeps block state and blank-line decisions correct.
  // ponytail: a literal U+0000 in the input would collide with the token
  // format; that is not valid AsciiDoc content.
  private static final char MASK = '\u0000';

  private final List<FormatRule> rules;

  public Formatter(List<FormatRule> rules) {
    this.rules = List.copyOf(rules);
  }

  public String format(String input) {
    String text = input.replace("\r\n", "\n").replace('\r', '\n');
    List<String> originals = new ArrayList<>();
    String masked = mask(text, originals);
    for (FormatRule rule : rules) {
      masked = rule.apply(masked);
    }
    return restore(masked, originals);
  }

  private static String mask(String text, List<String> originals) {
    String[] lines = text.split("\n", -1);
    List<String> out = new ArrayList<>(lines.length);
    boolean inBlock = false;
    char blockDelimiter = 0;
    for (String line : lines) {
      boolean fence = DELIMITED_FENCE.matcher(line).matches();
      if (fence) {
        char delimiter = line.trim().charAt(0);
        if (!inBlock) {
          inBlock = true;
          blockDelimiter = delimiter;
        } else if (delimiter == blockDelimiter) {
          inBlock = false;
        }
      }
      if (inBlock || fence) {
        out.add(MASK + Integer.toString(originals.size()) + MASK);
        originals.add(line);
      } else {
        out.add(line);
      }
    }
    return String.join("\n", out);
  }

  private static String restore(String text, List<String> originals) {
    if (originals.isEmpty()) {
      return text;
    }
    String[] lines = text.split("\n", -1);
    int restored = 0;
    for (int i = 0; i < lines.length; i++) {
      String line = lines[i];
      if (!line.isEmpty() && line.charAt(0) == MASK) {
        int end = line.indexOf(MASK, 1);
        if (end > 1) {
          lines[i] = originals.get(Integer.parseInt(line.substring(1, end)));
          restored++;
        }
      }
    }
    if (restored != originals.size()) {
      throw new IllegalStateException(
          "A format rule consumed a protected line (" + restored + "/" + originals.size() + ")");
    }
    return String.join("\n", lines);
  }
}
