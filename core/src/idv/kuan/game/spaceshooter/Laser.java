package idv.kuan.game.spaceshooter;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Laser {
    //pos ant dim
    float xPos, yPos;
    float width, height;


    //characteristics
    float movementSpeed;

    //graphics
    TextureRegion textureRegion;

    public Laser(float xPos, float yPos, float width, float height, float movementSpeed, TextureRegion textureRegion) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;
        this.movementSpeed = movementSpeed;
        this.textureRegion = textureRegion;
    }


    public void draw (Batch batch){
        batch.draw(textureRegion,xPos-width/2,yPos,width,height);
    }

    public Rectangle getBoundingBox(){
        return new Rectangle(xPos,yPos,width,height);
    }


}
