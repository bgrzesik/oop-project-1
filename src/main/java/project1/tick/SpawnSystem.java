package project1.tick;

import project1.Simulation;
import project1.actors.Bush;
import project1.data.SimulationConfig;
import project1.world.World;

import java.util.Random;

public class SpawnSystem implements TickListener {
//    public static final int JUNGLE_TOP = 20;
//    public static final int JUNGLE_RIGHT = 55;
//    public static final int JUNGLE_BOTTOM = 10;
//    public static final int JUNGLE_LEFT = 45;

    private final Random random = new Random(42);

    private int bushCount = 0;

    @Override
    public void tick(Simulation simulation) {
        spawnInJungle(simulation);
        spawnOutsideJungle(simulation);
    }

    public int getBushCount() {
        return bushCount;
    }

    public void spawnOutsideJungle(Simulation simulation) {
        SimulationConfig config = simulation.getConfig();
        World world = simulation.getWorld();

        int bottom = config.getWorldHeight() / 2 - config.getJungleHeight() / 2;
        int top = config.getWorldHeight() / 2 + config.getJungleHeight() / 2;
        int left = config.getWorldWidth() / 2 - config.getJungleWidth() / 2;
        int right = config.getWorldWidth() / 2 + config.getJungleWidth() / 2;

        for (int i = 0; i < config.getSpawnOutsideJungle(); i++) {
            int x, y;

            do {
                x = random.nextInt(world.getWidth());
                y = random.nextInt(world.getHeight());
            } while (left <= x && x <= right &&
                    bottom <= y && y <= top);

            int bushEnergy = config.getSpawnBushEnergy();

            Bush bush = new Bush(x, y, bushEnergy);
            world.addActor(bush);
            this.bushCount++;
        }
    }

    public void spawnInJungle(Simulation simulation) {
        SimulationConfig config = simulation.getConfig();
        World world = simulation.getWorld();

        int bottom = world.getHeight() / 2 - config.getJungleHeight() / 2;
        int left = world.getWidth() / 2 - config.getJungleWidth() / 2;

        for (int i = 0; i < config.getSpawnOutsideJungle(); i++) {
            int x = left + random.nextInt(config.getJungleWidth());
            int y = bottom + random.nextInt(config.getJungleHeight());

            int bushEnergy = config.getSpawnBushEnergy();

            Bush bush = new Bush(x, y, bushEnergy);
            world.addActor(bush);
            this.bushCount++;
        }
    }

}
