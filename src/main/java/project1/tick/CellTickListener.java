package project1.tick;

import project1.Simulation;
import project1.world.Cell;
import project1.world.World;

public interface CellTickListener extends TickListener {

    void tick(Simulation simulation, Cell cell);

    @Override
    default void tick(Simulation simulation) {
        World world = simulation.getWorld();
        for (int x = 0; x < world.getWidth(); x++) {
            for (int y = 0; y < world.getHeight(); y++) {
                Cell cell = world.getCell(x, y);
                this.tick(simulation, cell);
            }
        }
    }

}
