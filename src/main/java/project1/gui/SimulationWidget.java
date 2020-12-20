package project1.gui;

import com.badlogic.gdx.Gdx;
import glm_.vec2.Vec2;
import imgui.*;
import kotlin.reflect.KMutableProperty0;
import project1.Simulation;
import project1.actors.Animal;
import project1.actors.Bush;
import project1.tick.SpawnSystem;
import project1.visitors.DeathSystem;
import project1.visitors.StatisticsSystem;
import project1.visitors.WorldActorVisitor;
import project1.world.World;

import java.util.Random;

public class SimulationWidget implements Widget {
    private Simulation simulation;
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
    private float[] bushHist = new float[30];
    private float[] energyHist = new float[30];
    private float[] ageHist = new float[30];
    private float[] deathAgeHist = new float[30];

    public SimulationWidget(int simulationIdx, Simulation simulation) {
        this.simulation = simulation;
        this.simulationIdx = simulationIdx;
        this.worldWidget = new WorldWidget(simulation, simulationIdx,
                                           this, addActorWidget);
    }

    @Override
    public void render(ImGui ui) {
        StatisticsSystem statisticsSystem = this.simulation
                .getTickListener(StatisticsSystem.class);

        ui.begin("Simulation #" + simulationIdx, new boolean[]{false}, 0);
        ui.setWindowSize(new Vec2(700, 950), Cond.Once);

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

            Vec2 graphSize = new Vec2(200, 50);

            int aliveScaleMax = 3, childrenScaleMax = 3, bushScaleMax = 3, energyScaleMax = 3, deathAgeMax = 3;
            for (int i = 0; i < aliveHist.length; i++) {
                aliveScaleMax = (int) Math.max(aliveScaleMax, aliveHist[i]);
                childrenScaleMax = (int) Math.max(childrenScaleMax, childrenHist[i]);
                bushScaleMax = (int) Math.max(bushScaleMax, bushHist[i]);
                energyScaleMax = (int) Math.max(energyScaleMax, energyHist[i]);
                deathAgeMax = (int) Math.max(deathAgeMax, deathAgeHist[i]);
            }

            String text;

            if (statisticsSystem != null) {
                text = String.format("Alive: %d", statisticsSystem.getAliveAnimalsCount());
                ui.plotLines(text, aliveHist, 0, "", 0, aliveScaleMax, graphSize, 1);

                text = String.format("Bush: %d", statisticsSystem.getPresentBushCount());
                ui.plotLines(text, bushHist, 0, "", 0, bushScaleMax, graphSize, 1);

                text = String.format("Children: %.2f", statisticsSystem.getChildrenAverage());
                ui.plotLines(text, childrenHist, 0, "", 0, childrenScaleMax, graphSize, 1);

                text = String.format("Age: %.2f", statisticsSystem.getAgeAverage());
                ui.plotLines(text, ageHist, 0, "", 40, childrenScaleMax, graphSize, 1);

                text = String.format("Energy: %.2f", statisticsSystem.getEnergyAverage());
                ui.plotLines(text, energyHist, 0, "", 0, energyScaleMax, graphSize, 1);

                text = String.format("Death age: %.2f", statisticsSystem.getDeathAgeAverage());
                ui.plotLines(text, deathAgeHist, 0, "", 0, deathAgeMax, graphSize, 1);

                ui.text("Epoch: %d", simulation.getWorld().getEpoch());

                ui.text("Dead: %d", statisticsSystem.getDeadCount());

                ui.text("Bushes total: %d", statisticsSystem.getBushCount());
            }

            float[] genes = new float[8];
            if (statisticsSystem != null && statisticsSystem.getAliveAnimalsCount() != 0) {
                for (int i = 0; i < 8; i++) {
                    genes[i] = statisticsSystem.getGenes()[i] /
                            (float) statisticsSystem.getAliveAnimalsCount() / 8.0f;
                }
            }
            ui.plotHistogram("Genes", genes, 0, "", 0.0f, 1.0f, new Vec2(100, 100), 1);
        }

        if (ui.collapsingHeader("Add actors", 0)) {
            try {
                ui.inputInt("Amount", amount, 10, 1, 0);
            } catch (StringIndexOutOfBoundsException ignored) {
                amount.set(0);
            }
            Random random = new Random();

            addActorWidget.render(ui);
            World world = simulation.getWorld();
            switch (addActorWidget.getAddType()) {
                case AddActorWidget.ADD_ANIMAL:
                    for (int i = 0; i < amount.get(); i++) {
                        int x = random.nextInt(world.getWidth());
                        int y = random.nextInt(world.getHeight());
                        Animal animal = new Animal(world.getEpoch(), x, y, addActorWidget
                                .getEnergy(), addActorWidget.getGenes());
                        world.addActor(animal);
                    }
                    break;
                case AddActorWidget.ADD_BUSH:
                    for (int i = 0; i < amount.get(); i++) {
                        int x = random.nextInt(world.getWidth());
                        int y = random.nextInt(world.getHeight());
                        Bush animal = new Bush(x, y, addActorWidget.getEnergy());
                        world.addActor(animal);
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

                if (statisticsSystem != null) {
                    System.arraycopy(aliveHist, 1, aliveHist, 0, aliveHist.length - 1);
                    aliveHist[aliveHist.length - 1] = statisticsSystem.getAliveAnimalsCount();

                    System.arraycopy(childrenHist, 1, childrenHist, 0, childrenHist.length - 1);
                    childrenHist[childrenHist.length - 1] = statisticsSystem.getChildrenAverage();

                    System.arraycopy(bushHist, 1, bushHist, 0, bushHist.length - 1);
                    bushHist[bushHist.length - 1] = statisticsSystem.getPresentBushCount();

                    System.arraycopy(energyHist, 1, energyHist, 0, energyHist.length - 1);
                    energyHist[energyHist.length - 1] = statisticsSystem.getEnergyAverage();

                    System.arraycopy(ageHist, 1, ageHist, 0, ageHist.length - 1);
                    ageHist[ageHist.length - 1] = statisticsSystem.getAgeAverage();

                    System.arraycopy(deathAgeHist, 1, deathAgeHist, 0, deathAgeHist.length - 1);
                    deathAgeHist[deathAgeHist.length - 1] = statisticsSystem.getDeathAgeAverage();
                }
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
