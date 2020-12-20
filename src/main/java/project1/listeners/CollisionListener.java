package project1.listeners;

import project1.Simulation;
import project1.actors.WorldActor;
import project1.world.Cell;

import java.util.Set;

public interface CollisionListener {
    void collided(Simulation simulation, Set<WorldActor> actors, Cell cell);
}
