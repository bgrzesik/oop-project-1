package project1.tick;

import org.junit.Test;
import project1.Simulation;
import project1.data.SimulationConfig;

import static org.junit.Assert.*;

public class SpawnSystemTest {

    @Test
    public void tick() {
        SimulationConfig config = new SimulationConfig.Builder()
                .noSystems()
                .build();

        Simulation simulation = new Simulation(config);
        SpawnSystem spawnSystem = new SpawnSystem();

        simulation.addTickListeners(spawnSystem);
        simulation.tick();

        assertEquals(2, spawnSystem.getBushCount());

    }
}