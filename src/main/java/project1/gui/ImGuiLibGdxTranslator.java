package project1.gui;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import glm_.vec2.Vec2d;
import imgui.impl.glfw.ImplGlfw;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

public class ImGuiLibGdxTranslator extends InputAdapter {
    private static final Map<Integer, Integer> TRANSLATION = new HashMap<>();

    static {
        TRANSLATION.put(Input.Keys.TAB, GLFW.GLFW_KEY_TAB);
        TRANSLATION.put(Input.Keys.LEFT, GLFW.GLFW_KEY_LEFT);
        TRANSLATION.put(Input.Keys.RIGHT, GLFW.GLFW_KEY_RIGHT);
        TRANSLATION.put(Input.Keys.UP, GLFW.GLFW_KEY_UP);
        TRANSLATION.put(Input.Keys.DOWN, GLFW.GLFW_KEY_DOWN);
        TRANSLATION.put(Input.Keys.PAGE_UP, GLFW.GLFW_KEY_PAGE_UP);
        TRANSLATION.put(Input.Keys.PAGE_DOWN, GLFW.GLFW_KEY_PAGE_DOWN);
        TRANSLATION.put(Input.Keys.HOME, GLFW.GLFW_KEY_HOME);
        TRANSLATION.put(Input.Keys.END, GLFW.GLFW_KEY_END);
        TRANSLATION.put(Input.Keys.BACKSPACE, GLFW.GLFW_KEY_BACKSPACE);
        TRANSLATION.put(Input.Keys.ENTER, GLFW.GLFW_KEY_ENTER);
        TRANSLATION.put(Input.Keys.SPACE, GLFW.GLFW_KEY_SPACE);
        TRANSLATION.put(Input.Keys.ESCAPE, GLFW.GLFW_KEY_ESCAPE);
        TRANSLATION.put(Input.Keys.CONTROL_LEFT, GLFW.GLFW_KEY_LEFT_CONTROL);
        TRANSLATION.put(Input.Keys.CONTROL_RIGHT, GLFW.GLFW_KEY_RIGHT_CONTROL);
        TRANSLATION.put(Input.Keys.ALT_LEFT, GLFW.GLFW_KEY_LEFT_ALT);
        TRANSLATION.put(Input.Keys.ALT_RIGHT, GLFW.GLFW_KEY_RIGHT_ALT);
        TRANSLATION.put(Input.Keys.SHIFT_LEFT, GLFW.GLFW_KEY_LEFT_SHIFT);
        TRANSLATION.put(Input.Keys.SHIFT_RIGHT, GLFW.GLFW_KEY_RIGHT_SHIFT);
        TRANSLATION.put(Input.Keys.A, GLFW.GLFW_KEY_A);
        TRANSLATION.put(Input.Keys.C, GLFW.GLFW_KEY_C);
        TRANSLATION.put(Input.Keys.V, GLFW.GLFW_KEY_V);
        TRANSLATION.put(Input.Keys.X, GLFW.GLFW_KEY_X);
        TRANSLATION.put(Input.Keys.Y, GLFW.GLFW_KEY_Y);
        TRANSLATION.put(Input.Keys.Z, GLFW.GLFW_KEY_Z);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        ImplGlfw.Companion.getMouseButtonCallback()
                          .invoke(button, GLFW.GLFW_PRESS, 0);
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        ImplGlfw.Companion.getScrollCallback()
                          .invoke(new Vec2d(amountX, amountY));
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        ImplGlfw.Companion.getCharCallback()
                          .invoke((int) character);
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        Integer key = TRANSLATION.get(keycode);
        if (key != null) {
            ImplGlfw.Companion.getKeyCallback()
                              .invoke(key, 0, GLFW.GLFW_PRESS, 0);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        Integer key = TRANSLATION.get(keycode);
        if (key != null) {
            ImplGlfw.Companion.getKeyCallback()
                              .invoke(key, 0, GLFW.GLFW_RELEASE, 0);
        }
        return false;
    }
}
