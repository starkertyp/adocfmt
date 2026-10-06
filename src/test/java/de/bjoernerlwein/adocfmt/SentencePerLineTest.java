package de.bjoernerlwein.adocfmt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Test;

class SentencePerLineTest {

  private final Formatter formatter = new Formatter(List.of(new SentencePerLineRule()));

  private static String resource(String name) throws Exception {
    try (InputStream in =
        SentencePerLineTest.class.getResourceAsStream("/spec/sentence-per-line/" + name)) {
      return new String(in.readAllBytes(), StandardCharsets.UTF_8);
    }
  }

  @Test
  void formatsFixtureBeforeToAfter() throws Exception {
    assertEquals(resource("after.adoc"), formatter.format(resource("before.adoc")));
  }

  @Test
  void fixtureIsIdempotent() throws Exception {
    String once = formatter.format(resource("before.adoc"));
    assertEquals(once, formatter.format(once));
  }

  @Test
  void splitsTwoSentencesOnOneLine() {
    assertEquals("Erster Satz.\nZweiter Satz.\n", formatter.format("Erster Satz. Zweiter Satz.\n"));
  }

  @Test
  void joinsWrappedSentenceOntoOneLine() {
    assertEquals(
        "Ein Satz über mehrere Zeilen.\n", formatter.format("Ein Satz über\nmehrere Zeilen.\n"));
  }

  @Test
  void appendsMissingTrailingPeriod() {
    assertEquals("kein Punkt hier.\n", formatter.format("kein Punkt hier\n"));
  }

  @Test
  void doesNotSplitAbbreviation() {
    assertEquals("z.B. ein Beispiel.\n", formatter.format("z.B. ein Beispiel\n"));
  }

  @Test
  void doesNotSplitMultipartAbbreviationBeforeUppercase() {
    assertEquals("z.B. Der folgende Text.\n", formatter.format("z.B. Der folgende Text\n"));
  }

  @Test
  void doesNotSplitSingleTokenAbbreviationBeforeUppercase() {
    assertEquals("usw. Das gilt weiter.\n", formatter.format("usw. Das gilt weiter\n"));
  }

  @Test
  void abbreviationMatchIsCaseInsensitive() {
    assertEquals("D.h. Das gilt.\n", formatter.format("D.h. Das gilt\n"));
  }

  @Test
  void abbreviationDoesNotSuppressFollowingBoundary() {
    assertEquals(
        "usw. Der Satz endet hier.\nEin neuer Satz beginnt.\n",
        formatter.format("usw. Der Satz endet hier. Ein neuer Satz beginnt.\n"));
  }

  @Test
  void abbreviationHandlingIsIdempotent() {
    String once = formatter.format("z.B. Der folgende Text. usw. Das gilt.\n");
    assertEquals(once, formatter.format(once));
  }

  @Test
  void doesNotSplitDecimal() {
    assertEquals("3.14 ist Pi.\n", formatter.format("3.14 ist Pi\n"));
  }

  @Test
  void doesNotAppendPeriodAfterColon() {
    assertEquals("Folgende Punkte:\n", formatter.format("Folgende Punkte:\n"));
  }

  @Test
  void doesNotDoublePeriodAfterTrailingUnicodeWhitespace() {
    assertEquals("Ein Satz.\n", formatter.format("Ein Satz.\u00A0\n"));
    assertEquals("Ein Satz.\n", formatter.format("Ein Satz.\u2003\n"));
  }

  @Test
  void trailingUnicodeWhitespaceIsIdempotent() {
    String once = formatter.format("Ein Satz.\u00A0\n");
    assertEquals(once, formatter.format(once));
  }

  @Test
  void colonIntroBeforeListUnchanged() {
    String input = "Folgende Punkte:\n* Punkt eins\n";
    assertEquals(input, formatter.format(input));
  }

  @Test
  void colonParagraphIsIdempotent() {
    String once = formatter.format("Folgende Punkte:\n");
    assertEquals(once, formatter.format(once));
  }

  @Test
  void structuralLinesUnchanged() {
    String input =
        "= H. Noch ein Satz.\n"
            + "* list. item\n"
            + ":a: b\n"
            + "// c. d\n"
            + "[[x]]\n"
            + "|===\n"
            + "| a | b\n"
            + "|===\n"
            + "image::foo.png[]\n";
    assertEquals(input, formatter.format(input));
  }

  @Test
  void structuralInputIsIdempotent() {
    String input = "= H. Noch ein Satz.\n\n* list. item\n\n:a: b\n";
    String once = formatter.format(input);
    assertEquals(once, formatter.format(once));
  }

  @Test
  void captionLineIsNotProse() {
    String input = ".Caption. Zweiter Satz.\nEin Absatz. Zwei Sätze.\n";
    assertEquals(".Caption. Zweiter Satz.\nEin Absatz.\nZwei Sätze.\n", formatter.format(input));
  }

  @Test
  void captionHandlingIsIdempotent() {
    String once = formatter.format(".Caption. Zweiter Satz.\nEin Absatz. Zwei Sätze.\n");
    assertEquals(once, formatter.format(once));
  }

  @Test
  void controlLineIsNotProse() {
    String input = "<<<<\nEin Absatz. Zwei Sätze.\n";
    assertEquals("<<<<\nEin Absatz.\nZwei Sätze.\n", formatter.format(input));
  }

  @Test
  void controlLineHandlingIsIdempotent() {
    String once = formatter.format("<<<<\nEin Absatz. Zwei Sätze.\n");
    assertEquals(once, formatter.format(once));
  }

  @Test
  void leadingACellLineIsNotProse() {
    String input = "|===\na| include::../version.txt[]\n|===\n";
    assertEquals(input, formatter.format(input));
  }

  @Test
  void leadingACellLineStaysSeparateFromProse() {
    String input = "Ein Absatz. Zwei Sätze.\na| include::../version.txt[]\n";
    assertEquals(
        "Ein Absatz.\nZwei Sätze.\na| include::../version.txt[]\n", formatter.format(input));
  }

  @Test
  void leadingACellLineIsIdempotent() {
    String once = formatter.format("kein Punkt hier\na| include::../version.txt[]\n");
    assertEquals(once, formatter.format(once));
  }
}
