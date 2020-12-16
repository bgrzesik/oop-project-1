package project1.tick;

import org.junit.Test;
import project1.Simulation;
import project1.visitors.StatisticsSystem;

import static org.junit.Assert.*;

public class SpawnSystemTest {

    @Test
    public void tick() {

        Simulation simulation = new Simulation();
        SpawnSystem spawnSystem = new SpawnSystem();

        simulation.addTickListeners(spawnSystem);
        simulation.tick();

        assertEquals(2, spawnSystem.getBushCount());

    }
}