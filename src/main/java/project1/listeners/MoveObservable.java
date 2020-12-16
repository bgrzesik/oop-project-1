package project1.listeners;

public interface MoveObservable {

    void addMoveObserver(MoveObserver observer);

    void removeMoveObserver(MoveObserver observer);

}
