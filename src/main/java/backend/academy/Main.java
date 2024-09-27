package backend.academy;

import game.Game;
import hangman.SimpleHangman;
import keyboard.CapitalizedKeyboard;
import lombok.experimental.UtilityClass;
import util.SetupWizard;
import word.CapitalizedWord;

@UtilityClass
@SuppressWarnings("MagicNumber")
public class Main {
    public static void main(String[] args) {
        SetupWizard.setupConsole();
        int difficulty = SetupWizard.setupDifficulty();
        String word = SetupWizard.setupWord();

        Game hangmanGame = new Game(
            new SimpleHangman(),
            new CapitalizedKeyboard(),
            new CapitalizedWord(word)
        );

        hangmanGame.build();
        hangmanGame.start(difficulty);
    }
}
