package project1.world;

import project1.Simulation;
import project1.actors.WorldActor;
import project1.listeners.MoveObservable;
import project1.tick.TickListener;
import project1.visitors.WorldActorVisitor;

import java.util.HashSet;
import java.util.Set;

public class World implements TickListener {
    private int epoch = 0;
    private final int width;
    private final int height;

    private final Cell[][] cells;
    private final Set<WorldActor> actors = new HashSet<>();
    private Set<WorldActor> pendingRemoval;


    public World(int width, int height) {
        this.width = width;
        this.height = height;
        this.cells = new Cell[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                this.cells[x][y] = new Cell();
            }
        }
    }

    @Override
    public void tick(Simulation simulation) {
        epoch++;
    }

    public void addActor(WorldActor actor) {
        cells[actor.getX()][actor.getY()].addActor(actor);
        actors.add(actor);

        if (actor instanceof MoveObservable) {
            ((MoveObservable) actor)
                    .addMoveObserver(new WorldMoveObserver(this, actor));
        }
    }


    public void removeActor(WorldActor actor) {
        synchronized (this) {
            if (pendingRemoval != null) {
                pendingRemoval.add(actor);
            } else {
                actors.remove(actor);
                cells[actor.getX()][actor.getY()].removeActor(actor);
            }
        }
    }

    public Cell getCell(int x, int y) {
        return this.cells[x][y];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void accept(WorldActorVisitor visitor) {
        synchronized (this) {
            // Turn on buffering pending removal actors to increase performance
            pendingRemoval = new HashSet<>();
        }

        actors.forEach(a -> a.accept(visitor));

        synchronized (this) {
            for (WorldActor actor : pendingRemoval) {
                cells[actor.getX()][actor.getY()].removeActor(actor);
            }
            actors.removeAll(pendingRemoval);
            pendingRemoval = null;
        }
    }

    public int getEpoch() {
        return epoch;
    }
}
