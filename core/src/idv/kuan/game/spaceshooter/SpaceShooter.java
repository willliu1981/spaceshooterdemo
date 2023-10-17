package idv.kuan.game.spaceshooter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class SpaceShooter extends Game {
	GameScreen gameScreen;

	//*
	@Override
	public void create() {
		gameScreen=new GameScreen();
		setScreen(gameScreen);

	}

	@Override
	public void dispose() {
		gameScreen.dispose();
	}

	@Override
	public void render() {
		super.render();

		if(gameScreen.isGameOver){
			gameScreen=new GameScreen();
			setScreen(gameScreen);
		}
	}

	@Override
	public void resize(int width, int height) {
		gameScreen.resize(width, height);
	}
	//*/



/*
	SpriteBatch batch;
	Texture img;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}

	 //*/
}
