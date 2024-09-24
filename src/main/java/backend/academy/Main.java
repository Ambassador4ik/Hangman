package backend.academy;

import display.ConsoleDisplay;
import display.MutableLine;
import handlers.UserInputHandler;
import hangman.SimpleHangman;
import keyboard.CapitalizedKeyboard;
import lombok.experimental.UtilityClass;
import word.CapitalizedWord;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@UtilityClass
public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        CapitalizedKeyboard keyboard = new CapitalizedKeyboard();
        SimpleHangman hangman = new SimpleHangman();
        CapitalizedWord word = new CapitalizedWord("CLOWN");

        // Initialize ConsoleDisplay with the lines
        ConsoleDisplay display = new ConsoleDisplay(Arrays.asList(
            word.wordLine()
        ));

        display.render();

        Thread.sleep(1000);
        word.pushLetter('C');

    }

    /**
     * Simulates an external print to the console.
     * Adjusts the display's shift to accommodate the new line.
     *
     * @param display The ConsoleDisplay instance to adjust.
     * @param message The message to print externally.
     */
    public static void externalPrint(ConsoleDisplay display, String message) {
        // Shift the display up to make room for the external message
        display.shiftUp(1);

        // Print the external message
        System.out.println(message);
    }
}
