package game.netComponent;

import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import game.ServerMain;

/* This is the Initiation Dispatcher or the Reactor */
public class Reactor {
    private Map<Integer, EventHandler> registeredHandlers = new ConcurrentHashMap<Integer, EventHandler>();
    private Selector demultiplexer;

    private ServerMain server;

	private List<SelectionKey> clientKeys;

	private int IDs;
	public int getAndInc() {
		int x = IDs;
		IDs++;
		return x;
	}

    public void addClientKey(SelectionKey selectionKey) {
        clientKeys.add(selectionKey);
        server.add((int) selectionKey.attachment());
    }

    public List<SelectionKey> getClientKeys() {
        return clientKeys;
    }

    public Reactor(ServerMain server) throws Exception {
        demultiplexer = Selector.open();
        clientKeys = new ArrayList<>();
        this.server = server;
        IDs = 1;
    }

    public Selector getDemultiplexer() {
        return demultiplexer;
    }

    public void registerEventHandler(
            int eventType, EventHandler eventHandler) {
        registeredHandlers.put(eventType, eventHandler);
    }

    public void registerChannel(
            int eventType, SelectableChannel channel) throws Exception {
        channel.register(demultiplexer, eventType);
    }

    public void run() {
        // new Thread(new Runnable() {
        //     public void run() {
                try {
                    // Loop indefinitely
                    while (true) {
                        demultiplexer.select();

                        Set<SelectionKey> readyHandles =
                                demultiplexer.selectedKeys();
                        Iterator<SelectionKey> handleIterator =
                                readyHandles.iterator();

                        while (handleIterator.hasNext()) {
                            SelectionKey handle = handleIterator.next();

                            handleEvent(handle);
                                                 
                            handleIterator.remove();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
        //     }
        // }).start();
    }

    public void handleEvent(SelectionKey handle) throws Exception {
        if (handle.isAcceptable()) {
            EventHandler handler =
                    registeredHandlers.get(SelectionKey.OP_ACCEPT);
            handler.handleEvent(handle);
        }

        else if (handle.isReadable()) {
            EventHandler handler =
                    registeredHandlers.get(SelectionKey.OP_READ);
            handler.handleEvent(handle);
        }

        else if (handle.isWritable()) {
            EventHandler handler =
                    registeredHandlers.get(SelectionKey.OP_WRITE);
            handler.handleEvent(handle);
        }  
    }

	public void reply(byte[] message, SelectionKey targetKey) {
		//System.out.println("in reply");
		try {
			for (SelectionKey selectionKey: demultiplexer.keys()) {
				Channel channel = selectionKey.channel();
				if (channel instanceof SocketChannel && channel == targetKey.channel()) {
					SocketChannel socketChannel = (SocketChannel) channel;
					ByteBuffer byteBuffer = ByteBuffer.wrap(message);
					socketChannel.write(byteBuffer);
				}
			}			
		} catch (Exception e) {
			// check whether the client is shutdown
			e.printStackTrace();
		}
	}

    public void parseData(byte[] data, SelectionKey selectionKey) {
        try {
            ByteBuffer byteBuffer = ByteBuffer.wrap(data);
            int[] keyCodes = new int[data.length / Integer.BYTES];
            for (int i = 0; i < keyCodes.length; i++) {
                keyCodes[i] = byteBuffer.getInt();
                //System.out.println("keyCode " + keyCodes[i]);
            }
            server.respondToUserInput(keyCodes, selectionKey);            
        } catch (NullPointerException e) {
            System.out.println("parse data with null pointer!!!");
        }

    }

}
