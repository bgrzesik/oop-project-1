package project1.gui;

import glm_.vec2.Vec2;
import imgui.*;
import kotlin.reflect.KMutableProperty0;
import project1.data.SimulationConfig;

public class SimulationConfigWidget implements Widget {
    private final KMutableProperty0<Integer> minimalBreedEnergy = new MutableProperty0<>(20);
    private final KMutableProperty0<Integer> spawnBushEnergy = new MutableProperty0<>(10);
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
                ui.text("Parameters");

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

                    this.pendingConfig = new SimulationConfig.Builder()
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
}
