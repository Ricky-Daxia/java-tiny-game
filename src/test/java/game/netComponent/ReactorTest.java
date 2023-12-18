package game.netComponent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import org.junit.BeforeClass;
import org.junit.Test;


public class ReactorTest {

    private static Reactor reactor;

    @BeforeClass
    public static void init() throws Exception {
        reactor = new Reactor(null);
        new ReactorManager().startReactor(null);
    }

    @Test 
    public void testReactor() throws Exception {
        assertEquals(reactor.getAndInc(), 1);
        assertNotNull(reactor.getClientKeys());
        reactor.reply(null, null);
        reactor.parseData(null, null);
    }

    @Test(expected = Exception.class)
    public void testAccept() throws Exception {
        SelectionKey accepKey = new SelectionKey() {

                @Override
                public SelectableChannel channel() {
                        // TODO Auto-generated method stub
                        return null;
                }

                @Override
                public Selector selector() {
                        // TODO Auto-generated method stub
                        return null;
                }

                @Override
                public boolean isValid() {
                        // TODO Auto-generated method stub
                        return true;
                }

                @Override
                public void cancel() {
                        // TODO Auto-generated method stub
                        return;
                }

                @Override
                public int interestOps() {
                        // TODO Auto-generated method stub
                        return SelectionKey.OP_ACCEPT;
                }

                @Override
                public SelectionKey interestOps(int ops) {
                        // TODO Auto-generated method stub
                        return this;
                }

                @Override
                public int readyOps() {
                        // TODO Auto-generated method stub
                        return SelectionKey.OP_ACCEPT;
                }
  
        };
        reactor.handleEvent(accepKey);
    }
    
    @Test(expected = Exception.class)
    public void testRead() throws Exception {
        SelectionKey readKey = new SelectionKey() {

                @Override
                public SelectableChannel channel() {
                        // TODO Auto-generated method stub
                        return null;
                }

                @Override
                public Selector selector() {
                        // TODO Auto-generated method stub
                        return null;
                }

                @Override
                public boolean isValid() {
                        // TODO Auto-generated method stub
                        return true;
                }

                @Override
                public void cancel() {
                        // TODO Auto-generated method stub
                        return;
                }

                @Override
                public int interestOps() {
                        // TODO Auto-generated method stub
                        return SelectionKey.OP_READ;
                }

                @Override
                public SelectionKey interestOps(int ops) {
                        // TODO Auto-generated method stub
                        return this;
                }

                @Override
                public int readyOps() {
                        // TODO Auto-generated method stub
                        return SelectionKey.OP_READ;
                }
  
        };
        reactor.handleEvent(readKey);
    }

    @Test(expected = Exception.class)
    public void testWrite() throws Exception {
        SelectionKey writeKey = new SelectionKey() {

                @Override
                public SelectableChannel channel() {
                        // TODO Auto-generated method stub
                        return null;
                }

                @Override
                public Selector selector() {
                        // TODO Auto-generated method stub
                        return null;
                }

                @Override
                public boolean isValid() {
                        // TODO Auto-generated method stub
                        return true;
                }

                @Override
                public void cancel() {
                        // TODO Auto-generated method stub
                        return;
                }

                @Override
                public int interestOps() {
                        // TODO Auto-generated method stub
                        return SelectionKey.OP_WRITE;
                }

                @Override
                public SelectionKey interestOps(int ops) {
                        // TODO Auto-generated method stub
                        return this;
                }

                @Override
                public int readyOps() {
                        // TODO Auto-generated method stub
                        return SelectionKey.OP_WRITE;
                }

        }; 
        reactor.handleEvent(writeKey);
    }
}
