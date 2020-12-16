package project1.gui;

import com.badlogic.gdx.Gdx;
import glm_.vec2.Vec2;
import imgui.*;
import kotlin.reflect.KMutableProperty0;
import project1.Direction;
import project1.Simulation;
import project1.actors.Animal;
import project1.actors.Bush;
import project1.tick.SpawnSystem;
import project1.visitors.DeathSystem;
import project1.visitors.StatisticsSystem;
import project1.visitors.WorldActorVisitor;
import project1.world.World;

import java.util.Arrays;
import java.util.Random;

public class SimulationWidget implements Widget {
    private Simulation simulation = Simulation.createDefault();
    private WorldWidget worldWidget;
    private AddActorWidget addActorWidget = new AddActorWidget();
    private KMutableProperty0<Integer> amount = new MutableProperty0<>(100);
    private float time = 0;
    private float speed = 1.0f;

    private boolean simulate = false;
    private boolean[] showLines = new boolean[]{false};
    private int simulationIdx;

    private int quickAdd = AddActorWidget.ADD_NONE;

    private float[] aliveHist = new float[30];
    private float[] childrenHist = new float[30];
    private float[] deathHist = new float[30];
    private float[] spawnHist = new float[30];

    public SimulationWidget(int simulationIdx) {
        this.simulationIdx = simulationIdx;
        this.worldWidget = new WorldWidget(simulation.getWorld(), simulationIdx,
                                           this, addActorWidget);
    }

    @Override
    public void render(ImGui ui) {
        StatisticsSystem statisticsSystem = this.simulation
                .getTickListener(StatisticsSystem.class);
        DeathSystem deathSystem = this.simulation
                .getTickListener(DeathSystem.class);
        SpawnSystem spawnSystem = this.simulation
                .getTickListener(SpawnSystem.class);

        ui.begin("Simulation #" + simulationIdx, new boolean[]{false}, 0);
        ui.setWindowSize(new Vec2(400, 550), Cond.Once);

        ui.checkbox("Show lines", showLines);

        if (ui.collapsingHeader("Time control", 0)) {
            ui.text("Speed: %.2f", speed);
            if (ui.button("<<", new Vec2(0, 0))) {
                speed /= 2;
            }
            ui.sameLine(0, 5);
            if (ui.button("Normal", new Vec2(0, 0))) {
                speed = 1.0f;
            }
            ui.sameLine(0, 5);
            if (ui.button(">>", new Vec2(0, 0))) {
                speed *= 2;
            }

            if (ui.button(simulate ? "Pause" : "Start", new Vec2(0, 0))) {
                simulate = !simulate;
            }
            ui.sameLine(0, 5);
            if (ui.button("Tick", new Vec2(0, 0))) {
                simulation.tick();
            }
        }

        if (ui.collapsingHeader("Statistics", 0)) {
            ui.text("Epoch: %d", statisticsSystem.getEpoch());
            ui.text("Alive: %d", statisticsSystem.getAliveCount());

            Vec2 graphSize = new Vec2(200, 50);

            int aliveScaleMax = 10, childrenScaleMax = 10, deathScaleMax = 10, spawnScaleMax = 10;
            for (int i = 0; i < aliveHist.length; i++) {
                aliveScaleMax = (int) Math.max(aliveScaleMax, aliveHist[i]);
                childrenScaleMax = (int) Math.max(childrenScaleMax, childrenHist[i]);
                deathScaleMax = (int) Math.max(deathScaleMax, deathHist[i]);
                spawnScaleMax = (int) Math.max(spawnScaleMax, spawnHist[i]);
            }

            ui.plotLines("Alive", aliveHist, 0, "", 0, aliveScaleMax, graphSize, 1);

            ui.text("Children: %d", statisticsSystem.getChildrenSum());
            ui.plotLines("Children", childrenHist, 0, "", 0, childrenScaleMax, graphSize, 1);

            ui.text("Death: %d", deathSystem.getDeathCount());
            ui.plotLines("Death", deathHist, 0, "", 0, deathScaleMax, graphSize, 1);

            ui.text("Spawned bushes: %d", spawnSystem.getBushCount());
            ui.plotLines("Spawned bushes", spawnHist, 0, "", 0, spawnScaleMax, graphSize, 1);
        }

        if (ui.collapsingHeader("Actors", 0)) {
            simulation
                    .getWorld()
                    .accept(new WorldActorVisitor() {
                        @Override
                        public void visitBush(Bush bush) {
                            ActorDetailsWidget.showActorDetails(ui, simulation.getWorld(),
                                                                bush, false);
                        }

                        @Override
                        public void visitAnimal(Animal animal) {
                            ActorDetailsWidget.showActorDetails(ui, simulation.getWorld(),
                                                                animal, false);
                        }
                    });
        }

        if (ui.collapsingHeader("Add actors", 0)) {
            try {
                ui.inputInt("Amount", amount, 10, 1, 0);
            } catch (StringIndexOutOfBoundsException ignored) {
                amount.set(0);
            }
            Random random = new Random();

            addActorWidget.render(ui);
            switch (addActorWidget.getAddType()) {
                case AddActorWidget.ADD_ANIMAL:
                    for (int i = 0; i < amount.get(); i++) {
                        int x = random.nextInt(World.SIZE_X);
                        int y = random.nextInt(World.SIZE_Y);
                        Animal animal = new Animal(x, y, addActorWidget
                                .getEnergy(), addActorWidget.getGenes());
                        simulation.getWorld()
                                  .addActor(animal);
                    }
                    break;
                case AddActorWidget.ADD_BUSH:
                    for (int i = 0; i < amount.get(); i++) {
                        int x = random.nextInt(World.SIZE_X);
                        int y = random.nextInt(World.SIZE_Y);
                        Bush animal = new Bush(x, y, addActorWidget.getEnergy());
                        simulation.getWorld()
                                  .addActor(animal);
                    }
                    break;
                default:
                    break;
            }

            if (ui.radioButton("None", quickAdd == AddActorWidget.ADD_NONE)) {
                quickAdd = AddActorWidget.ADD_NONE;
            }
            ui.sameLine(0, 0);
            if (ui.radioButton("Bush", quickAdd == AddActorWidget.ADD_BUSH)) {
                quickAdd = AddActorWidget.ADD_BUSH;
            }
            ui.sameLine(0, 0);
            if (ui.radioButton("Animal", quickAdd == AddActorWidget.ADD_ANIMAL)) {
                quickAdd = AddActorWidget.ADD_ANIMAL;
            }

        }
        ui.end();

        if (simulate) {
            time += Gdx.graphics.getDeltaTime();
            if (time > 1.0f / speed) {
                this.simulation.tick();
                time = 0.0f;

                System.arraycopy(aliveHist, 1, aliveHist, 0, aliveHist.length - 1);
                aliveHist[aliveHist.length - 1] = statisticsSystem.getAliveCount();

                System.arraycopy(childrenHist, 1, childrenHist, 0, childrenHist.length - 1);
                childrenHist[childrenHist.length - 1] = statisticsSystem.getChildrenSum();

                System.arraycopy(deathHist, 1, deathHist, 0, deathHist.length - 1);
                deathHist[deathHist.length - 1] = deathSystem.getDeathCount();

                System.arraycopy(spawnHist, 1, spawnHist, 0, spawnHist.length - 1);
                spawnHist[spawnHist.length - 1] = spawnSystem.getBushCount();
            }
        }


        worldWidget.render(ui);
    }

    public int getQuickAdd() {
        return quickAdd;
    }

    public int getSimulationIdx() {
        return simulationIdx;
    }

    public boolean showLines() {
        return showLines[0];
    }
}
