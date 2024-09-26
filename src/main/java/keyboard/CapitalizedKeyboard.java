package keyboard;

import display.MutableLine;
import java.util.Arrays;
import lombok.Getter;

@Getter
public class CapitalizedKeyboard implements Keyboard {
    public static final String RESET = "\u001B[0m";
    private static final String STYLE = "\u001B[31m";
    private final MutableLine<String> keyboardLine1 =
        new MutableLine<String>(Arrays.asList("Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"));
    private final MutableLine<String> keyboardLine2 =
        new MutableLine<String>(Arrays.asList("", "A", "S", "D", "F", "G", "H", "J", "K", "L"));
    private final MutableLine<String> keyboardLine3 =
        new MutableLine<String>(Arrays.asList("  ", "Z", "X", "C", "V", "B", "N", "M"));

    /**
     * Adds a cross-out character to a letter.
     *
     * @param letter The original letter.
     * @return The letter with the cross-out character.
     */
    private static String addCrossOut(String letter) {
        StringBuilder sb = new StringBuilder();
        for (char c : letter.toCharArray()) {
            sb.append(c).append(STYLE);
        }
        return sb.toString();
    }

    /**
     * Highlights a given letter with the specified ANSI style.
     *
     * @param letter The letter to highlight.
     * @return The styled letter as a String.
     */
    private static String highlightLetter(String letter) {
        return STYLE + letter + RESET;
    }

    /**
     * Crosses out a letter on the keyboard by replacing it with the letter followed by a cross-out character.
     *
     * @param letter The letter to cross out.
     * @return The number of replacements made.
     */
    public boolean crossOutLetter(String letter) {
        String crossedOutLetter = highlightLetter(letter);
        if (keyboardLine1.replaceContent(letter, crossedOutLetter) > 0) {
            return true;
        }
        if (keyboardLine2.replaceContent(letter, crossedOutLetter) > 0) {
            return true;
        }
        return keyboardLine3.replaceContent(letter, crossedOutLetter) > 0;
    }
}
