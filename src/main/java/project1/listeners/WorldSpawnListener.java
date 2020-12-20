package project1.listeners;

import project1.actors.Bush;
import project1.world.World;

public class WorldSpawnListener implements SpawnListener {

    private final World world;

    public WorldSpawnListener(World world) {
        this.world = world;
    }

    @Override
    public void onSpawn(Bush bush) {
        this.world.addActor(bush);
    }
}
