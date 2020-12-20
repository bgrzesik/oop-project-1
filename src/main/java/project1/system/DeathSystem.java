package project1.system;

import project1.Simulation;
import project1.actors.Animal;
import project1.actors.Bush;
import project1.listeners.DeathListener;
import project1.tick.CellTickListener;
import project1.visitors.WorldActorVisitor;
import project1.world.Cell;
import project1.world.World;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DeathSystem implements WorldActorVisitor, CellTickListener {
    private final List<DeathListener> globalDeathListeners = new ArrayList<>();

    @Override
    public void visitBush(Bush bush) {
        if (bush.pendingRemoval()) {
            globalDeathListeners.forEach(l -> l.dead(bush));
        }
    }

    @Override
    public void visitAnimal(Animal animal) {
        if (animal.pendingRemoval()) {
            globalDeathListeners.forEach(l -> l.dead(animal));
        }
    }

    @Override
    public void tick(Simulation simulation, Cell cell) {
        World world = simulation.getWorld();
        world.accept(this);
    }

    public void addDeathListener(DeathListener deathListener) {
        globalDeathListeners.add(deathListener);
    }

    public void removeDeathListener(DeathListener deathListener) {
        globalDeathListeners.remove(deathListener);
    }
}
