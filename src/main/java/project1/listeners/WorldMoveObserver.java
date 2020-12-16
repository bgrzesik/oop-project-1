package project1.listeners;

import glm_.vec2.Vec2;
import glm_.vec2.Vec2i;
import project1.actors.Animal;
import project1.actors.WorldActor;
import project1.world.World;

import java.util.HashMap;
import java.util.Map;

public class WorldMoveObserver implements MoveObserver, DeathListener {
    private World world;

    private Map<WorldActor, Vec2i> oldPosition = new HashMap<>();

    public WorldMoveObserver(World world) {
        this.world = world;
    }

    @Override
    public void moved(WorldActor actor) {
        if (oldPosition.containsKey(actor)) {
            Vec2i old = oldPosition.get(actor);
            this.world.getCell(old.getX(), old.getY()).removeActor(actor);
        }

        this.oldPosition.put(actor, new Vec2i(actor.getX(), actor.getY()));
        this.world.getCell(actor.getX(), actor.getY())
                  .addActor(actor);
    }

    @Override
    public void dead(WorldActor actor) {
        this.oldPosition.remove(actor);
    }
}
