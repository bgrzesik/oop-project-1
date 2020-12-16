package project1.actors;

import project1.listeners.DeathListener;
import project1.visitors.WorldActorVisitor;

public interface WorldActor {

    boolean pendingRemoval();

    void accept(WorldActorVisitor visitor);

    void addDeathListener(DeathListener listener);

    void removeDeathListener(DeathListener listener);

    int getX();

    int getY();

    void setPosition(int x, int y);

    int getEnergy();

    void setEnergy(int energy);
}
