package game.screen;

import java.awt.event.KeyEvent;

import game.asciiPanel.AsciiPanel;

public class WinScreen extends RestartScreen {

    @Override
    public void displayOutput(AsciiPanel terminal, int id) {
        terminal.write("You won!", 0, 0);
        if (id == 0) {
            terminal.write("Press enter to go again.", 0, 1);
        }
    }

    @Override
    public Screen respondToUserInput(KeyEvent key, int id) {
        if (id == 0) {
            switch (key.getKeyCode()) {
                case KeyEvent.VK_ENTER:
                    return new SnakeGameScreen();
                default:
                    return this;
            }            
        }
        return this;
    }

    @Override
    public Screen respondToUserInput(int e, int id) {
        if (id == 0) {
            switch (e) {
                case KeyEvent.VK_ENTER:
                    return new SnakeGameScreen();
                default:
                    return this;
            }            
        }
        return this;
    }

}
