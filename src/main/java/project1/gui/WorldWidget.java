package project1.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import glm_.vec2.Vec2;
import imgui.*;
import imgui.classes.DrawList;
import imgui.internal.sections.ButtonFlag;
import kotlin.reflect.KMutableProperty0;
import project1.Simulation;
import project1.actors.Animal;
import project1.actors.Bush;
import project1.data.SimulationConfig;
import project1.visitors.WorldActorVisitorAdapter;
import project1.world.World;

public class WorldWidget implements Widget {
    public static final int LINE_COLOR = 0x22000000;
    public static final int WORLD_COLOR = 0xffbbffbb;
    public static final int JUNGLE_COLOR = 0xff77ff77;
    public static final String ANIMAL_TEXTURE = "Animal.png";
    public static final String BUSH_TEXTURE = "Bush.png";


    public Texture animalTexture;
    public Texture bushTexture;

    private final World world;
    private final int simulationIdx;
    private final SimulationConfig config;

    private final CellDetailsWidget detailsWidget;
    private final SimulationWidget simulationWidget;
    private final AddActorWidget addActorWidget;
    private float cellSize = 18;
    private Vec2 drag = new Vec2(0, 0);

    public WorldWidget(Simulation simulation, int simulationIdx,
                       SimulationWidget simulationWidget,
                       AddActorWidget addActorWidget) {

        this.config = simulation.getConfig();
        this.world = simulation.getWorld();
        this.simulationIdx = simulationIdx;
        this.simulationWidget = simulationWidget;
        this.addActorWidget = addActorWidget;

        this.detailsWidget = new CellDetailsWidget(world, simulationIdx);

        this.animalTexture = new Texture(Gdx.files.internal(ANIMAL_TEXTURE));
        this.bushTexture = new Texture(Gdx.files.internal(BUSH_TEXTURE));
    }


    @Override
    public void render(ImGui ui) {
        ui.begin("World #" + simulationIdx, (KMutableProperty0<Boolean>) null,
                 WindowFlag.HorizontalScrollbar.i | WindowFlag.NoNavInputs.i);

        Vec2 size = new Vec2(world.getWidth(), world.getHeight()).times(cellSize);

        ui.setWindowSize(size.plus(30, 60), Cond.Once);
        DrawList drawList = ui.getWindowDrawList();

        Vec2 pos = ui.getCursorScreenPos().plus(drag);

        drawList.addRectFilled(pos, pos.plus(size),
                               WORLD_COLOR, 0, 0);

        int left = config.getWorldWidth() / 2 - config.getJungleWidth() / 2;
        int bottom = config.getWorldHeight() / 2 - config.getJungleHeight() / 2;

        drawList.addRectFilled(pos.plus(new Vec2(left, bottom)
                                                .times(cellSize)),
                               pos.plus(new Vec2(left + config.getJungleWidth(),
                                                 bottom + config.getJungleHeight())
                                                .times(cellSize)),
                               JUNGLE_COLOR, 0, 0);

        if (simulationWidget.showLines()) {
            drawGridLines(drawList, pos);
        }

        this.world.accept(new WorldActorVisitorAdapter() {
            @Override
            public void visitBush(Bush bush) {
                drawBush(drawList, pos, bush.getX(), bush.getY());
            }
        });

        this.world.accept(new WorldActorVisitorAdapter() {
            @Override
            public void visitAnimal(Animal animal) {
                boolean highlighted = animal.getGenome()
                                            .equals(simulationWidget.getSelectedGenome());
                drawAnimal(drawList, pos, animal.getX(), animal.getY(), highlighted);
            }
        });

        ui.invisibleButton("Click", size, ButtonFlag.MouseButtonLeft.getI() |
                ButtonFlag.MouseButtonMiddle.getI() | ButtonFlag.MouseButtonRight.getI());

        Vec2 cell = ui.getMousePos().minus(pos).div(cellSize);
        int x = (int) Math.floor(cell.getX());
        int y = (int) Math.floor(world.getHeight() - cell.getY());

        if (x >= 0 && x < world.getWidth() && y >= 0 && y < world.getHeight()) {
            if (ui.isItemActive()) {
                if (ui.isMouseClicked(MouseButton.Left, false)) {
                    switch (simulationWidget.getQuickAdd()) {
                        case AddActorWidget.ADD_ANIMAL:
                            world.addActor(new Animal(world.getEpoch(), x, y,
                                                      addActorWidget.getEnergy(),
                                                      addActorWidget.getGenes()));
                            break;
                        case AddActorWidget.ADD_BUSH:
                            world.addActor(new Bush(x, y,
                                                    addActorWidget.getEnergy()));
                            break;
                    }
                }

                if (ui.isMouseDragging(MouseButton.Middle, 1.0f)) {
                    Vec2 mouseDelta = ui.getIo().getMouseDelta();
                    drag = drag.plus(mouseDelta);
                }

                if (ui.isMouseClicked(MouseButton.Right, false)) {
                    detailsWidget.setCell(x, y);
                }
            }
        }

        if (ui.isItemHovered(HoveredFlag.RootWindow)) {
            float mouseWheel = ui.getIo().getMouseWheel();
            cellSize += mouseWheel;
        }

        ui.end();

        detailsWidget.render(ui);
    }

    private void drawGridLines(DrawList drawList, Vec2 pos) {
        for (int x = 0; x < world.getWidth(); x++) {
            drawList.addLine(pos.plus(x * cellSize, 0),
                             pos.plus(x * cellSize, cellSize * world.getHeight()),
                             LINE_COLOR, 0.1f);

        }
        for (int y = 0; y < world.getHeight(); y++) {
            drawList.addLine(pos.plus(0, y * cellSize),
                             pos.plus(world.getWidth() * cellSize, y * cellSize),
                             LINE_COLOR, 0.1f);

        }
    }

    public void drawBush(DrawList drawList, Vec2 pos, int x, int y) {
        drawTexture(drawList, pos, x, y, bushTexture, 0xffffffff);

    }

    public void drawAnimal(DrawList drawList, Vec2 pos, int x, int y, boolean highlighted) {
        int color = highlighted ? 0xff0000ff : 0xffffffff;
        drawTexture(drawList, pos, x, y, animalTexture, color);
    }

    public void drawTexture(DrawList drawList, Vec2 pos, int x, int y, Texture texture, int color) {
        y = world.getHeight() - y - 1;
        pos = pos.plus(new Vec2(x, y).times(cellSize));

        drawList.addImage(texture.getTextureObjectHandle(),
                          pos.plus(0, 0),
                          pos.plus(cellSize, cellSize),
                          new Vec2(0, 0), new Vec2(1, 1), color);
    }

}
