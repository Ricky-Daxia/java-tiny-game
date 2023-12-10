package game;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class ServerMainTest {
    @Test
    public void testServerMain() {
        assertNotNull(new ServerMain());
    }

    @Test
    public void testRun() {
        ServerMain.main(null);
        //Client.main(null);
    }
}
