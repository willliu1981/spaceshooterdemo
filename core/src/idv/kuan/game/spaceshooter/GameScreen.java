package idv.kuan.game.spaceshooter;

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
        enemyShipTxtr.flip(false,true);
        //enemyShipTxtr.flip(false,true);

        //shield
        playerShipShieldTxtr = textureAtlas.findRegion("meteor_smallb");
        enemyShipShieldTxtr = textureAtlas.findRegion("meteor_largeb");

        //laser
        playerShipLaserTxtr = textureAtlas.findRegion("effect_yellow");
        enemyShipLaserTxtr = textureAtlas.findRegion("effect_purple");
        enemyShipLaserTxtr.flip(false,true);


        backgroungOffsets = new float[4];

        //*/

        //set up game objects
        playerShip = new PlayerShip(WORLD_WIDTH / 2, WORLD_HEIGHT * 1 / 4, 10, 10,
                2, 3,
                0.9f, 3, 45, 0.5f,
                playerShipTxtr, playerShipShieldTxtr, playerShipLaserTxtr);

        enemyShip = new EnemyShip(WORLD_WIDTH / 2, WORLD_HEIGHT * 3 / 4, 10, 10,
                2, 3,
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
            laser.yPos += laser.movementSpeed * delta;
            if (laser.yPos > WORLD_HEIGHT) {
                iterator.remove();
            }
        }

        iterator = enemyLserList.listIterator();
        while (iterator.hasNext()) {
            Laser laser = iterator.next();
            laser.draw(batch);
            laser.yPos -= laser.movementSpeed * delta;
            if (laser.yPos + laser.height < 0) {
                iterator.remove();
            }
        }


        batch.end();

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
