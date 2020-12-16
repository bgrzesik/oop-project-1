package project1;

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

    public static Simulation createDefault() {
        Simulation simulation = new Simulation();
        simulation.addTickListeners(new SpawnSystem());
        simulation.addTickListeners(new MoveSystem());
        simulation.addTickListeners(new DeathSystem());
        simulation.addTickListeners(new StatisticsSystem());
        CollisionSystem collisionSystem = new CollisionSystem();
        collisionSystem.addCollisionListener(new BreedingSystem());
        collisionSystem.addCollisionListener(new FeedingSystem());
        simulation.addTickListeners(collisionSystem);


        return simulation;
    }

}
