package project1.gui;

import glm_.vec2.Vec2;
import imgui.*;
import org.lwjgl.system.CallbackI;
import project1.Direction;
import project1.Simulation;
import project1.actors.Animal;
import project1.actors.Bush;
import project1.actors.WorldActor;
import project1.world.Cell;
import project1.world.World;

import java.util.*;

public class CellDetailsWidget implements Widget {
    private boolean[] windowOpen = new boolean[]{false};
    private int x = 0;
    private int y = 0;

    private World world;
    private int simulationIdx;
    private List<ActorDetailsWidget> pinned = new ArrayList<>();
    private AddActorWidget addActorWidget = new AddActorWidget();

    public CellDetailsWidget(World world, int simulationIdx) {
        this.world = world;
        this.simulationIdx = simulationIdx;
    }


    @Override
    public void render(ImGui ui) {
        if (windowOpen[0]) {
            ui.begin(String.format("Cell details (World #%d)", simulationIdx), windowOpen, 0);
            ui.setWindowSize(new Vec2(500, 300), Cond.Once);
            ui.text("X: %d", x);
            ui.text("Y: %d", y);

            Cell cell = world.getCell(x, y);

            Set<WorldActor> actors = new HashSet<>(cell.getActors());
            if (ui.collapsingHeader("Actors", TreeNodeFlag.DefaultOpen.i) && actors.size() > 0) {
                for (WorldActor actor : actors) {
                    if (ActorDetailsWidget.showActorDetails(ui, world, actor, true)) {
                        pinned.add(new ActorDetailsWidget(world, actor));
                    }
                }
            }

            if (ui.collapsingHeader("Add actor", TreeNodeFlag.DefaultOpen.i)) {
                addActorWidget.render(ui);
                switch (addActorWidget.getAddType()) {
                    case AddActorWidget.ADD_ANIMAL:
                        world.addActor(new Animal(x, y, addActorWidget.getEnergy(), addActorWidget
                                .getGenes()));
                        break;
                    case AddActorWidget.ADD_BUSH:
                        world.addActor(new Bush(x, y, addActorWidget.getEnergy()));
                        break;
                    default:
                        break;
                }
            }

            ui.end();
        }

        for (int i = 0; i < pinned.size(); i++) {
            pinned.get(i).render(ui);
        }
        pinned.removeIf(ActorDetailsWidget::shouldRemove);
    }


    public void setCell(int x, int y) {
        this.x = x;
        this.y = y;
        windowOpen[0] = true;
    }


}
