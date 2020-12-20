package project1.world;

import project1.actors.WorldActor;
import project1.listeners.MoveObserver;
import project1.world.World;

public class WorldMoveObserver implements MoveObserver {
    private final World world;
    private int oldX;
    private int oldY;

    public WorldMoveObserver(World world, WorldActor actor) {
        this.world = world;
        this.oldX = actor.getX();
        this.oldY = actor.getY();
    }

    @Override
    public void moved(WorldActor actor) {
        this.world.getCell(this.oldX, this.oldY).removeActor(actor);

        this.oldX = actor.getX();
        this.oldY = actor.getY();
        this.world.getCell(actor.getX(), actor.getY())
                  .addActor(actor);
    }

}
