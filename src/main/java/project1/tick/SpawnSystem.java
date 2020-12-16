package project1.tick;

import project1.Simulation;
import project1.actors.Bush;
import project1.world.World;

import java.util.Random;

public class SpawnSystem implements TickListener {
    public static final int JUNGLE_TOP = 20;
    public static final int JUNGLE_RIGHT = 55;
    public static final int JUNGLE_BOTTOM = 10;
    public static final int JUNGLE_LEFT = 45;
    public static final int BUSH_ENERGY = 40;

    private final Random random = new Random(42);

    private int bushCount = 0;

    @Override
    public void tick(Simulation simulation) {
        spawnJungle(simulation.getWorld());
        spawnOutsideJungle(simulation.getWorld());
        this.bushCount += 2;
    }

    public int getBushCount() {
        return bushCount;
    }

    public void spawnOutsideJungle(World world) {
        int x, y;

        do {
            x = random.nextInt(World.SIZE_X);
            y = random.nextInt(World.SIZE_Y);
        } while (JUNGLE_LEFT <= x && x <= JUNGLE_RIGHT &&
                 JUNGLE_BOTTOM <= y && y <= JUNGLE_TOP);

        Bush bush = new Bush(x, y, BUSH_ENERGY);
        world.addActor(bush);

    }

    public void spawnJungle(World world) {
        int x = JUNGLE_LEFT + random.nextInt(JUNGLE_RIGHT - JUNGLE_LEFT);
        int y = JUNGLE_BOTTOM + random.nextInt(JUNGLE_TOP - JUNGLE_BOTTOM);

        Bush bush = new Bush(x, y, BUSH_ENERGY);
        world.addActor(bush);
    }

}
