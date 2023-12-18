package game.world;

public class BeanAI extends CreatureAI{


    private CreatureFactory factory;
    private int spreadcount = 0;

    public static int spores = 2;
    public static double spreadchance = 0.01;

    public BeanAI(Creature creature, CreatureFactory factory) {
        super(creature);
        this.factory = factory;
    }

    public void onUpdate() {
        if (this.spreadcount < BeanAI.spores && Math.random() < BeanAI.spreadchance) {
            spread();
        }
    }

    public void decreaseBeans() {
        int leftBeans = this.factory.decreaseBeans();
        if (leftBeans == 0) {
            for (int i = 0; i < 3; i++) {
                spread();
            }
        }
    }

    private void spread() {
        int newx = creature.x() + (int) (Math.random() * 11) - 5;
        int newy = creature.y() + (int) (Math.random() * 11) - 5;

        if (!creature.canEnter(newx, newy)) {
            return;
        }

        if (this.factory.getTotalBeans() < CreatureFactory.maxBeans && creature.getWorld().occupied[newx][newy].compareAndSet(false, true)) {
            Creature child = this.factory.newBean();
            child.setX(newx);
            child.setY(newy);
            // this.factory.newBean();
            spreadcount++; 
        }

    }

    @Override
    public void onEnter(int x, int y, Tile tile) {
        throw new UnsupportedOperationException("Unimplemented method 'onEnter'");
    }

    // @Override
    // public void onNotify(String message) {
    //     throw new UnsupportedOperationException("Unimplemented method 'onNotify'");
    // }

    @Override
    public void attack(Creature another) {
        throw new UnsupportedOperationException("Unimplemented method 'attack'");
    }

}
