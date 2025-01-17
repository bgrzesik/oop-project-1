package project1.system;

import project1.Simulation;
import project1.actors.WorldActor;
import project1.listeners.CollisionListener;
import project1.tick.CellTickListener;
import project1.world.Cell;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CollisionSystem implements CellTickListener {
    private List<CollisionListener> listeners = new ArrayList<>();

    @Override
    public void tick(Simulation simulation, Cell cell) {
        Set<WorldActor> actors = cell.getActors();
        if (actors.size() > 1) {
            listeners.forEach(l -> l.collided(simulation, actors, cell));
        }
    }

    public void addCollisionListener(CollisionListener listener) {
        listeners.add(listener);
    }

    public void removeCollisionListener(CollisionListener listener) {
        listeners.remove(listener);
    }

}
