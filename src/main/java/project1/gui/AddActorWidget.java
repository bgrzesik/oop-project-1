package project1.gui;

import glm_.vec2.Vec2;
import imgui.ImGui;
import imgui.MutableProperty0;
import project1.Direction;

import java.util.Arrays;
import java.util.Random;

public class AddActorWidget implements Widget {

    public static final int ADD_NONE = 0;
    public static final int ADD_ANIMAL = 1;
    public static final int ADD_BUSH = 2;

    private int addType = ADD_NONE;

    private MutableProperty0<Integer> energy = new MutableProperty0<>(10);

    private int[] genes = new int[32];

    public AddActorWidget() {
        Arrays.fill(genes, 0);
    }

    @Override
    public void render(ImGui ui) {
        try {
            ui.inputInt("Energy", energy, 1, 1, 0);
        } catch (StringIndexOutOfBoundsException ignored) {
            energy.set(0);
        }
        ui.columns(16, "Genes", false);
        for (int i = 0; i < 32; i++) {
            if (ui.smallButton(String.valueOf(genes[i]))) {
                genes[i] = (genes[i] + 1) % 8;
            }
            ui.pushID(i);
            ui.nextColumn();
        }
        ui.columns(1, "", false);

        if (ui.button("Random genes", new Vec2(0, 0))) {
            Random random = new Random();
            for (int i = 0; i < 32; i++) {
                genes[i] = random.nextInt(8);
            }

            Arrays.sort(genes);
        }

        addType = ADD_NONE;

        if (ui.button("Add animal", new Vec2(0, 0))) {
            addType = ADD_ANIMAL;
        }

        ui.sameLine(0, 5);

        if (ui.button("Add bush", new Vec2(0, 0))) {
            addType = ADD_BUSH;
        }
    }

    public int getEnergy() {
        return energy.get();
    }

    public int[] getGenes() {
        return genes;
    }

    public int getAddType() {
        return addType;
    }
}
