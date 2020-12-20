package project1.system;

import project1.data.Direction;
import project1.Simulation;
import project1.actors.Animal;
import project1.actors.Bush;
import project1.tick.TickListener;
import project1.visitors.WorldActorVisitor;
import project1.world.World;

import java.util.Random;

public class MoveSystem implements WorldActorVisitor, TickListener {
    private Random random = new Random(42);
    private Simulation simulation;
    private World world;

    @Override
    public void visitBush(Bush bush) {
    }

    @Override
    public void visitAnimal(Animal animal) {
        int rotation = animal.getGenes()[random.nextInt(32)];
        animal.rotate(rotation);

        Direction direction = animal.getDirection();

        int x = (world.getWidth() + animal.getX() + direction.getOffsetX()) % world.getWidth();
        int y = (world.getHeight() + animal.getY() + direction.getOffsetY()) % world.getHeight();

        int moveEnergy = simulation.getConfig()
                                   .getMoveEnergy();

        animal.loseEnergy(moveEnergy);
        animal.setPosition(x, y);
    }

    @Override
    public void tick(Simulation simulation) {
        this.simulation = simulation;
        this.world = simulation.getWorld();
        this.world.accept(this);
    }

}
