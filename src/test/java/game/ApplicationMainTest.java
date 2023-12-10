package game;
import org.junit.Test;
import static org.junit.Assert.*;

public class ApplicationMainTest {
    @Test
    public void testRepaint() {
        assertEquals(new ApplicationMain().record, null);
        assertEquals(new ApplicationMain().recordCnt, 0);
    }
    
}
