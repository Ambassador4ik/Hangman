package backend.academy;

import game.Game;
import hangman.SimpleHangman;
import keyboard.CapitalizedKeyboard;
import lombok.experimental.UtilityClass;
import word.CapitalizedWord;

@UtilityClass
@SuppressWarnings("MagicNumber")
public class Main {
    public static void main(String[] args) {
        Game hangmanGame = new Game(
            new SimpleHangman(),
            new CapitalizedKeyboard(),
            new CapitalizedWord("APPROX")
        );

        hangmanGame.build();
        hangmanGame.start(6);
    }
}
