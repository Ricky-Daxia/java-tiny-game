package game.screen;

import game.asciiPanel.AsciiPanel;

public class WinScreen extends RestartScreen {

    @Override
    public void displayOutput(AsciiPanel terminal, int id) {
        terminal.write("You won!", 0, 0);
        terminal.write("Press enter to go again.", 0, 1);
    }

}
