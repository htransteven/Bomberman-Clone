package com.htransteven.bomberman;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.awt.*;

public class BomberMan extends Game {

	Game game;

	public BomberMan(){
		game = this;
	}

	@Override
	public void create(){
		//setScreen(new MenuScreen(game));

		setScreen(new MenuScreen(game));
	}
}
