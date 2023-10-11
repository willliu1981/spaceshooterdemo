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
    Ship playerShip, enemyShip;


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
        playerShipTxtr=textureAtlas.findRegion("ship_sidesA");
        enemyShipTxtr=textureAtlas.findRegion("enemy_A");
        //enemyShipTxtr.flip(false,true);

        //shield
        playerShipShieldTxtr=textureAtlas.findRegion("meteor_largeb");
        enemyShipShieldTxtr=textureAtlas.findRegion("meteor_smallb");

        //laser
        playerShipLaserTxtr=textureAtlas.findRegion("effect_yellow");
        enemyShipLaserTxtr=textureAtlas.findRegion("effect_purple");


        backgroungOffsets = new float[4];

        //*/

        //set up game objects
        playerShip = new Ship(2, 10, 10, 10, WORLD_WIDTH / 2, WORLD_HEIGHT / 4,
                playerShipTxtr, playerShipShieldTxtr);

        enemyShip = new Ship(2, 3, 10, 10, WORLD_WIDTH / 2, WORLD_HEIGHT * 3 / 4,
                enemyShipTxtr, enemyShipShieldTxtr);



        backgroundMaxScrollingSpeed = (float) WORLD_HEIGHT / 4;

        batch = new SpriteBatch();
    }


    @Override
    public void render(float delta) {


        batch.begin();


        //scrolling background
        /*
        backgroungOffset++;
        if (backgroungOffset % WORLD_HEIGHT == 0) {
            backgroungOffset = 0;
        }
        batch.draw(background, 0, -backgroungOffset, WORLD_WIDTH, WORLD_HEIGHT);
        batch.draw(background, 0, -backgroungOffset+WORLD_HEIGHT, WORLD_WIDTH, WORLD_HEIGHT);

         //*/


        renderBackground1(delta);

        //ship
        playerShip.draw(batch);
        enemyShip.draw(batch);





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
