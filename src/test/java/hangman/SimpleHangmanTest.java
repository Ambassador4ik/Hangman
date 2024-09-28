package hangman;

import display.MutableLine;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SimpleHangmanTest {

    @Test
    public void testNextStageAdvancesCorrectly() {
        // Arrange
        SimpleHangman hangman = new SimpleHangman();

        // Act
        boolean result = hangman.nextStage();

        // Assert
        assertTrue(result, "nextStage should return true before the last stage");

        // Verify that the head 'O' is drawn at line 2, position 1
        char head = hangman.hangmanLines().get(2).content().get(1);
        assertEquals('O', head, "Head should be drawn after first nextStage()");
    }

    @Test
    public void testNextStageReturnsFalseAfterLastStage() {
        // Arrange
        SimpleHangman hangman = new SimpleHangman();

        // Act & Advance through all stages
        boolean result = true;
        for (int i = 0; i < 6; i++) {
            result = hangman.nextStage();
            if (i < 5) {
                assertTrue(result, "nextStage should return true before the last stage");
            } else {
                assertFalse(result, "nextStage should return false after the last stage");
            }
        }

        // Verify that the right leg '\\' is drawn at line 4, position 2
        char rightLeg = hangman.hangmanLines().get(4).content().get(2);
        assertEquals('\\', rightLeg, "Right leg should be drawn at the last stage");
    }

    @Test
    public void testSetAttemptsValidInput() {
        // Arrange
        SimpleHangman hangman = new SimpleHangman();

        // Act
        boolean result = hangman.setAttempts(3);

        // Assert
        assertTrue(result, "setAttempts should return true for valid attempts");

        // Verify the hangman state corresponds to 3 attempts
        List<MutableLine<Character>> lines = hangman.hangmanLines();
        assertEquals('O', lines.get(2).content().get(1), "Head should be drawn");
        assertEquals('|', lines.get(3).content().get(1), "Body should be drawn");
        assertEquals('/', lines.get(3).content().get(0), "Left arm should be drawn");
        assertEquals(' ', lines.get(3).content().get(2), "Right arm should not be drawn yet");
    }

    @Test
    public void testSetAttemptsInvalidNegative() {
        // Arrange
        SimpleHangman hangman = new SimpleHangman();

        // Act
        boolean result = hangman.setAttempts(-1);

        // Assert
        assertFalse(result, "setAttempts should return false for negative attempts");
    }

    @Test
    public void testSetAttemptsInvalidTooHigh() {
        // Arrange
        SimpleHangman hangman = new SimpleHangman();

        // Act
        boolean result = hangman.setAttempts(7);

        // Assert
        assertFalse(result, "setAttempts should return false for attempts greater than 6");
    }

    @Test
    public void testNextStageDoesNotChangeAfterGameOver() {
        // Arrange
        SimpleHangman hangman = new SimpleHangman();

        // Advance to the game over state
        for (int i = 0; i < 6; i++) {
            hangman.nextStage();
        }

        // Capture the hangman contents after game over
        List<List<Character>> contentsBefore = hangman.hangmanLines().stream()
            .map(line -> List.copyOf(line.content()))
            .toList();

        // Act
        boolean result = hangman.nextStage();

        // Assert
        assertFalse(result, "nextStage should return false after game over");

        // Verify the hangman state has not changed
        List<MutableLine<Character>> linesAfter = hangman.hangmanLines();

        for (int i = 0; i < linesAfter.size(); i++) {
            List<Character> beforeContent = contentsBefore.get(i);
            List<Character> afterContent = linesAfter.get(i).content();
            assertEquals(beforeContent, afterContent, "Line " + i + " content should not change after game over");
        }
    }
}
