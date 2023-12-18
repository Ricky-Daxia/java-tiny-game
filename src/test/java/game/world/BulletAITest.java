package game.world;

import org.junit.Test;

public class BulletAITest {

    @Test(expected = UnsupportedOperationException.class)
    public void testBulletAI() {
        new BulletAI(new Creature(null, (char) 0, null, 0, 0, 0, 0), 0, 0).onEnter(0, 0, null);
    }

    @Test(expected = NullPointerException.class)
    public void testBulletAttack() {
        new BulletAI(new Creature(null, (char) 0, null, 0, 0, 0, 0), 0, 0).attack(new Creature(null, (char) 0, null, 0, 0, 0, 0));
    }
}
