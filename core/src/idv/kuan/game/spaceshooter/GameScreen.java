package idv.kuan.game.spaceshooter;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen implements Screen {

    //screen
    private Camera camera;
    private Viewport viewport;

    //graphics
    private SpriteBatch batch;
    private Texture background;
    private Texture[] backgrounds;

    //world parameters
    private final int WORLD_WIDTH = 72;
    private final int WORLD_HEIGHT = 128;


    //timing
    private int backgroungOffset;
    private float[] backgroungOffsets;
    private float backgroundMaxScrollingSpeed;

    public GameScreen() {
        camera = new OrthographicCamera();
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        //*
        background = new Texture("space01.jpg");
        backgroungOffset = 0;


        //*/

        //*
        backgrounds = new Texture[4];
        backgrounds[0] = new Texture("space01.jpg");
        backgrounds[1] = new Texture("Starspace01b.png");
        backgrounds[2] = new Texture("S2b.png");
        backgrounds[3] = new Texture("S3b.png");

        backgroungOffsets = new float[4];

        //*/

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
