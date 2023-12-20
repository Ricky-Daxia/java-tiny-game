package game;

import static org.junit.Assert.assertNotNull;

import java.awt.event.KeyEvent;

import org.junit.Test;
import org.mockito.Mockito;

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

    @Test
    public void test1() {
        new Client().keyPressed(Mockito.mock(KeyEvent.class));
    }

    @Test
    public void test2() {
        new Client().keyReleased(Mockito.mock(KeyEvent.class));
    }

    @Test
    public void test3() {
        new Client().keyTyped(Mockito.mock(KeyEvent.class));
    }
}
