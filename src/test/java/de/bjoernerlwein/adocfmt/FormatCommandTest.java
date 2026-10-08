package de.bjoernerlwein.adocfmt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import picocli.CommandLine;

class FormatCommandTest {

  private final StringWriter out = new StringWriter();
  private final StringWriter err = new StringWriter();
  private final CommandLine cmd =
      new CommandLine(new FormatCommand())
          .setOut(new PrintWriter(out, true))
          .setErr(new PrintWriter(err, true));

  @Test
  void defaultPrintsToStdoutAndLeavesFileUnchanged(@TempDir Path dir) throws Exception {
    Path file = dir.resolve("a.adoc");
    Files.writeString(file, "----\r\na\r\nb\r\n----\r\n");

    int code = cmd.execute(file.toString());

    assertEquals(0, code);
    assertEquals("----\na\nb\n----\n", out.toString());
    assertEquals("----\r\na\r\nb\r\n----\r\n", Files.readString(file));
  }

  @Test
  void writeRewritesFile(@TempDir Path dir) throws Exception {
    Path file = dir.resolve("a.adoc");
    Files.writeString(file, "----\r\na\r\nb\r\n----\r\n");

    int code = cmd.execute("--write", file.toString());

    assertEquals(0, code);
    assertEquals("----\na\nb\n----\n", Files.readString(file));
  }

  @Test
  void checkCleanExitsZero(@TempDir Path dir) throws Exception {
    Path file = dir.resolve("a.adoc");
    Files.writeString(file, "----\na\nb\n----\n");

    assertEquals(0, cmd.execute("--check", file.toString()));
  }

  @Test
  void checkDirtyExitsOne(@TempDir Path dir) throws Exception {
    Path file = dir.resolve("a.adoc");
    Files.writeString(file, "----\r\na\r\nb\r\n----\r\n");

    assertEquals(1, cmd.execute("--check", file.toString()));
  }

  @Test
  void diffPrintsUnifiedDiff(@TempDir Path dir) throws Exception {
    Path file = dir.resolve("a.adoc");
    Files.writeString(file, "----\r\na\r\nb\r\n----\r\n");

    int code = cmd.execute("--diff", file.toString());

    assertEquals(0, code);
    org.junit.jupiter.api.Assertions.assertTrue(out.toString().contains("@@"), out.toString());
  }

  @Test
  void stdinIsFormatted() {
    CommandLine stdinCmd =
        new CommandLine(
                new FormatCommand(
                    new ByteArrayInputStream(
                        "----\r\na\r\nb\r\n----\r\n".getBytes(StandardCharsets.UTF_8))))
            .setOut(new PrintWriter(out, true))
            .setErr(new PrintWriter(err, true));

    int code = stdinCmd.execute("--stdin");

    assertEquals(0, code);
    assertEquals("----\na\nb\n----\n", out.toString());
  }

  @Test
  void proseIsNotSplitByDefault(@TempDir Path dir) throws Exception {
    Path file = dir.resolve("a.adoc");
    Files.writeString(file, "Erster Satz. Zweiter Satz.");

    int code = cmd.execute(file.toString());

    assertEquals(0, code);
    assertEquals("Erster Satz. Zweiter Satz.", out.toString());
  }

  @Test
  void sentencePerLineFlagSplitsProseViaStdin() {
    CommandLine stdinCmd =
        new CommandLine(
                new FormatCommand(
                    new ByteArrayInputStream(
                        "Erster Satz. Zweiter Satz.".getBytes(StandardCharsets.UTF_8))))
            .setOut(new PrintWriter(out, true))
            .setErr(new PrintWriter(err, true));

    int code = stdinCmd.execute("--stdin", "--sentence-per-line");

    assertEquals(0, code);
    assertEquals("Erster Satz.\nZweiter Satz.", out.toString());
  }

  @Test
  void checkWithSentencePerLineFlagsProseDifference(@TempDir Path dir) throws Exception {
    Path file = dir.resolve("a.adoc");
    Files.writeString(file, "Erster Satz. Zweiter Satz.");

    assertEquals(1, cmd.execute("--check", "--sentence-per-line", file.toString()));
  }

  @Test
  void missingFileExitsTwo(@TempDir Path dir) {
    assertEquals(2, cmd.execute(dir.resolve("nope.adoc").toString()));
  }

  @Test
  void noPathsExitsTwo() {
    assertEquals(2, cmd.execute());
  }

  @Test
  void unknownOptionExitsTwo() {
    assertEquals(2, cmd.execute("--nope"));
  }
}
