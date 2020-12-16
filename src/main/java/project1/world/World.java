package project1.world;

import com.badlogic.gdx.graphics.Texture;
import project1.actors.WorldActor;
import project1.listeners.MoveObservable;
import project1.listeners.WorldDeathListener;
import project1.listeners.WorldMoveObserver;
import project1.visitors.StatisticsSystem;
import project1.visitors.WorldActorVisitor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class World {
    public static final int SIZE_X = 100;
    public static final int SIZE_Y = 30;

    private Cell[][] cells = new Cell[SIZE_X][SIZE_Y];
    private Set<WorldActor> actors = new HashSet<>();

    private WorldDeathListener deathListener;
    private WorldMoveObserver moveObserver;

    public World() {
        for (int x = 0; x < SIZE_X; x++) {
            for (int y = 0; y < SIZE_Y; y++) {
                cells[x][y] = new Cell();
            }
        }

        this.deathListener = new WorldDeathListener(this);
        this.moveObserver = new WorldMoveObserver(this);
    }

    public void addActor(WorldActor actor) {
        cells[actor.getX()][actor.getY()].addActor(actor);
        actors.add(actor);

        if (actor instanceof MoveObservable) {
            ((MoveObservable) actor)
                    .addMoveObserver(moveObserver);

            actor.addDeathListener(moveObserver);
        }

        actor.addDeathListener(deathListener);
    }


    public void removeActor(WorldActor actor) {
        cells[actor.getX()][actor.getY()].removeActor(actor);
        actors.remove(actor);
    }

    public Cell getCell(int x, int y) {
        return this.cells[x][y];
    }

    public void accept(WorldActorVisitor visitor) {
        Set<WorldActor> actors = new HashSet<>(this.actors);
        actors.forEach(a -> a.accept(visitor));
    }
}
