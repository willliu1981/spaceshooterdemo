package idv.kuan.game.spaceshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class MyInput extends Character2DInput {
    private PlayerShip ship;
    private final int WORLD_WIDTH;
    private final int WORLD_HEIGHT;


    public MyInput(PlayerShip ship, int worldWidth, int worldHeight) {
        this.ship = ship;
        this.WORLD_WIDTH = worldWidth;
        this.WORLD_HEIGHT = worldHeight;
    }


    @Override
    protected void update(float delta) {
        float movementDistance = ship.movementSpeed * delta;

        if (input(Direction.LEFT)) {
            ship.translate(Math.min(ship.boundingBox.x, movementDistance),
                    Input.Keys.LEFT);
        }

        if (input(Direction.RIGHT)) {
            ship.translate(Math.min(WORLD_WIDTH - ship.boundingBox.x - ship.boundingBox.width, movementDistance),
                    Input.Keys.RIGHT);
        }

        if (input(Direction.UP)) {
            ship.translate(Math.min(WORLD_HEIGHT / 2 - ship.boundingBox.y - ship.boundingBox.height, movementDistance),
                    Input.Keys.UP);
        }

        if (input(Direction.DOWN)) {
            ship.translate(Math.min(ship.boundingBox.y, movementDistance),
                    Input.Keys.DOWN);
        }


    }


}
