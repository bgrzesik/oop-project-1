package project1.system;

import project1.Simulation;
import project1.actors.Animal;
import project1.actors.Bush;
import project1.actors.WorldActor;
import project1.data.GenomeFrequency;
import project1.listeners.DeathListener;
import project1.listeners.SpawnListener;
import project1.tick.TickListener;
import project1.visitors.WorldActorVisitor;
import project1.world.World;

import java.util.Map;
import java.util.TreeMap;

public class StatisticsSystem implements WorldActorVisitor, TickListener, DeathListener {
    private Statistics stat = new Statistics();

    @Override
    public void tick(Simulation simulation) {
        stat.aliveAnimalsCount = 0;
        stat.presentBushCount = 0;
        stat.childrenSum = 0;
        stat.energySum = 0;
        stat.ageSum = 0;

        for (int i = 0; i < 8; i++) {
            stat.genes[i] = 0;
        }

        stat.genomes.clear();

        World world = simulation.getWorld();
        world.accept(this);
    }

    @Override
    public void visitBush(Bush bush) {
        stat.presentBushCount++;
    }

    @Override
    public void visitAnimal(Animal animal) {
        animal.age();

        stat.aliveAnimalsCount++;
        stat.childrenSum += animal.getChildrenCount();
        stat.energySum += animal.getEnergy();
        stat.ageSum += animal.getAge();

        String genome = animal.getGenome();
        stat.genomes.computeIfAbsent(genome, GenomeFrequency::new)
                    .incrementFrequency();

        for (int gene : animal.getGenes()) {
            stat.genes[gene] += 1;
        }

    }

    @Override
    public void dead(WorldActor actor) {
        if (actor instanceof Animal) {
            Animal animal = (Animal) actor;

            for (int gene : animal.getGenes()) {
                stat.deadGenes[gene] += 1;
            }

            stat.deadChildrenSum += animal.getChildrenCount();
            stat.deadEnergySum += animal.getEnergy(); // should be always zero
            stat.deadAgeSum += animal.getAge();
            stat.deadCount++;

            String genome = animal.getGenome();
            stat.deadGenomes.computeIfAbsent(genome, GenomeFrequency::new)
                            .incrementFrequency();

        } else if (actor instanceof Bush) {
            stat.eatenCount++;
        }
    }


    public Statistics getCurrentStatistics() {
        return stat;
    }

    public Statistics exportStatistics() {
        return (Statistics) stat.clone();
    }
}
