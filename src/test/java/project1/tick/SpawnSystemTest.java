package project1.tick;

import org.junit.Test;
import project1.Simulation;
import project1.data.SimulationConfig;
import project1.system.SpawnSystem;
import project1.system.StatisticsSystem;
import project1.world.WorldSpawnListener;

import static org.junit.Assert.*;

public class SpawnSystemTest {

    @Test
    public void tick() {
        SimulationConfig config = new SimulationConfig.Builder()
                .noSystems()
                .build();

        Simulation simulation = new Simulation(config);
        StatisticsSystem statisticsSystem = new StatisticsSystem();
        SpawnSystem spawnSystem = new SpawnSystem();
        spawnSystem.addSpawnListener(new WorldSpawnListener(simulation.getWorld()));
//        spawnSystem.addSpawnListener(statisticsSystem);

        simulation.addTickListeners(spawnSystem);
        simulation.addTickListeners(statisticsSystem);

        simulation.tick();

        assertEquals(2, statisticsSystem.getCurrentStatistics()
                                        .getTotalBushCount());

    }
}