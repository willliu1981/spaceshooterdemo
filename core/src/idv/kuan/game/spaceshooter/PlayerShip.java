package idv.kuan.game.spaceshooter;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PlayerShip extends Ship {
    public PlayerShip(float xCenter, float yCenter, float width, float height,
                      float movementSpeed, int shield, float laserWidth, float laserHeight,
                      float laserMovementSpeed, float timeBetweenShots,
                      TextureRegion shipTexture,
                      TextureRegion shieldtexture,
                      TextureRegion laserTexture) {
        super(xCenter, yCenter, width, height, movementSpeed, shield, laserWidth, laserHeight, laserMovementSpeed, timeBetweenShots, shipTexture, shieldtexture, laserTexture);
    }

    @Override
    public void drawShied(Batch batch) {
        batch.draw(shieldtexture, boundingBox.x-boundingBox.width*0.2f, boundingBox.y-boundingBox.height*0.6f,
                boundingBox.width*1.4f, boundingBox.height*2f);
    }

    @Override
    public Laser[] fireLasers() {
        Laser[] laser = new Laser[2];
        laser[0] = new Laser(boundingBox.x + boundingBox.width * 0.2f, boundingBox.y + boundingBox.height * 0.8f + laserHeight * 0.01f,
                laserWidth, laserHeight, laserMovementSpeed, laserTexture);
        laser[1] = new Laser(boundingBox.x + boundingBox.width * 0.8f, boundingBox.y + boundingBox.height * 0.8f + laserHeight * 0.01f,
                laserWidth, laserHeight, laserMovementSpeed, laserTexture);

        timeSinceLastShot = 0;

        return laser;
    }
}
