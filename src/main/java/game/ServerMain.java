package game;

import javax.swing.JFrame;

import java.util.List;
import java.awt.event.KeyEvent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import game.asciiPanel.AsciiFont;
import game.asciiPanel.AsciiPanel;
import game.netComponent.AcceptEventHandler;
import game.netComponent.NIOServer;
import game.netComponent.Reactor;
import game.netComponent.ReadEventHandler;
import game.netComponent.WriteEventHandler;
import game.screen.LoseScreen;
import game.screen.Screen;
import game.screen.SnakeGameScreen;
import game.screen.StartScreen;
import game.screen.WinScreen;

public class ServerMain extends JFrame {
    
    private AsciiPanel terminal;
    public Screen screen;

    public StartScreen startScreen;
    public WinScreen winScreen;
    public LoseScreen loseScreen;
    public SnakeGameScreen snakeGameScreen;

    public HashMap<Integer, Integer> states; 

    public void add(int id) {
        states.put(id, 0);
    }

    public ServerMain() {
        super();
        terminal = new AsciiPanel(40, 40, AsciiFont.gold);
        add(terminal);
        pack();
        startScreen = new StartScreen();
        winScreen = new WinScreen();
        loseScreen = new LoseScreen();
        snakeGameScreen = new SnakeGameScreen();
        states = new HashMap<>();
    }

    @Override
    public void repaint() {
        terminal.clear();
        snakeGameScreen.displayOutput(terminal, 0);
        snakeGameScreen.update();
        super.repaint();
        if (reactor != null) {
            for (SelectionKey key: reactor.getClientKeys()) {
                try {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(bos);
                    if (key.attachment() == null) {
                        System.out.println("empty attachment");
                        continue;
                    }
                    //System.out.println((int)key.attachment());
                    switch (states.get((int)key.attachment())) {
                        case 0:
                            oos.writeObject(startScreen);
                            break;
                        case 1:
                            oos.writeObject(winScreen);
                            break;
                        case 2:
                            oos.writeObject(loseScreen);
                            break;
                        case 3: 
                            oos.writeObject(snakeGameScreen);
                            break;
                        default:
                            break;
                    }
                    //System.out.println((int)key.attachment() + " " + states.get((int)key.attachment()));
                    oos.close();
                    reactor.reply(bos.toByteArray(), key);
                } catch (Exception e) {
                    System.out.println("encode screen failed");
                }
            }            
        }
    }

    // public void keyPressed(KeyEvent e) {
    //     screen = screen.respondToUserInput(e, 0);
    //     repaint();
    // }

    public void keyPressed(int e, int id) {
        Screen screen = null;
        switch (states.get(id)) {
            case 0:
                screen = startScreen.respondToUserInput(e, id);
                break;
            case 1:
                screen = winScreen.respondToUserInput(e, id);
                break;
            case 2:
                screen = loseScreen.respondToUserInput(e, id);
                break;
            case 3:
                screen = snakeGameScreen.respondToUserInput(e, id);
                break;
            default:
                break;
        }
        if (screen instanceof SnakeGameScreen) {
            if (states.get(id) != 3) {
                // new player
                snakeGameScreen.registerSnake(id);
                states.put(id, 3);
            }
        } else if (screen instanceof StartScreen) {
            states.put(id, 0);
        } else if (screen instanceof WinScreen) {
            states.put(id, 1);
        } else {
            states.put(id, 2);
        }
        repaint(); 
    }

    public Reactor reactor;

    public void respondToUserInput(int[] keyEvents, SelectionKey key) {
        System.out.println("key pressed");
        for (int keyEvent: keyEvents) {
            assert(key.attachment() != null);
            keyPressed(keyEvent, (int)key.attachment());
        }
    }

    public static void main(String[] args) {
        ServerMain serverApp = new ServerMain();
        serverApp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        serverApp.setVisible(true);
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);

        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                serverApp.repaint();
            }
        }, 100, 100, TimeUnit.MILLISECONDS);

        
        try {
            ServerSocketChannel server = ServerSocketChannel.open();
            server.socket().bind(new InetSocketAddress(9093));
            server.configureBlocking(false);

            serverApp.reactor = new Reactor(serverApp);
            serverApp.reactor.registerChannel(SelectionKey.OP_ACCEPT, server);

            serverApp.reactor.registerEventHandler(
                    SelectionKey.OP_ACCEPT, new AcceptEventHandler(
                    serverApp.reactor.getDemultiplexer(), serverApp.reactor));

            serverApp.reactor.registerEventHandler(
                    SelectionKey.OP_READ, new ReadEventHandler(
                    serverApp.reactor.getDemultiplexer(), serverApp.reactor));

            serverApp.reactor.registerEventHandler(
                    SelectionKey.OP_WRITE, new WriteEventHandler());

            serverApp.reactor.run(); // Run the dispatcher loop

        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}
