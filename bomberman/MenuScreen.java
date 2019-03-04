package com.htransteven.bomberman;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Graphics.DisplayMode;
//import com.htransteven.bomberman.appwarp.WarpController;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Steven Tran on 5/11/2017.
 */
public class MenuScreen extends ScreenAdapter {

    Game game;
    SpriteBatch b;
    Texture background;
    Texture select;
    ArrayList<Texture> options;
    int currOpt = 0;

    final int WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    final int HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();

    public MenuScreen(Game g){
        game = g;
    }

    @Override
    public void show(){
        b = new SpriteBatch();
        background = new Texture("MenuBG.jpg");
        select = new Texture("selectbomb.png");
        options = new ArrayList<Texture>();
        options.add(new Texture("play.png"));
        options.add(0, new Texture("settings.png"));
        options.add(0, new Texture("credits.png"));

    }

    @Override
    public void render(float delta){
        Gdx.gl.glClearColor( 0, 0, 0, 1 );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );

        b.begin();

        b.draw(background,0,0, WIDTH, HEIGHT);
        for(int i = 0; i < options.size(); i++){
            b.draw(options.get(i), 640, (100 * (i + 1)));
        }

        int x = Gdx.input.getX();
        int y;

        if(Gdx.graphics.isFullscreen()) {
            y = HEIGHT - Gdx.input.getY(); //Invert the y-coordinate
        } else {
            y = (HEIGHT / 2) - Gdx.input.getY();
        }

        b.draw(select, 535, (100 * (currOpt)));

        if(Gdx.graphics.isFullscreen()){
            if(x >= (WIDTH / 2) - (options.get(0).getWidth() / 2) && x <= (WIDTH / 2) + (options.get(0).getWidth() / 2)){
                if(y >= 100 && y <=  100 + options.get(0).getHeight()){
                    currOpt = 1;
                } else if (y >= 200 && y <= 200 + options.get(0).getHeight()){
                    currOpt = 2;
                } else if (y >= 300 && y <= 300 + options.get(0).getHeight()){
                    currOpt = 3;
                } else {
                    currOpt = -2;
                }
            } else {
                currOpt = -2;
            }
        } else {
            if (x >= (WIDTH / 4) - (options.get(0).getWidth() / 4) && x <= (WIDTH / 4) + (options.get(0).getWidth() / 4)) {
                if (y > 50 && y <= 50 + (options.get(0).getHeight() / 2)) {
                    currOpt = 1;
                } else if (y >= 100 && y <= 100 + (options.get(0).getHeight() / 2)){
                    currOpt = 2;
                } else if (y >= 150 && y <= 150 + (options.get(0).getHeight())) {
                    currOpt = 3;
                } else {
                    currOpt = -2;
                }
            } else {
                currOpt = -2;
            }
        }


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
            switch(currOpt){
                default:
                    break;

                case 3:
                    game.setScreen(new TutorialScreen(game));
                    //WarpController.getInstance().handleLeave();
                    break;

            }
        }


    }

    @Override
    public void dispose(){
        b.dispose();
        background.dispose();
        select.dispose();
        for(Texture t: options){
            t.dispose();
        }
    }
}
