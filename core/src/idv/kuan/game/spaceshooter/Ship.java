package idv.kuan.game.spaceshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public abstract class Ship {
    //ship characteristics
    float movementSpeed;
    int shield;

    //position & dimension
    protected Rectangle boundingBox;

    //laser information
    float laserWidth, laserHeight;
    float laserMovementSpeed;
    float timeBetweenShots;
    float timeSinceLastShot = 0;

    //graphics
    TextureRegion shipTexture, shieldtexture, laserTexture;

    public Ship(
            float xCenter, float yCenter,
            float width, float height,
            float movementSpeed, int shield,
            float laserWidth, float laserHeight, float laserMovementSpeed,
            float timeBetweenShots,
            TextureRegion shipTexture, TextureRegion shieldtexture, TextureRegion laserTexture) {
        this.movementSpeed = movementSpeed;
        this.shield = shield;
        this.boundingBox = new Rectangle(xCenter - width / 2, yCenter - height / 2, width, height);
        this.laserWidth = laserWidth;
        this.laserHeight = laserHeight;
        this.laserMovementSpeed = laserMovementSpeed;
        this.timeBetweenShots = timeBetweenShots;
        this.shipTexture = shipTexture;
        this.shieldtexture = shieldtexture;
        this.laserTexture = laserTexture;
    }

    public void update(float deltaTime) {
        timeSinceLastShot += deltaTime;
    }

    public boolean canFireLaser() {
        return timeSinceLastShot - timeBetweenShots > 0;
    }


    public abstract void drawShied(Batch batch);


    public abstract Laser[] fireLasers();

    public boolean intersects(Rectangle otherRectangle) {
        return this.boundingBox.overlaps(otherRectangle);
    }

    void draw(Batch batch) {
        if (shield > 0) {
            drawShied(batch);
        }
        batch.draw(shipTexture, boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);

    }

    public void hit(Laser laser) {
        if (shield > 0) {
            shield--;
        }
    }

    public void translate(float xChange, float yChange) {
        boundingBox.setPosition(boundingBox.x + xChange, boundingBox.y + yChange);
    }

    public void translate(float change, int direction) {
        switch (direction) {
            case Input.Keys.LEFT:
                boundingBox.setX(boundingBox.x - change);
                break;
            case Input.Keys.RIGHT:
                boundingBox.setX(boundingBox.x + change);
                break;
            case Input.Keys.UP:
                boundingBox.setY(boundingBox.y + change);
                break;
            case Input.Keys.DOWN:
                boundingBox.setY(boundingBox.y - change);
                break;


        }

    }
}
