package game.asciiPanel;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AsciiFontTest {
    @Test 
    public void testAsciiFont() {
        assertEquals(AsciiFont.gold.getWidth(), 24);
        assertEquals(AsciiFont.gold.getHeight(), 24);
    }
}
