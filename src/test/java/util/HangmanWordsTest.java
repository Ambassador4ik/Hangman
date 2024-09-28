package util;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import util.HangmanWords.Category;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HangmanWordsTest {

    @Test
    public void testGetRandomWord_ValidCategory() {
        // Arrange
        HangmanWords hangmanWords = new HangmanWords();
        int categoryId = Category.FRUITS.id();

        // Act
        String word = hangmanWords.getRandomWord(categoryId);

        // Assert
        List<String> words = hangmanWords.categoryWordsMap().get(categoryId);
        assertTrue(words.contains(word), "The word should be from the FRUITS category");
    }

    @Test
    public void testGetRandomWord_RandomCategory() {
        // Arrange
        HangmanWords hangmanWords = new HangmanWords();
        int categoryId = 0;

        // Act
        String word = hangmanWords.getRandomWord(categoryId);

        // Assert
        boolean found = hangmanWords.categoryWordsMap().values().stream()
            .anyMatch(list -> list.contains(word));
        assertTrue(found, "The word should be from one of the categories");
    }

    @Test
    public void testGetRandomWord_InvalidCategory() {
        // Arrange
        HangmanWords hangmanWords = new HangmanWords();
        int invalidCategoryId = -1;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            hangmanWords.getRandomWord(invalidCategoryId);
        });
    }

    @Test
    public void testGetAllCategories() {
        // Arrange
        HangmanWords hangmanWords = new HangmanWords();

        // Act
        List<Category> categories = hangmanWords.getAllCategories();

        // Assert
        assertEquals(5, categories.size(), "There should be 5 categories");
        assertTrue(categories.contains(Category.FRUITS));
        assertTrue(categories.contains(Category.ANIMALS));
        assertTrue(categories.contains(Category.COUNTRIES));
        assertTrue(categories.contains(Category.SPORTS));
        assertTrue(categories.contains(Category.COLORS));
    }

    @Test
    public void testGetRandomWord_PredictableRandom() {
        // Arrange
        SecureRandom predictableRandom = new SecureRandom() {
            private int nextIntCalls = 0;

            @Override
            public int nextInt(int bound) {
                nextIntCalls++;
                return 0; // Always return 0 for predictability
            }
        };

        Map<Integer, List<String>> categoryWordsMap = Map.of(
            Category.FRUITS.id(), List.of("apple", "banana"),
            Category.ANIMALS.id(), List.of("cat", "dog")
        );

        HangmanWords hangmanWords = new HangmanWords(categoryWordsMap, predictableRandom);

        // Act
        String word = hangmanWords.getRandomWord(Category.FRUITS.id());

        // Assert
        assertEquals("apple", word, "With predictable random, should always return 'apple'");
    }

    @Test
    public void testGetRandomWord_NoWordsInCategory() {
        // Arrange
        Map<Integer, List<String>> categoryWordsMap = Map.of(
            Category.FRUITS.id(), List.of()
        );
        HangmanWords hangmanWords = new HangmanWords(categoryWordsMap, new SecureRandom());

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            hangmanWords.getRandomWord(Category.FRUITS.id());
        });
    }
}
