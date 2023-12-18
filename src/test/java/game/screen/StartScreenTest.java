package game.screen;

import org.junit.Test;

import game.asciiPanel.AsciiFont;
import game.asciiPanel.AsciiPanel;

public class StartScreenTest {
    @Test 
    public void testStartScreen() {
        AsciiPanel asciiPanel = new AsciiPanel(40, 40, AsciiFont.gold);
        new StartScreen().displayOutput(asciiPanel, 0);
    }
}
