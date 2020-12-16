package project1.listeners;

import project1.data.Direction;
import project1.Simulation;
import project1.actors.Animal;
import project1.actors.WorldActor;
import project1.data.SimulationConfig;
import project1.world.Cell;
import project1.world.World;

import java.util.*;

import static project1.world.World.SIZE_X;
import static project1.world.World.SIZE_Y;

public class BreedingSystem implements CollisionListener {

    @Override
    public void collided(Simulation simulation, Set<WorldActor> actors, Cell cell) {
        SimulationConfig config = simulation.getConfig();

        Iterator<Animal> animals = actors.stream()
                                         .filter(a -> a instanceof Animal)
                                         .map(a -> (Animal) a)
                                         .filter(a -> a.getEnergy() > config.getMinimalBreedEnergy())
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
        Random random = new Random();

        float energyPart = simulation.getConfig().getParentEnergyPart();

        int energy = (int) (animalA.getEnergy() * energyPart + animalB.getEnergy() * energyPart);
        animalA.loseEnergy((int) (animalA.getEnergy() * energyPart));
        animalB.loseEnergy((int) (animalB.getEnergy() * energyPart));
        animalA.increaseChildren();
        animalB.increaseChildren();

        Direction direction = Direction.values()[random.nextInt(Direction.values().length)];

        int x = (animalA.getX() + direction.getOffsetX() + SIZE_X) % SIZE_X;
        int y = (animalA.getY() + direction.getOffsetY() + SIZE_Y) % SIZE_Y;

        int split0 = random.nextInt(32);
        int split1;
        do {
            split1 = random.nextInt(32);
        } while (split0 == split1);

        int lSplit = Math.min(split0, split1);
        int rSplit = Math.max(split0, split1);

        Animal[] animals = new Animal[] { animalA, animalB };
        List<Integer> list = new ArrayList<>(Arrays.asList(0, 0, 1, 1));

        int low = list.remove(random.nextInt(list.size()));
        int mid = list.remove(random.nextInt(list.size()));
        int high = list.remove(random.nextInt(list.size()));

        int[] genes = new int[32];

        System.arraycopy(animals[low].getGenes(), 0, genes, 0, lSplit);
        System.arraycopy(animals[mid].getGenes(), lSplit, genes, lSplit, rSplit - lSplit);
        System.arraycopy(animals[high].getGenes(), rSplit, genes, rSplit, 32 - rSplit);
        Arrays.sort(genes);

        Animal child = new Animal(x, y, energy, genes);
        simulation.getWorld()
                  .addActor(child);
    }

}
