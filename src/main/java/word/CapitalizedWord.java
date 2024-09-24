package word;

import display.Line;
import display.LineListener;
import display.MutableLine;
import lombok.Getter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class CapitalizedWord{
    @Getter private final MutableLine<String> wordLine = new MutableLine<>();
    private final String word;

    public CapitalizedWord(String word) {
        this.word = word.toUpperCase();
        String emptyChar = applyUnderline(" ");
        wordLine.setContent(new ArrayList<>(Collections.nCopies(word.length(), emptyChar)));
    }

    public boolean pushLetter(char letter) {
        int ind = word.indexOf(letter);
        if (ind == -1) return false;

        wordLine.setContent(ind, applyUnderline(word.charAt(ind)));
        return true;
    }

    private String applyUnderline(String string) {
        return "\u001B[4m" + string + "\u001B[0m";
    }

    private String applyUnderline(char ch) {
        return "\u001B[4m" + ch + "\u001B[0m";
    }
}
