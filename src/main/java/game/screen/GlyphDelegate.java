package game.screen;

import game.asciiPanel.AsciiPanel;

public interface GlyphDelegate {

    void printGlyph(AsciiPanel terminal, int offsetX, int offsetY);
}