package project1.actors;

import org.junit.Test;
import project1.Direction;

import static org.junit.Assert.*;

public class AnimalTest {

    @Test
    public void loseEnergy() {
        Animal animal = new Animal(0, 0, 10, new int[32]);
        assertFalse(animal.pendingRemoval());

        assertEquals(10, animal.getEnergy());

        animal.loseEnergy(5);

        assertFalse(animal.pendingRemoval());
        assertEquals(5, animal.getEnergy());

        animal.loseEnergy(5);

        assertEquals(0, animal.getEnergy());
        assertTrue(animal.pendingRemoval());
    }

    @Test
    public void gainEnergy() {
        Animal animal = new Animal(0, 0, 10, new int[32]);
        assertEquals(10, animal.getEnergy());
        assertFalse(animal.pendingRemoval());

        animal.gainEnergy(5);

        assertEquals(15, animal.getEnergy());
        assertFalse(animal.pendingRemoval());
    }
}