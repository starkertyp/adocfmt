package de.bjoernerlwein.adocfmt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Test;

class TableCellSpacingTest {

  private final Formatter formatter = new Formatter(List.of(new TableCellSpacingRule()));

  private static String resource(String name) throws Exception {
    try (InputStream in = TableCellSpacingTest.class.getResourceAsStream("/spec/table/" + name)) {
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
  void leadingMarkerWithoutSpace() {
    assertEquals("|===\n| text\n|===\n", formatter.format("|===\n|text\n|===\n"));
  }

  @Test
  void inlineMarkerWithoutSpaces() {
    assertEquals("|===\n| a | b\n|===\n", formatter.format("|===\n|a|b\n|===\n"));
  }

  @Test
  void mixedLeadingAndInlineMarkers() {
    assertEquals(
        "|===\n| heading | 1 | 2 | 3\n|===\n", formatter.format("|===\n|heading|1|2|3\n|===\n"));
  }

  @Test
  void alreadySpacedLineUnchanged() {
    assertEquals(
        "|===\n| heading | 1 | 2\n|===\n", formatter.format("|===\n| heading | 1 | 2\n|===\n"));
  }

  @Test
  void blankLineInsideTableUnchanged() {
    assertEquals("|===\n| a\n\n| b\n|===\n", formatter.format("|===\n| a\n\n| b\n|===\n"));
  }

  @Test
  void continuationLineWithoutMarkerUnchanged() {
    assertEquals(
        "|===\n| cell\nover multiple lines\n|===\n",
        formatter.format("|===\n| cell\nover multiple lines\n|===\n"));
  }

  @Test
  void pipeLineOutsideTableUnchanged() {
    assertEquals("a|b\n", formatter.format("a|b\n"));
  }

  @Test
  void emptyCellLineHasNoTrailingSpace() {
    assertEquals("|===\n|\n|===\n", formatter.format("|===\n|\n|===\n"));
  }

  @Test
  void whitespaceOnlyEmptyCellLineHasNoTrailingSpace() {
    assertEquals("|===\n|\n|===\n", formatter.format("|===\n|   \n|===\n"));
  }

  @Test
  void emptyCellLineIsIdempotent() {
    String once = formatter.format("|===\n|\n|===\n");
    assertEquals(once, formatter.format(once));
  }

  @Test
  void leadingASpecifierWithoutSpace() {
    assertEquals("|===\na| text\n|===\n", formatter.format("|===\na|text\n|===\n"));
  }

  @Test
  void leadingASpecifierAlreadySpaced() {
    assertEquals("|===\na| text\n|===\n", formatter.format("|===\na| text\n|===\n"));
  }

  @Test
  void leadingASpecifierWithInlineMarker() {
    assertEquals("|===\na| text | more\n|===\n", formatter.format("|===\na|text |more\n|===\n"));
  }

  @Test
  void emptyASpecifierCellHasNoTrailingSpace() {
    assertEquals("|===\na|\n|===\n", formatter.format("|===\na|\n|===\n"));
    assertEquals("|===\na|\n|===\n", formatter.format("|===\na|   \n|===\n"));
  }

  @Test
  void leadingASpecifierIsIdempotent() {
    String once = formatter.format("|===\na|text\n|===\n");
    assertEquals(once, formatter.format(once));
  }

  @Test
  void inlineAAfterLeadingMarkerIsContent() {
    assertEquals("|===\n| a | b\n|===\n", formatter.format("|===\n|a|b\n|===\n"));
  }
}
