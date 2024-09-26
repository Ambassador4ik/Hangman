package word;

import display.MutableLine;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;

public class CapitalizedWord implements Word {
    @Getter private final MutableLine<String> wordLine = new MutableLine<>();
    private final Set<Character> wordSet;
    @Getter private final char[] word;

    public CapitalizedWord(String word) {
        this.word = word.toCharArray();
        this.wordSet = new HashSet<>(word.length());
        for (char c : this.word) {
            this.wordSet.add(c);
        }
        String emptyChar = applyUnderline(" ");
        wordLine.setContent(new ArrayList<>(Collections.nCopies(this.word.length, emptyChar)));
    }

    public byte pushLetter(char letter) {
        if (!wordSet.contains(letter)) {
            return -1;
        }
        wordSet.remove(letter);

        boolean found = false;
        for (int ind = 0; ind < word.length; ind++) {
            if (word[ind] == letter) {
                wordLine.setContent(ind, applyUnderline(word[ind]));
                found = true;
            }
        }

        if (!found) {
            return -1;
        }

        if (wordSet.isEmpty()) {
            return 0;
        }
        return 1;
    }

    private String applyUnderline(Object input) {
        return "\u001B[4m" + input.toString() + "\u001B[0m";
    }
}
