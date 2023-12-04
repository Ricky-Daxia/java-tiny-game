package game.screen;

import java.io.Serializable;

import game.asciiPanel.AsciiPanel;

public class StartScreen extends RestartScreen {

    @Override
    public void displayOutput(AsciiPanel terminal, int id) {
        terminal.write("This is the start screen.", 2, 10);
        terminal.write("Press ENTER to continue...", 2, 11);
    }

}
