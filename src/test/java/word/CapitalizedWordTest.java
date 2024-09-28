package word;

import display.MutableLine;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CapitalizedWordTest {

    @Test
    void testConstructor() {
        // Given
        String testWord = "HELLO";
        CapitalizedWord capitalizedWord = new CapitalizedWord(testWord);

        // When
        char[] wordArray = capitalizedWord.word();
        MutableLine<String> wordLine = capitalizedWord.wordLine();

        // Then
        // Check that word() returns the correct char array
        assertArrayEquals(testWord.toCharArray(), wordArray);

        // Check that wordLine() is initialized with underlined spaces
        List<String> content = wordLine.content();
        String underlinedSpace = "\u001B[4m \u001B[0m";
        List<String> expectedContent = Collections.nCopies(testWord.length(), underlinedSpace);
        assertEquals(expectedContent, content);
    }

    @Test
    void testPushLetterCorrectGuess() {
        // Given
        String testWord = "HELLO";
        CapitalizedWord capitalizedWord = new CapitalizedWord(testWord);

        // When
        char guess = 'H';
        byte result = capitalizedWord.pushLetter(guess);

        // Then
        assertEquals(1, result); // Since wordSet is not empty after this guess
        List<String> content = capitalizedWord.wordLine().content();
        String underlinedH = "\u001B[4mH\u001B[0m";
        String underlinedSpace = "\u001B[4m \u001B[0m";
        List<String> expectedContent = List.of(underlinedH, underlinedSpace, underlinedSpace, underlinedSpace, underlinedSpace);
        assertEquals(expectedContent, content);
    }

    @Test
    void testPushLetterIncorrectGuess() {
        // Given
        String testWord = "HELLO";
        CapitalizedWord capitalizedWord = new CapitalizedWord(testWord);

        // When
        char guess = 'X';
        byte result = capitalizedWord.pushLetter(guess);

        // Then
        assertEquals(-1, result); // Incorrect guess
        List<String> content = capitalizedWord.wordLine().content();
        // Content should remain unchanged
        String underlinedSpace = "\u001B[4m \u001B[0m";
        List<String> expectedContent = Collections.nCopies(testWord.length(), underlinedSpace);
        assertEquals(expectedContent, content);
    }

    @Test
    void testPushLetterCorrectGuessMultipleOccurrences() {
        // Given
        String testWord = "HELLO";
        CapitalizedWord capitalizedWord = new CapitalizedWord(testWord);

        // When
        char guess = 'L';
        byte result = capitalizedWord.pushLetter(guess);

        // Then
        assertEquals(1, result); // Since wordSet is not empty after this guess
        List<String> content = capitalizedWord.wordLine().content();
        String underlinedSpace = "\u001B[4m \u001B[0m";
        String underlinedL = "\u001B[4mL\u001B[0m";
        List<String> expectedContent = List.of(
            underlinedSpace,
            underlinedSpace,
            underlinedL,
            underlinedL,
            underlinedSpace
        );
        assertEquals(expectedContent, content);
    }

    @Test
    void testPushLetterGuessAllLetters() {
        // Given
        String testWord = "HI";
        CapitalizedWord capitalizedWord = new CapitalizedWord(testWord);

        // When
        byte resultH = capitalizedWord.pushLetter('H');
        byte resultI = capitalizedWord.pushLetter('I');

        // Then
        assertEquals(1, resultH); // After first guess, wordSet is not empty
        assertEquals(0, resultI); // After second guess, wordSet should be empty
        List<String> content = capitalizedWord.wordLine().content();
        String underlinedH = "\u001B[4mH\u001B[0m";
        String underlinedI = "\u001B[4mI\u001B[0m";
        List<String> expectedContent = List.of(underlinedH, underlinedI);
        assertEquals(expectedContent, content);
    }

    @Test
    void testPushLetterGuessSameLetterTwice() {
        // Given
        String testWord = "HELLO";
        CapitalizedWord capitalizedWord = new CapitalizedWord(testWord);

        // When
        byte resultFirst = capitalizedWord.pushLetter('H');
        byte resultSecond = capitalizedWord.pushLetter('H');

        // Then
        assertEquals(1, resultFirst);
        assertEquals(-1, resultSecond); // Second time, 'H' is no longer in wordSet
        List<String> content = capitalizedWord.wordLine().content();
        String underlinedH = "\u001B[4mH\u001B[0m";
        String underlinedSpace = "\u001B[4m \u001B[0m";
        List<String> expectedContent = List.of(underlinedH, underlinedSpace, underlinedSpace, underlinedSpace, underlinedSpace);
        assertEquals(expectedContent, content);
    }

    @Test
    void testPushLetterCaseSensitivity() {
        // Given
        String testWord = "Hello";
        CapitalizedWord capitalizedWord = new CapitalizedWord(testWord);

        // When
        byte resultLowerCase = capitalizedWord.pushLetter('h');
        byte resultUpperCase = capitalizedWord.pushLetter('H');

        // Then
        assertEquals(-1, resultLowerCase); // 'h' is not in wordSet if 'H' is uppercase
        assertEquals(1, resultUpperCase);  // 'H' is correctly identified
    }

    @Test
    void testPushLetterAfterCompletion() {
        // Given
        String testWord = "HI";
        CapitalizedWord capitalizedWord = new CapitalizedWord(testWord);

        // When
        capitalizedWord.pushLetter('H');
        capitalizedWord.pushLetter('I');
        // Now wordSet should be empty
        byte result = capitalizedWord.pushLetter('H'); // Attempt to push a letter after completion

        // Then
        assertEquals(-1, result); // Should return -1 as 'H' is no longer in wordSet
    }
}
