package project1.system;

import glm_.vec2.Vec2i;
import project1.Simulation;
import project1.actors.Bush;
import project1.data.SimulationConfig;
import project1.listeners.SpawnListener;
import project1.tick.TickListener;
import project1.world.Cell;
import project1.world.World;

import java.util.*;

public class SpawnSystem implements TickListener {
    private final Random random = new Random(42);
    private final List<SpawnListener> listeners = new ArrayList<>();

    @Override
    public void tick(Simulation simulation) {
        World world = simulation.getWorld();
        SimulationConfig config = simulation.getConfig();

        List<Vec2i> freeJungleSpots = new LinkedList<>();
        List<Vec2i> freePlainsSpots = new LinkedList<>();

        for (int x = 0; x < world.getWidth(); x++) {
            for (int y = 0; y < world.getHeight(); y++) {
                Cell cell = world.getCell(x, y);

                if (cell.getActors().stream().anyMatch(a -> a instanceof Bush)) {
                    continue;
                }

                if (inJungle(x, y, config)) {
                    freeJungleSpots.add(new Vec2i(x, y));
                } else {
                    freePlainsSpots.add(new Vec2i(x, y));
                }
            }
        }


        int n = Math.min(config.getSpawnOutsideJungle(), freePlainsSpots.size());
        spawnBushesFromSpots(freePlainsSpots, config, n);

        n = Math.min(config.getSpawnInJungle(), freeJungleSpots.size());
        spawnBushesFromSpots(freeJungleSpots, config, n);
    }

    private void spawnBushesFromSpots(List<Vec2i> freeJungleSpots, SimulationConfig config, int n) {
        for (int i = 0; i < n; i++) {
            Vec2i spot = freeJungleSpots.remove(random.nextInt(freeJungleSpots.size()));
            int bushEnergy = config.getSpawnBushEnergy();

            Bush bush = new Bush(spot.getX(), spot.getY(), bushEnergy);
            listeners.forEach(l -> l.onSpawn(bush));
        }
    }

    public boolean inJungle(int x, int y, SimulationConfig config) {
        int bottom = config.getWorldHeight() / 2 - config.getJungleHeight() / 2;
        int top = config.getWorldHeight() / 2 + config.getJungleHeight() / 2;
        int left = config.getWorldWidth() / 2 - config.getJungleWidth() / 2;
        int right = config.getWorldWidth() / 2 + config.getJungleWidth() / 2;

        return left <= x && x <= right &&
                bottom <= y && y <= top;
    }


    public void addSpawnListener(SpawnListener listener) {
        listeners.add(listener);
    }

    public void  removeSpawnListener(SpawnListener listener) {
        listeners.remove(listener);
    }


}
