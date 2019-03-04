package com.htransteven.bomberman.desktop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.htransteven.bomberman.BomberMan;
import com.htransteven.bomberman.GameScreen;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1920;
		config.width = 1080;
		config.title = "Bomber Man by Steven Huynh-Tran";
		config.forceExit = true;
		config.fullscreen = true;
		config.resizable = false;
		new LwjglApplication(new BomberMan(), config);
	}
}
