package game.world;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CreatureTest {
    @Test 
    public void testCreature() {
        Creature creature = new Creature(null, (char) 0, null, 0, 0, 0, 0);
        assertEquals(creature.attackValue(), 0);
        assertEquals(creature.defenseValue(), 0);
        assertEquals(creature.visionRadius(), 0);
        creature.setAttackedGlyph((char) 1);
        creature.changeAttacked();
    }
    
    
}
