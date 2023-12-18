package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

    // public AtomicBoolean recordMode;
    // private AtomicBoolean playingRecord;
    // public AtomicBoolean playFinished;
    public boolean recordMode;
    private boolean playingRecord;
    public boolean playFinished;

    private void playRecord() {
        // check whether dir exists
        // is dir means there are records
        // replay and delete folder
        recordCnt = 0;
        for (long i = 0; ; i++) {
            //System.out.println("i= " + i);
            String scene = record + "\\" + String.valueOf(i) + ".txt";
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
                        screen = (Screen) ois.readObject();
                        repaint();
                        recordCnt++;
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
        if (screen instanceof SnakeGameScreen) {
            ((SnakeGameScreen) screen).synchronize();
        }
        System.out.println("----------ready to go----------");
    }

    public ApplicationMain() {
        super();
        terminal = new AsciiPanel(40, 40, AsciiFont.gold);
        add(terminal);
        pack();
        screen = new StartScreen();
        recordMode = false;
        playFinished = false;
        playingRecord = false;
        addKeyListener(this);
        repaint();
    }

    @Override
    public void repaint() {
        //System.out.println("rep");
        terminal.clear();
        screen.displayOutput(terminal, 0);
        super.repaint();
    }

    private void initRecord() {
        String dirname = "record";
        record = new File(dirname);
        if (!record.isDirectory()) {
            record.mkdir();
            recordCnt = 0;      
        }        
    }

    public void keyPressed(KeyEvent e) {
        if (playingRecord == true) {
            return;
        }
        if (e.getKeyCode() == KeyEvent.VK_P && screen instanceof StartScreen) {
            initRecord();
            screen = new SnakeGameScreen();
            ((SnakeGameScreen) screen).registerSnake(0);
            recordMode = true;
            playFinished = true;
        } else if (e.getKeyCode() == KeyEvent.VK_R && screen instanceof StartScreen) {
            initRecord();
            playingRecord = true;
            new Thread(new Runnable() {
                public void run() {
                    playRecord();
                    playingRecord = false;
                    recordMode = true;
                    playFinished = true;
                }
            }).start();
            return;
        } else {
            boolean isNew = false;
            if (!(screen instanceof SnakeGameScreen)) {
                isNew = true;
            }
            screen = screen.respondToUserInput(e, 0);
            if (isNew && screen instanceof SnakeGameScreen) {
                ((SnakeGameScreen) screen).registerSnake(0);
                playFinished = true;
            }    
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

        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(10);
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (app.playFinished == false) {
                    return;
                }
                if (app.screen instanceof SnakeGameScreen) {
                    ((SnakeGameScreen) app.screen).update();
                }
                app.repaint();
                if (app.recordMode == true) {
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
            }
        }, 100, 100, TimeUnit.MILLISECONDS);
    }

}
