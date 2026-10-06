package de.bjoernerlwein.adocfmt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Test;

class CollapseBlankLinesTest {

  private final Formatter formatter = new Formatter(List.of(new CollapseBlankLinesRule()));

  private static String resource(String name) throws Exception {
    try (InputStream in =
        CollapseBlankLinesTest.class.getResourceAsStream("/spec/blank-lines/" + name)) {
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
  void collapsesThreeEmptyLinesToOne() {
    assertEquals("a\n\nb\n", formatter.format("a\n\n\n\nb\n"));
  }

  @Test
  void preservesSingleEmptyLine() {
    assertEquals("a\n\nb\n", formatter.format("a\n\nb\n"));
  }

  @Test
  void preservesWhitespaceOnlyLine() {
    assertEquals("a\n   \nb\n", formatter.format("a\n   \nb\n"));
  }

  @Test
  void loneNewlineStays() {
    assertEquals("\n", formatter.format("\n"));
  }

  @Test
  void keepsMissingTrailingNewline() {
    assertEquals("a\n\nb", formatter.format("a\n\n\nb"));
  }

  @Test
  void preservesEmptyLinesInsideDelimitedBlock() {
    String input = "----\na\n\n\nb\n----\n";
    assertEquals(input, formatter.format(input));
  }

  @Test
  void unitScenariosAreIdempotent() {
    for (String input :
        List.of(
            "a\n\n\n\nb\n",
            "a\n\nb\n",
            "a\n   \nb\n",
            "\n",
            "a\n\n\nb",
            "----\na\n\n\nb\n----\n")) {
      String once = formatter.format(input);
      assertEquals(once, formatter.format(once));
    }
  }
}
