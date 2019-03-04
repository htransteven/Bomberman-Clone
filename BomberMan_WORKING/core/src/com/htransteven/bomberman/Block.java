package com.htransteven.bomberman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

/**
 * Created by Steven Tran on 5/16/2017.
 */
public class Block extends Rectangle{
    int row, col, level, type;

    int myX, myY;

    Texture tileSet;
    ArrayList<Sprite> tiles;
    Sprite currType;


    public Block(int r, int c, int lvl, int t){
        super(Gdx.graphics.getWidth() / 2 - 528 + (32 * c), Gdx.graphics.getHeight() - (32 * (r + 1)), 32, 32);
        row = r;
        col = c;
        level = lvl;
        type = t;
        tiles = new ArrayList<Sprite>();
        tileSet = new Texture("tiles_" + lvl + ".png");
        loadTextures();
        setPosition();
    }

    public void loadTextures(){
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

        setType(type);
    }

    public void setType(int blockType){
        /*
        System.out.println("PREV TYPE: " + type);
        System.out.println("NEW TYPE: " + blockType);
        System.out.println("R | C = " + this.row + " | " + this.col);
        */
        type = blockType;
        currType = tiles.get(type);
    }

    public void render(SpriteBatch b){
        currType.draw(b);
    }

    public void setPosition(){
        //Set position
        int h = Gdx.graphics.getHeight();
        int startX = Gdx.graphics.getWidth() / 2 - 528; //Half the board, minus 16 blocks, and 1/2 a block
        for(Sprite s : tiles) {
            s.setSize(32, 32);
            s.setPosition(startX + (32 * col), h - (32 * (row + 1)));
            //s.setPosition((32 * col), (32 * row));
        }
        myX = 32 * col;
        myY = h - (32 * (row + 1));
    }

    public int getLeftX(){
        return myX;
    }
    public int getRightX() { return myX + 32;}

    public int getTopY() { return myY;}
    public int getBotY() { return myY - 32;};

    public int getType(){
        return type;
    }
}
