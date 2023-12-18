package game.netComponent;

import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class ReadEventHandler implements EventHandler {

    private Selector demultiplexer;
    private Reactor reactor;

    public ReadEventHandler(Selector demultiplexer, Reactor reactor) {
        this.demultiplexer = demultiplexer;
        this.reactor = reactor;
    }

    public void handleEvent(SelectionKey handle) throws Exception {
        System.out.println("===== Read Event Handler =====");

        ByteBuffer inputBuffer = ByteBuffer.allocate(1024);
        SocketChannel socketChannel =
                (SocketChannel) handle.channel();

        // socketChannel.read(inputBuffer); // Read data from client

        // // Rewind the buffer to start reading from the beginning
        // inputBuffer.flip();

        // byte[] buffer = new byte[inputBuffer.limit()];
        // inputBuffer.get(buffer);

        // System.out.println("Received message from client : " +
        //         new String(buffer));

        // // Rewind the buffer to the previous state.
        // inputBuffer.flip();
        // // Register the interest for writable readiness event for
        // // this channel in order to echo back the message
        // socketChannel.register(
        //         demultiplexer, SelectionKey.OP_WRITE, inputBuffer);

        int numRead = -1;
		try {
            numRead = socketChannel.read(inputBuffer);
        } catch (SocketException socketException) {
            // reactor.getClientKeys().remove(handle);
			Socket socket = socketChannel.socket();
			SocketAddress remoteAddr = socket.getRemoteSocketAddress();
			System.out.println("Connection closed by client: " + remoteAddr);
			socketChannel.close();
			handle.cancel();         
            return; 
        }
        if (numRead != -1) {
            byte[] data = new byte[numRead];
            System.arraycopy(inputBuffer.array(), 0, data, 0, numRead);
            //System.out.println("Got: " + new String(data));
            reactor.parseData(data, handle);            
        }
    }
}