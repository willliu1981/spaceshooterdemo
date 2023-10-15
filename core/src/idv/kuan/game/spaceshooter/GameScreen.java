package idv.kuan.game.spaceshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.LinkedList;
import java.util.ListIterator;

public class GameScreen implements Screen {

    //screen
    private Camera camera;
    private Viewport viewport;

    //graphics
    private SpriteBatch batch;
    private TextureAtlas textureAtlas;
    private Texture background;
    private TextureRegion[] backgrounds;

    private TextureRegion playerShipTxtr, enemyShipTxtr, playerShipShieldTxtr, enemyShipShieldTxtr,
            playerShipLaserTxtr, enemyShipLaserTxtr;


    //world parameters
    private final int WORLD_WIDTH = 72;
    private final int WORLD_HEIGHT = 128;


    //timing
    private int backgroungOffset;
    private float[] backgroungOffsets;
    private float backgroundMaxScrollingSpeed;

    //game objects
    private Ship playerShip, enemyShip;
    private LinkedList<Laser> playerLserList;
    private LinkedList<Laser> enemyLserList;


    public GameScreen() {
        camera = new OrthographicCamera();
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        //set up pictures
        textureAtlas = new TextureAtlas("spaceshooter.pack");

        //*
        background = new Texture("space01.jpg");
        backgroungOffset = 0;


        //*/

        //*
        backgrounds = new TextureRegion[4];
        backgrounds[0] = textureAtlas.findRegion("space01");
        backgrounds[1] = textureAtlas.findRegion("Starspace01b");
        backgrounds[2] = textureAtlas.findRegion("S2b");
        backgrounds[3] = textureAtlas.findRegion("S3b");

        //ship
        playerShipTxtr = textureAtlas.findRegion("ship_sidesA");
        enemyShipTxtr = textureAtlas.findRegion("enemy_A");
        enemyShipTxtr.flip(false, true);
        //enemyShipTxtr.flip(false,true);

        //shield
        playerShipShieldTxtr = textureAtlas.findRegion("meteor_smallb");
        enemyShipShieldTxtr = textureAtlas.findRegion("meteor_largeb");

        //laser
        playerShipLaserTxtr = textureAtlas.findRegion("effect_yellow");
        enemyShipLaserTxtr = textureAtlas.findRegion("effect_purple");
        enemyShipLaserTxtr.flip(false, true);


        backgroungOffsets = new float[4];

        //*/

        //set up game objects
        playerShip = new PlayerShip(WORLD_WIDTH / 2, WORLD_HEIGHT * 1 / 4, 10, 10,
                48, 10,
                0.9f, 3, 45, 0.5f,
                playerShipTxtr, playerShipShieldTxtr, playerShipLaserTxtr);

        enemyShip = new EnemyShip(WORLD_WIDTH / 2, WORLD_HEIGHT * 3 / 4, 10, 10,
                2, 5,
                0.9f, 3, 45, 0.5f,
                enemyShipTxtr, enemyShipShieldTxtr, enemyShipLaserTxtr);

        playerLserList = new LinkedList<>();
        enemyLserList = new LinkedList<>();


        backgroundMaxScrollingSpeed = (float) WORLD_HEIGHT / 4;

        batch = new SpriteBatch();
    }


    @Override
    public void render(float delta) {


        batch.begin();

        detectInput(delta);

        playerShip.update(delta);
        enemyShip.update(delta);

        //scrolling background
        renderBackground1(delta);

        //ship
        playerShip.draw(batch);
        enemyShip.draw(batch);

        //lasers
        //create new Lasers
        if (playerShip.canFireLaser()) {
            Laser[] lasers = playerShip.fireLasers();
            for (Laser laser : lasers) {
                playerLserList.add(laser);
            }
        }

        if (enemyShip.canFireLaser()) {
            Laser[] lasers = enemyShip.fireLasers();
            for (Laser laser : lasers) {
                enemyLserList.add(laser);
            }
        }


        //draw lasers
        //remove old lasers
        ListIterator<Laser> iterator = playerLserList.listIterator();
        while (iterator.hasNext()) {
            Laser laser = iterator.next();
            laser.draw(batch);
            laser.boundingBox.y += laser.movementSpeed * delta;
            if (laser.boundingBox.y > WORLD_HEIGHT) {
                iterator.remove();
            }
        }

        iterator = enemyLserList.listIterator();
        while (iterator.hasNext()) {
            Laser laser = iterator.next();
            laser.draw(batch);
            laser.boundingBox.y -= laser.movementSpeed * delta;
            if (laser.boundingBox.y + laser.boundingBox.height < 0) {
                iterator.remove();
            }
        }

        //laser
        renderRasers(delta);

        //explosions
        detectCollisions();

        batch.end();

    }

    private void detectInput(float delta) {
        // keyboard input


        //strategy: determine the max distance the ship can move
        //check each key that matters and move accordingly

        float xChange = 0, yChange = 0;
        float movementDistance = playerShip.movementSpeed * delta;


        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            xChange = Math.min(WORLD_WIDTH - playerShip.boundingBox.x - playerShip.boundingBox.width, movementDistance);
            playerShip.translate(xChange, 0);
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            xChange = Math.min(playerShip.boundingBox.x, movementDistance);
            playerShip.translate(-xChange, 0);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            yChange = Math.min(WORLD_HEIGHT / 2 - playerShip.boundingBox.y - playerShip.boundingBox.height, movementDistance);
            playerShip.translate(0, yChange);
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            yChange = Math.min(playerShip.boundingBox.y, movementDistance);
            playerShip.translate(0, -yChange);
        }

    }

    private void detectInput2(float delta) {
        // keyboard input


        //strategy: determine the max distance the ship can move
        //check each key that matters and move accordingly

        float leftLimit, rightLimit, upLimit, downLimit;
        leftLimit = -playerShip.boundingBox.x;
        downLimit = -playerShip.boundingBox.y;
        rightLimit = WORLD_WIDTH - playerShip.boundingBox.x - playerShip.boundingBox.width;
        upLimit = WORLD_HEIGHT / 2 - playerShip.boundingBox.y - playerShip.boundingBox.height;

        if (Gdx.input.isKeyPressed((Input.Keys.RIGHT)) && rightLimit > 0) {
            playerShip.translate(Math.min(playerShip.movementSpeed * delta, rightLimit), 0f);
        }
        if (Gdx.input.isKeyPressed((Input.Keys.UP)) && upLimit > 0) {
            playerShip.translate(0f, Math.min(playerShip.movementSpeed * delta, upLimit));
        }
        if (Gdx.input.isKeyPressed((Input.Keys.LEFT)) && leftLimit < 0) {
            playerShip.translate(Math.max(-playerShip.movementSpeed * delta, leftLimit), 0f);
        }
        if (Gdx.input.isKeyPressed((Input.Keys.DOWN)) && downLimit < 0) {
            playerShip.translate(0f, Math.max(-playerShip.movementSpeed * delta, downLimit));
        }

    }

    private void detectCollisions() {
        //for each player laser,check whether it intersects an enemy ship
        ListIterator<Laser> iterator = playerLserList.listIterator();
        while (iterator.hasNext()) {
            Laser laser = iterator.next();
            if (enemyShip.intersects(laser.getBoundingBox())) {
                enemyShip.hit(laser);
                iterator.remove();
            }
        }

        //for each enemy laser,check whether it intersects an player ship
        iterator = enemyLserList.listIterator();
        while (iterator.hasNext()) {
            Laser laser = iterator.next();
            if (playerShip.intersects(laser.getBoundingBox())) {
                playerShip.hit(laser);
                iterator.remove();
            }
        }

    }

    private void renderRasers(float delta) {
        //draw lasers
        //remove old lasers
        ListIterator<Laser> iterator = playerLserList.listIterator();
        while (iterator.hasNext()) {
            Laser laser = iterator.next();
            laser.draw(batch);
            laser.boundingBox.y += laser.movementSpeed * delta;
            if (laser.boundingBox.y > WORLD_HEIGHT) {
                iterator.remove();
            }
        }

        iterator = enemyLserList.listIterator();
        while (iterator.hasNext()) {
            Laser laser = iterator.next();
            laser.draw(batch);
            laser.boundingBox.y -= laser.movementSpeed * delta;
            if (laser.boundingBox.y + laser.boundingBox.height < 0) {
                iterator.remove();
            }
        }
    }


    private void renderBackground2(float delta) {
        backgroungOffsets[0] += delta * backgroundMaxScrollingSpeed / 8;
        backgroungOffsets[1] += delta * backgroundMaxScrollingSpeed / 4;
        backgroungOffsets[2] += delta * backgroundMaxScrollingSpeed / 2;
        backgroungOffsets[3] += delta * backgroundMaxScrollingSpeed;


        for (int layer = 0; layer < 3; layer++) {
            if (backgroungOffsets[layer] > WORLD_HEIGHT) {
                backgroungOffsets[layer] = 0;
            }
            batch.draw(backgrounds[layer], 0, -backgroungOffsets[layer], WORLD_WIDTH, WORLD_HEIGHT);
            batch.draw(backgrounds[layer], 0, -backgroungOffsets[layer] + WORLD_HEIGHT, WORLD_WIDTH, WORLD_HEIGHT);
        }
    }

    private void renderBackground1(float delta) {

        backgroungOffsets[0] += delta * backgroundMaxScrollingSpeed / 8;
        backgroungOffsets[1] += delta * backgroundMaxScrollingSpeed / 4;
        backgroungOffsets[2] += delta * backgroundMaxScrollingSpeed / 2;
        backgroungOffsets[3] += delta * backgroundMaxScrollingSpeed;


        for (int layer = 0; layer < backgroungOffsets.length; layer++) {
            if (backgroungOffsets[layer] > WORLD_HEIGHT) {
                backgroungOffsets[layer] = 0;
            }
            batch.draw(backgrounds[layer], 0, -backgroungOffsets[layer], WORLD_WIDTH, WORLD_HEIGHT);
            batch.draw(backgrounds[layer], 0, -backgroungOffsets[layer] + WORLD_HEIGHT, WORLD_WIDTH, WORLD_HEIGHT);
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void show() {

    }

    @Override
    public void dispose() {

    }
}
