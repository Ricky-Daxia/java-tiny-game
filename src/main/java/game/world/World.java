package game.world;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class World implements Serializable {

    private Tile[][] tiles;
    private int width;
    private int height;
    private List<Creature> creatures;
    private ReentrantLock creaturesLock;
    public transient AtomicBoolean[][] occupied;

    public World(Tile[][] tiles) {
        this.tiles = tiles;
        this.width = tiles.length;
        this.height = tiles[0].length;
        this.creatures = new ArrayList<>();
        this.creaturesLock = new ReentrantLock();
        occupied = new AtomicBoolean[this.width][this.height];
        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                occupied[i][j] = new AtomicBoolean();
            }
        }
    }

    public void lock() {
        creaturesLock.lock();
    }

    public void unlock() {
        creaturesLock.unlock();
    }

    public Tile tile(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return Tile.WALL;
        } else {
            return tiles[x][y];
        }
    }

    public char glyph(int x, int y) {
        return tiles[x][y].glyph();
    }

    public Color color(int x, int y) {
        return tiles[x][y].color();
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public void dig(int x, int y) {
        if (tile(x, y).isDiggable()) {
            tiles[x][y] = Tile.FLOOR;
        }
    }

    public void addAtEmptyLocation(Creature creature) {
        int x;
        int y;

        do {
            x = (int) (Math.random() * this.width);
            y = (int) (Math.random() * this.height);
        } while (!tile(x, y).isGround() || this.creature(x, y) != null || !this.occupied[x][y].compareAndSet(false, true));
    
        creature.setX(x);
        creature.setY(y);

        this.creaturesLock.lock();
        this.creatures.add(creature);
        this.creaturesLock.unlock();
    }

    // only for bullet to use, bullets can occupy the same place 
    public void addAtLocation(Creature creature, int x, int y) {
        if (!tile(x, y).isGround() || this.creature(x, y) != null) {
            return;
        }

        creature.setX(x);
        creature.setY(y);

        this.creaturesLock.lock();
        this.creatures.add(creature);
        this.creaturesLock.unlock();
    }
    // lock
    public Creature creature(int x, int y) {
        Creature creature = null;
        this.creaturesLock.lock();
        for (Creature c : this.creatures) {
            if (c.x() == x && c.y() == y) {
                creature = c;
            }
        }
        this.creaturesLock.unlock();
        return creature;
    }
    // lock
    public List<Creature> getCreatures() {
        return this.creatures;
    }
    // lock
    public void remove(Creature target) {
        if (target.getAI() instanceof BeanAI) {
            ((BeanAI) target.getAI()).decreaseBeans();
        }  
        creaturesLock.lock();  
        this.creatures.remove(target);
        creaturesLock.unlock();
        this.occupied[target.x()][target.y()].compareAndSet(true, false);
    }
    // lock
    public void update() {
        creaturesLock.lock();
        ArrayList<Creature> toUpdate = new ArrayList<>(this.creatures);

        for (Creature creature : toUpdate) {
            if (creature.getAI() instanceof SnakeAI || creature.getAI() instanceof BossAI) {
                continue;
            }
            creature.update();
        }
        creaturesLock.unlock();
    }

    // private int manPosX;
    // private int manPosY;
    // public void setManPos(int x, int y) {
    //     manPosX = x;
    //     manPosY = y;
    // }
    // public int getManPosX() {
    //     return manPosX;
    // }
    // public int getManPosY() {
    //     return manPosY;
    // }
    private int[] manPos;
    public void setManPos(int[] manPos) {
        this.manPos = manPos;
    }
    public int[] getManPos() {
        return manPos;
    }


    public void setPolluted(int x, int y) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (tiles[i][j] != Tile.FLOOR && tiles[i][j] != Tile.Polluted) {
                    continue;
                } else if (Math.abs(i - x) > 1 || Math.abs(j - y) > 1) {
                    tiles[i][j] = Tile.FLOOR;
                } else {
                    tiles[i][j] = Tile.Polluted;
                }
            }
        }
    }
}
