package hangman;

import display.MutableLine;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@SuppressWarnings({"MagicNumber", "MultipleStringLiterals"})
public class SimpleHangman implements Hangman {
    @Getter private final List<MutableLine<Character>> hangmanLines = Arrays.asList(
        new MutableLine<>(toCharacterList(" +--+"), ""),
        new MutableLine<>(toCharacterList(" |  |"), ""),
        new MutableLine<>(toCharacterList("    |"), ""),
        new MutableLine<>(toCharacterList("    |"), ""),
        new MutableLine<>(toCharacterList("    |"), ""),
        new MutableLine<>(toCharacterList("    |"), ""),
        new MutableLine<>(toCharacterList("====="), "")
    );

    private final List<Runnable> hangmanStages = Arrays.asList(
        () -> hangmanLines.get(2).setContent(1, 'O'),
        () -> hangmanLines.get(3).setContent(1, '|'),
        () -> hangmanLines.get(3).setContent(0, '/'),
        () -> hangmanLines.get(3).setContent(2, '\\'),
        () -> hangmanLines.get(4).setContent(0, '/'),
        () -> hangmanLines.get(4).setContent(2, '\\')
    );

    private int currentStage = 0;

    private static List<Character> toCharacterList(String str) {
        return str.chars()
            .mapToObj(c -> (char) c)
            .collect(Collectors.toList());
    }

    public boolean nextStage() {
        if (currentStage + 1 >= hangmanStages.size()) {
            hangmanStages.get(currentStage).run();
            return false;
        }
        hangmanStages.get(currentStage).run();
        ++currentStage;
        return true;
    }

    public boolean setAttempts(int attempts) {
        if (attempts < 0 || attempts > 6) {
            return false;
        }

        int skipStages = 6 - attempts;

        while (skipStages > 0) {
            hangmanStages.get(currentStage).run();
            ++currentStage;
            --skipStages;
        }
        return true;
    }
}
