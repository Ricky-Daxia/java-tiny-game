package game.world;

import java.awt.Color;
import java.io.Serializable;

public class Creature implements Serializable {

    public static final int MOVE_UP = 1;
    public static final int MOVE_DOWN = 2;
    public static final int MOVE_LEFT = 3;
    public static final int MOVE_RIGHT = 4;

    private World world;

    public World getWorld() {
        return world;
    }

    private int x, prevX;

    public void setX(int x) {
        this.prevX = this.x;
        this.x = x;
    }

    public int x() {
        return x;
    }

    public int prevX() {
        return prevX;
    }

    private int y, prevY;

    public void setY(int y) {
        this.prevY = this.y;
        this.y = y;
    }

    public int y() {
        return y;
    }

    public int prevY() {
        return prevY;
    }

    private char glyph;
    private char attackedGlyph;
    private boolean attacked;

    public void setAttackedGlyph(char attackedGlyph) {
        this.attackedGlyph = attackedGlyph;
    }

    public void changeGlyph() {
        char origin = this.glyph;
        this.glyph = this.attackedGlyph;
        this.attackedGlyph = origin;
    }

    public void changeAttacked() {
        attacked = !attacked;
        changeGlyph();
    }

    public boolean getAttacked() {
        return attacked;
    }

    public char glyph() {
        return this.glyph;
    }

    private Color color;

    public Color color() {
        return this.color;
    }

    private CreatureAI ai;

    public void setAI(CreatureAI ai) {
        this.ai = ai;
    }

    public CreatureAI getAI(){
        return this.ai;
    }

    private int maxHP;

    public int maxHP() {
        return this.maxHP;
    }

    private int hp;

    public int hp() {
        return this.hp;
    }

    public synchronized void modifyHP(int amount) {
        this.hp += amount;

        if (this.hp < 1) {
            world.remove(this);
        }
    }

    private int attackValue;

    public int attackValue() {
        return this.attackValue;
    }

    private int defenseValue;

    public int defenseValue() {
        return this.defenseValue;
    }

    private int visionRadius;

    public int visionRadius() {
        return this.visionRadius;
    }

    public boolean canSee(int wx, int wy) {
        return ai.canSee(wx, wy);
    }

    public Tile tile(int wx, int wy) {
        return world.tile(wx, wy);
    }

    public void dig(int wx, int wy) {
        world.dig(wx, wy);
    }

    public void moveBy(int mx, int my) {
        Creature other = world.creature(x + mx, y + my);

        if (other == null) {
            // ai.onEnter(x + mx, y + my, world.tile(x + mx, y + my));
            ai.creature.tryMove(x + mx, y + my);
        } else if (!(other.getAI() instanceof SnakeAI)) {
            ai.attack(other);
        }
    }

    private boolean bulletlike;
    
    public void setBullerlike(boolean val) {
        bulletlike = val;
    }

    public boolean bulletLike() {
        return bulletlike;
    }
    // every creature invokes this method
    public void tryMove(int x, int y) {
        if (!canEnter(x, y)) {
            System.out.println("try to move to impossible pos" + x + " " + y);
            if (ai instanceof SnakeAI) {
                return;
            }
            modifyHP(-maxHP);
            world.remove(this);
        } else {
            Creature other = world.creature(x, y);
            if (other == null || other.bulletLike()) {
                if (!bulletlike) {
                    world.occupied[x()][y()].compareAndSet(true, false);
                    while (world.occupied[x][y].compareAndSet(false, true)) ;
                    setX(x);
                    setY(y);
                } else { // bullet
                    setX(x);
                    setY(y);                    
                }
                //System.out.println("tryMove set");
            } else {
                if (this.bulletlike && other.getAI() instanceof SnakeAI) {
                    System.out.println("bullet cannot hurt man");
                    return;
                }
                ai.attack(other);
                //System.out.println((int)other.glyph);
                //System.out.println("tryMove attack");
            }
        }
    }

    public void update() {
        this.ai.onUpdate();
    }

    public boolean canEnter(int x, int y) {
        // consider whether tile is locked
        return world.tile(x, y).isGround();
    }

    public void notify(String message, Object... params) {
        ai.onNotify(String.format(message, params));
    }

    public Creature(World world, char glyph, Color color, int maxHP, int attack, int defense, int visionRadius) {
        this.world = world;
        this.glyph = glyph;
        this.color = color;
        this.maxHP = maxHP;
        this.hp = 0;
        this.attackValue = attack;
        this.defenseValue = defense;
        this.visionRadius = visionRadius;
        this.bulletlike = false;
        this.attacked = false;
        this.attackedGlyph = 0;
    }

}
