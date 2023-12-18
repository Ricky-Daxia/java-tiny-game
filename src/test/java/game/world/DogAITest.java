package game.world;

import org.junit.Test;

import game.asciiPanel.AsciiPanel;

public class DogAITest {
    @Test 
    public void testDogAI() {
        DogAI dogAI = new DogAI(new Creature(null, (char) 243, AsciiPanel.brightGreen, 1, 1, 1, 0));
        try {
            dogAI.onUpdate();
        } catch (NullPointerException e) {

        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void test1() {
        new DogAI(new Creature(null, (char) 243, AsciiPanel.brightGreen, 1, 1, 1, 0)).onEnter(0, 0, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void test3() {
        new DogAI(new Creature(null, (char) 243, AsciiPanel.brightGreen, 1, 1, 1, 0)).attack(null);
    }
}
