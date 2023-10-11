package idv.kuan.game.spaceshooter;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Ship {
    //ship characteristics
    float movementSpeed;
    int shield;

    //position & dimension
    float xPos, yPos;
    float width, height;

    //graphics
    TextureRegion shipTexture, shieldtexture;

    public Ship(float movementSpeed, int shield,
                float width, float height,
                float xCenter, float yCenter,
                TextureRegion shipTexture, TextureRegion shieldtexture) {
        this.movementSpeed = movementSpeed;
        this.shield = shield;
        this.xPos = xCenter - width / 2;
        this.yPos = yCenter - height / 2;
        this.width = width;
        this.height = height;
        this.shipTexture = shipTexture;
        this.shieldtexture = shieldtexture;
    }


    void draw(Batch batch){
        batch.draw(shipTexture,xPos,yPos,width,height);
        if(shield>0){
            batch.draw(shieldtexture,xPos,yPos,width,height);
        }
    }
}
