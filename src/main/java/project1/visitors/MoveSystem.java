package project1.visitors;

import project1.data.Direction;
import project1.Simulation;
import project1.actors.Animal;
import project1.actors.Bush;
import project1.tick.TickListener;
import project1.world.World;

import java.util.Random;

public class MoveSystem implements WorldActorVisitor, TickListener {
    private Random random = new Random(42);
    private Simulation simulation;

    @Override
    public void visitBush(Bush bush) {
    }

    @Override
    public void visitAnimal(Animal animal) {
        int rotation = animal.getGenes()[random.nextInt(32)];
        animal.rotate(rotation);

        Direction direction = animal.getDirection();

        int x = (World.SIZE_X + animal.getX() + direction.getOffsetX()) % World.SIZE_X;
        int y = (World.SIZE_Y + animal.getY() + direction.getOffsetY()) % World.SIZE_Y;

        int moveEnergy = simulation.getConfig()
                                   .getMoveEnergy();

        animal.loseEnergy(moveEnergy);
        animal.setPosition(x, y);
    }

    @Override
    public void tick(Simulation simulation) {
        this.simulation = simulation;

        World world = simulation.getWorld();
        world.accept(this);
    }

}
