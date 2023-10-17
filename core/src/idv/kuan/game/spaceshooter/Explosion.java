package idv.kuan.game.spaceshooter;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Explosion {

    private float explosionTimer;
    private Rectangle boundingBox;
    private Animation<TextureRegion> animation;


    public Explosion(Texture texture, Rectangle boundingBox, float totalAnimationTime) {
        this.boundingBox = boundingBox;

        //split texture and convert to 1D
        TextureRegion[][] textureRegion2D = TextureRegion.split(texture, texture.getWidth() / 4, texture.getHeight() / 2);
        TextureRegion[] textureRegion1D = new TextureRegion[8];

        for (int i = 0, h = 0; h < 2; h++) {
            for (int w = 0; w < 4; w++) {
                textureRegion1D[i] = textureRegion2D[h][w];
                i++;
            }
        }

        animation = new Animation<TextureRegion>(totalAnimationTime / 8, textureRegion1D);
        this.explosionTimer = 0;

    }

    public void update(float delta) {
        explosionTimer += delta;
    }


    public void draw(Batch batch) {
        batch.draw(animation.getKeyFrame(explosionTimer), boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
    }

    public boolean isFinished() {
        return animation.isAnimationFinished(explosionTimer);
    }
}
