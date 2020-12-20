package project1.world;

import project1.actors.WorldActor;
import project1.listeners.DeathListener;
import project1.world.World;

public class WorldDeathListener implements DeathListener {

    private World world;

    public WorldDeathListener(World world) {
        this.world = world;
    }

    @Override
    public void dead(WorldActor actor) {
        this.world.removeActor(actor);
    }
}
