package project1.system;

import project1.Simulation;
import project1.actors.Animal;
import project1.actors.Bush;
import project1.actors.WorldActor;
import project1.listeners.CollisionListener;
import project1.world.Cell;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;

public class FeedingSystem implements CollisionListener {
    @Override
    public void collided(Simulation simulation, Set<WorldActor> actors, Cell cell) {
        Iterator<Bush> bushes = actors.stream()
                                      .filter(o -> o instanceof Bush)
                                      .map(o -> ((Bush) o))
                                      .sorted(Comparator.comparingInt(Bush::getEnergy))
                                      .iterator();

        Iterator<Animal> animals = actors.stream()
                                         .filter(o -> o instanceof Animal )
                                         .map(o -> ((Animal) o))
                                         .sorted(Comparator.comparingInt(Animal::getEnergy))
                                         .iterator();

        while (animals.hasNext() && bushes.hasNext()) {
            Animal animal = animals.next();
            Bush bush = bushes.next();

            animal.gainEnergy(bush.getEnergy());
            bush.setEaten(true);

        }
    }
}
