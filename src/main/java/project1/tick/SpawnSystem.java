package project1.tick;

import project1.Simulation;
import project1.actors.Bush;
import project1.data.SimulationConfig;
import project1.world.World;

import java.util.Random;

public class SpawnSystem implements TickListener {
    public static final int JUNGLE_TOP = 20;
    public static final int JUNGLE_RIGHT = 55;
    public static final int JUNGLE_BOTTOM = 10;
    public static final int JUNGLE_LEFT = 45;

    private final Random random = new Random(42);

    private int bushCount = 0;

    @Override
    public void tick(Simulation simulation) {
        SimulationConfig config = simulation.getConfig();

        for (int i = 0; i < config.getSpawnInJungle(); i++) {
            spawnInJungle(simulation);
            this.bushCount++;
        }

        for (int i = 0; i < config.getSpawnOutsideJungle(); i++) {
            spawnOutsideJungle(simulation);
            this.bushCount++;
        }
    }

    public int getBushCount() {
        return bushCount;
    }

    public void spawnOutsideJungle(Simulation simulation) {
        int x, y;

        do {
            x = random.nextInt(World.SIZE_X);
            y = random.nextInt(World.SIZE_Y);
        } while (JUNGLE_LEFT <= x && x <= JUNGLE_RIGHT &&
                JUNGLE_BOTTOM <= y && y <= JUNGLE_TOP);

        int bushEnergy = simulation.getConfig()
                                   .getSpawnBushEnergy();

        Bush bush = new Bush(x, y, bushEnergy);
        World world = simulation.getWorld();

        world.addActor(bush);

    }

    public void spawnInJungle(Simulation simulation) {
        int x = JUNGLE_LEFT + random.nextInt(JUNGLE_RIGHT - JUNGLE_LEFT);
        int y = JUNGLE_BOTTOM + random.nextInt(JUNGLE_TOP - JUNGLE_BOTTOM);

        int bushEnergy = simulation.getConfig()
                                   .getSpawnBushEnergy();

        Bush bush = new Bush(x, y, bushEnergy);
        World world = simulation.getWorld();

        world.addActor(bush);
    }

}
