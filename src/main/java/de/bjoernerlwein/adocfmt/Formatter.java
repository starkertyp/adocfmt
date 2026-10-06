package de.bjoernerlwein.adocfmt;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class Formatter {

  private static final Pattern DELIMITED_FENCE = Pattern.compile("^(-{4,}|\\*{4,})\\s*$");

  private final List<FormatRule> rules;

  public Formatter(List<FormatRule> rules) {
    this.rules = List.copyOf(rules);
  }

  public String format(String input) {
    String text = input.replace("\r\n", "\n").replace('\r', '\n');
    String[] lines = text.split("\n", -1);
    List<String> out = new ArrayList<>(lines.length);
    List<String> run = new ArrayList<>();
    boolean inBlock = false;
    char blockDelimiter = 0;
    boolean runProtected = false;
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
      boolean protectedLine = inBlock || fence;
      if (!run.isEmpty() && protectedLine != runProtected) {
        emit(run, runProtected, out);
        run.clear();
      }
      runProtected = protectedLine;
      run.add(line);
    }
    emit(run, runProtected, out);
    return String.join("\n", out);
  }

  private void emit(List<String> run, boolean protectedRun, List<String> out) {
    if (run.isEmpty()) {
      return;
    }
    if (protectedRun) {
      out.addAll(run);
      return;
    }
    String text = String.join("\n", run);
    for (FormatRule rule : rules) {
      text = rule.apply(text);
    }
    out.addAll(List.of(text.split("\n", -1)));
  }
}
