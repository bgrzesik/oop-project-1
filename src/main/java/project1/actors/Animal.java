package project1.actors;

import project1.data.Direction;
import project1.listeners.MoveObservable;
import project1.listeners.MoveObserver;
import project1.visitors.WorldActorVisitor;

import java.util.ArrayList;
import java.util.List;

public class Animal extends AbstractWorldActor implements MoveObservable {

    private final List<Animal> children = new ArrayList<>();
    private final List<MoveObserver> moveObservers = new ArrayList<>();
    private final int[] genes;
    private final int born;

    private Direction direction = Direction.N;
    private int age = 0;


    public Animal(int born, int x, int y, int energy, int[] genes) {
        super(x, y, energy);
        this.genes = genes;
        this.born = born;
    }

    public void loseEnergy(int energy) {
        this.energy -= energy;
    }

    public void gainEnergy(int energy) {
        this.energy += energy;
    }

    @Override
    public void accept(WorldActorVisitor visitor) {
        visitor.visitAnimal(this);
    }

    @Override
    public void setPosition(int x, int y) {
        super.setPosition(x, y);
        this.moveObservers.forEach(o -> o.moved(this));
    }

    public int[] getGenes() {
        return genes;
    }

    public String getGenome() {
        String[] genesString = new String[32];

        for (int i = 0; i < 32; i++) {
            genesString[i] = String.valueOf(genes[i]);
        }

        return String.join("", genesString);
    }

    public Direction getDirection() {
        return direction;
    }

    public void rotate(int rotation) {
        int ordinal = (this.direction.ordinal() + rotation) % Direction.values().length;
        this.direction = Direction.values()[ordinal];
    }

    public List<Animal> getChildren() {
        return this.children;
    }

    public int getChildrenCount() {
        return this.children.size();
    }

    public int getAge() {
        return age;
    }

    public void age() {
        this.age++;
    }

    @Override
    public void addMoveObserver(MoveObserver observer) {
        this.moveObservers.add(observer);
    }

    @Override
    public void removeMoveObserver(MoveObserver observer) {
        this.moveObservers.remove(observer);
    }

    public void addChild(Animal child) {
        this.children.add(child);
    }

    public int getBorn() {
        return born;
    }
}
