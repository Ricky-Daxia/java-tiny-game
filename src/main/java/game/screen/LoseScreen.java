package game.screen;

import game.asciiPanel.AsciiPanel;

public class LoseScreen extends RestartScreen {

    @Override
    public void displayOutput(AsciiPanel terminal) {
        terminal.write("You lost!", 0, 0);
        terminal.write("Press enter to try again.", 0, 1);
    }

}
