package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;

import game.asciiPanel.AsciiFont;
import game.asciiPanel.AsciiPanel;
import game.screen.Screen;
import game.screen.SnakeGameScreen;
import game.screen.StartScreen;

public class ApplicationMain extends JFrame implements KeyListener {

    private AsciiPanel terminal;
    public Screen screen;
    
    public File record;
    public long recordCnt;

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
        boolean isNew = false;
        if (!(screen instanceof SnakeGameScreen)) {
            isNew = true;
        }
        screen = screen.respondToUserInput(e, 0);
        if (isNew && screen instanceof SnakeGameScreen) {
            ((SnakeGameScreen) screen).registerSnake(0);
        }
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

        // check whether dir exists
        String dirname = "record";
        app.record = new File(dirname);
        if (app.record.isDirectory()) {
            // is dir means there are records
            // replay and delete folder
            app.recordCnt = 0;
            for (long i = 0; ; i++) {
                String scene = app.record + "\\" + String.valueOf(i) + ".txt";
                try (InputStream in = new FileInputStream(scene);
                    BufferedInputStream bis = new BufferedInputStream(in)) {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[30000];
                    int len = bis.read(buffer);
                    if (len != -1) {
                        bos.write(buffer, 0, len);
                        byte[] data = bos.toByteArray();
                        try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
                            ObjectInputStream ois = new ObjectInputStream(bais)) {
                            app.screen = (Screen) ois.readObject();
                            app.repaint();
                            app.recordCnt++;
                        } catch (IOException | ClassNotFoundException e) {
                            System.out.println("decode screen failed");
                        } 
                        Thread.sleep(100);
                    }
                } catch (IOException e) {
                    System.out.println("IOException when reading record " + i);
                    break;
                } catch (InterruptedException e) {
                    System.out.println("InterruptedException when replaying");
                    continue;
                }
            }
            // delete folder and all files
            // File[] files = app.record.listFiles();
            // if (files != null) {
            //     for (File f: files) {
            //         f.delete();
            //     }
            // }
            // app.record.delete();
        } else {
            app.record.mkdir();
            app.recordCnt = 0;            
        }
        if (app.screen instanceof SnakeGameScreen) {
            ((SnakeGameScreen) app.screen).synchronize();
        }
        System.out.println("----------ready to go----------");
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (app.screen instanceof SnakeGameScreen) {
                    ((SnakeGameScreen) app.screen).update();
                }
                app.repaint();
                try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                    oos.writeObject(app.screen);
                    oos.close();
                    byte[] data = baos.toByteArray();
                    String scene = app.record + "\\" + String.valueOf(app.recordCnt) + ".txt";
                    try (FileOutputStream out = new FileOutputStream(scene);
                        BufferedOutputStream bos = new BufferedOutputStream(out)) {
                        bos.write(data);
                        app.recordCnt++;
                    } catch (IOException e) {
                        System.out.println("IOException when encoding record");
                    }
                } catch (IOException e) {
                    System.out.println("IOException when writing record");
                }
            }
        }, 100, 100, TimeUnit.MILLISECONDS);
    }

}
