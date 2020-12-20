package project1.world;

import project1.actors.WorldActor;

import java.util.HashSet;
import java.util.Set;

public class Cell {
    private final Set<WorldActor> actors = new HashSet<>();

    public void addActor(WorldActor actor) {
        this.actors.add(actor);
    }

    public void removeActor(WorldActor actor) {
        this.actors.remove(actor);
    }

    public Set<WorldActor> getActors() {
        return this.actors;
    }
}
