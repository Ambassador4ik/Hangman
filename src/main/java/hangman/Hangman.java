package hangman;

import display.MutableLine;
import java.util.List;

/**
 * The Hangman interface defines the core behaviors for managing
 * the state and progression of a hangman display.
 */
public interface Hangman {

    /**
     * Retrieves the current lines representing the hangman display.
     *
     * @return a list of {@code MutableLine<Character>} representing each line of the hangman.
     */
    List<MutableLine<Character>> hangmanLines();

    /**
     * Advances the hangman to the next stage.
     *
     * @return {@code true} if there are more stages to advance to, {@code false} if the final stage has been reached.
     */
    boolean nextStage();

    /**
     * Sets the number of remaining attempts and adjusts the hangman display accordingly.
     *
     * @param attempts the number of attempts remaining (must be between 0 and 6 inclusive).
     * @return {@code true} if the attempts were set successfully, {@code false} otherwise.
     */
    boolean setAttempts(int attempts);
}
