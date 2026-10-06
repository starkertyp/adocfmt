package de.bjoernerlwein.adocfmt;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class TableCellSpacingRule implements FormatRule {

  // Line-based table detection. Delimited `----` blocks are segments the
  // Formatter never exposes to this rule, so listing content cannot toggle
  // table state here.

  private static final Pattern FENCE = Pattern.compile("^\\|===\\s*$");
  private static final Pattern MARKER = Pattern.compile("\\s*\\|\\s*");
  private static final Pattern LEADING = Pattern.compile("^\\s*\\|");
  private static final Pattern A_LEADING = Pattern.compile("^\\s*a\\|");
  private static final Pattern EMPTY_CELL = Pattern.compile("^\\|\\s*$");
  private static final Pattern EMPTY_A_CELL = Pattern.compile("^\\s*a\\|\\s*$");

  @Override
  public String apply(String input) {
    String[] lines = input.split("\n", -1);
    List<String> out = new ArrayList<>(lines.length);
    boolean inTable = false;
    for (String line : lines) {
      if (FENCE.matcher(line).matches()) {
        inTable = !inTable;
        out.add(line);
      } else if (inTable && !line.isBlank() && line.indexOf('|') >= 0) {
        if (EMPTY_CELL.matcher(line).matches()) {
          out.add("|");
          continue;
        }
        if (EMPTY_A_CELL.matcher(line).matches()) {
          out.add("a|");
          continue;
        }
        String spaced = MARKER.matcher(line).replaceAll(" | ");
        if (LEADING.matcher(line).find()) {
          spaced = spaced.replaceFirst("^\\s*\\| ", "| ");
        } else if (A_LEADING.matcher(line).find()) {
          spaced = spaced.replaceFirst("^\\s*a \\| ", "a| ");
        }
        out.add(spaced);
      } else {
        out.add(line);
      }
    }
    return String.join("\n", out);
  }
}
