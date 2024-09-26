package handlers;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import lombok.Getter;

/**
 * CustomPrintStream provides a static API compatible with {@code System.out}
 * but uses an underlying PrintStream for output.
 */
public final class OutputHandler { // Make the class final to prevent subclassing

    /**
     * The underlying PrintStream used for output.
     */
    @Getter
    private static PrintStream printStream = System.out;

    // Private constructor to prevent instantiation
    private OutputHandler() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Sets the underlying PrintStream to a new OutputStream with the specified encoding.
     *
     * @param out         the new OutputStream
     * @param autoFlush   whether to autoflush on println, printf, or format
     * @param charsetName the name of the character set to use
     * @throws UnsupportedEncodingException if the specified charset is not supported
     */
    public static void setPrintStream(OutputStream out, boolean autoFlush, String charsetName)
        throws UnsupportedEncodingException {
        printStream = new PrintStream(out, autoFlush, charsetName);
    }

    /**
     * Sets the underlying PrintStream to a new OutputStream with UTF-8 encoding.
     *
     * @param out       the new OutputStream
     * @param autoFlush whether to autoflush on println, printf, or format
     */
    public static void setPrintStream(OutputStream out, boolean autoFlush) {
        try {
            setPrintStream(out, autoFlush, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            // This should never happen with StandardCharsets.UTF_8
            throw new RuntimeException("UTF-8 encoding is not supported", e);
        }
    }

    // --- Print Methods ---

    public static void print(boolean b) {
        printStream.print(b);
    }

    public static void print(char c) {
        printStream.print(c);
    }

    public static void print(int i) {
        printStream.print(i);
    }

    public static void print(long l) {
        printStream.print(l);
    }

    public static void print(float f) {
        printStream.print(f);
    }

    public static void print(double d) {
        printStream.print(d);
    }

    public static void print(char[] s) {
        printStream.print(s);
    }

    public static void print(String s) {
        printStream.print(s);
    }

    public static void print(Object obj) {
        printStream.print(obj);
    }

    // --- println Methods ---

    public static void println() {
        printStream.println();
    }

    public static void println(boolean x) {
        printStream.println(x);
    }

    public static void println(char x) {
        printStream.println(x);
    }

    public static void println(int x) {
        printStream.println(x);
    }

    public static void println(long x) {
        printStream.println(x);
    }

    public static void println(float x) {
        printStream.println(x);
    }

    public static void println(double x) {
        printStream.println(x);
    }

    public static void println(char[] x) {
        printStream.println(x);
    }

    public static void println(String x) {
        printStream.println(x);
    }

    public static void println(Object x) {
        printStream.println(x);
    }

    // --- printf Methods ---

    public static PrintStream printf(String format, Object... args) {
        return printStream.printf(format, args);
    }

    public static PrintStream printf(Locale l, String format, Object... args) {
        return printStream.printf(l, format, args);
    }

    // --- format Methods ---

    public static PrintStream format(String format, Object... args) {
        return printStream.format(format, args);
    }

    public static PrintStream format(Locale l, String format, Object... args) {
        return printStream.format(l, format, args);
    }

    // --- append Methods ---

    public static PrintStream append(CharSequence csq) {
        return printStream.append(csq);
    }

    public static PrintStream append(CharSequence csq, int start, int end) {
        return printStream.append(csq, start, end);
    }

    public static PrintStream append(char c) {
        return printStream.append(c);
    }

    // --- Other Methods ---

    public static void flush() {
        printStream.flush();
    }

    public static void close() {
        printStream.close();
    }

    public static boolean checkError() {
        return printStream.checkError();
    }
}
