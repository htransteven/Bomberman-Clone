package com.htransteven.bomberman;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
//import com.google.gwt.thirdparty.json.JSONObject;
//import com.htransteven.bomberman.appwarp.WarpController;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by Steven Tran on 5/12/2017.
 */
public class GameScreen extends ScreenAdapter {

    //OrthographicCamera camera;

    final int playerCount;

    Game game;
    Texture tileSet;
    ArrayList<Sprite> tiles;
    ArrayList<Bomb> bombs;
    SpriteBatch b;

    int[][] numMap;
    Block[][] blocks;
    int level;

    //final int WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    //final int HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();

    int h = Gdx.graphics.getHeight();
    int w = Gdx.graphics.getWidth();

    ArrayList<Sprite> explosionHoriz;
    ArrayList<Sprite> explosionVert;

    Player[] players;

    public GameScreen(Game g, int lvl, int pCount, Player[] playerList) {
        game = g;
        level = lvl;
        playerCount = pCount;
        players = playerList;

        /*
        WarpClient.initialize("6ed93d050e1bf8a3092aaf174c6ec7025fb5b49fd11ccf7ff8cae1e1767e0860","2056dfbd976ce66fe4044612f04ff1f6d44a2130d4952942760db6e6bd1e8ec4");
        WarpClient myGame = null;
        try {
            myGame = WarpClient.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        myGame.addConnectionRequestListener(new ConnectionListener());
        */
    }

    @Override
    public void show(){

        //camera = new OrthographicCamera(1920,1080);

        if(!Gdx.graphics.isFullscreen()){
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        }

        tileSet = new Texture("tiles_1.png");
        tiles = new ArrayList<Sprite>();
        b = new SpriteBatch();

        //camera.position.set(camera.viewportWidth / 4f,camera.viewportHeight / 2f, 0);
        //camera.update();

        //b.setProjectionMatrix(camera.combined);

        int x = 0;
        int y = 0;

        while(x < tileSet.getWidth() && y < tileSet.getHeight()){
            tiles.add(new Sprite(new TextureRegion(tileSet, x, y,32,32)));
            x+=32;
            if(x >= tileSet.getWidth()){
                x = 0;
                y += 32;
            }
        }

        numMap = readMap("level" + level + ".txt");

        blocks = new Block[33][33];
        loadBlocks();

        bombs = new ArrayList<Bomb>();

        explosionHoriz = new ArrayList<Sprite>();
        explosionVert = new ArrayList<Sprite>();
        for(int i = 1; i <= 4; i++){
            if(i % 2 == 0){
                explosionHoriz.add(new Sprite(new TextureRegion(new Texture("explosions1.png"), (16 * i) + i, 0, 16, 16)));
            } else {
                explosionVert.add(new Sprite(new TextureRegion(new Texture("explosions1.png"), (16 * i) + i, 0, 16, 16)));
            }
        }

    }

    @Override
    public void render(float delta){
        Gdx.gl.glClearColor( 0, 0, 0, 1 );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );

        checkBombs();

        b.begin();

        renderBlocks(b);
        renderBombs(b);
        checkPlayers();
        for(Player p : players){
            p.update(b);
            renderInfo(p, b);
        }

        b.end();

        /* //DISABLED DUE TO SCALING ISSUES
        if((Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.ALT_RIGHT)) && Gdx.input.isKeyPressed(Input.Keys.ENTER)){
            if(Gdx.graphics.isFullscreen()) {
                Gdx.graphics.setWindowedMode(WIDTH / 2, HEIGHT / 2);
            } else {
                Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
            }
        }
        */

        for(int i = 0; i < 4; i++){
            checkInput(i);
        }

        //sendInfo(p1.getPosX(), p1.getPosY(), p1.getRow(), p1.getCol(), p1.getLives());

    }

    public void checkInput(int num){
        switch(num) {
            case 0:
                if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                    players[num].moveHorizontal(0, blocks, bombs);
                    players[num].currDIR = 0;
                }
                if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                    players[num].moveHorizontal(1, blocks, bombs);
                    players[num].currDIR = 1;
                }
                if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                    players[num].moveVertical(0, blocks, bombs);
                    players[num].currDIR = 2;
                }
                if ((Gdx.input.isKeyPressed(Input.Keys.UP))) {
                    players[num].moveVertical(1, blocks, bombs);
                    players[num].currDIR = 3;
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
                    players[num].placeBomb(bombs);
                }
                if (!Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT) && !Gdx.input.isKeyPressed(Input.Keys.DOWN) && !Gdx.input.isKeyPressed(Input.Keys.UP)) {
                    players[num].isStanding = true;
                } else {
                    players[num].isStanding = false;
                }
                break;
            case 1:
                if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                    players[num].moveHorizontal(0, blocks, bombs);
                    players[num].currDIR = 0;
                }
                if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                    players[num].moveHorizontal(1, blocks, bombs);
                    players[num].currDIR = 1;
                }
                if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                    players[num].moveVertical(0, blocks, bombs);
                    players[num].currDIR = 2;
                }
                if ((Gdx.input.isKeyPressed(Input.Keys.W))) {
                    players[num].moveVertical(1, blocks, bombs);
                    players[num].currDIR = 3;
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                    players[num].placeBomb(bombs);
                }
                if (!Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.D) && !Gdx.input.isKeyPressed(Input.Keys.S) && !Gdx.input.isKeyPressed(Input.Keys.W)) {
                    players[num].isStanding = true;
                } else {
                    players[num].isStanding = false;
                }
                break;
        }
    }

    @Override
    public void dispose(){
        b.dispose();
        tileSet.dispose();
    }

    public int[][] readMap(String fileName){
        int[][] map = new int[33][33];
        int r = 0;
        int c = 0;

        try {
            Scanner scanner = new Scanner(new File(fileName));
            Scanner line;
            while(scanner.hasNextLine()){
                line = new Scanner(scanner.nextLine());
                while(line.hasNextInt()){
                    if(c >= 33){
                        System.out.println("ERROR: Line #" + r + " - has too many blocks!");
                        return null;
                    } else {
                        map[r][c] = line.nextInt();
                        c++;
                    }
                }
                c = 0;
                r++;
            }

        } catch (FileNotFoundException e){

        }
        return map;

    }

    public void renderBlocks(SpriteBatch batch){
        for(Block[] row: blocks){
            for(Block b: row){
                b.render(batch);
                /*
                if(b.getType() == 1){
                    System.out.println("ROW | COL = " + b.row + "|" + b.col);
                }
                */
            }
        }
    }

    public void loadBlocks(){
        for(int r = 0; r < numMap.length; r++){
            for(int c = 0; c < numMap[r].length; c++){
                blocks[r][c] = new Block(r,c,1,numMap[r][c]);
            }
        }
    }

    public void checkBombs(){
        for(int i = 0; i < bombs.size(); i++){
            if(!bombs.get(i).isAlive() && !bombs.get(i).isExploding()){
                bombs.remove(i);
            }
        }
    }

    public void renderBombs(SpriteBatch batch){
        for(Bomb b : bombs){
            b.draw(batch, blocks, players);
        }
    }

    public void checkPlayers(){
        int aliveCounter = 0;
        for(Player p : players){
            if(p.getLives() > 0){
                aliveCounter++;
            }
        }
        if(aliveCounter == 0){
            System.out.println("DRAW");
            Gdx.app.exit();
        } else if (aliveCounter == 1){
            System.out.println("GAME OVER");
            Gdx.app.exit();
        }
    }

    public void renderInfo(Player p, SpriteBatch b){
        BitmapFont font = new BitmapFont();
        String color = p.getColor();
        int yStart = -1;

        if(color.equals("White")){
            yStart = 1000;
        } else if(color.equals("Black")){
            yStart = 800;
        } else if(color.equals("Red")){
            yStart = 600;
        } else if(color.equals("Green")){
            yStart = 400;
        } else if(color.equals("Blue")){
            yStart = 200;
        }

        font.draw(b, p.getColor() + ": ", 50, yStart);
        font.draw(b, "Lives: " + p.getLives(), 50, yStart - 25); //Font draws at top right
        font.draw(b, "Speed: " + p.getSpeed(), 50, yStart - 50); //Font draws at top right
        font.draw(b, "Bombs: " + p.getMaxBombs(), 50, yStart - 75); //Font draws at top right
        font.draw(b, "Radius: " + p.getRadius(), 50, yStart - 100); //Font draws at top right
    }

    /*
    private void sendInfo(int x, int y, int row, int column, int lives){
        try {
            JSONObject data = new JSONObject();
            data.put("x", x);
            data.put("y", y);
            data.put("row", row);
            data.put("col", column);
            data.put("lives", lives);
            WarpController.getInstance().sendGameUpdate(data.toString());
        }catch (Exception e){

        }
    }
    */
}
