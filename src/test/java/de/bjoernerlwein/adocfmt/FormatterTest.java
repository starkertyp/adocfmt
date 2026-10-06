package de.bjoernerlwein.adocfmt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class FormatterTest {

  @Test
  void passThroughWithNoRules() {
    Formatter formatter = new Formatter(List.of());
    assertEquals("hello\n", formatter.format("hello\n"));
  }

  @Test
  void appliesRulesInOrder() {
    Formatter formatter =
        new Formatter(List.of(s -> s.replace("a", "b"), s -> s.replace("b", "c")));
    assertEquals("cc\n", formatter.format("aa\n"));
  }

  @Test
  void normalizesLineEndings() {
    Formatter formatter = new Formatter(List.of());
    assertEquals("a\nb\n", formatter.format("a\r\nb\r\n"));
  }

  @Test
  void isIdempotent() {
    Formatter formatter = new Formatter(List.of(s -> s.replace("a", "b")));
    String once = formatter.format("a\r\n");
    assertEquals(once, formatter.format(once));
  }

  @Test
  void keepsMissingTrailingNewline() {
    Formatter formatter = new Formatter(List.of());
    assertEquals("a", formatter.format("a"));
  }

  @Test
  void keepsSingleTrailingNewline() {
    Formatter formatter = new Formatter(List.of());
    assertEquals("a\n", formatter.format("a\n"));
  }
}
