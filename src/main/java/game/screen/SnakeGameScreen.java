package game.screen;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

import game.asciiPanel.AsciiPanel;
import game.world.BossAI;
import game.world.Creature;
import game.world.CreatureFactory;
import game.world.SnakeAI;
import game.world.World;
import game.world.WorldBuilder;

public class SnakeGameScreen implements Screen, Serializable {

    private static final int WORLD_WIDTH = 40;
    private static final int WORLD_HEIGHT = 40;

    private static final int SCREEN_WIDTH = 40;
    private static final int SCREEN_HEIGHT = 40;

    private static final int LEFT_KEY = 37;
    private static final int RIGHT_KEY = 39;
    private static final int UP_KEY = 38;
    private static final int DOWN_KEY = 40;

    private static final int DELAY_TIME = 200;

    private int direction = 1;

    private HashMap<Integer, Creature> snakes;
    private Creature boss;
    private CreatureFactory factory;

    private List<String> messages;
    private List<String> oldMessages;

    private World world;

    public SnakeGameScreen() {
        super();

        createWorld();
        this.messages = new ArrayList<String>();
        this.oldMessages = new ArrayList<String>();

        CreatureFactory creatureFactory = new CreatureFactory(this.world);
        this.factory = creatureFactory;
        createCreatures(creatureFactory);
    }

    private void createCreatures(CreatureFactory creatureFactory) {
        // this.snake = creatureFactory.newSnake(this.messages);
        this.snakes = new HashMap<>();
        // snakes.put(0, creatureFactory.newSnake(this.messages)); 

        for (int i = 0; i < 5; i++) {
            creatureFactory.newBean();
        }

        this.boss = creatureFactory.newBoss(this.messages, creatureFactory);
    }

    public void registerSnake(int id) {
        this.snakes.put(id, this.factory.newSnake(this.messages));
    }

    private void createWorld() {
        world = new WorldBuilder(WORLD_WIDTH, WORLD_HEIGHT).makeRectangle().build();
    }

    @Override
    public void displayOutput(AsciiPanel terminal, int id) {

        // Terrain and creatures
        displayTiles(terminal, getScrollX(), getScrollY());
        // Player
        //((GlyphDelegate) snake.getAI()).printGlyph(terminal, getScrollX(), getScrollY());
        // Stats
        if (!snakes.containsKey(id)) {
            //System.out.println("snake with id " + id + " not exist");
            return;
        }        
        String stats = String.format("%3d/%3d hp", snakes.get(id).hp(), snakes.get(id).maxHP()); // use id
        terminal.write(stats, 1, 0);
        // Messages
        displayMessages(terminal, this.messages);
    }

    private void displayTiles(AsciiPanel terminal, int left, int top) {
        // Show terrain
        for (int x = 0; x < SCREEN_WIDTH; x++) {
            for (int y = 0; y < SCREEN_HEIGHT; y++) {
                int wx = x + left;
                int wy = y + top;

                // if (snake.canSee(wx, wy)) {
                //     terminal.write(world.glyph(wx, wy), x, y, world.color(wx, wy));
                // } else {
                //     terminal.write(world.glyph(wx, wy), x, y, Color.DARK_GRAY);
                // }
                terminal.write(world.glyph(wx, wy), x, y, Color.DARK_GRAY);
            }
        }
        // Show creatures
        // world.lock();
        for (Creature creature : world.getCreatures()) {
            if (creature.x() >= left && creature.x() < left + SCREEN_WIDTH && creature.y() >= top
                    && creature.y() < top + SCREEN_HEIGHT) {
                // if (snake.canSee(creature.x(), creature.y())) {
                // if (creature.getAI() instanceof GlyphDelegate) {
                //     ((GlyphDelegate) creature.getAI()).printGlyph(terminal, left, top);
                // } else {
                    terminal.write(creature.glyph(), creature.x() - left, creature.y() - top, creature.color());
                // }
                // }
            }
        }
        // world.unlock();
        // Creatures can choose their next action now
        // world.update();
        // update();
    }

    public void update() {
        //System.out.println("snakes.size() = " + snakes.size());
        synchronized (snakes) {
            int[] manPos = new int[snakes.size()];
            int pos = 0;
            for (Creature creature: snakes.values()) {
                manPos[pos] = (creature.x() << 10) | creature.y();
                pos++;
            }
            world.setManPos(manPos);      
        }
        world.update();
    }

    private void displayMessages(AsciiPanel terminal, List<String> messages) {
        int top = SCREEN_HEIGHT - messages.size();
        for (int i = 0; i < messages.size(); i++) {
            terminal.write(messages.get(i), 1, top + i + 1);
        }
        this.oldMessages.addAll(messages);
        messages.clear();
    }

    public int getScrollX() {
        return 0; //Math.max(0, Math.min(snake.x() - SCREEN_WIDTH / 2, world.width() - SCREEN_WIDTH));
    }

    public int getScrollY() {
        return 0; //Math.max(0, Math.min(snake.y() - SCREEN_HEIGHT / 2, world.height() - SCREEN_HEIGHT));
    }

    @Override
    public Screen respondToUserInput(KeyEvent key, int id) {

        // switch (key.getKeyCode()) {
        // case LEFT_KEY:
        // direction = snake.MOVE_LEFT;
        // break;
        // case RIGHT_KEY:
        // direction = snake.MOVE_RIGHT;
        // break;
        // case UP_KEY:
        // direction = snake.MOVE_UP;
        // break;
        // case DOWN_KEY:
        // direction = snake.MOVE_DOWN;
        // break;
        // }

        if (this.snakes.get(id).hp() < 0) {
            this.factory.getExecutor().shutdown();
            return new LoseScreen();
        } else if (this.boss.hp() < 1) {
            this.factory.getExecutor().shutdown();
            return new WinScreen();
        }

        switch (key.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                snakes.get(id).moveBy(-1, 0);
                break;
            case KeyEvent.VK_RIGHT:
                snakes.get(id).moveBy(1, 0);
                break;
            case KeyEvent.VK_UP:
                snakes.get(id).moveBy(0, -1);
                break;
            case KeyEvent.VK_DOWN:
                snakes.get(id).moveBy(0, 1);
                break;
            case KeyEvent.VK_A:
                factory.newBullet(snakes.get(id).x(), snakes.get(id).y(), -1, 0);
                break;
            case KeyEvent.VK_D:
                factory.newBullet(snakes.get(id).x(), snakes.get(id).y(), 1, 0);
                break;
            case KeyEvent.VK_W:
                factory.newBullet(snakes.get(id).x(), snakes.get(id).y(), 0, -1);
                break;
            case KeyEvent.VK_S:
                factory.newBullet(snakes.get(id).x(), snakes.get(id).y(), 0, 1);
                break;
        }
        world.update();

        return this;
    }

    @Override
    public Screen respondToUserInput(int e, int id) {

        if (this.snakes.get(id).hp() < 0) {
            this.factory.getExecutor().shutdown();
            this.snakes.remove(id);
            return new LoseScreen();
        } else if (this.boss.hp() < 1) {
            this.factory.getExecutor().shutdown();
            return new WinScreen();
        }

        switch (e) {
            case KeyEvent.VK_LEFT:
                snakes.get(id).moveBy(-1, 0);
                break;
            case KeyEvent.VK_RIGHT:
                snakes.get(id).moveBy(1, 0);
                break;
            case KeyEvent.VK_UP:
                snakes.get(id).moveBy(0, -1);
                break;
            case KeyEvent.VK_DOWN:
                snakes.get(id).moveBy(0, 1);
                break;
            case KeyEvent.VK_A:
                factory.newBullet(snakes.get(id).x(), snakes.get(id).y(), -1, 0);
                break;
            case KeyEvent.VK_D:
                factory.newBullet(snakes.get(id).x(), snakes.get(id).y(), 1, 0);
                break;
            case KeyEvent.VK_W:
                factory.newBullet(snakes.get(id).x(), snakes.get(id).y(), 0, -1);
                break;
            case KeyEvent.VK_S:
                factory.newBullet(snakes.get(id).x(), snakes.get(id).y(), 0, 1);
                break;
        }
        world.update();
        return this;
    }

    // sync with record
    public void synchronize() {
        if (world.occupied == null) {
            world.occupied = new AtomicBoolean[world.width()][world.height()];
            for (int i = 0; i < world.width(); i++) {
                for (int j = 0; j < world.height(); j++) {
                    world.occupied[i][j] = new AtomicBoolean();
                }
            }
            for (Creature creature: world.getCreatures()) {
                int x = creature.x(), y = creature.y();
                world.occupied[x][y].compareAndSet(false, true);
            }
        }
        if (this.factory.emptyExecutor()) {
            this.factory.setExecutor(new ScheduledThreadPoolExecutor(20));
            for (Creature snake: this.snakes.values()) {
                this.factory.setSnakeTask((SnakeAI) snake.getAI());
            }            
            this.factory.setBossTask((BossAI) this.boss.getAI());
        }
    }

}
