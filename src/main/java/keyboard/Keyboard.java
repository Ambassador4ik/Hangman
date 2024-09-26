package keyboard;

import display.MutableLine;

/**
 * Interface representing the core behavior of a Keyboard.
 */
public interface Keyboard {

    /**
     * Crosses out a specified letter on the keyboard by replacing it with a highlighted version.
     *
     * @param letter The letter to be crossed out.
     * @return {@code true} if the letter was successfully crossed out; {@code false} otherwise.
     */
    boolean crossOutLetter(String letter);

    /**
     * Retrieves the first line of the keyboard.
     *
     * @return A list of strings representing the first line of keys.
     */
    MutableLine<String> keyboardLine1();

    /**
     * Retrieves the second line of the keyboard.
     *
     * @return A list of strings representing the second line of keys.
     */
    MutableLine<String> keyboardLine2();

    /**
     * Retrieves the third line of the keyboard.
     *
     * @return A list of strings representing the third line of keys.
     */
    MutableLine<String> keyboardLine3();
}
