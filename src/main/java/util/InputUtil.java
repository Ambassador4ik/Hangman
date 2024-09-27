package util;

import handlers.OutputHandler;
import java.util.Scanner;

public final class InputUtil {

    // Private constructor to prevent instantiation
    private InputUtil() {
        throw new UnsupportedOperationException("InputUtil is a utility class and cannot be instantiated.");
    }

    // Single Scanner instance for all methods
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Reads an integer from the user within the specified range [min, max].
     *
     * @param prompt The message to display to the user.
     * @param min    The minimum acceptable integer (inclusive).
     * @param max    The maximum acceptable integer (inclusive).
     * @return The integer entered by the user within the specified range.
     */
    public static int readIntInRange(String prompt, int min, int max) {
        while (true) {
            OutputHandler.print(prompt + " ");
            String input = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value < min || value > max) {
                    OutputHandler.printf("Please enter an integer between %d and %d.%n", min, max);
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                OutputHandler.println("Invalid input. Please enter a valid integer.");
            }
        }
    }

    /**
     * Reads a yes/no response from the user.
     *
     * @param prompt The message to display to the user.
     * @return true if the user enters 'y' or 'Y', false if the user enters 'n' or 'N'.
     */
    public static boolean readYesNo(String prompt) {
        while (true) {
            OutputHandler.print(prompt + " (y/n): ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y")) {
                return true;
            } else if (input.equals("n")) {
                return false;
            } else {
                OutputHandler.println("Invalid input. Please enter 'y' for yes or 'n' for no.");
            }
        }
    }

    /**
     * Reads a single word from the user with a maximum number of characters.
     *
     * @param prompt    The message to display to the user.
     * @param maxLength The maximum number of characters allowed.
     * @return The word entered by the user that does not exceed the specified length.
     */
    public static String readWordWithLimit(String prompt, int maxLength) {
        while (true) {
            OutputHandler.print(prompt + " ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                OutputHandler.println("Input cannot be empty. Please enter a valid word.");
                continue;
            }
            if (input.contains(" ")) {
                OutputHandler.println("Please enter a single word without spaces.");
                continue;
            }
            if (input.length() > maxLength) {
                OutputHandler.printf("Word is too long. Please enter a word with no more than %d characters.%n",
                    maxLength);
                continue;
            }
            return input;
        }
    }

    /**
     * Optionally, call this method to close the scanner when it's no longer needed.
     * It's generally not recommended to close System.in, so use with caution.
     */
    public static void closeScanner() {
        scanner.close();
    }
}

