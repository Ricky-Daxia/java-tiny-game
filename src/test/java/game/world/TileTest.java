package game.world;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import game.asciiPanel.AsciiPanel;

public class TileTest {
    @Test
    public void test1() {
        assertEquals(Tile.FLOOR.color(), AsciiPanel.green);
        assertEquals(Tile.WALL.color(), AsciiPanel.brightBlack);
        assertEquals(Tile.WATER.color(), AsciiPanel.blue);
        assertEquals(Tile.LeftUp.color(), AsciiPanel.magenta);
        assertEquals(Tile.RightUp.color(), AsciiPanel.magenta);
        assertEquals(Tile.LeftDown.color(), AsciiPanel.magenta);
        assertEquals(Tile.RightDown.color(), AsciiPanel.magenta);
        assertEquals(Tile.Horizontal.color(), AsciiPanel.magenta);
        assertEquals(Tile.Vertical.color(), AsciiPanel.magenta);
        assertEquals(Tile.Polluted.color(), AsciiPanel.magenta);
    }

    @Test
    public void test2() {
        assertEquals(Tile.FLOOR.isDiggable(), false);
    }
}
