package project1;

import project1.data.SimulationConfig;
import project1.listeners.BreedingSystem;
import project1.listeners.FeedingSystem;
import project1.tick.CollisionSystem;
import project1.tick.SpawnSystem;
import project1.tick.TickListener;
import project1.visitors.DeathSystem;
import project1.visitors.MoveSystem;
import project1.visitors.StatisticsSystem;
import project1.world.World;

import java.util.ArrayList;
import java.util.List;

public class Simulation {

    private World world = new World();
    private List<TickListener> listeners = new ArrayList<>();
    private final SimulationConfig config;

    public Simulation(SimulationConfig config) {
        this.config = config;
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

        if (config.isSpawnSystemOn()) {
            simulation.addTickListeners(new SpawnSystem());
        }

        if (config.isMoveSystemOn()) {
            simulation.addTickListeners(new MoveSystem());
        }

        if (config.isDeathSystemOn()) {
            simulation.addTickListeners(new DeathSystem());
        }

        if (config.isStatisticsSystemOn()) {
            simulation.addTickListeners(new StatisticsSystem());
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
