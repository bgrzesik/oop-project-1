package project1.gui;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import glm_.vec2.Vec2;
import imgui.*;
import kotlin.reflect.KMutableProperty0;
import project1.data.SimulationConfig;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SimulationConfigWidget implements Widget {
    private final KMutableProperty0<Integer> worldWidth = new MutableProperty0<>(100);
    private final KMutableProperty0<Integer> worldHeight = new MutableProperty0<>(30);
    private final KMutableProperty0<Integer> jungleWidth = new MutableProperty0<>(10);
    private final KMutableProperty0<Integer> jungleHeight = new MutableProperty0<>(10);

    private final KMutableProperty0<Integer> minimalBreedEnergy = new MutableProperty0<>(20);
    private final KMutableProperty0<Integer> spawnBushEnergy = new MutableProperty0<>(60);
    private final KMutableProperty0<Integer> spawnInJungle = new MutableProperty0<>(1);
    private final KMutableProperty0<Integer> spawnOutsideJungle = new MutableProperty0<>(1);
    private final KMutableProperty0<Integer> moveEnergy = new MutableProperty0<>(1);
    private final KMutableProperty0<Float> parentEnergyPart = new MutableProperty0<>(0.25f);

    private final KMutableProperty0<Boolean> spawnSystemOn = new MutableProperty0<>(true);
    private final KMutableProperty0<Boolean> moveSystemOn = new MutableProperty0<>(true);
    private final KMutableProperty0<Boolean> deathSystemOn = new MutableProperty0<>(true);
    private final KMutableProperty0<Boolean> statisticsSystemOn = new MutableProperty0<>(true);
    private final KMutableProperty0<Boolean> breedingSystemOn = new MutableProperty0<>(true);
    private final KMutableProperty0<Boolean> feedingSystemOn = new MutableProperty0<>(true);

    private final KMutableProperty0<Integer> selectedConfiguration = new MutableProperty0<>(0);
    private final byte[] configurationNameBuf = new byte[255];

    public static final String ID = "Simulation configuration";
    private boolean opened = false;
    private boolean calledOpen = false;

    private SimulationConfig pendingConfig = null;


    @Override
    public void render(ImGui ui) {
        if (!opened) {
            return;
        }

        if (ui.begin("This a bug and popup needs parent window", (KMutableProperty0<Boolean>) null, WindowFlag.NoDecoration.i)) {
            ui.setWindowPos(new Vec2(Integer.MAX_VALUE, Integer.MAX_VALUE), Cond.Always);
            ui.setWindowSize(new Vec2(0, 0), Cond.Always);

            if (!calledOpen) {
                ui.openPopup(ID, PopupFlag.None.i);
                calledOpen = true;
            }

            if (ui.beginPopupModal(ID, (KMutableProperty0<Boolean>) null, WindowFlag.AlwaysAutoResize.i)) {
                if (ui.collapsingHeader("Configurations", 0)) {
                    FileHandle root = Gdx.files.getFileHandle(".", Files.FileType.Local);
                    FileHandle[] list = root.list(".conf.json");

                    List<String> names = Arrays.stream(list)
                                               .map(FileHandle::nameWithoutExtension)
                                               .filter(e -> e.endsWith(".conf"))
                                               .map(e -> e.substring(0, e.length() - 5))
                                               .collect(Collectors.toList());
                    names.add(0, "<New>");
                    names.add(1, "<Default>");

                    if (ui.combo("Configuration", selectedConfiguration, names, 10)) {
                        if (selectedConfiguration.get() == 1) {
                            Arrays.fill(configurationNameBuf, (byte) 0);
                        } else if (selectedConfiguration.get() != 0) {
                            byte[] bytes = names.get(selectedConfiguration.get()).getBytes();
                            Arrays.fill(configurationNameBuf, (byte) 0);
                            System.arraycopy(bytes, 0, configurationNameBuf, 0, bytes.length);
                        }
                    }

                    ui.inputText("Name###confName", configurationNameBuf, 0, null, null);

                    if (ui.button("Load", new Vec2(0, 0))) {
                        String name = new String(configurationNameBuf).trim();
                        if (name.length() == 0) {
                            loadConfig(new SimulationConfig.Builder()
                                               .loadDefault()
                                               .build());
                        } else {
                            try {
                                FileHandle child = root.child(name + ".conf.json");
                                if (child.exists()) {
                                    loadConfig(new SimulationConfig.Builder()
                                                       .fromFile(child)
                                                       .build());
                                }
                            } catch (Exception ignored) {
                            }
                        }
                    }

                    ui.sameLine(0, 5);

                    if (ui.button("Save", new Vec2(0, 0))) {
                        String name = new String(configurationNameBuf).trim();
                        try {
                            FileHandle child = root.child(name + ".conf.json");
                            this.buildConfig().saveToFile(child);
                        } catch (Exception ignored) {
                        }
                    }

                }

                ui.text("Parameters");

                inputInt(ui, "World width", worldWidth);
                inputInt(ui, "World height", worldHeight);

                inputInt(ui, "Jungle width", jungleWidth);
                inputInt(ui, "Jungle height", jungleHeight);

                inputInt(ui, "Minimal energy required to breed", minimalBreedEnergy);
                inputInt(ui, "Energy that bush spawns with", spawnBushEnergy);
                inputInt(ui, "Amount of bushes that spawn in jungle every cycle", spawnInJungle);
                inputInt(ui, "Amount of bushes that spawn outside jungle every cycle", spawnOutsideJungle);
                inputInt(ui, "Energy that animal consume when moving", moveEnergy);

                try {
                    ui.inputFloat("A part of energy that parent gives to child", parentEnergyPart, 0.01f, 0.1f, "%.3f", 0);
                } catch (StringIndexOutOfBoundsException ignored) {
                    parentEnergyPart.set(0.0f);
                }

                ui.separator();

                ui.text("Systems");
                ui.checkbox("Spawn", spawnSystemOn);
                ui.sameLine(0, 5);
                ui.checkbox("Move", moveSystemOn);
                ui.sameLine(0, 5);
                ui.checkbox("Death", deathSystemOn);
                ui.sameLine(0, 5);
                ui.checkbox("Statistics", statisticsSystemOn);
                ui.sameLine(0, 5);
                ui.checkbox("Breed", breedingSystemOn);
                ui.sameLine(0, 5);
                ui.checkbox("Feed", feedingSystemOn);

                ui.separator();

                if (ui.button("Start", new Vec2(0, 0))) {
                    ui.closeCurrentPopup();

                    this.pendingConfig = buildConfig();
                }

                ui.sameLine(0, 5);

                if (ui.button("Cancel", new Vec2(0, 0))) {
                    ui.closeCurrentPopup();
                }

                ui.endPopup();
            }
            ui.end();
        }
    }


    private void inputInt(ImGui ui, String label, KMutableProperty0<Integer> property) {
        try {
            ui.inputInt(label, property, 1, 10, 0);
        } catch (StringIndexOutOfBoundsException ignored) {
            property.set(0);
        }
    }

    public void open() {
        opened = true;
        calledOpen = false;
    }

    public SimulationConfig popConfig() {
        SimulationConfig pendingConfig = this.pendingConfig;
        this.pendingConfig = null;
        return pendingConfig;
    }

    private void loadConfig(SimulationConfig config) {
        worldWidth.set(config.getWorldWidth());
        worldHeight.set(config.getWorldHeight());
        jungleWidth.set(config.getJungleWidth());
        jungleHeight.set(config.getJungleHeight());
        minimalBreedEnergy.set(config.getMinimalBreedEnergy());
        spawnBushEnergy.set(config.getSpawnBushEnergy());
        spawnInJungle.set(config.getSpawnInJungle());
        spawnOutsideJungle.set(config.getSpawnOutsideJungle());
        moveEnergy.set(config.getMoveEnergy());
        parentEnergyPart.set(config.getParentEnergyPart());
        spawnSystemOn.set(config.isSpawnSystemOn());
        moveSystemOn.set(config.isMoveSystemOn());
        deathSystemOn.set(config.isDeathSystemOn());
        statisticsSystemOn.set(config.isSpawnSystemOn());
        breedingSystemOn.set(config.isBreedingSystemOn());
        feedingSystemOn.set(config.isFeedingSystemOn());
    }

    private SimulationConfig buildConfig() {
        return new SimulationConfig.Builder()
                .setWorldWidth(worldWidth.get())
                .setWorldHeight(worldHeight.get())
                .setJungleWidth(jungleWidth.get())
                .setJungleHeight(jungleHeight.get())

                .setMinimalBreedEnergy(minimalBreedEnergy.get())
                .setSpawnBushEnergy(spawnBushEnergy.get())
                .setSpawnInJungle(spawnInJungle.get())
                .setSpawnOutsideJungle(spawnOutsideJungle.get())
                .setMoveEnergy(moveEnergy.get())
                .setParentEnergyPart(parentEnergyPart.get())

                .setSpawnSystemOn(spawnSystemOn.get())
                .setMoveSystemOn(moveSystemOn.get())
                .setDeathSystemOn(deathSystemOn.get())
                .setStatisticsSystemOn(spawnSystemOn.get())
                .setBreedingSystemOn(breedingSystemOn.get())
                .setFeedingSystemOn(feedingSystemOn.get())
                .build();
    }
}
