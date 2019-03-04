package com.htransteven.bomberman;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;


import java.util.ArrayList;

/**
 * Created by Steven Tran on 5/13/2017.
 */
public class Player extends Rectangle{

    int origX = Gdx.graphics.getWidth() / 2 - 528; //Half the board, minus 16 blocks, and 1/2 a block
    int origY = 24;

    ArrayList<Sprite> imagesUP;
    ArrayList<Sprite> imagesDOWN;
    ArrayList<Sprite> imagesLEFT;
    ArrayList<Sprite> imagesRIGHT;

    public int speed, lives, radius, maxBombs, kills;
    int bombsPlaced;

    int row, col;

    private int myX, myY;

    private boolean tempGod = false;
    private float godTimer = 1.5f;

    private int color;
    int currIMG = 0;
    int currDIR = 0;
    float imgTimer = 0.15f;

    boolean inBomb = false;
    boolean isStanding = true;

    public Player(int c, int row, int column){
        super(Gdx.graphics.getWidth() / 2 - 528 + (32 * column), 24 + (32 * row), 32, 32); //For X and Y look at bottom of constructor

        this.row = row;
        col = column;

        color = c;
        speed = 2; //Default Speed: 2
        lives = 3; //Default Lives: 3
        radius = 1; //Default: 1
        maxBombs = 1; //Default Max: 1

        imagesUP = new ArrayList<Sprite>();
        imagesDOWN = new ArrayList<Sprite>();
        imagesLEFT = new ArrayList<Sprite>();
        imagesRIGHT = new ArrayList<Sprite>();
        setSprites();

        myX = origX + (32 * column);
        myY = origY + (32 * row); //Add 24 b/c map doesn't go all the way down the screen;
        super.setPosition(myX, myY);

    }

    public Player(int c, int row, int column, int s, int l, int rad, int b){
        super(Gdx.graphics.getWidth() / 2 - 528 + (32 * column), 24 + (32 * row), 32, 32); //For X and Y look at bottom of constructor

        this.row = row;
        col = column;

        color = c;
        speed = s;
        lives = l;
        radius = rad;
        maxBombs = b;

        imagesUP = new ArrayList<Sprite>();
        imagesDOWN = new ArrayList<Sprite>();
        imagesLEFT = new ArrayList<Sprite>();
        imagesRIGHT = new ArrayList<Sprite>();
        setSprites();

        myX = origX + (32 * column);
        myY = origY + (32 * row); //Add 24 b/c map doesn't go all the way down the screen;
    }

    public void update(SpriteBatch b){ //0 - Left, 1 - Right, 2 - Down, 3 - Up
        if(lives < 0){
            return;
        }
        positionSprites();
        if(currIMG >= imagesRIGHT.size()){
            currIMG = 0;
        }

        switch(currDIR){
            case 0:
                if(isStanding){
                    imagesLEFT.get(0).draw(b);
                } else {
                    imagesLEFT.get(currIMG).draw(b);
                }
                break;
            case 1:
                if(isStanding){
                    imagesRIGHT.get(0).draw(b);
                } else {
                    imagesRIGHT.get(currIMG).draw(b);
                }
                break;
            case 2:
                if(isStanding){
                    imagesDOWN.get(0).draw(b);
                } else {
                    imagesDOWN.get(currIMG).draw(b);
                }
                break;
            case 3:
                if(isStanding){
                    imagesUP.get(0).draw(b);
                } else {
                    imagesUP.get(currIMG).draw(b);
                }
                break;
        }

        if(imgTimer < 0){
            imgTimer = 0.15f;
            currIMG++;
            if(currIMG >= imagesLEFT.size()){ //Same # of images in sprites 8
                currIMG = 0;
            }
        } else {
            imgTimer -= Gdx.graphics.getDeltaTime();
        }

        if(tempGod){
            if(godTimer > 0) {
                godTimer -= Gdx.graphics.getDeltaTime();
            } else {
                tempGod = false;
                godTimer = 1.5f;
                setHitAlpha(1);
            }
        }
    }

    private void positionSprites(){
        for(Sprite s: imagesLEFT){
            s.setPosition(super.getX(), super.getY());
        }
        for(Sprite s: imagesRIGHT){
            s.setPosition(super.getX(), super.getY());
        }
        for(Sprite s: imagesDOWN){
            s.setPosition(super.getX(), super.getY());
        }
        for(Sprite s: imagesUP){
            s.setPosition(super.getX(), super.getY());
        }
    }

    private void setSprites(){

        //Sprite Player Dimensions 22x24 (Width x Height)
        switch(color){
            case 1:
                for(int i = 0; i < 8; i++) { //# of selection screen sprites = 9
                    imagesRIGHT.add(new Sprite(new TextureRegion(new Texture("white/right.png"), 16 * i, 0, 16, 24)));

                    for(Sprite s : imagesRIGHT){
                        Sprite temp = new Sprite(s);
                        temp.flip(true, false);
                        imagesLEFT.add(temp);
                    }

                    imagesUP.add(new Sprite(new TextureRegion(new Texture("white/up.png"), 14 * i, 0, 14, 24)));
                    imagesDOWN.add(new Sprite(new TextureRegion(new Texture("white/down.png"), 14 * i, 0, 14, 24)));
                }
                break;
            case 2:
                for(int i = 0; i < 8; i++) { //# of selection screen sprites = 9
                    imagesRIGHT.add(new Sprite(new TextureRegion(new Texture("black/right.png"), 16 * i, 0, 16, 24)));

                    for(Sprite s : imagesRIGHT){
                        Sprite temp = new Sprite(s);
                        temp.flip(true, false);
                        imagesLEFT.add(temp);
                    }

                    imagesUP.add(new Sprite(new TextureRegion(new Texture("black/up.png"), 14 * i, 0, 14, 24)));
                    imagesDOWN.add(new Sprite(new TextureRegion(new Texture("black/down.png"), 14 * i, 0, 14, 24)));
                }
                break;
            case 3:
                for(int i = 0; i < 8; i++) { //# of selection screen sprites = 9
                    imagesRIGHT.add(new Sprite(new TextureRegion(new Texture("red/right.png"), 16 * i, 0, 16, 24)));

                    for(Sprite s : imagesRIGHT){
                        Sprite temp = new Sprite(s);
                        temp.flip(true, false);
                        imagesLEFT.add(temp);
                    }

                    imagesUP.add(new Sprite(new TextureRegion(new Texture("red/up.png"), 14 * i, 0, 14, 24)));
                    imagesDOWN.add(new Sprite(new TextureRegion(new Texture("red/down.png"), 14 * i, 0, 14, 24)));
                }
                break;
            case 4:
                for(int i = 0; i < 8; i++) { //# of selection screen sprites = 9
                    imagesRIGHT.add(new Sprite(new TextureRegion(new Texture("green/right.png"), 16 * i, 0, 16, 24)));

                    for(Sprite s : imagesRIGHT){
                        Sprite temp = new Sprite(s);
                        temp.flip(true, false);
                        imagesLEFT.add(temp);
                    }

                    imagesUP.add(new Sprite(new TextureRegion(new Texture("green/up.png"), 14 * i, 0, 14, 24)));
                    imagesDOWN.add(new Sprite(new TextureRegion(new Texture("green/down.png"), 14 * i, 0, 14, 24)));
                }
                break;
            case 5:
                for(int i = 0; i < 8; i++) { //# of selection screen sprites = 9
                    imagesRIGHT.add(new Sprite(new TextureRegion(new Texture("blue/right.png"), 16 * i, 0, 16, 24)));

                    for(Sprite s : imagesRIGHT){
                        Sprite temp = new Sprite(s);
                        temp.flip(true, false);
                        imagesLEFT.add(temp);
                    }

                    imagesUP.add(new Sprite(new TextureRegion(new Texture("blue/up.png"), 14 * i, 0, 14, 24)));
                    imagesDOWN.add(new Sprite(new TextureRegion(new Texture("blue/down.png"), 14 * i, 0, 14, 24)));
                }
                break;
        }
        for(Sprite s: imagesLEFT){ //Adjust to Width: 32
            s.setSize(32, 32);
        }
        for(Sprite s: imagesRIGHT){ //Adjust to Width: 32
            s.setSize(32, 32);
        }
        for(Sprite s: imagesDOWN){ //Adjust to Width: 32
            s.setSize(32, 32);
        }
        for(Sprite s: imagesUP){ //Adjust to Width: 32
            s.setSize(32, 32);
        }
    }

    private void setHitAlpha(float alpha){
        for(Sprite s : imagesLEFT){
            s.setAlpha(alpha);
        }
        for(Sprite s : imagesRIGHT){
            s.setAlpha(alpha);
        }
        for(Sprite s : imagesUP){
            s.setAlpha(alpha);
        }
        for(Sprite s : imagesDOWN){
            s.setAlpha(alpha);
        }
    }

    public void moveHorizontal(int direction, Block[][] blocks, ArrayList<Bomb> bombs){ // 0 - Left / 1 - Right
        int prevX = myX;
        boolean blockFlag = false;
        boolean bombFlag = false;
        Bomb tempBomb = null;

        inBomb = false;
        for(Bomb b: bombs){
            if(this.overlaps(b)){
                inBomb = true;
                tempBomb = b;
            }
        }

        switch(direction) {
            case 0:
                myX -= speed;
                super.setPosition(myX, myY);
                for(Block[] row : blocks){
                    for(Block b : row){
                        if(b.getType() != 2 && this.overlaps(b)){
                            myX = prevX;
                            super.setPosition(myX, myY);
                            blockFlag = true;
                        }
                    }
                }
                for(Bomb b : bombs){
                    if(tempBomb != null && b.column != tempBomb.column && this.overlaps(b)){
                        bombFlag = true;
                    }
                }
                for(Bomb b : bombs){
                    if(this.overlaps(b)){
                        myX = prevX;
                        super.setPosition(myX, myY);
                    }
                    if(inBomb && !blockFlag && !bombFlag){
                        myX -= speed;
                        super.setPosition(myX, myY);
                    }
                }
                break;
            case 1:
                myX += speed;
                super.setPosition(myX, myY);
                for(Block[] row : blocks){
                    for(Block b : row){
                        if(b.getType() != 2 && this.overlaps(b)){
                            myX = prevX;
                            super.setPosition(myX, myY);
                            blockFlag = true;
                        }
                    }
                }
                for(Bomb b : bombs){
                    if(tempBomb != null && b.column != tempBomb.column && this.overlaps(b)){
                        bombFlag = true;
                    }
                }
                for(Bomb b : bombs){
                    if(this.overlaps(b)){
                        myX = prevX;
                        super.setPosition(myX, myY);
                    }
                    if(inBomb && !blockFlag && !bombFlag){
                        myX += speed;
                        super.setPosition(myX, myY);
                    }
                }
                break;
        }

        col = (myX - origX) / 32;
        row = (myY - origY) / 32;
    }

    public void moveVertical(int direction, Block[][] blocks, ArrayList<Bomb> bombs){ // 0 - Down / 1 - Up
        int prevY = myY;
        boolean blockFlag = false;
        boolean bombFlag = false;
        Bomb tempBomb =  null;

        inBomb = false;
        for(Bomb b: bombs){
            if(this.overlaps(b)){
                inBomb = true;
                tempBomb = b;
            }
        }

        switch(direction){
            case 0:
                myY -= speed;
                super.setPosition(myX, myY);
                for(Block[] row : blocks){
                    for(Block b : row){
                        if(b.getType() != 2 && this.overlaps(b)){
                            myY = prevY;
                            super.setPosition(myX, myY);
                            blockFlag = true;
                        }
                    }
                }
                for(Bomb b : bombs){
                    if(tempBomb != null && b.row != tempBomb.row && this.overlaps(b)){
                        bombFlag = true;
                    }
                }
                for(Bomb b : bombs){
                    if(this.overlaps(b)){
                        myY = prevY;
                        super.setPosition(myX, myY);
                    }
                    if(inBomb && !blockFlag && !bombFlag){
                        myY -= speed;
                        super.setPosition(myX, myY);
                    }
                }
                break;
            case 1:
                myY += speed;
                super.setPosition(myX, myY);
                for(Block[] row : blocks){
                    for(Block b : row){
                        if(b.getType() != 2 && this.overlaps(b)){
                            myY = prevY;
                            super.setPosition(myX, myY);
                            blockFlag = true;
                        }
                    }
                }
                for(Bomb b : bombs){
                    if(tempBomb != null && b.row != tempBomb.row && this.overlaps(b)){
                        bombFlag = true;
                    }
                }
                for(Bomb b : bombs){
                    if(this.overlaps(b)){
                        myY = prevY;
                        super.setPosition(myX, myY);
                    }
                    if(inBomb && !blockFlag && !bombFlag){
                        myY += speed;
                        super.setPosition(myX, myY);
                    }
                }
                break;
            }

        col = (myX - origX) / 32;
        row = (myY - origY) / 32;
    }

    public void placeBomb(ArrayList<Bomb> b){

        col = (myX - origX) / 32;
        row = (myY - origY) / 32;

        int bombX = origX + (32 * col);
        int bombY = origY + (32 * row);

        if(maxBombs - bombsPlaced > 0){
            b.add(new Bomb(this, radius, bombX, bombY, row, col));
            bombsPlaced++;
            inBomb = true;
        }
    }

    public int getPosX(){
        return myX;
    }

    public int getPosY(){
        return myY;
    }

    public int getRow() {
        row = (myY - origY) / 32;
        return row; }

    public int getCol() {
        col = (myX - origX) / 32;
        return col; }

    public int getLives() { return lives; }

    public int getSpeed() { return speed; }

    public int getRadius() { return radius; }

    public int getMaxBombs() { return maxBombs; }

    public void decrementBomb(){
        bombsPlaced--;
    }

    public void incrementKills(){ kills++; }

    public void gotHit(Player enemy){
        if(!tempGod){
            lives--;
            enemy.incrementKills();
            tempGod = true;
            setHitAlpha(0.75f);
        }
    }

    public String getColor(){
        switch(color){
            case 1:
                return "White";
            case 2:
                return "Black";
            case 3:
                return "Red";
            case 4:
                return "Green";
            case 5:
                return "Blue";
            default:
                return "";
        }
    }
}
