package project1.world;

import project1.actors.WorldActor;

public interface WorldActorListener {
    void addActor(WorldActor actor);
    void removeActor(WorldActor actor);
}
