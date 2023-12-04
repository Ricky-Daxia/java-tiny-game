package game.screen;

import game.asciiPanel.AsciiPanel;
import java.awt.event.KeyEvent;
import java.io.Serializable;

public abstract class RestartScreen implements Screen, Serializable {

    @Override
    public abstract void displayOutput(AsciiPanel terminal);

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                return new SnakeGameScreen();
            default:
                return this;
        }
    }

    @Override
    public Screen respondToUserInput(int e) {
        switch (e) {
            case KeyEvent.VK_ENTER:
                return new SnakeGameScreen();
            default:
                return this;
        }
    }

}
