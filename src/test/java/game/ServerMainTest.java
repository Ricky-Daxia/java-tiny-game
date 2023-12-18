package game;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class ServerMainTest {
    @Test(expected = NullPointerException.class)
    public void testServerMain() {
        assertNotNull(new ServerMain());
        new ServerMain().add(0);
        new ServerMain().respondToUserInput(null, null);
    }

    @Test
    public void testRun() {
        ServerMain.main(null);
        //Client.main(null);
    }

    @Test(expected = Exception.class)
    public void test1() {
        new ServerMain().keyPressed(0, 0);
    }

}
