package game.netComponent;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class AcceptEventHandler implements EventHandler{
    private Selector demultiplexer;
    private Reactor reactor;

    public AcceptEventHandler(Selector demultiplexer, Reactor reactor) {
        this.demultiplexer = demultiplexer;
        this.reactor = reactor;
    }

    public void handleEvent(SelectionKey handle) throws Exception {
        System.out.println("===== Accept Event Handler =====");
        ServerSocketChannel serverSocketChannel =
                (ServerSocketChannel) handle.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        if (socketChannel != null) {
            socketChannel.configureBlocking(false);
            SelectionKey selectionKey = socketChannel.register(
                    demultiplexer, SelectionKey.OP_READ);
            int id = reactor.getAndInc();
            selectionKey.attach(id);
            reactor.addClientKey(selectionKey);
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos);
                oos.writeObject(id);
                oos.close();
                reactor.reply(bos.toByteArray(), selectionKey);
            } catch (Exception e) {
                System.out.println("write id failed");
            }
        }

    }

}