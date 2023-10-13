package idv.kuan.game.spaceshooter;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Laser {
    //pos ant dim
    protected Rectangle boundingBox;


    //characteristics
    float movementSpeed;

    //graphics
    TextureRegion textureRegion;

    public Laser(float xPos, float yPos, float width, float height, float movementSpeed, TextureRegion textureRegion) {
        boundingBox = new Rectangle(xPos, yPos, width, height);
        this.movementSpeed = movementSpeed;
        this.textureRegion = textureRegion;
    }


    public void draw(Batch batch) {
        batch.draw(textureRegion, boundingBox.x - boundingBox.width / 2, boundingBox.y, boundingBox.width, boundingBox.height);
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }


}
