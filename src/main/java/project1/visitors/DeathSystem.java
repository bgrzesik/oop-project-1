package project1.visitors;

import project1.Simulation;
import project1.actors.Animal;
import project1.actors.Bush;
import project1.listeners.DeathListener;
import project1.tick.CellTickListener;
import project1.world.Cell;
import project1.world.World;

import java.util.HashSet;
import java.util.Set;

public class DeathSystem implements WorldActorVisitor, CellTickListener {
    private World world;
    private int deathCount = 0;

    @Override
    public void visitBush(Bush bush) {
        if (bush.pendingRemoval()) {
            bush.kill();
        }
    }

    @Override
    public void visitAnimal(Animal animal) {
        if (animal.pendingRemoval()) {
            animal.kill();
            deathCount += 1;
        }
    }

    @Override
    public void tick(Simulation simulation, Cell cell) {
        this.world = simulation.getWorld();
        this.world.accept(this);
    }

    public int getDeathCount() {
        return deathCount;
    }
}
