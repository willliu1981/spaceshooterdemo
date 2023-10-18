package idv.kuan.game.spaceshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class Character2DInput implements InputProcessor {
    protected static enum Direction {
        LEFT, RIGHT, UP, DOWN

    }

    public Character2DInput() {
        keys.put(Input.Keys.LEFT, Direction.LEFT);
        keys.put(Input.Keys.A, Direction.LEFT);

        keys.put(Input.Keys.RIGHT, Direction.RIGHT);
        keys.put(Input.Keys.D, Direction.RIGHT);

        keys.put(Input.Keys.UP, Direction.UP);
        keys.put(Input.Keys.W, Direction.UP);

        keys.put(Input.Keys.DOWN, Direction.DOWN);
        keys.put(Input.Keys.S, Direction.DOWN);
    }

    private Map<Integer, Direction> keys = new HashMap<>();
    private Set<Direction> currentKeypress = new HashSet();

    protected boolean input(Direction direction) {


        return currentKeypress.contains(direction);
    }

    protected abstract void update(float delta);


    @Override
    public boolean keyDown(int keycode) {
        currentKeypress.add(keys.get(keycode));


        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        currentKeypress.remove(keys.get(keycode));

        return true;
    }


    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }


}
