package game.screen;

import game.asciiPanel.AsciiPanel;
import java.awt.event.KeyEvent;

public interface Screen {

    public void displayOutput(AsciiPanel terminal);

    public Screen respondToUserInput(KeyEvent key);

    public Screen respondToUserInput(int e);
}
