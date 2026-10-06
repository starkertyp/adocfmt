package de.bjoernerlwein.adocfmt;

import com.github.difflib.DiffUtils;
import com.github.difflib.UnifiedDiffUtils;
import com.github.difflib.patch.Patch;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Spec;

@Command(
    name = "adocfmt",
    mixinStandardHelpOptions = true,
    version = "adocfmt 0.1.0",
    description = "Format AsciiDoc files.")
public final class FormatCommand implements Callable<Integer> {

  @Parameters(arity = "0..*", paramLabel = "<paths>", description = "Files to format.")
  private List<Path> paths = new ArrayList<>();

  @Option(
      names = {"-w", "--write"},
      description = "Rewrite files in place.")
  private boolean write;

  @Option(
      names = {"-c", "--check"},
      description = "Exit 1 if any file would change.")
  private boolean check;

  @Option(names = "--diff", description = "Print a unified diff instead of the formatted content.")
  private boolean diff;

  @Option(names = "--stdin", description = "Read from standard input.")
  private boolean stdin;

  @Spec private CommandSpec spec;

  private final Formatter formatter = new Formatter(FormatRules.defaults());
  private final InputStream in;

  public FormatCommand() {
    this(System.in);
  }

  public FormatCommand(InputStream in) {
    this.in = in;
  }

  @Override
  public Integer call() {
    PrintWriter out = spec.commandLine().getOut();
    PrintWriter err = spec.commandLine().getErr();

    if (stdin) {
      String input = readStdin();
      if (input == null) {
        return 2;
      }
      out.print(formatter.format(input));
      out.flush();
      return 0;
    }

    if (paths.isEmpty()) {
      err.println("adocfmt: missing required <paths> (or use --stdin)");
      return 2;
    }

    boolean changed = false;
    for (Path path : paths) {
      String original;
      try {
        original = Files.readString(path, StandardCharsets.UTF_8);
      } catch (IOException e) {
        err.println("adocfmt: " + path + ": " + e.getMessage());
        return 2;
      }

      String formatted = formatter.format(original);
      if (formatted.equals(original)) {
        if (!check && !write && !diff) {
          out.print(formatted);
        }
        continue;
      }

      changed = true;
      if (check) {
        err.println("adocfmt: " + path + ": would reformat");
      } else if (diff) {
        printDiff(out, path, original, formatted);
      } else if (write) {
        try {
          Files.writeString(path, formatted, StandardCharsets.UTF_8);
        } catch (IOException e) {
          err.println("adocfmt: " + path + ": " + e.getMessage());
          return 2;
        }
      } else {
        out.print(formatted);
      }
    }

    out.flush();
    return check && changed ? 1 : 0;
  }

  private String readStdin() {
    try {
      return new String(in.readAllBytes(), StandardCharsets.UTF_8);
    } catch (IOException e) {
      spec.commandLine().getErr().println("adocfmt: stdin: " + e.getMessage());
      return null;
    }
  }

  private void printDiff(PrintWriter out, Path path, String original, String formatted) {
    List<String> before = List.of(original.split("\n", -1));
    List<String> after = List.of(formatted.split("\n", -1));
    Patch<String> patch = DiffUtils.diff(before, after);
    for (String line :
        UnifiedDiffUtils.generateUnifiedDiff(path.toString(), path.toString(), before, patch, 3)) {
      out.println(line);
    }
  }
}
