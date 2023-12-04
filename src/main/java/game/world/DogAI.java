package game.world;

public class DogAI extends CreatureAI {

    private int[] dx = {-1, 0, 1, 0};
    private int[] dy = {0, 1, 0, -1};

    private long timeStamp;

    public DogAI(Creature creature) {
        super(creature);
        timeStamp = System.currentTimeMillis();
    }

    @Override 
    public void onUpdate() {
        if (System.currentTimeMillis() - timeStamp < 1000) {
            return;
        }
        boolean keep = false;
        int x = creature.x(), y = creature.y();
        World world = creature.getWorld();
        for (int i = 0; i < 4; i++) {
            int newX = x + dx[i], newY = y + dy[i];
            Creature target = world.creature(newX, newY);
            if (target != null && target.getAI() instanceof SnakeAI) {
                keep = true;
            }
        }
        if (!keep) {
            creature.modifyHP(-creature.maxHP());
            creature.getWorld().remove(creature);
        }
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
        throw new UnsupportedOperationException("Unimplemented method 'attack'");
    }
}
