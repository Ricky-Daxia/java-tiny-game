package game.screen;

import game.asciiPanel.AsciiPanel;

public class StartScreen extends RestartScreen {

    @Override
    public void displayOutput(AsciiPanel terminal, int id) {
        terminal.write("This is the start screen.", 2, 10);
        terminal.write("Press ENTER to continue...", 2, 11);
        if (id == 0) {
            terminal.write("Press P to play in recording mode", 2, 12);
            terminal.write("Press R to replay record and continue", 2, 13);
        }
    }

}
