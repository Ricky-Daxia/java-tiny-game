package game.screen;

import game.asciiPanel.AsciiPanel;
import java.awt.event.KeyEvent;

public interface Screen {

    public void displayOutput(AsciiPanel terminal, int id);

    public Screen respondToUserInput(KeyEvent key, int id);

    public Screen respondToUserInput(int e, int id);
}
