package project1;

import project1.data.SimulationConfig;
import project1.system.*;
import project1.tick.TickListener;
import project1.world.World;
import project1.world.WorldDeathListener;
import project1.world.WorldSpawnListener;

import java.util.ArrayList;
import java.util.List;

public class Simulation {

    private final World world;
    private final List<TickListener> listeners = new ArrayList<>();
    private final SimulationConfig config;

    public Simulation(SimulationConfig config) {
        this.config = config;
        this.world = new World(config.getWorldWidth(), config.getWorldHeight());
        listeners.add(world);
    }

    public World getWorld() {
        return this.world;
    }

    public void tick() {
        listeners.forEach(l -> l.tick(this));
    }

    public void addTickListeners(TickListener listener) {
        listeners.add(listener);
    }

    public <T> T getTickListener(Class<T> clazz) {
        return (T) listeners
                .stream()
                .filter(e -> clazz.isInstance(e))
                .findFirst()
                .orElse(null);
    }

    public SimulationConfig getConfig() {
        return config;
    }

    public static Simulation create(SimulationConfig config) {
        Simulation simulation = new Simulation(config);
        World world = simulation.getWorld();

        StatisticsSystem statisticsSystem = null;

        if (config.isMoveSystemOn()) {
            simulation.addTickListeners(new MoveSystem());
        }

        if (config.isStatisticsSystemOn()) {
            statisticsSystem = new StatisticsSystem();
            simulation.addTickListeners(statisticsSystem);
        }

        if (config.isSpawnSystemOn()) {
            SpawnSystem spawnSystem = new SpawnSystem();
            simulation.addTickListeners(spawnSystem);
            spawnSystem.addSpawnListener(new WorldSpawnListener(world));

            if (config.isStatisticsSystemOn()) {
                spawnSystem.addSpawnListener(statisticsSystem);
            }
        }

        if (config.isDeathSystemOn()) {
            DeathSystem deathSystem = new DeathSystem();
            simulation.addTickListeners(deathSystem);
            deathSystem.addDeathListener(new WorldDeathListener(world));

            if (config.isStatisticsSystemOn()) {
                deathSystem.addDeathListener(statisticsSystem);
            }
        }

        if (config.isBreedingSystemOn() || config.isFeedingSystemOn()) {
            CollisionSystem collisionSystem = new CollisionSystem();

            if (config.isBreedingSystemOn()) {
                collisionSystem.addCollisionListener(new BreedingSystem());
            }

            if (config.isFeedingSystemOn()) {
                collisionSystem.addCollisionListener(new FeedingSystem());
            }

            simulation.addTickListeners(collisionSystem);
        }

        return simulation;
    }

}
