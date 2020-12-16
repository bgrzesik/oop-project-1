package project1;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.*;
import glm_.vec2.Vec2i;
import imgui.ImGui;
import imgui.classes.Context;
import imgui.impl.gl.ImplGL3;
import imgui.impl.glfw.ImplGlfw;
import project1.data.SimulationConfig;
import project1.gui.ImGuiLibGdxTranslator;
import project1.gui.SimulationConfigWidget;
import project1.gui.SimulationWidget;
import uno.glfw.GlfwWindow;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

public class SimApplication extends ApplicationAdapter {

    private GlfwWindow window;
    private ImplGL3 implGL3;
    private ImplGlfw implGlfw;
    private ImGui ui = ImGui.INSTANCE;
    private Context context;

    private List<SimulationWidget> simulationWidgets = new ArrayList<>();
    private SimulationConfigWidget configWidget = new SimulationConfigWidget();

    private int simulationIdx = 0;


    @Override
    public void create() {
        Lwjgl3Window handle = ((Lwjgl3Graphics) Gdx.graphics).getWindow();

        this.window = GlfwWindow.from(handle.getWindowHandle());
        this.context = new Context();
        this.implGlfw = ImplGlfw.Companion.initForOpengl(this.window, false, null);
        this.implGL3 = new ImplGL3();
        this.ui.styleColorsDark(null);

        Gdx.input.setInputProcessor(new ImGuiLibGdxTranslator());
        updateCallbacks();
    }

    @Override
    public void resume() {
        updateCallbacks();
    }

    private void updateCallbacks() {
        long windowHandle = ((Lwjgl3Graphics) Gdx.graphics).getWindow().getWindowHandle();
        ((DefaultLwjgl3Input) Gdx.input).windowHandleChanged(windowHandle);
    }

    @Override
    public void render() {
        updateCallbacks();

        implGL3.newFrame();
        implGlfw.newFrame();
        ui.newFrame();

        if (ui.beginMainMenuBar()) {
            if (ui.beginMenu("Simulation", true)) {
                if (ui.menuItem("New", "", false, true)) {
                    configWidget.open();
                }

                if (ui.beginMenu("Kill", simulationWidgets.size() > 0)) {
                    simulationWidgets.removeIf(simulationWidget -> ui
                            .menuItem("Simulation #" + simulationWidget .getSimulationIdx(),
                                      "", false, true));

                    ui.endMenu();
                }

                if (ui.menuItem("Exit", "", false, true)) {
                    Gdx.app.exit();
                }

                ui.endMenu();
            }

            ui.endMainMenuBar();
        }

        for (SimulationWidget simulationWidget : simulationWidgets) {
            simulationWidget.render(ui);
        }

        configWidget.render(ui);
        SimulationConfig config = configWidget.popConfig();
        if (config != null) {
            simulationWidgets.add(new SimulationWidget(simulationIdx, Simulation.create(config)));
            simulationIdx += 1;
        }

        ui.render();

        Vec2i framebufferSize = window.getFramebufferSize();
        Gdx.gl.glViewport(0, 0, framebufferSize.getX(), framebufferSize.getY());
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        implGL3.renderDrawData(ui.getDrawData());
    }


    @Override
    public void dispose() {
        super.dispose();
        context.shutdown();
        implGL3.shutdown();
        implGlfw.shutdown();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.useOpenGL3(true, 3, 3);
        config.setWindowedMode(1900, 900);
        config.setIdleFPS(60);
        config.setForegroundFPS(60);
        config.setWindowPosition(3000, 100);

        SimApplication sim = new SimApplication();

        new Lwjgl3Application(sim, config);
    }

}
