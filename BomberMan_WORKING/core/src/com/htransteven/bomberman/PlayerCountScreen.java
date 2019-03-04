
package com.htransteven.bomberman;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
//import com.shephertz.app42.gaming.multiplayer.client.WarpClient;

//import com.shephertz.app42.gaming.multiplayer.client.WarpClient;

import java.util.ArrayList;

/**
 * Created by Steven Tran on 5/13/2017.
 */

public class PlayerCountScreen extends ScreenAdapter {

    Sprite selectPlayers;
    ArrayList<Sprite> playerButtons;

    int currIMG;

    float pictureTimer = 0.0f;

    SpriteBatch b;

    Game game;

    public PlayerCountScreen(Game g) {
        game = g;
    }

    @Override
    public void show() {
        b = new SpriteBatch();

        selectPlayers = new Sprite(new Texture("playerCount.png"), 1920, 300);

        playerButtons = new ArrayList<Sprite>();

        for(int i = 1; i <= 4; i++){
            playerButtons.add(new Sprite(new Texture(i + "_player.png"), 256, 256));
        }

        setPositions();
    }

    @Override
    public void render(float delta){
        Gdx.gl.glClearColor( 0, 0, 0, 1 );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );

        b.begin();

        selectPlayers.draw(b);

        for(Sprite s : playerButtons){
            s.draw(b);
        }
        b.end();

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            Gdx.app.exit();
        }

        if((Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.ALT_RIGHT)) && Gdx.input.isKeyPressed(Input.Keys.ENTER)){
            if(Gdx.graphics.isFullscreen()) {
                Gdx.graphics.setWindowedMode(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
            } else {
                Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
            }
        }

        int x = Gdx.input.getX();
        int y = Gdx.graphics.getHeight() - Gdx.input.getY();

        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)){
            game.setScreen(new SelectionScreen(game, 1));
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)){
            game.setScreen(new SelectionScreen(game, 2));
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)){
            game.setScreen(new SelectionScreen(game, 3));
        } else  if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)){
            game.setScreen(new SelectionScreen(game, 4));
        }
    }

    public void setPositions(){

        selectPlayers.setPosition(0, Gdx.graphics.getHeight() - 500);
        playerButtons.get(0).setPosition((Gdx.graphics.getWidth() * 1 / 5) - 128, 200);
        playerButtons.get(1).setPosition((Gdx.graphics.getWidth() * 2 / 5) - 128, 200);//250 spacing
        playerButtons.get(2).setPosition((Gdx.graphics.getWidth() * 3 / 5) - 128, 200);
        playerButtons.get(3).setPosition((Gdx.graphics.getWidth() * 4 / 5) - 128, 200);

    }

    @Override
    public void dispose(){
        b.dispose();
    }
}
