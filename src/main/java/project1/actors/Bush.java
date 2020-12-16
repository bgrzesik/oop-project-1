package project1.actors;

import project1.visitors.WorldActorVisitor;

public class Bush extends AbstractWorldActor {
    private boolean eaten = false;

    public Bush(int x, int y, int energy) {
        super(x, y, energy);
    }

    @Override
    public void accept(WorldActorVisitor visitor) {
        visitor.visitBush(this);
    }

    public boolean wasEaten() {
        return eaten;
    }

    public void setEaten(boolean eaten) {
        this.eaten = eaten;
    }

    @Override
    public boolean pendingRemoval() {
        return this.eaten;
    }

}
