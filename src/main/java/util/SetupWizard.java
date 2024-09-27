package util;

import handlers.OutputHandler;

public class SetupWizard {
    public static void setupConsole() {
        OutputHandler.print("\033[H\033[2J");
        OutputHandler.flush();
        OutputHandler.println("This program requires non-emulated console to run.\nPlease make sure you are not using IDEA integrated terminal.");

        if (!InputUtil.readYesNo("Are you using a terminal with ANSI support?")) {
            System.exit(0);
        };
    }

    public static int setupDifficulty() {
        return InputUtil.readIntInRange("Enter attempt count (1-6):", 1, 6);
    }

    public static String setupWord() {
        return InputUtil.readWordWithLimit("Enter a word to test:", 10).toUpperCase();
    }
}
