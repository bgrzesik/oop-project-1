package project1.visitors;

import project1.Direction;
import project1.Simulation;
import project1.actors.Animal;
import project1.actors.Bush;
import project1.tick.CellTickListener;
import project1.tick.TickListener;
import project1.world.Cell;

public class StatisticsSystem implements WorldActorVisitor,  TickListener {
    private int aliveCount;
    private int childrenSum;
    private int epoch = 0;

    private int[] genes = new int[8];


    @Override
    public void tick(Simulation simulation) {
        this.aliveCount = 0;
        this.childrenSum = 0;
        this.epoch += 1;

        for (int i = 0; i < 8; i++) {
            genes[i] = 0;
        }

        simulation.getWorld().accept(this);
    }

    @Override
    public void visitBush(Bush bush) {
    }

    @Override
    public void visitAnimal(Animal animal) {
        this.aliveCount += 1;
        this.childrenSum += animal.getChildren();

        for (int gene : animal.getGenes()) {
            this.genes[gene] += 1;
        }
    }

    public int getEpoch() {
        return epoch;
    }

    public int getAliveCount() {
        return aliveCount;
    }

    public int getChildrenSum() {
        return childrenSum;
    }
}
