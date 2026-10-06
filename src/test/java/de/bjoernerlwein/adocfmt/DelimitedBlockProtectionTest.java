package de.bjoernerlwein.adocfmt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Test;

class DelimitedBlockProtectionTest {

  private final Formatter formatter =
      new Formatter(List.of(new HeadingBlankLineRule(), new TableCellSpacingRule()));

  private static String resource(String name) throws Exception {
    try (InputStream in =
        DelimitedBlockProtectionTest.class.getResourceAsStream("/spec/verbatim/" + name)) {
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
  void headingLikeLineInsideBlockUntouched() {
    String input = "----\n= H\ntext\n----\n";
    assertEquals(input, formatter.format(input));
  }

  @Test
  void tableLikeLineInsideBlockUntouched() {
    String input = "----\n|a|b\n|heading|1|2\n----\n";
    assertEquals(input, formatter.format(input));
  }

  @Test
  void fencesEmittedUnchanged() {
    String input = "-----\n= H\n-----\n";
    assertEquals(input, formatter.format(input));
  }

  @Test
  void unclosedBlockProtectsToEndOfDocument() {
    String input = "----\n= H\ntext|pipe\n";
    assertEquals(input, formatter.format(input));
  }

  @Test
  void shortHyphenRunIsNotAFence() {
    assertEquals("---\n\n= H\n", formatter.format("---\n= H\n"));
  }

  @Test
  void asteriskBlockProtectsContent() {
    String input = "****\n= H\ntext|pipe\n****\n";
    assertEquals(input, formatter.format(input));
  }

  @Test
  void asteriskBlockNestingListingBlockStaysProtected() {
    String input = "****\n= Outer\n----\n= Inner\n|a|b\n----\nouter\n****\n";
    assertEquals(input, formatter.format(input));
  }

  @Test
  void shortAsteriskRunIsNotAFence() {
    assertEquals("***\n\n= H\n", formatter.format("***\n= H\n"));
  }

  @Test
  void asteriskLineInsideListingBlockDoesNotCloseIt() {
    String input = "----\n****\n= H\ntext|pipe\n----\n";
    assertEquals(input, formatter.format(input));
  }

  @Test
  void contentOutsideBlockStillFormatted() {
    String input = "= H\ntext\n----\n= X\n----\n";
    assertEquals("= H\n\ntext\n----\n= X\n----\n", formatter.format(input));
  }

  @Test
  void protectedEdgesAreIdempotent() {
    String[] inputs = {
      "----\n= H\ntext\n----\n",
      "----\n|a|b\n----\n",
      "-----\n= H\n-----\n",
      "----\n= H\ntext|pipe\n",
    };
    for (String input : inputs) {
      String once = formatter.format(input);
      assertEquals(once, formatter.format(once), input);
    }
  }
}
