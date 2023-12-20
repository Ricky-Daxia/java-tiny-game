package game.screen;

import java.awt.event.KeyEvent;

import org.junit.Test;
import org.mockito.Mockito;

import game.asciiPanel.AsciiFont;
import game.asciiPanel.AsciiPanel;

public class WinScreenTest {
    @Test 
    public void testRestartScreen() {
        AsciiPanel asciiPanel = new AsciiPanel(40, 40, AsciiFont.gold);
        new WinScreen().displayOutput(asciiPanel, 0);
        new WinScreen().respondToUserInput(Mockito.mock(KeyEvent.class), 0);
        new WinScreen().respondToUserInput(Mockito.mock(KeyEvent.class), 1);
        new WinScreen().respondToUserInput(KeyEvent.VK_ENTER, 0);
        new WinScreen().respondToUserInput(KeyEvent.VK_0, 0);
        new WinScreen().respondToUserInput(KeyEvent.VK_ENTER, 1);
    }
}
