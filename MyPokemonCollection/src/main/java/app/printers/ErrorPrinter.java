package app.printers;

public class ErrorPrinter {
    private ErrorPrinter() {
    }

    public static void print(String message) {
        String ANSI_RED = "\t\\u001B[31m";
        String ANSI_RESET = "\u001B[0m";
        System.err.println(ANSI_RED + message + ANSI_RESET);
    }
}
