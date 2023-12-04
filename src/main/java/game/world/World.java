package game.world;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class World implements Serializable {

    private Tile[][] tiles;
    private int width;
    private int height;
    private List<Creature> creatures;

    public World(Tile[][] tiles) {
        this.tiles = tiles;
        this.width = tiles.length;
        this.height = tiles[0].length;
        this.creatures = new ArrayList<>();
    }

    public synchronized Tile tile(int x, int y) {
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
        } while (!tile(x, y).isGround() || this.creature(x, y) != null);

        creature.setX(x);
        creature.setY(y);

        this.creatures.add(creature);
    }

    public void addAtLocation(Creature creature, int x, int y) {
        if (!tile(x, y).isGround() || this.creature(x, y) != null) {
            return;
        }

        creature.setX(x);
        creature.setY(y);

        this.creatures.add(creature);
    }

    public Creature creature(int x, int y) {
        for (Creature c : this.creatures) {
            if (c.x() == x && c.y() == y) {
                return c;
            }
        }
        return null;
    }

    public List<Creature> getCreatures() {
        return this.creatures;
    }

    public void remove(Creature target) {
        if (target.getAI() instanceof BeanAI) {
            ((BeanAI) target.getAI()).decreaseBeans();
        }        
        this.creatures.remove(target);
    }

    public void update() {
        ArrayList<Creature> toUpdate = new ArrayList<>(this.creatures);

        for (Creature creature : toUpdate) {
            if (creature.getAI() instanceof SnakeAI || creature.getAI() instanceof BossAI) {
                continue;
            }
            creature.update();
        }
    }

    private int manPosX;
    private int manPosY;
    public void setManPos(int x, int y) {
        manPosX = x;
        manPosY = y;
    }
    public int getManPosX() {
        return manPosX;
    }
    public int getManPosY() {
        return manPosY;
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
