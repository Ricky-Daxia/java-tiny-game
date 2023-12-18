package game.world;

import org.junit.Test;

public class BossAITest {
    @Test(expected = NullPointerException.class)
    public void testBossAI() {
        new BossAI(new Creature(null, (char) 0, null, 0, 0, 0, 0), null).onEnter(0, 0, null);
    }

    @Test(expected = NullPointerException.class)
    public void testBossAttack() {
        new BossAI(new Creature(null, (char) 0, null, 0, 0, 0, 0), null).attack(new Creature(null, (char) 0, null, 0, 0, 0, 0));
    }
}
