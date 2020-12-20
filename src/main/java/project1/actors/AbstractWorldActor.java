package project1.actors;

public abstract class AbstractWorldActor implements WorldActor {
    protected int x;
    protected int y;
    protected int energy;

    public AbstractWorldActor(int x, int y, int energy) {
        this.x = x;
        this.y = y;
        this.energy = energy;
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
}
