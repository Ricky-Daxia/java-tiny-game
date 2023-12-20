package game;

import static org.junit.Assert.assertNotNull;

import java.awt.event.KeyEvent;

import org.junit.Test;

public class ServerMainTest {
    @Test(expected = NullPointerException.class)
    public void testServerMain() {
        ServerMain serverMain = new ServerMain();
        assertNotNull(serverMain);
        serverMain.add(1);
        serverMain.transit(1, KeyEvent.VK_ENTER);
        serverMain.transit(1, KeyEvent.VK_LEFT);
        serverMain.transit(1, KeyEvent.VK_RIGHT);
        serverMain.transit(1, KeyEvent.VK_UP);
        serverMain.transit(1, KeyEvent.VK_DOWN);
        serverMain.repaint();
        serverMain.respondToUserInput(null, null);
    }

    @Test
    public void testRun() {
        ServerMain.main(null);
    }

    @Test(expected = Exception.class)
    public void test1() {
        new ServerMain().keyPressed(0, 0);
    }

    @Test 
    public void test2() {
        ServerMain serverMain = new ServerMain();
        serverMain.add(1);
        serverMain.keyPressed(KeyEvent.VK_ENTER, 1);
        serverMain.keyPressed(KeyEvent.VK_UP, 1);
        serverMain.keyPressed(KeyEvent.VK_LEFT, 1);
        serverMain.keyPressed(KeyEvent.VK_DOWN, 1);
        serverMain.keyPressed(KeyEvent.VK_RIGHT, 1);
    }

}
