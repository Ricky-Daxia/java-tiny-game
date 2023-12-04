package game.world;

public class BulletAI extends CreatureAI {

    private int dx;
    private int dy;
    
    public BulletAI(Creature creature, int dx, int dy) {
        super(creature);
        this.dx = dx;
        this.dy = dy;
        creature.setBullerlike(true);
    }

    @Override 
    public void onUpdate() {
        creature.tryMove(creature.x() + dx, creature.y() + dy);
    }

    @Override
    public void onEnter(int x, int y, Tile tile) {
        throw new UnsupportedOperationException("Unimplemented method 'onEnter'");
    }

    @Override
    public void onNotify(String message) {
        throw new UnsupportedOperationException("Unimplemented method 'onNotify'");
    }

    @Override
    public void attack(Creature another) {
        another.modifyHP(-creature.attackValue());
        creature.modifyHP(-another.attackValue());
        if (another.hp() < 1) {
            creature.getWorld().remove(another);
        }
        if (creature.hp() < 1) {
            creature.getWorld().remove(creature);
        }
    }
}
