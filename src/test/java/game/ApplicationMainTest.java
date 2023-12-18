package game;
import org.junit.Test;
import static org.junit.Assert.*;

public class ApplicationMainTest {
    @Test
    public void testRepaint() {
        assertEquals(new ApplicationMain().record, null);
        assertEquals(new ApplicationMain().recordCnt, 0);
    }
    
    @Test 
    public void testApplicationMain() {
        ApplicationMain.main(null);
    }

    @Test(expected = NullPointerException.class)
    public void test1() {
        new ApplicationMain().keyPressed(null);
    }

    @Test 
    public void test2() {
        new ApplicationMain().keyReleased(null);
    }

    @Test 
    public void test3() {
        new ApplicationMain().keyTyped(null);
    }
}
