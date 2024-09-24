package hangman;

import display.MutableLine;
import lombok.Getter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SimpleHangman {
    @Getter private final List<MutableLine<Character>> hangmanLines = Arrays.asList(
        new MutableLine<>(toCharacterList(" +--+"),""),
        new MutableLine<>(toCharacterList(" |  |"),""),
        new MutableLine<>(toCharacterList("    |"),""),
        new MutableLine<>(toCharacterList("    |"),""),
        new MutableLine<>(toCharacterList("    |"),""),
        new MutableLine<>(toCharacterList("    |"),""),
        new MutableLine<>(toCharacterList("====="),"")
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

    public boolean nextStage() {
        if (currentStage >= hangmanStages.size()) {
            return false;
        }
        hangmanStages.get(currentStage).run();
        ++currentStage;
        return true;
    }

    private static List<Character> toCharacterList(String str) {
        return str.chars()
                  .mapToObj(c -> (char) c)
                  .collect(Collectors.toList());
    }
}
