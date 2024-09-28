package util;

import handlers.OutputHandler;

@SuppressWarnings("MagicNumber")
public final class SetupWizard {
    // Private constructor to prevent instantiation
    private SetupWizard() {
        // Optional: Throw an exception if this ever *is* called
        throw new UnsupportedOperationException("Utility class - cannot be instantiated");
    }

    public static void setupConsole() {
        OutputHandler.print("\033[H\033[2J");
        OutputHandler.flush();
        OutputHandler.println("This program requires non-emulated console to run.\n"
            + "Please make sure you are not using IDEA integrated terminal.");

        if (!InputUtil.readYesNo("Are you using a terminal with ANSI support?")) {
            System.exit(0);
        }
    }

    public static int setupDifficulty() {
        return InputUtil.readIntInRange("Enter attempt count (1-6):", 1, 6);
    }

    public static String setupWord() {
        return InputUtil.readWordWithLimit("Enter a word to test:", 10).toUpperCase();
    }

    public static String setupWordChoice() {
        HangmanWords hangmanWords = new HangmanWords();
        OutputHandler.println("Please specify word category "
            + "(0 - Random, 1 - Fruits, 2 - Animals, 3 - Countries, 4 - Sports, 5 - Colors)");
        int categoryId = InputUtil.readIntInRange("Enter category ID:", 0,
            hangmanWords.getAllCategories().size());

        return hangmanWords.getRandomWord(categoryId).toUpperCase();
    }
}
