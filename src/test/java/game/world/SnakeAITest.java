package game.world;

import org.junit.Test;

public class SnakeAITest {
    @Test
    public void test1() {
        new SnakeAI(new Creature(null, (char) 0, null, 0, 0, 0, 0)).onEnter(0, 0, null);
    }

    @Test(expected = NullPointerException.class)
    public void test2() {
        new SnakeAI(new Creature(null, (char) 0, null, 0, 0, 0, 0)).attack(new Creature(null, (char) 0, null, 0, 0, 0, 0));
    }
}
