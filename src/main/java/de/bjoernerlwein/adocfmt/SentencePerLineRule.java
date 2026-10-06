package de.bjoernerlwein.adocfmt;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

public final class SentencePerLineRule implements FormatRule {

  // Line-based prose classifier. Delimited `----` blocks are withheld by the
  // Formatter, so listing/verbatim content never reaches this rule. The prefix
  // list does not parse AsciiDoc blocks; add prefixes when a real document
  // needs them.
  // ponytail: does not split before a quote or bracket (`Ende."`); upgrade to a
  // richer tokenizer when needed.
  private static final Pattern NON_PROSE =
      Pattern.compile(
          "^=+ "
              + "|^\\s*[*\\-]+\\s"
              + "|^\\s*\\d+\\.\\s"
              + "|^\\s*a?\\|"
              + "|^[A-Za-z][\\w-]*::"
              + "|^:[^:]+:"
              + "|^\\s*\\[.*\\]\\s*$"
              + "|^\\."
              + "|^<+$"
              + "|^//");

  private static final Pattern BOUNDARY = Pattern.compile("(?<=\\.)\\s+(?=[A-ZÄÖÜ])");

  // strip() skips non-breaking spaces (U+00A0) because Character.isWhitespace
  // excludes them; \p{Z} covers every Unicode separator.
  private static final Pattern EDGE_WHITESPACE = Pattern.compile("^[\\s\\p{Z}]+|[\\s\\p{Z}]+$");

  // Multi-part abbreviations look like letter.letter(.letter) and end in a
  // period (z.B., d.h., m.a.W.), so their shape is enough to recognize them.
  private static final Pattern MULTIPART_ABBREVIATION = Pattern.compile("\\p{L}(?:\\.\\p{L})+\\.");

  // Period-bearing single-token abbreviations from
  // https://www.charlingua.de/post/abkuerzungen, lowercased.
  // ponytail: hardcoded list; extend it when a real document needs more.
  private static final Set<String> ABBREVIATIONS =
      Set.of(
          "abb.",
          "abk.",
          "abs.",
          "allg.",
          "alt.",
          "aufl.",
          "bes.",
          "bspw.",
          "bsp.",
          "bzw.",
          "ca.",
          "dazw.",
          "desgl.",
          "ehem.",
          "eigtl.",
          "einschl.",
          "entspr.",
          "etw.",
          "evtl.",
          "ggf.",
          "ggü.",
          "inkl.",
          "jmd.",
          "kompl.",
          "mwst.",
          "pers.pron.",
          "poss.pron.",
          "pkt.",
          "rel.satz.",
          "st.",
          "tel.",
          "u.",
          "ugs.",
          "urspr.",
          "usw.",
          "vgl.",
          "verh.",
          "vh.",
          "wdh.",
          "zzgl.",
          "zzt.",
          "adj.",
          "adv.",
          "art.",
          "dt.",
          "fut.",
          "imp.",
          "konj.",
          "präp.",
          "pl.",
          "refl.",
          "nom.",
          "akk.",
          "dat.",
          "gen.",
          "bhf.",
          "hbf.",
          "hr.",
          "fr.",
          "frl.",
          "dr.",
          "geb.",
          "unverh.",
          "led.",
          "gesch.",
          "verw.",
          "sek.",
          "min.",
          "std.",
          "tägl.",
          "mtl.",
          "jährl.",
          "inzw.",
          "abds.",
          "jh.",
          "jan.",
          "feb.",
          "apr.",
          "aug.",
          "sept.",
          "okt.",
          "nov.",
          "dez.",
          "mio.",
          "kal.");

  @Override
  public String apply(String input) {
    String[] lines = input.split("\n", -1);
    List<String> out = new ArrayList<>(lines.length);
    List<String> paragraph = new ArrayList<>();
    for (String line : lines) {
      if (isProse(line)) {
        paragraph.add(EDGE_WHITESPACE.matcher(line).replaceAll(""));
      } else {
        flush(paragraph, out);
        out.add(line);
      }
    }
    flush(paragraph, out);
    return String.join("\n", out);
  }

  private static boolean isProse(String line) {
    return !line.isBlank() && !NON_PROSE.matcher(line).find();
  }

  private static void flush(List<String> paragraph, List<String> out) {
    if (paragraph.isEmpty()) {
      return;
    }
    String[] sentences = BOUNDARY.split(String.join(" ", paragraph));
    paragraph.clear();
    List<String> merged = new ArrayList<>(sentences.length);
    for (String raw : sentences) {
      String sentence = EDGE_WHITESPACE.matcher(raw).replaceAll("");
      if (!merged.isEmpty() && endsWithAbbreviation(merged.get(merged.size() - 1))) {
        int last = merged.size() - 1;
        merged.set(last, merged.get(last) + " " + sentence);
      } else {
        merged.add(sentence);
      }
    }
    for (int i = 0; i < merged.size(); i++) {
      String sentence = merged.get(i);
      if (i == merged.size() - 1 && !sentence.endsWith(".") && !sentence.endsWith(":")) {
        sentence = sentence + ".";
      }
      out.add(sentence);
    }
  }

  private static boolean endsWithAbbreviation(String text) {
    int space = text.lastIndexOf(' ');
    String last = space < 0 ? text : text.substring(space + 1);
    return MULTIPART_ABBREVIATION.matcher(last).matches()
        || ABBREVIATIONS.contains(last.toLowerCase(Locale.ROOT));
  }
}
