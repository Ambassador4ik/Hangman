package game;

import display.ConsoleDisplay;
import display.ImmutableLine;
import handlers.OutputHandler;
import handlers.UserInputHandler;
import hangman.Hangman;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate; // Changed from Function to Predicate
import keyboard.Keyboard;
import word.Word;

public class Game {
    private static final String LINE_SEPARATOR = "--------------------";
    private final Hangman hangman;
    private final Keyboard keyboard;
    private final Word word;
    private final Predicate<String> logic;
    private final ConsoleDisplay display = new ConsoleDisplay(List.of());
    private final UserInputHandler inputHandler;
    private boolean gameBuilt = false;

    public Game(Hangman hangman, Keyboard keyboard, Word word) {
        this.hangman = hangman;
        this.keyboard = keyboard;
        this.word = word;

        this.logic = (String input) -> {
            if (input == null || !input.matches("^[A-Za-z]$")) {
                return true;
            }
            char letter = input.toUpperCase().charAt(0);
            byte pushStatus = word.pushLetter(letter);
            if (pushStatus == 0) {
                return false; // Terminates the game if the word is guessed
            }

            if (keyboard.crossOutLetter(Character.toString(letter)) && pushStatus == -1) {
                return hangman.nextStage(); // Terminates the game if the word is not guessed
            }

            return true;
        };

        inputHandler = new UserInputHandler(
            logic,
            "Enter letter: ",
            "exit"
        );
    }

    public void build() {
        display.addLines(List.of(
            new ImmutableLine(LINE_SEPARATOR)
        ));
        display.addLines(hangman.hangmanLines());
        display.addLines(
            Arrays.asList(
                new ImmutableLine(LINE_SEPARATOR),
                keyboard.keyboardLine1(),
                keyboard.keyboardLine2(),
                keyboard.keyboardLine3(),
                new ImmutableLine(LINE_SEPARATOR),
                word.wordLine(),
                new ImmutableLine(LINE_SEPARATOR)
            )
        );
        gameBuilt = true;
    }

    public void start(int attempts) {
        if (!gameBuilt) {
            throw new IllegalStateException("Attempting to start a game that was not built!");
        }

        OutputHandler.print("\033[H\033[2J");
        OutputHandler.flush();

        display.render();
        hangman.setAttempts(attempts);
        inputHandler.run();
    }
}
