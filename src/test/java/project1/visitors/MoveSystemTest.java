package project1.visitors;

import org.junit.Test;
import project1.Direction;
import project1.Simulation;
import project1.actors.Animal;
import project1.tick.SpawnSystem;
import project1.world.World;

import java.util.Arrays;

import static org.junit.Assert.*;

public class MoveSystemTest {

    @Test
    public void tick() {
        Simulation simulation = new Simulation();
        MoveSystem moveSystem = new MoveSystem();
        simulation.addTickListeners(moveSystem);

        World world = simulation.getWorld();
        int[] genes = new int[32];
        Arrays.fill(genes, 0);

        Animal animal = new Animal(10, 10, 5, genes);
        world.addActor(animal);

        assertEquals(10, animal.getX());
        assertEquals(10, animal.getY());

        simulation.tick();

        assertEquals(10, animal.getX());
        assertEquals(11, animal.getY());

        simulation.tick();

        assertEquals(10, animal.getX());
        assertEquals(12, animal.getY());
    }
}