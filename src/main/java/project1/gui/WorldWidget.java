package project1.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import glm_.vec2.Vec2;
import imgui.*;
import imgui.classes.DrawList;
import imgui.internal.sections.ButtonFlag;
import kotlin.reflect.KMutableProperty0;
import project1.actors.Animal;
import project1.actors.Bush;
import project1.visitors.WorldActorVisitorAdapter;
import project1.world.World;

import static project1.tick.SpawnSystem.*;
import static project1.world.World.SIZE_X;
import static project1.world.World.SIZE_Y;

public class WorldWidget implements Widget {
//    public static final int ANIMAL_COLOR = 0xff1111ee;
//    public static final int BUSH_COLOR = 0xff026e00;
//    public static final int LINE_COLOR = 0x22000000;
//    public static final int WORLD_COLOR = 0xff74e8de;
//    public static final int JUNGLE_COLOR = 0xff00bbaa;

    public static final int LINE_COLOR = 0x22000000;
    public static final int WORLD_COLOR = 0xffffffff;
    public static final int JUNGLE_COLOR = 0xffbbffbb;

    public Texture animalTexture;
    public Texture bushTexture;

    private World world;
    private int simulationIdx;

    private CellDetailsWidget detailsWidget;
    private SimulationWidget simulationWidget;
    private AddActorWidget addActorWidget;
    private float cellSize = 18;
    private Vec2 drag = new Vec2(0, 0);

    public WorldWidget(World world, int simulationIdx,
                       SimulationWidget simulationWidget,
                       AddActorWidget addActorWidget) {

        this.world = world;
        this.simulationIdx = simulationIdx;
        this.simulationWidget = simulationWidget;
        this.addActorWidget = addActorWidget;
        detailsWidget = new CellDetailsWidget(world, simulationIdx);

        animalTexture = new Texture(Gdx.files.internal("Animal2.png"));
        bushTexture = new Texture(Gdx.files.internal("Bush2.png"));
    }


    @Override
    public void render(ImGui ui) {
        ui.begin("World #" + simulationIdx, (KMutableProperty0<Boolean>) null,
                 WindowFlag.HorizontalScrollbar.i | WindowFlag.NoNavInputs.i);

        Vec2 size = new Vec2(SIZE_X, World.SIZE_Y).times(cellSize);

        ui.setWindowPos(new Vec2(30, 120), Cond.Once);
        ui.setWindowSize(size.plus(30, 60), Cond.Once);
        DrawList drawList = ui.getWindowDrawList();

        Vec2 pos = ui.getCursorScreenPos().plus(drag);

        drawList.addRectFilled(pos, pos.plus(size),
                               WORLD_COLOR, 0, 0);

        drawList.addRectFilled(pos.plus(new Vec2(JUNGLE_LEFT, 30 - JUNGLE_TOP)
                                                .times(cellSize)),
                               pos.plus(new Vec2(JUNGLE_RIGHT, 30 - JUNGLE_BOTTOM)
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
                drawAnimal(drawList, pos, animal.getX(), animal.getY());
            }
        });

        ui.invisibleButton("Click", size, ButtonFlag.MouseButtonLeft.getI() |
                ButtonFlag.MouseButtonMiddle.getI() | ButtonFlag.MouseButtonRight.getI());

        Vec2 cell = ui.getMousePos().minus(pos).div(cellSize);
        int x = (int) Math.floor(cell.getX());
        int y = (int) Math.floor(30 - cell.getY());

        if (x >= 0 && x < SIZE_X && y >= 0 && y < SIZE_Y) {
            if (ui.isItemActive()) {
                if (ui.isMouseClicked(MouseButton.Left, false)) {
                    switch (simulationWidget.getQuickAdd()) {
                        case AddActorWidget.ADD_ANIMAL:
                            world.addActor(new Animal(x, y,
                                                      addActorWidget.getEnergy(), addActorWidget
                                                              .getGenes()));
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
        for (int x = 0; x < SIZE_X; x++) {
            drawList.addLine(pos.plus(x * cellSize, 0),
                             pos.plus(x * cellSize, cellSize * SIZE_Y),
                             LINE_COLOR, 0.1f);

        }
        for (int y = 0; y < SIZE_Y; y++) {
            drawList.addLine(pos.plus(0, y * cellSize),
                             pos.plus(SIZE_X * cellSize, y * cellSize),
                             LINE_COLOR, 0.1f);

        }
    }

    public void drawBush(DrawList drawList, Vec2 pos, int x, int y) {
        drawTexture(drawList, pos, x, y, bushTexture);
//        y = 29 - y;
//
//        pos = pos.plus(new Vec2(x, y).times(cellSize));
//        drawList.addTriangleFilled(pos.plus(0, cellSize),
//                                   pos.plus(cellSize, cellSize),
//                                   pos.plus(cellSize / 2.0f, 0),
//                                   BUSH_COLOR);
//
//        drawList.addTriangleFilled(pos.plus(0, cellSize / 2.0f),
//                                   pos.plus(cellSize, cellSize / 2.0f),
//                                   pos.plus(cellSize / 2.0f, 0),
//                                   BUSH_COLOR);

    }

    public void drawAnimal(DrawList drawList, Vec2 pos, int x, int y) {
        drawTexture(drawList, pos, x, y, animalTexture);
//        y = 29 - y;
//        pos = pos.plus(new Vec2(x, y).times(cellSize));
//
//        drawList.addCircleFilled(pos.plus(cellSize / 2.0f, 2.0f * cellSize / 8.0f),
//                                 cellSize / 6.0f, ANIMAL_COLOR, 30);
//
//        drawList.addCircleFilled(pos.plus(cellSize / 2.0f, cellSize / 2.0f),
//                                 cellSize / 6.0f, ANIMAL_COLOR, 30);
//
//
//        drawList.addLine(pos.plus(6.0f * cellSize / 8.0f, cellSize / 8.0f),
//                         pos.plus(2.0f * cellSize / 8.0f, 7.0f * cellSize / 8.0f),
//                         ANIMAL_COLOR, 2.0f);
//
//        drawList.addLine(pos.plus(2.0f * cellSize / 8.0f, cellSize / 8.0f),
//                         pos.plus(6.0f * cellSize / 8.0f, 7.0f * cellSize / 8.0f),
//                         ANIMAL_COLOR, 2.0f);
    }

    public void drawTexture(DrawList drawList, Vec2 pos, int x, int y, Texture texture) {
        y = 29 - y;
        pos = pos.plus(new Vec2(x, y).times(cellSize));

        drawList.addImage(texture.getTextureObjectHandle(),
                          pos.plus(0, 0),
                          pos.plus(cellSize, cellSize),
                          new Vec2(0, 0), new Vec2(1, 1), 0xffffffff);
    }

    public float getCellSize() {
        return cellSize;
    }

    public void setCellSize(float cellSize) {
        this.cellSize = cellSize;
    }
}
