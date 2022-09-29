package demolition;

import processing.core.PApplet;
import processing.core.PImage;
import java.util.*;
import java.io.*;


public class Player {

    protected int yVal = 0;
    protected int xVal = 0;
    protected PImage image;
    protected PImage[] down = new PImage [4];
    protected PImage[] up = new PImage [4];
    protected PImage[] right = new PImage [4];
    protected PImage[] left = new PImage [4];
    protected PImage[] imageTodraw = down;
    protected int counterTurn = 0;
    protected String[][] wallsCheck = MapGame.mapping;
    protected int counting = 0;
    
    private int playerLives;
    private boolean killed = false;
    public boolean winning = false;

    /**
     * Constructor for a player, requires x, y values, an image and num of lives
     * the default value of killed is false.
     * @param xVal in pixel
     * @param yVal in pixel
     * @param image the image of the player
     * @param playerLives the lives that the player has
     */    
    public Player(int xVal, int yVal, PImage image, int playerLives) {
        this.xVal = xVal;
        this.yVal = yVal;
        this.image = image;
        this.playerLives = playerLives;
        this.killed = false;

    }
    
    /**
     * Set imageTodraw to the corresponding method which is down
     * Checking the x,y coordinates of the player's downward moves to see if 
     * they encouter any walls. Set y value to be itself, and return false if it does, otherwise 
     * increment the yVal by 32 and return true.
     * @return true or false,
     */
    public boolean moveDown() {
        int row = (yVal + 32)/32 - 1;
        int col = xVal/32;
        imageTodraw = down;

        if (wallsCheck [row][col].equals("W") ||  wallsCheck [row][col].equals("B")) {
            yVal = yVal;
        }else {
            yVal += 32;
            return true;
        }
        return false;
    }

    /**
     * Set imageTodraw to the corresponding method which is up
     * Checking the x,y coordinates of the player's upward moves to see if 
     * they encouter any walls. Set y value to be itself, and return false if it does, otherwise 
     * decrement the yVal by 32 and return true.
     * @return true or false,
     */
    public boolean moveUp() {
        int row = (yVal - 32)/32 - 1;
        int col = xVal/32;
        imageTodraw = up;

        if (wallsCheck [row][col].equals("W") ||  wallsCheck [row][col].equals("B")) {
            yVal = yVal;
        }else {
            yVal -= 32;
            return true;
        }
        return false;
    }

    /**
     * Set imageTodraw to the corresponding method which is left
     * Checking the x,y coordinates of the player's left moves to see if 
     * they encouter any walls. Set x value to be itself, and return false if it does, otherwise 
     * decrement the xVal by 32 and return true.
     * @return true or false,
     */
    public boolean moveLeft() {
        int row = (yVal/32)-1;
        int col = (xVal - 32)/32;
        imageTodraw = left;

        if (wallsCheck [row][col].equals("W") ||  wallsCheck [row][col].equals("B")) {
            xVal = xVal;
        }else {
            xVal-=32;
            return true;
        }
        return false;
    }

    /**
     * Set imageTodraw to the corresponding method which is right
     * Checking the x,y coordinates of the player's right moves to see if 
     * they encouter any walls. Set x value to be itself, and return false if it does, otherwise 
     * increment the xVal by 32 and return true.
     * @return true or false,
     */
    public boolean moveRight() {
        int row = (yVal/32)-1;
        int col = (xVal + 32)/32;
        imageTodraw = right;

        if (wallsCheck [row][col].equals("W") ||  wallsCheck [row][col].equals("B")) {
            xVal = xVal;
        }else{
            xVal+=32;
            return true;
        }
        return false;
    }

    /**
     * @return current x coordinate of the player in the map
     */
    public int getxVal(){
        return this.xVal;
    }

    /**
     * @return current y coordinate of the player in the map
     */
    public int getyVal(){
        return this.yVal;
    }

    /**
     * if playerLives is less than 0, then set it equal to 0 and return playerLives,
     * otherwise return the current playerLives 
     * @return playerLives
     */
    public int getLives() {
        if (this.playerLives < 0) {
            this.playerLives = 0;
        }
        return this.playerLives;
    }

    /**
     * if the player's killed is true then set the killed back to false and return false.
     * Otherwise, return true 
     * @return true or false
     */
    public boolean isAlive() {
        if (this.killed == true) {
            this.killed = false;
            return false;

        } else {
            return true;
        }
    }

    /**
     * if the player's lives equal to 0, return true, otherwise, return false
     * @return true or false
     */
    public boolean isGameOver() {
        if (this.playerLives == 0) {
            return true;
        }
        return false;
    }

    /**
     * given a bomb, this will check if the player is caught in the explosion of the bomb
     * from all 4 directions
     * decrement the player's lives by 1 and set killed to true if it does 
     * @param bomb the bomb object
     */
    public void bombCollision(Bomb bomb) {
        // verbottom
        if (this.yVal <= bomb.getyVal() + bomb.BOTTOM && this.yVal >= bomb.getyVal()
        && this.xVal == bomb.getxVal()) {
            this.playerLives -= 1;
            this.killed = true;
        
        // vertop
        } else if (this.yVal >= bomb.getyVal() - bomb.TOP && this.yVal <= bomb.getyVal()
        && this.xVal == bomb.getxVal()) {
            this.playerLives -= 1;
            this.killed = true;
        
        // honright
        } else if (this.xVal <= bomb.getxVal() + bomb.RIGHT && this.xVal >= bomb.getxVal() 
        && this.yVal == bomb.getyVal()) {
            this.playerLives -= 1;
            this.killed = true;

        // honleft
        } else if (this.xVal >= bomb.getxVal() - bomb.LEFT && this.xVal <= bomb.getxVal() 
        && this.yVal == bomb.getyVal()) {
            this.playerLives -= 1;
            this.killed = true;
        }
    }

    /**
     * given the list of both red and yellow enemies, this will check if at anytime
     * the player will encouter to the enemies.
     * decrement the player's lives by 1 and set killed to true if it does 
     * @param red which is the list of red enemies
     * @param yellow which is the list of yellow enemies
     */
    public void enemiesCollision (List<Enemies> red, List<yellowEnemies> yellow) {
        for (int i = 0; i < red.size(); i++) {
            if (red.get(i).getLives() != 0) {
                if (this.xVal == red.get(i).getxVal() && this.yVal == red.get(i).getyVal()) {
                    this.playerLives-=1;
                    this.killed=true;
                }
            }
        }
        for (int i = 0; i < yellow.size(); i++) {
            if (yellow.get(i).getLives() != 0) {
                if (this.xVal == yellow.get(i).getxVal() && this.yVal == yellow.get(i).getyVal()) {
                    this.playerLives-=1;
                    this.killed=true;
                }
            }
        }
    }

    /**
     * given the PApplet object which help to load the image of the red enemies's 4 directions
     * @param app PApplet object to load the image of the player
     */
    public void loadImage (PApplet app) {
        for (int i=0; i < 4; i++) {
            down[i] = app.loadImage("src/main/resources/player/player"+(i+1)+".png");
            up[i] = app.loadImage("src/main/resources/player/player_up"+(i+1)+".png");
            right[i] = app.loadImage("src/main/resources/player/player_right"+(i+1)+".png");
            left[i] = app.loadImage("src/main/resources/player/player_left"+(i+1)+".png");
        }

    }

    /**
     * given the coordination of x and y of the goal tile which will be check against 
     * the player's current location to see if the player reach the goal.
     * set the winning to true and return true if he does. Otherwise return false.
     * @param goalx which is the goal x-coordinate location in pixel
     * @param goaly which is the goal y-coordinate location in pixel
     * @return winning
     */
    public boolean isWin(int goalx, int goaly) {
        if (this.xVal == goalx && this.yVal == goaly) {
            winning = true;
            return winning;
        }
        return winning;
    }

    /**
     * given the PApplet object which help to draw the image of the red enemies's corresponding 
     * direction. If killed equals to true and the player's lives still greater than 0 than 
     * set the x,y of player to be back to the original position
     * @param app PApplet object to draw the image of the player
     */
    public void draw(PApplet app) {
        app.image(imageTodraw[counterTurn],this.xVal,this.yVal);
        if (counting == 8) {
            counterTurn = (counterTurn + 1) % imageTodraw.length;
            counting = 0;
        }
        counting+=1;
    }
}