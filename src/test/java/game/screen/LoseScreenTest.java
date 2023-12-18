package game.screen;

import org.junit.Test;

import game.asciiPanel.AsciiFont;
import game.asciiPanel.AsciiPanel;

public class LoseScreenTest {
    @Test 
    public void testLoseScreen() {
        AsciiPanel asciiPanel = new AsciiPanel(40, 40, AsciiFont.gold);
        new LoseScreen().displayOutput(asciiPanel, 0);
    }
}
