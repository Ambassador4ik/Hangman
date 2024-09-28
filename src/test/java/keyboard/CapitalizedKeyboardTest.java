package keyboard;

import java.util.Arrays;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CapitalizedKeyboardTest {

    @Test
    public void testInitialState() {
        CapitalizedKeyboard keyboard = new CapitalizedKeyboard();

        assertEquals(
            Arrays.asList("Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"),
            keyboard.keyboardLine1().content()
        );

        assertEquals(
            Arrays.asList("", "A", "S", "D", "F", "G", "H", "J", "K", "L"),
            keyboard.keyboardLine2().content()
        );

        assertEquals(
            Arrays.asList("  ", "Z", "X", "C", "V", "B", "N", "M"),
            keyboard.keyboardLine3().content()
        );
    }

    @Test
    public void testCrossOutLetter() {
        CapitalizedKeyboard keyboard = new CapitalizedKeyboard();

        boolean result = keyboard.crossOutLetter("A");
        assertTrue(result);

        String expectedHighlightedA = CapitalizedKeyboard.STYLE + "A" + CapitalizedKeyboard.RESET;
        assertTrue(
            keyboard.keyboardLine2().content().contains(expectedHighlightedA),
            "Expected keyboardLine2 to contain the highlighted 'A'"
        );

        assertFalse(
            keyboard.keyboardLine2().content().contains("A"),
            "Expected keyboardLine2 not to contain 'A'"
        );
    }

    @Test
    public void testCrossOutLetterNonExistingLetter() {
        CapitalizedKeyboard keyboard = new CapitalizedKeyboard();

        boolean result = keyboard.crossOutLetter("1");
        assertFalse(result);

        assertFalse(
            keyboard.keyboardLine1().content().stream().anyMatch(s -> s.contains(CapitalizedKeyboard.STYLE)),
            "No letters in keyboardLine1 should be highlighted"
        );
        assertFalse(
            keyboard.keyboardLine2().content().stream().anyMatch(s -> s.contains(CapitalizedKeyboard.STYLE)),
            "No letters in keyboardLine2 should be highlighted"
        );
        assertFalse(
            keyboard.keyboardLine3().content().stream().anyMatch(s -> s.contains(CapitalizedKeyboard.STYLE)),
            "No letters in keyboardLine3 should be highlighted"
        );
    }
}
