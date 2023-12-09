package game;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import javax.swing.JFrame;

import game.asciiPanel.AsciiFont;
import game.asciiPanel.AsciiPanel;
import game.screen.Screen;

public class Client extends JFrame implements KeyListener {

    private AsciiPanel terminal;
    public Screen screen;

    public Selector selector;
    public SocketChannel client;

    public int id;
    public long cnt;

    public Client() {
        super();
        terminal = new AsciiPanel(40, 40, AsciiFont.gold);
        add(terminal);
        pack();
        addKeyListener(this);
        id = 0;
        cnt = 0;
    }

    @Override 
    public void repaint() {
        terminal.clear();
        screen.displayOutput(terminal, id);
        super.repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //System.out.println("key pressed");
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(e.getKeyCode());
        buffer.flip();
        try {
            client.write(buffer);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        buffer.clear();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    public static void main(String[] args) {
        Client app = new Client();
        try {
            app.selector = Selector.open();
            InetSocketAddress hostAddress = new InetSocketAddress("localhost", 9093);
            app.client = SocketChannel.open(hostAddress);
            app.client.configureBlocking(false);
            app.client.register(app.selector, SelectionKey.OP_READ);
            app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            app.setVisible(true);

            System.out.println("Client... started");     
            
            // Thread clientThread = new Thread(new Runnable() {
            //     @Override
            //     public void run() {
            //         try {
            //             while (true) {
            //                 int count = selector.select();
            //                 System.out.println(count);
            //                 if (count > 0) {
            //                     Iterator<SelectionKey> iterator =  selector.selectedKeys().iterator();
            //                     while (iterator.hasNext()) {
            //                         SelectionKey selectionKey = iterator.next();
            //                         if (selectionKey.isReadable()) {
            //                             SocketChannel channel = (SocketChannel) selectionKey.channel();
            //                             ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            //                             channel.read(byteBuffer);
            //                             System.out.println(new String(byteBuffer.array()));
            //                         }
            //                         iterator.remove();
            //                     }
            //                 }
            //             }   
            //         } catch (Exception e) {
            //             e.printStackTrace();
            //         }             
            //     }
            // });
            // clientThread.setDaemon(false);
            // clientThread.start();
            
            try {
                while (true) {
                    int count = app.selector.select();
                    //System.out.println(count);
                    if (count > 0) {
                        Iterator<SelectionKey> iterator =  app.selector.selectedKeys().iterator();
                        while (iterator.hasNext()) {
                            SelectionKey selectionKey = iterator.next();
                            if (selectionKey.isReadable()) {
                                SocketChannel channel = (SocketChannel) selectionKey.channel();
                                ByteBuffer byteBuffer = ByteBuffer.allocate(100000);
                                int length = channel.read(byteBuffer);
                                //System.out.println(length);
                                byte[] bufferArray = byteBuffer.array();
                                ByteArrayInputStream bis = new ByteArrayInputStream(bufferArray);
                                ObjectInputStream ois = new ObjectInputStream(bis);
                                if (app.cnt == 0) {
                                    app.id = (int) ois.readObject();
                                    app.cnt++;
                                    //System.out.println(length + " id= " + app.id);
                                } else {
                                    try {
                                        app.screen = (Screen) ois.readObject();
                                        app.repaint();
                                    } catch (Exception e) {
                                        System.out.println("decode screen failed");
                                    } finally {
                                        ois.close();
                                    }                                    
                                }
                            }
                            iterator.remove();
                        }
                    }
                }   
            } catch (Exception e) {
                e.printStackTrace();
            }             
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
