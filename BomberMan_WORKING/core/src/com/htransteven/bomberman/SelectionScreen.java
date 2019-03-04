
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

public class SelectionScreen extends ScreenAdapter {

    final int playerCount;
    int currentChoosing = 0;

    Sprite selectColor;

    ArrayList<Sprite> white, black, red, blue, green;
    ArrayList<Sprite> colorButtons;

    Player[] players;

    int currIMG;

    float pictureTimer = 0.0f;

    SpriteBatch b;

    Game game;

    public SelectionScreen(Game g, int playerC) {
        game = g;
        playerCount = playerC;
    }

    @Override
    public void show() {
        b = new SpriteBatch();

        players = new Player[playerCount];

        selectColor = new Sprite(new Texture("SelectColor.png"), 1920, 300);
        colorButtons = new ArrayList<Sprite>();

        white = new ArrayList<Sprite>();
        black = new ArrayList<Sprite>();
        red = new ArrayList<Sprite>();
        blue = new ArrayList<Sprite>();
        green = new ArrayList<Sprite>();

        for(int c = 1; c <= 5; c++){
            for (int i = 0; i < 9; i++) { //# of selection screen sprites = 9
                switch (c) {
                    case 1:
                        white.add(new Sprite(new TextureRegion(new Texture("Selection.png"), 1 + (22 * i), 24 * (c - 1), 22, 24)));
                        break;
                    case 2:
                        black.add(new Sprite(new TextureRegion(new Texture("Selection.png"), 1 + (22 * i), 24 * (c - 1), 22, 24)));
                        break;
                    case 3:
                        red.add(new Sprite(new TextureRegion(new Texture("Selection.png"), 1 + (22 * i), 24 * (c - 1), 22, 24)));
                        break;
                    case 4:
                        green.add(new Sprite(new TextureRegion(new Texture("Selection.png"), 1 + (22 * i), 24 * (c - 1), 22, 24)));
                        break;
                    case 5:
                        blue.add(new Sprite(new TextureRegion(new Texture("Selection.png"), 1 + (22 * i), 24 * (c - 1), 22, 24)));
                        break;
                }
            }

            switch(c){
                case 1:
                    for(Sprite s: white){
                        s.setSize(183, 200); //Old - 110x120
                    }
                    break;
                case 2:
                    for(Sprite s: black){
                        s.setSize(183, 200);
                    }
                    break;
                case 3:
                    for(Sprite s: red){
                        s.setSize(183, 200);
                    }
                    break;
                case 4:
                    for(Sprite s: green){
                        s.setSize(183, 200);
                    }
                    break;
                case 5:
                    for(Sprite s: blue){
                        s.setSize(183, 200);
                    }
                    break;
            }
        }

        colorButtons.add(new Sprite(new Texture("selectWhite.png"), 300, 100));
        colorButtons.add(new Sprite(new Texture("selectBlack.png"), 300, 100));
        colorButtons.add(new Sprite(new Texture("selectRed.png"), 300, 100));
        colorButtons.add(new Sprite(new Texture("selectGreen.png"), 300, 100));
        colorButtons.add(new Sprite(new Texture("selectBlue.png"), 300, 100));

        setPositions();
    }

    @Override
    public void render(float delta){
        Gdx.gl.glClearColor( 0, 0, 0, 1 );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );

        b.begin();

        selectColor.draw(b);

        for(int i = 1; i <= 5; i++){
            switch(i){
                case 1:
                    white.get(currIMG).draw(b);
                    break;
                case 2:
                    black.get(currIMG).draw(b);
                    break;
                case 3:
                    red.get(currIMG).draw(b);
                    break;
                case 4:
                    green.get(currIMG).draw(b);
                    break;
                case 5:
                    blue.get(currIMG).draw(b);
                    break;
            }
        }

        for(Sprite s: colorButtons){
            s.draw(b);
        }

        b.end();

        if(pictureTimer > 0.5f){
            pictureTimer = 0;
            currIMG++;
            if(currIMG >= white.size()){
                currIMG = 0;
            }
        } else {
            pictureTimer += delta;
        }

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

            if ((Gdx.input.justTouched())) {
                //Get colorButton x coordinate and add by 300
                if (y >= 280 && y <= 380) {
                    if (x >= (Gdx.graphics.getWidth() / 2) - 850 && x <= (Gdx.graphics.getWidth() / 2) - 550) { //White
                        switch(currentChoosing) {
                            case 0:
                                players[currentChoosing] = new Player(1, 1, 1);
                                break;
                            case 1:
                                players[currentChoosing] = new Player(1, 1, 31);
                                break;
                            case 2:
                                players[currentChoosing] = new Player(1, 31, 31);
                                break;
                            case 3:
                                players[currentChoosing] = new Player(1, 31, 1);
                                break;
                        }
                        currentChoosing++;

                    } else if (x >= (Gdx.graphics.getWidth() / 2) - 500 && x <= (Gdx.graphics.getWidth() / 2) - 200) { //Black
                        switch(currentChoosing) {
                            case 0:
                                players[currentChoosing] = new Player(2, 1, 1);
                                break;
                            case 1:
                                players[currentChoosing] = new Player(2, 1, 31);
                                break;
                            case 2:
                                players[currentChoosing] = new Player(2, 31, 31);
                                break;
                            case 3:
                                players[currentChoosing] = new Player(2, 31, 1);
                                break;
                        }
                        currentChoosing++;


                    } else if (x >= (Gdx.graphics.getWidth() / 2) - 150 && x <= (Gdx.graphics.getWidth() / 2) + 150) { //Red
                        switch(currentChoosing) {
                            case 0:
                                players[currentChoosing] = new Player(3, 1, 1);
                                break;
                            case 1:
                                players[currentChoosing] = new Player(3, 1, 31);
                                break;
                            case 2:
                                players[currentChoosing] = new Player(3, 31, 31);
                                break;
                            case 3:
                                players[currentChoosing] = new Player(3, 31, 1);
                                break;
                        }
                        currentChoosing++;


                    } else if (x >= (Gdx.graphics.getWidth() / 2) + 199 && x <= (Gdx.graphics.getWidth() / 2) + 499) { //Green
                        switch(currentChoosing) {
                            case 0:
                                players[currentChoosing] = new Player(4, 1, 1);
                                break;
                            case 1:
                                players[currentChoosing] = new Player(4, 1, 31);
                                break;
                            case 2:
                                players[currentChoosing] = new Player(4, 31, 31);
                                break;
                            case 3:
                                players[currentChoosing] = new Player(4, 31, 1);
                                break;
                        }
                        currentChoosing++;


                    } else if (x >= (Gdx.graphics.getWidth() / 2) + 549 && x <= (Gdx.graphics.getWidth() / 2) + 849) { //Blue
                        switch(currentChoosing) {
                            case 0:
                                players[currentChoosing] = new Player(4, 1, 1);
                                break;
                            case 1:
                                players[currentChoosing] = new Player(4, 1, 31);
                                break;
                            case 2:
                                players[currentChoosing] = new Player(4, 31, 31);
                                break;
                            case 3:
                                players[currentChoosing] = new Player(4, 31, 1);
                                break;
                        }
                        currentChoosing++;

                        /*
                        try {
                            WarpClient.getInstance().connectWithUserName("BLUE");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        */

                    }
                }
                if(currentChoosing >= playerCount) {
                    game.setScreen(new GameScreen(game, 1, playerCount, players));
                }
        }
    }

    public void setPositions(){

        selectColor.setPosition(0, Gdx.graphics.getHeight() - 300);

        for(Sprite s: white){
            s.setPosition((Gdx.graphics.getWidth() / 2) - 791, 480);
        }
        colorButtons.get(0).setPosition((Gdx.graphics.getWidth() / 2) - 850, 280);

        for(Sprite s: black){
            s.setPosition((Gdx.graphics.getWidth() / 2) - 441, 480);
        }
        colorButtons.get(1).setPosition((Gdx.graphics.getWidth() / 2) - 500, 280);

        for(Sprite s: red){ //167px spacing, 183 character width
            s.setPosition((Gdx.graphics.getWidth() / 2) - 91, 480);
        }
        colorButtons.get(2).setPosition((Gdx.graphics.getWidth() / 2) - 150, 280);

        for(Sprite s: green){
            s.setPosition((Gdx.graphics.getWidth() / 2) + 258, 480);
        }
        colorButtons.get(3).setPosition((Gdx.graphics.getWidth() / 2) + 199, 280);

        for(Sprite s: blue){
            s.setPosition((Gdx.graphics.getWidth() / 2) + 608, 480);
        }
        colorButtons.get(4).setPosition((Gdx.graphics.getWidth() / 2) + 549, 280);

    }

    @Override
    public void dispose(){
        b.dispose();
    }
}
