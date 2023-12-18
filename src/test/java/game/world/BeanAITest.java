package game.world;

import org.junit.Test;

public class BeanAITest {
    @Test(expected = UnsupportedOperationException.class)
    public void test1() {
        new BeanAI(new Creature(null, (char) 0, null, 0, 0, 0, 0), null).onEnter(0, 0, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void test2() {
        new BeanAI(new Creature(null, (char) 0, null, 0, 0, 0, 0), null).attack(null);
    }

    @Test 
    public void testBeanAI() {
        new BeanAI(new Creature(null, (char) 0, null, 0, 0, 0, 0), new CreatureFactory(null)).decreaseBeans();
    }
}
