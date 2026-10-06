package de.bjoernerlwein.adocfmt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Test;

class HeadingBlankLineTest {

  private final Formatter formatter = new Formatter(List.of(new HeadingBlankLineRule()));

  private static String resource(String name) throws Exception {
    try (InputStream in =
        HeadingBlankLineTest.class.getResourceAsStream("/spec/headings/" + name)) {
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
  void headingFollowedByText() {
    assertEquals("= H\n\ntext\n", formatter.format("= H\ntext\n"));
  }

  @Test
  void headingFollowedByHeading() {
    assertEquals("= A\n\n== B\n", formatter.format("= A\n== B\n"));
  }

  @Test
  void headingAlreadyFollowedByBlankLine() {
    assertEquals("= H\n\ntext\n", formatter.format("= H\n\ntext\n"));
  }

  @Test
  void headingAsLastLine() {
    assertEquals("= H", formatter.format("= H"));
    assertEquals("text\n\n= H\n", formatter.format("text\n= H\n"));
  }

  @Test
  void equalsWithoutFollowingSpaceUnchanged() {
    assertEquals("=notHeading\ntext\n", formatter.format("=notHeading\ntext\n"));
  }

  @Test
  void textBeforeHeading() {
    assertEquals("text\n\n= H\n", formatter.format("text\n= H\n"));
  }

  @Test
  void headingAlreadyHasBlankLineBeforeIt() {
    assertEquals("text\n\n= H\n", formatter.format("text\n\n= H\n"));
  }

  @Test
  void headingAsFirstLine() {
    assertEquals("= H\n\ntext\n", formatter.format("= H\ntext\n"));
  }

  @Test
  void anchorAboveHeading() {
    assertEquals("text\n\n[[a]]\n= H\n", formatter.format("text\n[[a]]\n= H\n"));
  }

  @Test
  void blankLineBetweenAnchorAndHeadingRemoved() {
    assertEquals("[[a]]\n= H\n", formatter.format("[[a]]\n\n= H\n"));
  }

  @Test
  void blankLineAlreadyBeforeAnchor() {
    assertEquals("text\n\n[[a]]\n= H\n", formatter.format("text\n\n[[a]]\n= H\n"));
  }

  @Test
  void multipleStackedAnchors() {
    assertEquals("text\n\n[[a]]\n[[b]]\n= H\n", formatter.format("text\n[[a]]\n[[b]]\n= H\n"));
  }

  @Test
  void anchorAsFirstLine() {
    assertEquals("[[a]]\n= H\n", formatter.format("[[a]]\n= H\n"));
  }
}
