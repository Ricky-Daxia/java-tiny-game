package game;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

import java.awt.event.KeyEvent;

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

    public void test1() {
        new ApplicationMain().keyPressed(Mockito.mock(KeyEvent.class));
    }

    @Test 
    public void test2() {
        new ApplicationMain().keyReleased(Mockito.mock(KeyEvent.class));
    }

    @Test 
    public void test3() {
        new ApplicationMain().keyTyped(Mockito.mock(KeyEvent.class));
    }
}
