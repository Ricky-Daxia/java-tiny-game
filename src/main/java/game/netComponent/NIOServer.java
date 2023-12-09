package game.netComponent;

import java.awt.event.KeyEvent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * This is a simple NIO based server.
 *
 */
public class NIOServer {
	private Selector selector;

	private InetSocketAddress listenAddress;
	private final static int PORT = 9093;
	private List<SelectionKey> clientKeys;

	private int IDs;
	private int getAndInc() {
		int x = IDs;
		IDs++;
		return x;
	}

	// public static void main(String[] args) throws Exception {
	// 	try {
	// 		new NIOServer("localhost", 9093).startServer();
	// 	} catch (IOException e) {
	// 		e.printStackTrace();
	// 	}
	// }

	public NIOServer(String address, int port) throws IOException {
		listenAddress = new InetSocketAddress(address, port);
		this.clientKeys = new ArrayList<>();
		IDs = 1;
	}

	/**
	 * Start the server
	 * 
	 * @throws IOException
	 */
	public Selector startServer() throws IOException {
		this.selector = Selector.open();
		ServerSocketChannel serverChannel = ServerSocketChannel.open();
		serverChannel.configureBlocking(false);

		// bind server socket channel to port
		serverChannel.socket().bind(listenAddress);
		serverChannel.register(this.selector, SelectionKey.OP_ACCEPT);

		System.out.println("Server started on port >> " + PORT);

        return this.selector;
	}

	// accept client connection
	public SelectionKey accept(SelectionKey key) throws IOException {
		ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
		SocketChannel channel = serverChannel.accept();
		channel.configureBlocking(false);
		Socket socket = channel.socket();
		SocketAddress remoteAddr = socket.getRemoteSocketAddress();
		System.out.println("Connected to: " + remoteAddr);

		/*
		 * Register channel with selector for further IO (record it for read/write
		 * operations, here we have used read operation)
		 */
		SelectionKey selectionKey = channel.register(this.selector, SelectionKey.OP_READ);
		int id = getAndInc();
		selectionKey.attach(id);
		clientKeys.add(selectionKey);
		// write 
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(id);
			oos.close();
			reply(bos.toByteArray(), selectionKey);
		} catch (Exception e) {
			System.out.println("write id failed");
		}
		return selectionKey;
	}

	public List<SelectionKey> getClientKeys() {
		return clientKeys;
	}

	// read from the socket channel
	public int[] read(SelectionKey key) throws IOException {
		SocketChannel channel = (SocketChannel) key.channel();
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		int numRead = -1;
		numRead = channel.read(buffer);

		if (numRead == -1) {
			Socket socket = channel.socket();
			SocketAddress remoteAddr = socket.getRemoteSocketAddress();
			System.out.println("Connection closed by client: " + remoteAddr);
			channel.close();
			key.cancel();
			return null;
		}

		byte[] data = new byte[numRead];
		System.arraycopy(buffer.array(), 0, data, 0, numRead);
		//System.out.println("Got: " + new String(data));

        int[] keyEvents = parseData(data);
        return keyEvents;
	}

    private int[] parseData(byte[] data) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
		int[] keyCodes = new int[data.length / Integer.BYTES];
		for (int i = 0; i < keyCodes.length; i++) {
			keyCodes[i] = byteBuffer.getInt();
			//System.out.println("keyCode " + keyCodes[i]);
		}
        return keyCodes;
    }

	// public void write(SelectionKey key) throws IOException {
	// 	SocketChannel cilentChannel = (SocketChannel) key.channel();
	// 	String info = cnt + " from server";
	// 	cnt++;
	// 	ByteBuffer buffer = ByteBuffer.wrap(info.getBytes());
	// 	cilentChannel.write(buffer);
	// 	key.interestOps(SelectionKey.OP_READ);
	// }

	public void reply(byte[] message, SelectionKey targetKey) {
		// System.out.println("in reply");
		try {
			for (SelectionKey selectionKey: selector.keys()) {
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
}