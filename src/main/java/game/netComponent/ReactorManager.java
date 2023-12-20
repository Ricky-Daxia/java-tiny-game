package game.netComponent;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;

import game.ServerMain;

public class ReactorManager {
    private static final int SERVER_PORT = 9093;

    private Reactor reactor;

    public Reactor getReactor() {
        return reactor;
    }

    public void startReactor(ServerMain serverMain) throws Exception {

        ServerSocketChannel server = ServerSocketChannel.open();
        server.socket().bind(new InetSocketAddress(SERVER_PORT));
        server.configureBlocking(false);

        reactor = new Reactor(serverMain);
        reactor.registerChannel(SelectionKey.OP_ACCEPT, server);

        reactor.registerEventHandler(
                SelectionKey.OP_ACCEPT, new AcceptEventHandler(
                reactor.getDemultiplexer(), reactor));

        reactor.registerEventHandler(
                SelectionKey.OP_READ, new ReadEventHandler(
                reactor.getDemultiplexer(), reactor));

        reactor.registerEventHandler(
                SelectionKey.OP_WRITE, new WriteEventHandler());

        if (serverMain != null) {
                reactor.run(); // Run the dispatcher loop
        }

    }

    // public static void main(String[] args) {
    //     System.out.println("Server Started at port : " + SERVER_PORT);
    //     try {
    //         new ReactorManager().startReactor(SERVER_PORT);
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    // }

}
