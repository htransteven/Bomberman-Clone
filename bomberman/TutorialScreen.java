package com.htransteven.bomberman;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.awt.*;

/**
 * Created by Steven Tran on 5/12/2017.
 */
public class TutorialScreen extends ScreenAdapter {

    Game game;
    final int WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    final int HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();

    int h = Gdx.graphics.getHeight();
    int w = Gdx.graphics.getWidth();

    SpriteBatch b;
    Texture background;


    public TutorialScreen(Game g){
        game = g;
    }

    @Override
    public void show(){
        b = new SpriteBatch();
        background = new Texture("tutscreen.png");
    }

    @Override
    public void render(float delta){
        Gdx.gl.glClearColor( 0, 0, 0, 1 );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );

        b.begin();

        b.draw(background, 0 , 0, w, h);

        b.end();

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            Gdx.app.exit();
        }

        if((Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.ALT_RIGHT)) && Gdx.input.isKeyPressed(Input.Keys.ENTER)){
            if(Gdx.graphics.isFullscreen()) {
                Gdx.graphics.setWindowedMode(WIDTH / 2, HEIGHT / 2);
            } else {
                Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
            }
        }

        if(Gdx.input.justTouched()){
            game.setScreen(new PlayerCountScreen(game));
        }

    }

    @Override
    public void dispose(){
        b.dispose();
        background.dispose();
    }
}
