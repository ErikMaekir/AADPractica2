package erikmaekir.aadpractica2;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

public class CSVWriter {

    private PrintWriter pw;
    private char separator;
    private char quotechar;
    private char escapechar;
    private String lineEnd;

    public static final char DEFAULT_ESCAPE_CHARACTER = '"';

    public static final char DEFAULT_SEPARATOR = ';';

    public static final char DEFAULT_QUOTE_CHARACTER = '"';

    public static final String DEFAULT_LINE_END = "\n";

    public CSVWriter(Writer writer) {
        this(writer, DEFAULT_SEPARATOR, DEFAULT_QUOTE_CHARACTER,
                DEFAULT_ESCAPE_CHARACTER, DEFAULT_LINE_END);
    }

    public CSVWriter(Writer writer, char separator, char quotechar, char escapechar, String lineEnd) {
        this.pw = new PrintWriter(writer);
        this.separator = separator;
        this.quotechar = quotechar;
        this.escapechar = escapechar;
        this.lineEnd = lineEnd;
    }

    public void writeNext(String[] nextLine) {

        if (nextLine == null)
            return;

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < nextLine.length; i++) {

            if (i != 0) {
                sb.append(separator);
            }

            String nextElement = nextLine[i];
            if (nextElement == null){
                continue;
            }
            sb.append(quotechar);
            for (int j = 0; j < nextElement.length(); j++) {
                char nextChar = nextElement.charAt(j);
                if (nextChar == quotechar) {
                    sb.append(escapechar).append(nextChar);
                } else if (nextChar == escapechar) {
                    sb.append(escapechar).append(nextChar);
                } else {
                    sb.append(nextChar);
                }
            }
            sb.append(quotechar);
        }

        sb.append(lineEnd);
        pw.write(sb.toString());

    }

    public void close() throws IOException {
        pw.flush();
        pw.close();
    }
}
