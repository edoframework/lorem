// Splits each line of a UTF-8 file into words with ICU4J's BreakIterator and writes one JSON
// array of words per line. Only runs of letters are words; punctuation and spaces are dropped.
// Run by segment.py: java -cp icu4j.jar Segment.java <locale> <file>

import com.ibm.icu.text.BreakIterator;
import com.ibm.icu.util.ULocale;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Segment {
  public static void main(String[] args) throws Exception {
    BreakIterator words = BreakIterator.getWordInstance(new ULocale(args[0]));
    PrintStream out = new PrintStream(System.out, false, StandardCharsets.UTF_8);
    for (String line : Files.readAllLines(Path.of(args[1]), StandardCharsets.UTF_8)) {
      words.setText(line);
      StringBuilder json = new StringBuilder("[");
      int start = words.first();
      for (int end = words.next(); end != BreakIterator.DONE; start = end, end = words.next()) {
        int status = words.getRuleStatus();
        if (status >= BreakIterator.WORD_LETTER && status < BreakIterator.WORD_IDEO_LIMIT) {
          if (json.length() > 1) json.append(',');
          json.append('"').append(line.substring(start, end).replace("\\", "\\\\").replace("\"", "\\\"")).append('"');
        }
      }
      out.println(json.append(']'));
    }
    out.flush();
  }
}
