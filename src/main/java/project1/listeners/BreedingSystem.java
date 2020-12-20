package project1.listeners;

import project1.data.Direction;
import project1.Simulation;
import project1.actors.Animal;
import project1.actors.WorldActor;
import project1.data.SimulationConfig;
import project1.world.Cell;
import project1.world.World;

import java.util.*;

public class BreedingSystem implements CollisionListener {

    @Override
    public void collided(Simulation simulation, Set<WorldActor> actors, Cell cell) {
        SimulationConfig config = simulation.getConfig();

        Iterator<Animal> animals = actors.stream()
                                         .filter(a -> a instanceof Animal)
                                         .map(a -> (Animal) a)
                                         .filter(a -> a.getEnergy() > config
                                                 .getMinimalBreedEnergy())
                                         .sorted(Comparator.comparingInt(Animal::getEnergy))
                                         .iterator();

        while (animals.hasNext()) {
            Animal animalA = animals.next();

            if (!animals.hasNext()) {
                break;
            }

            Animal animalB = animals.next();

            breed(simulation, animalA, animalB);
        }

    }

    private void breed(Simulation simulation, Animal animalA, Animal animalB) {
        World world = simulation.getWorld();

        Random random = new Random();

        float energyPart = simulation.getConfig().getParentEnergyPart();

        int energy = (int) (animalA.getEnergy() * energyPart + animalB.getEnergy() * energyPart);
        animalA.loseEnergy((int) (animalA.getEnergy() * energyPart));
        animalB.loseEnergy((int) (animalB.getEnergy() * energyPart));

        Direction direction = Direction.values()[random.nextInt(Direction.values().length)];

        int x = (animalA.getX() + direction.getOffsetX() + world.getWidth()) % world.getWidth();
        int y = (animalA.getY() + direction.getOffsetY() + world.getHeight()) % world.getHeight();

        int split0 = random.nextInt(32);
        int split1;
        do {
            split1 = random.nextInt(32);
        } while (split0 == split1);

        int lSplit = Math.min(split0, split1);
        int rSplit = Math.max(split0, split1);

        Animal[] animals = new Animal[]{animalA, animalB};
        List<Integer> list = new ArrayList<>(Arrays.asList(0, 0, 1, 1));

        int low = list.remove(random.nextInt(list.size()));
        int mid = list.remove(random.nextInt(list.size()));
        int high = list.remove(random.nextInt(list.size()));

        int[] genes = new int[32];

        System.arraycopy(animals[low].getGenes(), 0, genes, 0, lSplit);
        System.arraycopy(animals[mid].getGenes(), lSplit, genes, lSplit, rSplit - lSplit);
        System.arraycopy(animals[high].getGenes(), rSplit, genes, rSplit, 32 - rSplit);

        List<Integer> geneCount = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0);
        for (int i = 0; i < 32; i++) {
            geneCount.set(genes[i], geneCount.get(genes[i]) + 1);
        }

        while (geneCount.contains(0)) {
            int missingGene = geneCount.indexOf(0);

            int gene = geneCount.indexOf(Collections.max(geneCount));

            geneCount.set(gene, geneCount.get(gene) - 1);
            geneCount.set(missingGene, 1);
        }

        int cursor = 0;
        for (int gene = 0; gene < 8; gene++) {
            for (int i = cursor; i < cursor + geneCount.get(gene); i++) {
                genes[i] = gene;
            }
            cursor += geneCount.get(gene);
        }

        Animal child = new Animal(world.getEpoch(), x, y, energy, genes);
        child.rotate(random.nextInt(Direction.values().length));
        world.addActor(child);

        animalA.addChild(child);
        animalB.addChild(child);
    }

}
