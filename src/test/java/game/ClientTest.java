package game;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class ClientTest {
    @Test
    public void testClient() {
        assertNotNull(new Client());
    }

    @Test
    public void testRun() {
        //ServerMain.main(null);
        Client.main(null);
    }
}
