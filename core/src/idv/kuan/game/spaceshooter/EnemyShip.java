package idv.kuan.game.spaceshooter;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class EnemyShip extends Ship {
    public EnemyShip(float xCenter, float yCenter, float width, float height,
                     float movementSpeed, int shield,
                     float laserWidth, float laserHeight,
                     float laserMovementSpeed, float timeBetweenShots,
                     TextureRegion shipTexture,
                     TextureRegion shieldtexture, TextureRegion laserTexture) {
        super(xCenter, yCenter, width, height, movementSpeed, shield, laserWidth, laserHeight, laserMovementSpeed, timeBetweenShots, shipTexture, shieldtexture, laserTexture);
    }

    @Override
    public void drawShied(Batch batch) {
        batch.draw(shieldtexture, boundingBox.x - boundingBox.width * 0.15f, boundingBox.y - boundingBox.height * 0.1f,
                boundingBox.width * 1.30f, boundingBox.height * 1.30f);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    @Override
    public Laser[] fireLasers() {
        Laser[] laser = new Laser[1];
        laser[0] = new Laser(boundingBox.x + boundingBox.width * 0.5f, boundingBox.y - boundingBox.height * 0.01f - laserHeight * 0.99f,
                laserWidth, laserHeight, laserMovementSpeed, laserTexture);

        timeSinceLastShot = 0;

        return laser;
    }
}
