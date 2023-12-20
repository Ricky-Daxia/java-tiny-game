package game.asciiPanel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;
import java.awt.Color;

public class AsciiPanelTest {

@Test( expected = IllegalArgumentException.class )
  public void testSetNegativeCursorX() {
    AsciiPanel panel = new AsciiPanel();
    panel.setCursorX(-1);

    fail("Should have thrown an IllegalArgumentException.");
  }

  @Test( expected = IllegalArgumentException.class )
  public void testSetCursorXOutsideOfMax() {
    AsciiPanel panel = new AsciiPanel();
    panel.setCursorX(Integer.MAX_VALUE);

    fail("Should have thrown an IllegalArgumentException.");
  }

  @Test( expected = IllegalArgumentException.class )
  public void testSetNegativeCursorY() {
    AsciiPanel panel = new AsciiPanel();
    panel.setCursorY(-1);

    fail("Should have thrown an IllegalArgumentException.");
  }

  @Test( expected = IllegalArgumentException.class )
  public void testSetCursorYOutsideOfMax() {
    AsciiPanel panel = new AsciiPanel();
    panel.setCursorY(Integer.MAX_VALUE);

    fail("Should have thrown an IllegalArgumentException.");
  }

  @Test
  public void testSetCursorX() {
    AsciiPanel panel = new AsciiPanel(1, 1);
    panel.setCursorX(0);
  }

  @Test
  public void testSetCursorY() {
    AsciiPanel panel = new AsciiPanel(1, 1);
    panel.setCursorY(0);
  }

  @Test( expected = NullPointerException.class )
  public void testSetNullDefaultBackgroundColor() {
    AsciiPanel panel = new AsciiPanel();
    panel.setDefaultBackgroundColor(null);

    fail("Should have thrown a NullPointerException.");
  }

  @Test( expected = NullPointerException.class )
  public void testSetNullDefaultForegroundColor() {
    AsciiPanel panel = new AsciiPanel();
    panel.setDefaultForegroundColor(null);

    fail("Should have thrown a NullPointerException.");
  }

  @Test( expected = IllegalArgumentException.class )
  public void testConstructorZeroWidth() {
    new AsciiPanel(0, 24);

    fail("Should have thrown an IllegalArgumentException.");
  }

  @Test( expected = IllegalArgumentException.class )
  public void testConstructorZeroHeight() {
    new AsciiPanel(80, 0);

    fail("Should have thrown an IllegalArgumentException.");
  }

  @Test( expected = NullPointerException.class )
  public void testWriteNullFail() {
    AsciiPanel panel = new AsciiPanel(80, 1);
    panel.write(null);
    fail("Should have thrown a NullPointerException.");
  }

  @Test( expected = IllegalArgumentException.class )
  public void testWriteInvalidLengthFail() {
    AsciiPanel panel = new AsciiPanel(80, 1);
    String superLongString = String.format("%0100d", 1);
    panel.write(superLongString);
    fail("Should have thrown an IllegalArgumentException.");
  }

  @Test
  public void testWriteChar() {
    AsciiPanel panel = new AsciiPanel(1, 1);
    panel.write(' ');
  }

  @Test
  public void testWriteCharFG() {
    AsciiPanel panel = new AsciiPanel(1, 1);
    panel.write(' ', AsciiPanel.white);
  }

  @Test
  public void testWriteCharFGBG() {
    AsciiPanel panel = new AsciiPanel(1, 1);
    panel.write(' ', AsciiPanel.white, AsciiPanel.black);
  }

  @Test
  public void testWriteCharXY() {
    AsciiPanel panel = new AsciiPanel(1, 1);
    panel.write(' ', 0, 0);
  }

  @Test
  public void testWriteCharXYFG() {
    AsciiPanel panel = new AsciiPanel(1, 1);
    panel.write(' ', 0, 0, AsciiPanel.white);
  }

  @Test
  public void testWriteCharXYFGBG() {
    AsciiPanel panel = new AsciiPanel(1, 1);
    panel.write(' ', 0, 0, AsciiPanel.white, AsciiPanel.black);
  }

  @Test
  public void testWriteString() {
    AsciiPanel panel = new AsciiPanel(1, 1);
    panel.write(" ");
  }

  @Test
  public void testWriteStringFG() {
    AsciiPanel panel = new AsciiPanel(1, 1);
    panel.write(" ", AsciiPanel.white);
  }

  @Test
  public void testWriteStringFGBG() {
    AsciiPanel panel = new AsciiPanel(1, 1);
    panel.write(" ", AsciiPanel.white, AsciiPanel.black);
  }

  @Test
  public void testWriteStringXY() {
    AsciiPanel panel = new AsciiPanel(1, 1);
    panel.write(" ", 0, 0);
  }

  @Test
  public void testWriteStringXYFG() {
    AsciiPanel panel = new AsciiPanel(1, 1);
    panel.write(" ", 0, 0, AsciiPanel.white);
  }

  @Test
  public void testWriteStringXYFGBG() {
    AsciiPanel panel = new AsciiPanel(1, 1);
    panel.write(" ", 0, 0, AsciiPanel.white, AsciiPanel.black);
  }

  @Test
  public void testWriteCenter() {
    AsciiPanel panel = new AsciiPanel(1, 1);
    panel.writeCenter(" ", 0);
  }

  @Test
  public void testWriteCenterFG() {
    AsciiPanel panel = new AsciiPanel(1, 1);
    panel.writeCenter(" ", 0, AsciiPanel.white);
  }

  @Test
  public void testWriteCenterFGBG() {
    AsciiPanel panel = new AsciiPanel(1, 1);
    panel.writeCenter(" ", 0, AsciiPanel.white, AsciiPanel.black);
  }

  @Test
  public void testWrite() {
    int width = 100;
    int height = 100;
    AsciiPanel panel = new AsciiPanel(width, height);

    // write out characters in a specific pattern such that we can verify each is written correctly to the specified
    // position
    Color oddColumnForeground = new Color(255, 255, 255);
    Color evenColumnForeground = new Color(0, 0, 0);
    Color oddRowBackground = new Color(255, 255, 0);
    Color evenRowBackground = new Color(0, 255, 255);

    AsciiCharacterData[][] expectedCharacterData = new AsciiCharacterData[width][height];

    for (int x = 0; x < width; x++) {
      for (int y = 0; y < width; y++) {
        AsciiCharacterData characterData = new AsciiCharacterData();
        characterData.character = (char)(x + y);
        if (x % 2 == 0) {
          characterData.foregroundColor = evenColumnForeground;
        } else {
          characterData.foregroundColor = oddColumnForeground;
        }

        if (y % 2 == 0) {
          characterData.backgroundColor = evenRowBackground;
        } else {
          characterData.backgroundColor = oddRowBackground;
        }
        panel.write(characterData.character, x, y);
        expectedCharacterData[x][y] = characterData;
      }
    }

    // now validate that it was written as expected
    char[][] writtenData = panel.getChars();
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        char expectedCharData = expectedCharacterData[x][y].character;
        char writtenCharData = writtenData[x][y];
        assertEquals(expectedCharData, writtenCharData);
      }
    }
  }


  @Test
  public void testAsciiPanel() {
    AsciiPanel panel = new AsciiPanel(1, 1);
    assertEquals(panel.getCursorX(), 0);
    assertEquals(panel.getCursorY(), 0);
    assertNotNull(panel.getDefaultBackgroundColor());
    panel.setDefaultBackgroundColor(new Color(255));
    assertNotNull(panel.getDefaultForegroundColor());
    panel.setDefaultForegroundColor(new Color(255));
    assertNotNull(panel.getAsciiFont());
    panel.setAsciiFont(panel.getAsciiFont());
  }

  @Test(expected = NullPointerException.class) 
  public void testUpdate() {
    new AsciiPanel(1, 1).update(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWrite2() {
    new AsciiPanel(1, 1).write((char) -1);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testWrite3() {
    new AsciiPanel(1, 1).write((char) -1, new Color(0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWrite4() {
    new AsciiPanel(1, 1).write((char) -1, new Color(0), new Color(0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWrite5() {
    new AsciiPanel(1, 1).write((char) -1, 0, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWrite6() {
    new AsciiPanel(1, 1).write((char) 0, -1, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWrite7() {
    new AsciiPanel(1, 1).write((char) 0, 0, -1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWrite8() {
    new AsciiPanel(1, 1).write((char) -1, 0, 0, new Color(0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWrite9() {
    new AsciiPanel(1, 1).write((char) 0, -1, 0, new Color(0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWrite10() {
    new AsciiPanel(1, 1).write((char) 0, 0, -1, new Color(0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWrite11() {
    new AsciiPanel(1, 1).write((char) -1, 0, 0, new Color(0), new Color(0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWrite12() {
    new AsciiPanel(1, 1).write((char) 0, -1, 0, new Color(0), new Color(0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWrite13() {
    new AsciiPanel(1, 1).write((char) 0, 0, -1, new Color(0), new Color(0));
  }

  @Test
  public void testWrite14() {
    new AsciiPanel(1, 1).write((char) 0, 0, 0, null, new Color(0));
  }

  @Test
  public void testWrite15() {
    new AsciiPanel(1, 1).write((char) 0, 0, 0, new Color(0), null);
  }

  @Test(expected = NullPointerException.class)
  public void testWrite16() {
    new AsciiPanel(1, 1).write(null, new Color(0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWrite17() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 100000; i++) {
      sb.append('0');
    }
    new AsciiPanel().write(sb.toString(), new Color(0));
  }

  @Test(expected = NullPointerException.class)
  public void testWrite18() {
    new AsciiPanel(1, 1).write(null, new Color(0), new Color(0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWrite19() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 100000; i++) {
      sb.append('0');
    }
    new AsciiPanel().write(sb.toString(), new Color(0), new Color(0));
  }

  @Test(expected = NullPointerException.class)
  public void testWrite20() {
    new AsciiPanel(1, 1).write(null, 0, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWrite21() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 100000; i++) {
      sb.append('0');
    }
    new AsciiPanel().write(sb.toString(), 0, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWrite22() {
    new AsciiPanel().write("123", -1, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWrite23() {
    new AsciiPanel().write("123", 0, -1);
  }

  @Test(expected = NullPointerException.class)
  public void testWrite24() {
    new AsciiPanel(1, 1).write(null, 0, 0, new Color(0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWrite25() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 100000; i++) {
      sb.append('0');
    }
    new AsciiPanel().write(sb.toString(), 0, 0, new Color(0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWrite26() {
    new AsciiPanel().write("123", -1, 0, new Color(0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWrite27() {
    new AsciiPanel().write("123", 0, -1, new Color(0));
  }

  @Test(expected = NullPointerException.class)
  public void testWrite28() {
    new AsciiPanel(1, 1).write(null, 0, 0, new Color(0), new Color(0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWrite29() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 100000; i++) {
      sb.append('0');
    }
    new AsciiPanel().write(sb.toString(), 0, 0, new Color(0), new Color(0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWrite30() {
    new AsciiPanel().write("123", -1, 0, new Color(0), new Color(0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWrite31() {
    new AsciiPanel().write("123", 0, -1, new Color(0), new Color(0));
  }

  @Test
  public void test32() {
    new AsciiPanel().write("123", 0, 0, null, new Color(0));
  }

  @Test 
  public void test33() {
    new AsciiPanel().write("123", 0, 0, new Color(0), null);
  }

  @Test(expected = NullPointerException.class)
  public void test34() {
    new AsciiPanel().writeCenter(null, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void test35() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 100000; i++) {
      sb.append('0');
    }
    new AsciiPanel().writeCenter(sb.toString(), 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void test36() {
    new AsciiPanel().writeCenter("123", -1);
  }

  @Test(expected = NullPointerException.class)
  public void test37() {
    new AsciiPanel().writeCenter(null, 0, new Color(0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void test38() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 100000; i++) {
      sb.append('0');
    }
    new AsciiPanel().writeCenter(sb.toString(), 0, new Color(0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void test39() {
    new AsciiPanel().writeCenter("123", -1, new Color(0));
  }

  @Test(expected = NullPointerException.class)
  public void test40() {
    new AsciiPanel().writeCenter(null, 0, new Color(0), new Color(0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void test41() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 100000; i++) {
      sb.append('0');
    }
    new AsciiPanel().writeCenter(sb.toString(), 0, new Color(0), new Color(0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void test42() {
    new AsciiPanel().writeCenter("123", -1, new Color(0), new Color(0));
  }

  @Test
  public void test43() {
    new AsciiPanel().writeCenter("123", 0, null, new Color(0));
  }

  @Test
  public void test44() {
    new AsciiPanel().writeCenter("123", 0, new Color(0), null);
  }
}
