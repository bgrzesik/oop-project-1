package project1.visitors;

import project1.Simulation;
import project1.actors.Animal;
import project1.actors.Bush;
import project1.tick.TickListener;
import project1.world.World;

public class StatisticsSystem implements WorldActorVisitor, TickListener {
    private int aliveAnimalsCount;
    private int presentBushCount;
    private int childrenSum;
    private int energySum;
    private int ageSum;

    private final int[] genes = new int[8];


    @Override
    public void tick(Simulation simulation) {
        this.aliveAnimalsCount = 0;
        this.presentBushCount = 0;
        this.childrenSum = 0;
        this.energySum = 0;
        this.ageSum = 0;

        for (int i = 0; i < 8; i++) {
            genes[i] = 0;
        }

        World world = simulation.getWorld();
       world.accept(this);
    }

    @Override
    public void visitBush(Bush bush) {
        this.presentBushCount++;
    }

    @Override
    public void visitAnimal(Animal animal) {
        animal.age();

        this.aliveAnimalsCount++;
        this.childrenSum += animal.getChildrenCount();
        this.energySum += animal.getEnergy();
        this.ageSum += animal.getAge();

        for (int gene : animal.getGenes()) {
            this.genes[gene] += 1;
        }

    }

    public int getAliveAnimalsCount() {
        return aliveAnimalsCount;
    }

    public int getPresentBushCount() {
        return presentBushCount;
    }

    public float getEnergyAverage() {
        if (aliveAnimalsCount == 0) {
            return 0;
        }
        return energySum / (float) aliveAnimalsCount;
    }

    public float getAgeAverage() {
        if (aliveAnimalsCount == 0) {
            return 0;
        }
        return ageSum / (float) aliveAnimalsCount;
    }

    public float getChildrenAverage() {
        if (aliveAnimalsCount == 0) {
            return 0;
        }
        return childrenSum / (float) aliveAnimalsCount;
    }

    public int[] getGenes() {
        return genes;
    }
}
