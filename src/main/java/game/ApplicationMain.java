package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;

import game.asciiPanel.AsciiFont;
import game.asciiPanel.AsciiPanel;
import game.screen.Screen;
import game.screen.StartScreen;

public class ApplicationMain extends JFrame implements KeyListener {

    private AsciiPanel terminal;
    private Screen screen;
    

    public ApplicationMain() {
        super();
        terminal = new AsciiPanel(40, 40, AsciiFont.gold);
        add(terminal);
        pack();
        screen = new StartScreen();

        addKeyListener(this);
        repaint();
    }

    @Override
    public void repaint() {
        terminal.clear();
        screen.displayOutput(terminal, 0);
        super.repaint();
    }

    public void keyPressed(KeyEvent e) {
        screen = screen.respondToUserInput(e, 0);
        repaint();
    }

    public void keyReleased(KeyEvent e) {

    }

    public void keyTyped(KeyEvent e) {

    }

    public static void main(String[] args) {
        ApplicationMain app = new ApplicationMain();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setVisible(true);
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                app.repaint();
                try {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(bos);
                    oos.writeObject(app.screen);
                    oos.close();
                    byte[] res = bos.toByteArray();
                    //System.out.println(res.length);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 100, 100, TimeUnit.MILLISECONDS);
    }

}
