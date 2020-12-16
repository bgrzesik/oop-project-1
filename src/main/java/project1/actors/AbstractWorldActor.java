package project1.actors;

import project1.listeners.DeathListener;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractWorldActor implements WorldActor {
    protected int x;
    protected int y;
    protected int energy;

    private List<DeathListener> deathListeners = new ArrayList<>();

    public AbstractWorldActor(int x, int y, int energy) {
        this.x = x;
        this.y = y;
        this.energy = energy;
    }

    @Override
    public void addDeathListener(DeathListener listener) {
        deathListeners.add(listener);
    }

    @Override
    public void removeDeathListener(DeathListener listener) {
        deathListeners.remove(listener);
    }

    @Override
    public boolean pendingRemoval() {
        return energy <= 0;
    }

    @Override
    public int getEnergy() {
        return energy;
    }

    @Override
    public void setEnergy(int energy) {
        this.energy = energy;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void kill() {
        deathListeners.forEach(l -> l.dead(this));
        deathListeners.clear();
    }
}
