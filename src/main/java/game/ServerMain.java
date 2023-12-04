package game;

import javax.swing.JFrame;

import java.util.List;
import java.awt.event.KeyEvent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import game.asciiPanel.AsciiFont;
import game.asciiPanel.AsciiPanel;
import game.netComponent.NIOServer;
import game.screen.Screen;
import game.screen.StartScreen;

public class ServerMain extends JFrame {
    
    private AsciiPanel terminal;
    public Screen screen;

    public ServerMain() {
        super();
        terminal = new AsciiPanel(30, 20, AsciiFont.gold);
        add(terminal);
        pack();
        screen = new StartScreen();
        repaint();
    }

    @Override
    public void repaint() {
        terminal.clear();
        screen.displayOutput(terminal);
        super.repaint();
    }

    public void keyPressed(KeyEvent e) {
        screen = screen.respondToUserInput(e);
        repaint();
    }

    public void keyPressed(int e) {
        screen = screen.respondToUserInput(e);
        repaint(); 
    }

    public NIOServer server;
    public Selector selector;
    public long cnt = 0;

    public static void main(String[] args) {
        ServerMain serverApp = new ServerMain();
        serverApp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //serverApp.setVisible(true);
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);

        try {
            serverApp.server = new NIOServer("localhost", 9093);
            serverApp.selector = serverApp.server.startServer();

            executor.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    //serverApp.repaint();
                    for (SelectionKey key: serverApp.server.getClientKeys()) {
                        try {
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            ObjectOutputStream oos = new ObjectOutputStream(bos);
                            oos.writeObject(serverApp.screen);
                            oos.close();
                            serverApp.server.reply(bos.toByteArray(), key);
                        } catch (Exception e) {
                            System.out.println("encode screen failed");
                        } finally {
    
                        }
                    }
                }
            }, 100, 100, TimeUnit.MILLISECONDS);

            while (true) {
                // wait for events
                int readyCount = serverApp.selector.select();
                if (readyCount == 0) {
                    continue;
                }

                // process selected keys...
                Set<SelectionKey> readyKeys = serverApp.selector.selectedKeys();
                Iterator iterator = readyKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = (SelectionKey) iterator.next();

                    // Remove key from set so we don't process it twice
                    iterator.remove();

                    if (!key.isValid()) {
                        continue;
                    }

                    if (key.isAcceptable()) { // Accept client connections
                        serverApp.server.accept(key);
                    } else if (key.isReadable()) { // Read from client
                        int[] keyEvents = serverApp.server.read(key);
                        if (keyEvents == null) {
                            continue;
                        }
                        for (int keyEvent: keyEvents) {
                            serverApp.keyPressed(keyEvent);
                        }
                    } else if (key.isWritable()) {
                        // write data to client...
                        // serverApp.server.write(key);
                    }
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}
