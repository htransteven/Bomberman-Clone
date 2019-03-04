package com.htransteven.bomberman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Created by Steven Tran on 5/16/2017.
 */
public class Bomb extends Rectangle {

    int origX = Gdx.graphics.getWidth() / 2 - 528; //Half the board, minus 16 blocks, and 1/2 a block
    int origY = 24;

    int explodePosX = 0;
    int explodeNegX = 0;
    int explodePosY = 0;
    int explodeNegY = 0;

    boolean checkedRadius = false;

    ArrayList<Sprite> bombPic;

    Sprite explosionCenter;
    ArrayList<Sprite> explosionHoriz;
    ArrayList<Sprite> explosionVert;

    ArrayList<Sprite> crossExplosions;

    int currIMG = 0;
    float imgTimer = 0.3f;

    int radius;

    int row, column;
    int myX, myY;

    float timer = 3;
    float expTimer = 1;

    boolean alive;
    boolean exploding = false;

    Player owner;
    boolean checkDecerement = false;

    public Bomb(Player p, int rad, int x, int y, int r, int c) {
        super(x,y,32,32);

        owner = p;

        radius = rad;
        row = r;
        column = c;
        myX = x;
        myY = y;

        bombPic = new ArrayList<Sprite>();
        explosionCenter = new Sprite(new TextureRegion(new Texture("explosions1.png"), 0, 0, 16, 16));
        explosionHoriz = new ArrayList<Sprite>();
        explosionVert = new ArrayList<Sprite>();
        crossExplosions = new ArrayList<Sprite>();

        for(int i = 0; i < 4; i++) { //# of selection screen sprites = 4, Basic Bomb is first 4 images
            bombPic.add(new Sprite(new TextureRegion(new Texture("bombTypes.png"), (16 * i) + i, 0, 16, 16)));
        }

        for(int i = 1; i <= 4; i++){
            if(i % 2 == 0){
                explosionVert.add(new Sprite(new TextureRegion(new Texture("explosions1.png"), (16 * i) + i, 0, 16, 16)));
            } else {
                explosionHoriz.add(new Sprite(new TextureRegion(new Texture("explosions1.png"), (16 * i) + i, 0, 16, 16)));
            }
        }

        for(Sprite s: bombPic){ //Adjust to Width: 32
            s.setSize(32, 32);
            s.setPosition(origX + (c * 32), origY + (r * 32));
        }

        alive = true;
    }

    public void draw(SpriteBatch b, Block[][] blocks, Player[] p){
        if(alive) {
            bombPic.get(currIMG).draw(b);
        }
        updateBomb(Gdx.graphics.getDeltaTime(), b, blocks, p);
        if(!alive) {
            if (expTimer > 0) {
                for (Sprite s : crossExplosions) {
                    s.draw(b);
                }

                expTimer -= 0.1f;
            } else {
                exploding = false;
            }
        }

    }

    public void updateBomb(float delta, SpriteBatch b, Block[][] blocks, Player[] p){
        if(timer <= 0){
            explode(blocks, p);
        } else {
            timer -= delta;
        }

        if(imgTimer <= 0){
            currIMG++;
            if(currIMG >= bombPic.size()){
                currIMG = 0;
            }
            imgTimer = 0.3f;
        } else {
            imgTimer -= delta;
        }
    }

    public void explode(Block[][] blocks, Player[] p){
        exploding = true;
        alive = false;

        if(!checkedRadius) {
            checkRadius(blocks, p);
            checkedRadius = true;
        }
        //System.out.println(explodePosX + "|" + explodeNegX + "|" + explodePosY + "|" + explodeNegY);
        setExplosions();
        if(!checkDecerement){
            owner.decrementBomb();
            checkDecerement = true;
        }
    }

    public boolean isAlive(){
        return alive;
    }
    public boolean isExploding(){ return exploding; }

    public void setExplosions(){
        Sprite horizontalExplosion = explosionHoriz.get(0);
        Sprite verticalExplosion = explosionVert.get(0);
        Sprite horizontalEndExplosion = explosionHoriz.get(1);
        Sprite verticalEndExplosion = explosionVert.get(1);
        explosionCenter.setPosition(origX + (column * 32), origY + (row * 32));
        crossExplosions.add(explosionCenter);
        for(int count = 1; count <= 4; count++) {
            switch(count){
                case 1: //RIGHT EXPLOSIONS
                    for (int i = 1; i <= explodePosX; i++) {
                        Sprite temp;
                        if (i != explodePosX) {
                            temp = new Sprite(horizontalExplosion);
                        } else {
                            temp = new Sprite(horizontalEndExplosion);
                        }
                        temp.setPosition(explosionCenter.getX() + (32 * i), explosionCenter.getY());
                        crossExplosions.add(temp);
                    }
                    break;
                case 2: //DOWN EXPLOSIONS
                    for (int i = 1; i <= explodeNegY; i++) {
                        Sprite temp;
                        if (i != explodeNegY) {
                            temp = new Sprite(verticalExplosion);
                        } else {
                            temp = new Sprite(verticalEndExplosion);
                        }
                        temp.setPosition(explosionCenter.getX() , explosionCenter.getY() + (32 * i));
                        crossExplosions.add(temp);
                    }
                    break;
                case 3: //LEFT EXPLOSIONS
                    for (int i = 1; i <= explodeNegX; i++) {
                        Sprite temp;
                        if (i != explodeNegX) {
                            temp = new Sprite(horizontalExplosion);
                        } else {
                            temp = new Sprite(horizontalEndExplosion);
                        }
                        temp.flip(true, false);
                        temp.setPosition(explosionCenter.getX() - (32 * i), explosionCenter.getY());
                        crossExplosions.add(temp);
                    }
                    break;
                case 4: //UP EXPLOSIONS
                    for (int i = 1; i <= explodePosY; i++) {
                        Sprite temp;
                        if (i != explodePosY) {
                            temp = new Sprite(verticalExplosion);
                        } else {
                            temp = new Sprite(verticalEndExplosion);
                        }
                        temp.flip(false, true);
                        temp.setPosition(explosionCenter.getX() , explosionCenter.getY() - (32 * i));
                        crossExplosions.add(temp);
                    }
                    break;
            }
        }
        for(Sprite s : crossExplosions){
            s.setSize(32,32);
        }
    }

    public void checkRadius(Block[][] blocks, Player[] playerList){
        explodePosX = 0;
        explodeNegX = 0;
        explodePosY = 0;
        explodeNegY = 0;
        for(int r = 1; r <= radius; r++){ //DOWN
            for(Player p : playerList){
                if (p.overlaps(new Rectangle(this.getX(), this.getY() + (32 * r), 32, 32))) {
                    p.gotHit(owner); //Increment through gotHit(Player enemy) so incrementKills() is only called once
                }
            }
            if((blocks.length - 1) - row - r == blocks.length - 1){
                explodeNegY = r - 1; //Get explosion before the barrier
                break;
            }
            int type = blocks[(blocks.length - 1) - row - r][column].getType();
            if(type == 0) {
                break;
            } else if(type == 1){
                blocks[(blocks.length - 1) - row  - r][column].setType(2);
                explodeNegY = r;
                break;
            } else {
                explodeNegY++;
            }
        }
        for(int r = 1; r <= radius; r++){ //UP
            for(Player p : playerList){
                if (p.overlaps(new Rectangle(this.getX(), this.getY() - (32 * r), 32, 32))) {
                    p.gotHit(owner);
                }
            }
            if((blocks.length - 1) - row + r == 0){
                explodePosY = r - 1;
                break;
            }
            int type = blocks[(blocks.length - 1) - row + r][column].getType();
            if(type == 0) {
                break;
            } else if(type == 1){
                blocks[(blocks.length - 1) - row  + r][column].setType(2);
                explodePosY = r;
                break;
            } else {
                explodePosY++;
            }
        }
        for(int c = 1; c <= radius; c++){ //RIGHT
            for(Player p : playerList) {
                if (p.overlaps(new Rectangle(this.getX() - (32 * c), this.getY(), 32, 32))) {
                    p.gotHit(owner);
                }
            }
            if(column + c == blocks[(blocks.length - 1) - row].length - 1){
                break;
            }
            int type = blocks[(blocks.length - 1) - row][column + c].getType();
            //System.out.println("RIGHT: "  + blocks[(blocks.length - 1) - row][column+c].row + " | " + blocks[(blocks.length - 1) - row][column+c].col + " | TYPE - " + blocks[(blocks.length - 1) - row][column+c].getType());
            if(type == 0){
                break;
            } else if(type == 1){
                blocks[(blocks.length - 1) - row][column + c].setType(2);
                explodePosX = c;
                break;
            } else {
                explodePosX++;
            }
        }
        //System.out.println("\nLEFT");
        for(int c = 1; c <= radius; c++){ //LEFT
            for(Player p : playerList) {
                if (p.overlaps(new Rectangle(this.getX() + (32 * c), this.getY(), 32, 32))) {
                    p.gotHit(owner);
                }
            }
            if(column - c == 0){
                break;
            }
            int type = blocks[(blocks.length - 1) - row][column - c].getType();
            //System.out.println("LEFT: "  + blocks[(blocks.length - 1) - row][column-c].row + " | " + blocks[(blocks.length - 1) - row][column-c].col+ " | TYPE - " + blocks[(blocks.length - 1) - row][column+c].getType());
            if(type == 0){
                break;
            } else if(type == 1){
                blocks[(blocks.length - 1) - row][column - c].setType(2);
                explodeNegX = c;
                break;
            } else {
                explodeNegX++;
            }
        }
        //System.out.println("\nDONE");
    }
}
