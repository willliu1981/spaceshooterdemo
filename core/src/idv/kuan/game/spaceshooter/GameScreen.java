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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.LinkedList;
import java.util.ListIterator;

public class GameScreen implements Screen {

    private static final float TOUCH_MOVEMENT_THRESHOLD = 0.5f;
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

    private Texture explodeTxtr;


    //world parameters
    private final int WORLD_WIDTH = 72;
    private final int WORLD_HEIGHT = 128;


    //timing
    private int backgroungOffset;
    private float[] backgroungOffsets;
    private float backgroundMaxScrollingSpeed;

    //game objects
    private PlayerShip playerShip;
    private EnemyShip enemyShip;
    private LinkedList<EnemyShip> enemyShips;
    private LinkedList<Laser> playerLserList;
    private LinkedList<Laser> enemyLserList;
    private LinkedList<Explosion> explosions = new LinkedList<>();


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

        //explode
        explodeTxtr = new Texture("explode01b.png");

        //set up game objects
        playerLserList = new LinkedList<>();
        enemyLserList = new LinkedList<>();
        enemyShips = new LinkedList<>();
        explosions = new LinkedList<>();

        playerShip = new PlayerShip(WORLD_WIDTH / 2, WORLD_HEIGHT * 1 / 4, 10, 10,
                48, 10,
                0.9f, 3, 45, 0.5f,
                playerShipTxtr, playerShipShieldTxtr, playerShipLaserTxtr);

        enemyShip = new EnemyShip(WORLD_WIDTH / 2, WORLD_HEIGHT * 3 / 4, 10, 10,
                2, 5,
                0.9f, 3, 45, 0.5f,
                enemyShipTxtr, enemyShipShieldTxtr, enemyShipLaserTxtr);


        enemyShips.add(enemyShip);


        backgroundMaxScrollingSpeed = (float) WORLD_HEIGHT / 4;

        batch = new SpriteBatch();
    }

    boolean isGameOver = false;

    public boolean isGameOver() {
        return isGameOver;
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
        enemyShips.forEach(s -> s.draw(batch));
        //enemyShip.draw(batch);

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

        //detect collisions between lasers and ships
        detectCollisions();

        //explosions
        updateAndRenderExplosion(delta);

        batch.end();

    }

    private void detectInput(float delta) {
        // keyboard input


        //strategy: determine the max distance the ship can move
        //check each key that matters and move accordingly

        float movementDistance = playerShip.movementSpeed * delta;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            playerShip.translate(Math.min(playerShip.boundingBox.x, movementDistance),
                    Input.Keys.LEFT);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            playerShip.translate(Math.min(WORLD_WIDTH - playerShip.boundingBox.x - playerShip.boundingBox.width, movementDistance),
                    Input.Keys.RIGHT);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            playerShip.translate(Math.min(playerShip.boundingBox.y, movementDistance),
                    Input.Keys.DOWN);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            playerShip.translate(Math.min(WORLD_HEIGHT / 2 - playerShip.boundingBox.y - playerShip.boundingBox.height, movementDistance),
                    Input.Keys.UP);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            isGameOver = true;
        }


        // touch input(aso mouse)
        if (Gdx.input.isTouched()) {
            //get this screen position of the touch
            float xTouchPixels = Gdx.input.getX();
            float yTouchPixels = Gdx.input.getY();

            //convert to world position
            Vector2 touchPoint = new Vector2(xTouchPixels, yTouchPixels);
            touchPoint = viewport.unproject(touchPoint);

            //calculate the x and y differences
            Vector2 playerShipCenter = new Vector2(playerShip.boundingBox.x + playerShip.boundingBox.width / 2,
                    playerShip.boundingBox.y + playerShip.boundingBox.height / 2);

            float touchDistance = touchPoint.dst(playerShipCenter);

            if (touchDistance > TOUCH_MOVEMENT_THRESHOLD) {
                float xToucnDifference = touchPoint.x - playerShipCenter.x;
                float yToucnDifference = touchPoint.y - playerShipCenter.y;

                //scale to the maximum speed of the ship
                float xMove = xToucnDifference / touchDistance * playerShip.movementSpeed * delta;
                float yMove = yToucnDifference / touchDistance * playerShip.movementSpeed * delta;

                if (xMove > 0) {
                    xMove = Math.min(xMove, WORLD_WIDTH - playerShip.boundingBox.x - playerShip.boundingBox.width);
                } else {
                    xMove = Math.max(xMove, -playerShip.boundingBox.x);
                }

                if (yMove > 0) {
                    yMove = Math.min(yMove, WORLD_HEIGHT / 2 - playerShip.boundingBox.y - playerShip.boundingBox.height);
                } else {
                    yMove = Math.max(yMove, -playerShip.boundingBox.y);
                }

                playerShip.translate(xMove, yMove);

            }


        }

    }

    private void updateAndRenderExplosion(float delta) {
        ListIterator<Explosion> explosionListIterator = explosions.listIterator();
        while (explosionListIterator.hasNext()) {
            Explosion explosion = explosionListIterator.next();
            explosion.update(delta);
            if (explosion.isFinished()) {
                explosionListIterator.remove();
            } else {
                explosion.draw(batch);
            }
        }
    }


    private void detectCollisions() {
        //for each player laser,check whether it intersects an enemy ship
        ListIterator<Laser> laserIterator = playerLserList.listIterator();
        while (laserIterator.hasNext()) {
            Laser laser = laserIterator.next();

            ListIterator<EnemyShip> enemyShipListIterator = enemyShips.listIterator();
            while (enemyShipListIterator.hasNext()) {
                EnemyShip enemyShip = enemyShipListIterator.next();

                if (enemyShip.intersects(laser.getBoundingBox())) {
                    if (enemyShip.hitAndCheckDestroyed(laser)) {
                        enemyShipListIterator.remove();
                        explosions.add(new Explosion(explodeTxtr,
                                new Rectangle(enemyShip.boundingBox.x - enemyShip.boundingBox.width / 2,
                                        enemyShip.boundingBox.y - enemyShip.boundingBox.height / 2,
                                        enemyShip.boundingBox.width * 2, enemyShip.boundingBox.height * 2
                                ), 0.5f));
                    }

                    laserIterator.remove();
                    break;
                }
            }

        }

        //for each enemy laser,check whether it intersects an player ship
        laserIterator = enemyLserList.listIterator();
        while (laserIterator.hasNext()) {
            Laser laser = laserIterator.next();
            if (playerShip.intersects(laser.getBoundingBox())) {
                playerShip.hitAndCheckDestroyed(laser);
                laserIterator.remove();
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
