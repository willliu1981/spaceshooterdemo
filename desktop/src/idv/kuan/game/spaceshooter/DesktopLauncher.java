package idv.kuan.game.spaceshooter;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import idv.kuan.game.spaceshooter.SpaceShooter;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(1024,1792);
		config.setForegroundFPS(60);
		config.setTitle("SpaceShooter");
		new Lwjgl3Application(new SpaceShooter(), config);
	}
}
