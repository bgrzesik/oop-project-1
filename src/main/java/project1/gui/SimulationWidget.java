package project1.gui;

import com.badlogic.gdx.Gdx;
import glm_.vec2.Vec2;
import imgui.*;
import imgui.internal.sections.ButtonFlag;
import kotlin.reflect.KMutableProperty0;
import project1.Simulation;
import project1.actors.Animal;
import project1.actors.Bush;
import project1.data.GenomeFrequency;
import project1.system.Statistics;
import project1.system.StatisticsSystem;
import project1.world.World;

import java.util.Map;
import java.util.Random;
import java.util.TreeSet;

public class SimulationWidget implements Widget {
    public static final float MIN_TIME_SPEED = 1.0f / 16.0f;
    public static final float MAX_TIME_SPEED = 64;

    public static final int PENDING_PAUSE = 0;
    public static final int PENDING_INFINITE = -1;

    private Simulation simulation;
    private WorldWidget worldWidget;
    private AddActorWidget addActorWidget = new AddActorWidget();
    private KMutableProperty0<Integer> amount = new MutableProperty0<>(100);
    private KMutableProperty0<Integer> cycles = new MutableProperty0<>(10);
    private float time = 0;
    private float speed = 1.0f;

    private int pendingCycles = 0;
    private boolean[] showLines = new boolean[]{false};
    private int simulationIdx;

    private int quickAdd = AddActorWidget.ADD_NONE;

    private float[] aliveHist = new float[30];
    private float[] childrenHist = new float[30];
    private float[] bushHist = new float[30];
    private float[] energyHist = new float[30];
    private float[] ageHist = new float[30];
    private float[] deathAgeHist = new float[30];

    private String selectedGenome = null;

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

        Statistics statistics = statisticsSystem != null ?
                statisticsSystem.getCurrentStatistics() : null;

        ui.begin("Simulation #" + simulationIdx, new boolean[]{false}, 0);
        ui.setWindowSize(new Vec2(700, 950), Cond.Once);

        ui.checkbox("Show lines", showLines);

        if (ui.collapsingHeader("Time control", 0)) {
            ui.text("Speed: %.2f", speed);
            if (ui.buttonEx("<<", new Vec2(0, 0), speed > MIN_TIME_SPEED ? 0 : ButtonFlag.Disabled
                    .getI())) {
                speed /= 2;
            }
            ui.sameLine(0, 5);
            if (ui.button("Normal", new Vec2(0, 0))) {
                speed = 1.0f;
            }
            ui.sameLine(0, 5);
            if (ui.buttonEx(">>", new Vec2(0, 0), speed < MAX_TIME_SPEED ? 0 : ButtonFlag.Disabled
                    .getI())) {
                speed *= 2;
            }

            switch (pendingCycles) {
                case PENDING_INFINITE:
                    if (ui.button("Pause", new Vec2(0, 0))) {
                        pendingCycles = PENDING_PAUSE;
                    }
                    break;
                case PENDING_PAUSE:
                    if (ui.button("Start", new Vec2(0, 0))) {
                        pendingCycles = PENDING_INFINITE;
                    }
                    break;
                default:
                    ui.buttonEx("Running", new Vec2(0, 0), ButtonFlag.Disabled.getI());
                    break;
            }

            ui.sameLine(0, 5);
            if (ui.button("Tick", new Vec2(0, 0))) {
                simulation.tick();
            }

            ui.inputInt("", cycles, 1, 10, 0);
            ui.sameLine(0, 5);
            if (ui.button("Run Cycles", new Vec2(0, 0))) {
                pendingCycles = cycles.get();
            }
        }

        if (ui.collapsingHeader("Statistics", 0) && statisticsSystem != null) {

            Vec2 graphSize = new Vec2(300, 100);

            int aliveScaleMax = 3, childrenScaleMax = 3, bushScaleMax = 3, energyScaleMax = 3, deathAgeMax = 3;
            for (int i = 0; i < aliveHist.length; i++) {
                aliveScaleMax = (int) Math.max(aliveScaleMax, aliveHist[i]);
                childrenScaleMax = (int) Math.max(childrenScaleMax, childrenHist[i]);
                bushScaleMax = (int) Math.max(bushScaleMax, bushHist[i]);
                energyScaleMax = (int) Math.max(energyScaleMax, energyHist[i]);
                deathAgeMax = (int) Math.max(deathAgeMax, deathAgeHist[i]);
            }

            String text;

            text = String.format("Alive: %d", statistics.getAliveAnimalsCount());
            ui.plotLines(text, aliveHist, 0, "", 0, aliveScaleMax, graphSize, 1);

            text = String.format("Bush: %d", statistics.getPresentBushCount());
            ui.plotLines(text, bushHist, 0, "", 0, bushScaleMax, graphSize, 1);

            text = String.format("Children: %.2f", statistics.getChildrenAverage());
            ui.plotLines(text, childrenHist, 0, "", 0, childrenScaleMax, graphSize, 1);

            text = String.format("Age: %.2f", statistics.getAgeAverage());
            ui.plotLines(text, ageHist, 0, "", 40, childrenScaleMax, graphSize, 1);

            text = String.format("Energy: %.2f", statistics.getEnergyAverage());
            ui.plotLines(text, energyHist, 0, "", 0, energyScaleMax, graphSize, 1);

            text = String.format("Death age: %.2f", statistics.getDeathAgeAverage());
            ui.plotLines(text, deathAgeHist, 0, "", 0, deathAgeMax, graphSize, 1);

            ui.text("Epoch: %d", simulation.getWorld().getEpoch());

            ui.text("Dead: %d", statistics.getDeadCount());

            ui.text("Bushes total: %d", statistics.getTotalBushCount());

            float[] genes = new float[8];
            if (statistics.getAliveAnimalsCount() != 0) {
                for (int i = 0; i < 8; i++) {
                    genes[i] = statistics.getGenes()[i] /
                            (float) statistics.getAliveAnimalsCount() / 8.0f;
                }
            }
            ui.plotHistogram("Genes", genes, 0, "", 0.0f, 1.0f, new Vec2(100, 100), 1);

            if (ui.collapsingHeader("Genomes", 0)) {
                ui.columns(2, "", true);

                Map<String, GenomeFrequency> genomes = statistics.getGenomeFrequency();
                TreeSet<GenomeFrequency> frequencies = new TreeSet<>(genomes.values());

                for (GenomeFrequency frequency : frequencies) {
                    boolean selected = frequency.getGenome().equals(selectedGenome);

                    if (ui.selectable(frequency.getGenome(), selected, 0, new Vec2(0, 0))) {
                        if (frequency.getGenome().equals(selectedGenome)) {
                            selectedGenome = null;
                        } else {
                            selectedGenome = frequency.getGenome();
                        }
                    }

                    ui.nextColumn();
                    ui.text("%d", frequency.getFrequency());
                    ui.nextColumn();
                }

                ui.columns(1, "", false);
            }
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

        if (pendingCycles != PENDING_PAUSE) {
            time += Gdx.graphics.getDeltaTime();
            if (time > 1.0f / speed) {
                this.simulation.tick();
                time = 0.0f;

                if (pendingCycles != PENDING_INFINITE) {
                    pendingCycles--;
                }

                if (statisticsSystem != null) {
                    System.arraycopy(aliveHist, 1, aliveHist, 0, aliveHist.length - 1);
                    aliveHist[aliveHist.length - 1] = statistics.getAliveAnimalsCount();

                    System.arraycopy(childrenHist, 1, childrenHist, 0, childrenHist.length - 1);
                    childrenHist[childrenHist.length - 1] = statistics.getChildrenAverage();

                    System.arraycopy(bushHist, 1, bushHist, 0, bushHist.length - 1);
                    bushHist[bushHist.length - 1] = statistics.getPresentBushCount();

                    System.arraycopy(energyHist, 1, energyHist, 0, energyHist.length - 1);
                    energyHist[energyHist.length - 1] = statistics.getEnergyAverage();

                    System.arraycopy(ageHist, 1, ageHist, 0, ageHist.length - 1);
                    ageHist[ageHist.length - 1] = statistics.getAgeAverage();

                    System.arraycopy(deathAgeHist, 1, deathAgeHist, 0, deathAgeHist.length - 1);
                    deathAgeHist[deathAgeHist.length - 1] = statistics.getDeathAgeAverage();
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

    public String getSelectedGenome() {
        return selectedGenome;
    }

    public Simulation getSimulation() {
        return simulation;
    }
}
