package game;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class ClientTest {
    @Test(expected = NullPointerException.class)
    public void testClient() {
        assertNotNull(new Client());
        new Client().repaint();
    }

    @Test
    public void testRun() {
        //ServerMain.main(null);
        Client.main(null);
    }

    @Test(expected = Exception.class)
    public void test1() {
        new Client().keyPressed(null);
    }

    @Test
    public void test2() {
        new Client().keyReleased(null);
    }

    @Test
    public void test3() {
        new Client().keyTyped(null);
    }
}
