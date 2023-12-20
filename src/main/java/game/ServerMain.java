package game;

import javax.swing.JFrame;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import game.asciiPanel.AsciiFont;
import game.asciiPanel.AsciiPanel;
import game.netComponent.ReactorManager;
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

    private void removeSnakes() {
        // remove snakes that exits the game
        ArrayList<Integer> toRemove = new ArrayList<>();
        for (int id: states.keySet()) {
            boolean st = false;
            for (SelectionKey key: reactor.getReactor().getClientKeys()) {
                if (key.isValid() == false) {
                    continue;
                }
                Object attr = key.attachment();
                if (attr == null) {
                    System.out.println("should not happen");
                    continue;
                } else if (id == (int)attr) {
                    st = true;
                    break;
                }
            }
            if (st == false) {
                toRemove.add(id);
            }
        }
        for (int id: toRemove) {
            snakeGameScreen.removeSnake(id);
        }        
    }

    public void reply() {
        removeSnakes();
        for (SelectionKey key: reactor.getReactor().getClientKeys()) {
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
                reactor.getReactor().reply(bos.toByteArray(), key);
            } catch (Exception e) {
                System.out.println("encode screen failed");
            }
        }            
    } 

    @Override
    public void repaint() {
        terminal.clear();
        snakeGameScreen.displayOutput(terminal, 0);
        snakeGameScreen.update();
        super.repaint();
        if (reactor != null) {
            reply();
        }
    }

    // public void keyPressed(KeyEvent e) {
    //     screen = screen.respondToUserInput(e, 0);
    //     repaint();
    // }

    public void transit(int e, int id) {
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
    }

    public void keyPressed(int e, int id) {
        transit(e, id);
        repaint(); 
    }

    public ReactorManager reactor;

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
            serverApp.startService();
        } catch (Exception e) {
            e.printStackTrace();
        }            
    }

    public void startService() throws Exception {
        reactor = new ReactorManager();
        reactor.startReactor(this);
    }
}
