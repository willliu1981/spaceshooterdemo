package idv.kuan.game.spaceshooter;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Ship {
    //ship characteristics
    float movementSpeed;
    int shield;

    //position & dimension
    float xPos, yPos;
    float width, height;

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
        this.xPos = xCenter - width / 2;
        this.yPos = yCenter - height / 2;
        this.width = width;
        this.height = height;
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

    void draw(Batch batch) {
        batch.draw(shipTexture, xPos, yPos, width, height);
        if (shield > 0) {
            batch.draw(shieldtexture, xPos, yPos, width, height);
        }
    }

    public abstract Laser[] fireLasers();
}
