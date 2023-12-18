package game.screen;

import org.junit.Test;

import game.asciiPanel.AsciiFont;
import game.asciiPanel.AsciiPanel;

public class WinScreenTest {
    @Test 
    public void testRestartScreen() {
        AsciiPanel asciiPanel = new AsciiPanel(40, 40, AsciiFont.gold);
        new WinScreen().displayOutput(asciiPanel, 0);
    }
}
