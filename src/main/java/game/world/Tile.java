package game.world;

import game.asciiPanel.AsciiPanel;
import java.awt.Color;
import java.io.Serializable;

public enum Tile {

    FLOOR((char) 250, AsciiPanel.green), // 0

    WALL((char) 176, AsciiPanel.brightBlack), // 1

    WATER((char) 240, AsciiPanel.blue), // 2

    LeftUp((char) 218, AsciiPanel.magenta), // 3

    RightUp((char) 191, AsciiPanel.magenta), // 4

    LeftDown((char) 192, AsciiPanel.magenta), // 5

    RightDown((char) 217, AsciiPanel.magenta), // 6

    Horizontal((char) 220, AsciiPanel.magenta), // 7

    Vertical((char) 222, AsciiPanel.magenta), // 8

    Polluted((char) 253, AsciiPanel.magenta); // 9 

    private char glyph;

    public char glyph() {
        return glyph;
    }

    private Color color;

    public Color color() {
        return color;
    }

    public boolean isDiggable() {
        return false;
    }

    public boolean isGround() {
        return this == Tile.FLOOR || this == Tile.Polluted;
    }

    public boolean isPolluted() {
        return this == Tile.Polluted;
    }

    Tile(char glyph, Color color) {
        this.glyph = glyph;
        this.color = color;
    }
}
