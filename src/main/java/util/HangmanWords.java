package util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * A utility class for managing Hangman words categorized by different themes.
 * Provides functionality to retrieve random words based on category IDs.
 */
@Getter
public final class HangmanWords {

    /**
     * Enum representing different categories of words.
     * Each category has an associated integer ID.
     */
    @Getter
    @RequiredArgsConstructor
    public enum Category {
        FRUITS(1),
        ANIMALS(2),
        COUNTRIES(3),
        SPORTS(4),
        COLORS(5);

        private final int id;

        // Static map to associate IDs with Categories
        private static final Map<Integer, Category> ID_MAP = new HashMap<>();

        static {
            for (Category category : Category.values()) {
                ID_MAP.put(category.id(), category);
            }
        }

        /**
         * Returns the Category corresponding to the given ID.
         *
         * @param id the category ID
         * @return the corresponding Category, or null if not found
         */
        public static Category fromId(int id) {
            return ID_MAP.get(id);
        }

        /**
         * Returns a random Category using the provided SecureRandom instance.
         *
         * @param random SecureRandom instance
         * @return a randomly selected Category
         */
        public static Category getRandomCategory(SecureRandom random) {
            Category[] categories = values();
            int randomIndex = random.nextInt(categories.length);
            return categories[randomIndex];
        }
    }

    // Immutable map holding categories and their corresponding word lists
    private final Map<Integer, List<String>> categoryWordsMap;

    // SecureRandom instance for generating secure random numbers
    @Setter
    private SecureRandom secureRandom;

    /**
     * Constructs a new HangmanWords instance with the default word lists and SecureRandom.
     */
    public HangmanWords() {
        this(defaultCategoryWordsMap(), defaultSecureRandom());
    }

    /**
     * Constructs a new HangmanWords instance with the specified word lists and SecureRandom.
     *
     * @param categoryWordsMap the category words map
     * @param secureRandom the SecureRandom instance
     */
    public HangmanWords(Map<Integer, List<String>> categoryWordsMap, SecureRandom secureRandom) {
        this.categoryWordsMap = categoryWordsMap;
        this.secureRandom = secureRandom;
    }

    private static Map<Integer, List<String>> defaultCategoryWordsMap() {
        return Map.of(
            Category.FRUITS.id(), List.of(
                "apple", "banana", "cherry", "date", "elderberry",
                "fig", "grape", "honeydew", "kiwi", "lemon"
            ),
            Category.ANIMALS.id(), List.of(
                "alligator", "bear", "cat", "dog", "elephant",
                "frog", "giraffe", "hippopotamus", "iguana", "jaguar"
            ),
            Category.COUNTRIES.id(), List.of(
                "argentina", "brazil", "canada", "denmark", "egypt",
                "france", "germany", "hungary", "india", "japan"
            ),
            Category.SPORTS.id(), List.of(
                "basketball", "cricket", "dodgeball", "equestrian", "fencing",
                "gymnastics", "hockey", "skating", "judo", "karate"
            ),
            Category.COLORS.id(), List.of(
                "azure", "beige", "cyan", "denim", "emerald",
                "fuchsia", "gold", "hazel", "indigo", "jade"
            )
        );
    }

    private static SecureRandom defaultSecureRandom() {
        try {
            return SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("No strong secure random available", e);
        }
    }

    /**
     * Retrieves a random word from the specified category.
     * If categoryId is 0, a random category is selected first.
     *
     * @param categoryId the ID of the category (0 for random category)
     * @return a randomly selected word from the specified or random category
     * @throws IllegalArgumentException if the categoryId is invalid
     */
    public String getRandomWord(int categoryId) {
        Category category;

        if (categoryId == 0) {
            category = Category.getRandomCategory(secureRandom());
        } else {
            category = Category.fromId(categoryId);
            if (category == null) {
                throw new IllegalArgumentException("Invalid category ID: " + categoryId);
            }
        }

        List<String> words = categoryWordsMap().get(category.id());
        if (words == null || words.isEmpty()) {
            throw new IllegalStateException("No words available for category: " + category);
        }

        int randomIndex = secureRandom().nextInt(words.size());
        return words.get(randomIndex);
    }

    /**
     * Retrieves the list of all available categories.
     *
     * @return a list of Category enums
     */
    public List<Category> getAllCategories() {
        return List.of(Category.values());
    }
}
