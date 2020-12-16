package project1.listeners;

import project1.actors.WorldActor;
import project1.world.Cell;
import project1.world.World;

import java.util.List;
import java.util.Set;

public interface CollisionListener {
    void collided(World world, Set<WorldActor> actors, Cell cell);
}
