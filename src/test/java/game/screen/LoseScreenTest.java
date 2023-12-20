package game.screen;

import java.awt.event.KeyEvent;

import org.junit.Test;
import org.mockito.Mockito;

import game.asciiPanel.AsciiFont;
import game.asciiPanel.AsciiPanel;

public class LoseScreenTest {
    @Test 
    public void testLoseScreen() {
        AsciiPanel asciiPanel = new AsciiPanel(40, 40, AsciiFont.gold);
        new LoseScreen().displayOutput(asciiPanel, 0);
        new LoseScreen().respondToUserInput(Mockito.mock(KeyEvent.class), 0);
        new LoseScreen().respondToUserInput(KeyEvent.VK_0, 0);
    }
}
