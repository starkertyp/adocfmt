package de.bjoernerlwein.adocfmt;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TableCellSpacingRule implements FormatRule {

  // Line-based table detection. Delimited blocks are masked by the Formatter,
  // so listing content only reaches this rule as opaque token lines without
  // cell markers and cannot toggle table state here.

  private static final Pattern FENCE = Pattern.compile("^\\|===\\s*$");
  // ponytail: lookbehind only skips a single backslash, so `\\|` (escaped
  // backslash + real marker) is left unspaced; count backslashes if ever needed
  private static final Pattern MARKER = Pattern.compile("\\s*(?<!\\\\)\\|\\s*");
  private static final Pattern LEADING = Pattern.compile("^\\s*\\|");
  // Cell style specifiers (a, d, e, h, l, m, s, v) directly attached to the leading `|`.
  private static final Pattern STYLE_LEADING = Pattern.compile("^\\s*([adehlmsv])\\|");
  private static final Pattern EMPTY_CELL = Pattern.compile("^\\|\\s*$");
  private static final Pattern EMPTY_STYLE_CELL = Pattern.compile("^\\s*([adehlmsv])\\|\\s*$");

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
        Matcher emptyStyle = EMPTY_STYLE_CELL.matcher(line);
        if (emptyStyle.matches()) {
          out.add(emptyStyle.group(1) + "|");
          continue;
        }
        String spaced = MARKER.matcher(line).replaceAll(" | ");
        if (LEADING.matcher(line).find()) {
          spaced = spaced.replaceFirst("^\\s*\\| ", "| ");
        } else if (STYLE_LEADING.matcher(line).find()) {
          spaced = spaced.replaceFirst("^\\s*([adehlmsv]) \\| ", "$1| ");
        }
        out.add(spaced);
      } else {
        out.add(line);
      }
    }
    return String.join("\n", out);
  }
}
