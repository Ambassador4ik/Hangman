package word;

import display.MutableLine;

/**
 * Interface representing the core behavior of a word.
 * It allows pushing letters to reveal parts of the word and provides access
 * to the current state of the word and its original form.
 */
public interface Word {

    /**
     * Pushes a letter to the word.
     * If the letter exists in the word, it reveals all instances of that letter by
     * applying an underline and removes the letter from the internal tracking set.
     *
     * @param letter The character to push into the word.
     * @return <ul>
     *                 <li>-1 if the letter is not present in the word.</li>
     *                 <li>0 if all letters have been successfully pushed (word fully revealed).</li>
     *                 <li>1 if the letter was successfully pushed but the word is not yet fully revealed.</li>
     *             </ul>
     */
    byte pushLetter(char letter);

    /**
     * Retrieves the current state of the word line.
     * The word line reflects which letters have been revealed (underlined) and which remain hidden.
     *
     * @return A {@link MutableLine} representing the current state of the word.
     */
    MutableLine<String> wordLine();

    /**
     * Retrieves the original word.
     *
     * @return The original word as a {@link String}.
     */
    char[] word();
}
