package demolition;

import processing.core.PApplet;
import processing.core.PImage;
import java.util.*;


public class Bomb {
    private int yVal = 0;
    private int xVal = 0;
    private PImage image;
    private String[][] wallsBroken;
    private int BOMBcount = 0;
    
    private static PImage imageCentre;
    private static PImage imageVer;
    private static PImage imageEndtop;
    private static PImage imageEndbottom;
    private static PImage imageHon;
    private static PImage imageEndright;
    private static PImage imageEndleft;
    private static PImage[] bombBefore = new PImage [8];

    public int LEFT = 0;
    public int RIGHT = 0;
    public int TOP = 0;
    public int BOTTOM = 0;

    /**
     * Constructor for a bomb, requires x, y values, and a current map 2d array
     * @param xVal the coordinate of bomb's x coordinate in pixel
     * @param yVal the coordinate of bomb's y coordinate in pixel
     * @param mapPlay the 2d array current map
     */
    public Bomb(int xVal, int yVal, String[][] mapPlay) {
        this.xVal = xVal;
        this.yVal = yVal;
        wallsBroken = mapPlay;
    }
    
    /**
     * return x coordinate of the bomb in the map
     * @return xVal
     */
    public int getxVal() {
        return this.xVal;
    }

    /**
     * return y coordinate of the bomb in the map
     * @return yVal
     */
    public int getyVal() {
        return this.yVal;
    }

    /**
     * given the PApplet object to load bomb's images and explosion images 
     * @param app PApplet object used to load images
     */
    public static void explosionLoad (PApplet app) {
        imageCentre = app.loadImage("src/main/resources/explosion/centre.png");
        imageVer = app.loadImage("src/main/resources/explosion/vertical.png");
        imageEndtop = app.loadImage("src/main/resources/explosion/end_top.png");
        imageEndbottom = app.loadImage("src/main/resources/explosion/end_bottom.png");
        
        imageHon = app.loadImage("src/main/resources/explosion/horizontal.png");
        imageEndright = app.loadImage("src/main/resources/explosion/end_right.png");
        imageEndleft = app.loadImage("src/main/resources/explosion/end_left.png");

        for (int i=0; i < 8; i++) {
            bombBefore[i] = app.loadImage("src/main/resources/bomb/bomb"+(i+1)+".png");
        }
    
    }

    /**
     * given any 2 coordinates on the map to check if they are either broken or thick walls
     * return 1 if its thick, -1 if its broken and 0 for none
     * @param i which represents row in 2d array map
     * @param j which represents collumn in 2d array map
     * @return 1,-1 or 0
     */
    public int wall(int i, int j) {
        if(wallsBroken[i][j].equals("W")) {
            return 1;

        }else if (wallsBroken[i][j].equals("B")) {
            wallsBroken[i][j] = " ";
            return -1;
        }
        
        return 0;
    }

    /**
     * calculate the index of 2d array from the pixel to check if there is any
     * any walls (thick or broken) within 1, or 2 grid space from the bomb location to draw the bottom of
     * the explosion.
     * Return 0 if theres thick wall next to the bomb, or return 32 and make change 
     * to the map if theres a broken wall within 1 grid space from the bomb. Same process goes 
     * for 2 grid space(fixed bottom 2).
     * @return 0,32,64
     */
    public int drawBottom() {
        int fixed = this.yVal;
        int horizontal = xVal/32;

        int fixedBottom1 = (fixed + 32)/32 - 1;
        int fixedBottom2 = (fixed + 64)/32 - 1;

        if (wallsBroken[fixedBottom1][horizontal].equals("W")) {
            return 0;

        } else if (wallsBroken[fixedBottom1][horizontal].equals("B")) {
            wallsBroken[fixedBottom1][horizontal] = " ";
            return 32;

        } else if (wallsBroken [fixedBottom2][horizontal].equals("W")) {
            return 32;

        } else if (wallsBroken [fixedBottom2][horizontal].equals("B")) {
            wallsBroken[fixedBottom2][horizontal] = " ";
            return 64;

        } else {
            return 64;
        }
            
    }
    
    /**
     * calculate the index of 2d array from the pixel to check if there is any
     * any walls (thick or broken) within 1, or 2 grid space from the bomb location to draw the top
     * of the explosion.
     * Return 0 if theres thick wall next to the bomb, or return 32 and make change 
     * to the map if theres a broken wall within 1 grid space from the bomb. Same process goes 
     * for 2 grid space(fixed top 2).
     * @return 0,32,64
     */
    public int drawTop() {
        int fixed = this.yVal;
        int horizontal = xVal/32;

        int fixedTop1 = (fixed - 32)/32 - 1;
        int fixedTop2 = (fixed - 64)/32 - 1;

        if (wallsBroken [fixedTop1][horizontal].equals("W")) {            
            return 0;

        } else if (wallsBroken [fixedTop1][horizontal].equals("B")) {
            wallsBroken[fixedTop1][horizontal] = " ";
            return 32;

        } else if (wallsBroken [fixedTop2][horizontal].equals("W")) {
            return 32;

        } else if (wallsBroken [fixedTop2][horizontal].equals("B")) {
            wallsBroken[fixedTop2][horizontal] = " ";
            return 64;

        } else {
            return 64;
        }
    }

    /**
     * calculate the index of 2d array from the pixel to check if there is any
     * any walls (thick or broken) within 1, or 2 grid space from the bomb location to draw the right
     * of the explosion.
     * Return 0 if theres thick wall next to the bomb, or return 32 and make change 
     * to the map if theres a broken wall within 1 grid space from the bomb. Same process goes 
     * for 2 grid space(fixed right 2).
     * @return 0,32,64
     */
    public int drawRight() {
        int fixed = this.xVal;
        int col = (this.yVal)/32 - 1;

        int fixedRight1 = (fixed + 32)/32;
        int fixedRight2 = (fixed + 64)/32;

        if (wallsBroken[col][fixedRight1].equals("W")) {
            return 0;

        } else if (wallsBroken[col][fixedRight1].equals("B")) {
            wallsBroken[col][fixedRight1] = " ";
            return 32;

        } else if (wallsBroken[col][fixedRight2].equals("W")) {
            return 32;

        } else if (wallsBroken[col][fixedRight2].equals("B")) {
            wallsBroken[col][fixedRight2] = " ";
            return 64;

        } else{
            return 64;
        }
    }

    /**
     * calculate the index of 2d array from the pixel to check if there is any
     * any walls (thick or broken) within 1, or 2 grid space from the bomb location to draw the left
     * of the explosion.
     * Return 0 if theres thick wall next to the bomb, or return 32 and make change 
     * to the map if theres a broken wall within 1 grid space from the bomb. Same process goes 
     * for 2 grid space(fixed right 2).
     * @return 0,32,64
     */
    public int drawLeft() {
        int fixed = this.xVal;
        int col = (this.yVal)/32 - 1;

        int fixedLeft1 = (fixed - 32)/32;
        int fixedLeft2 = (fixed - 64)/32;

        if (wallsBroken[col][fixedLeft1].equals("W")) {
            return 0;

        } else if (wallsBroken[col][fixedLeft1].equals("B")) {
            wallsBroken[col][fixedLeft1] = " ";
            return 32;

        } else if (wallsBroken[col][fixedLeft2].equals("W")) {
            return 32;

        } else if (wallsBroken[col][fixedLeft2].equals("B")) {
            wallsBroken[col][fixedLeft1] = " ";
            return 64;
        
        } else {
            return 64;
        }
    }


    /**
     * given the PApplet object to draw the explosion.
     * if BOMBcount equal 0 (to make sure it will only be called once) then call all the draw method above
     * to get the information how to draw the explosion.
     * draw none if the returned value from the draw method is 0, draw explosion for 1 grid space from
     * the bomb, and if returned value is 64, then draw explosion for 2 grid space.
     * verExplosion here is for adjusting the explosion image y-coordinate
     * @param app PApplet object used to draw images
     */
    public void explodeDraw(PApplet app) {
        int verExplosion = (yVal + 19);
        // centre
        app.image(imageCentre, xVal, yVal + 19);

        if (BOMBcount == 0) {
            LEFT = drawLeft();
            TOP = drawTop();
            BOTTOM = drawBottom();
            RIGHT = drawRight();
        }
        BOMBcount+=1;

        if (LEFT == 32) {
            app.image(imageHon, xVal-32, verExplosion);

        } else if (LEFT == 64) {
            app.image(imageHon, xVal-32, verExplosion);
            app.image(imageEndleft, xVal-64, verExplosion);

        } else {
            ;
        }

        if (RIGHT == 32) {
            app.image(imageHon, this.xVal + 32, verExplosion);
        } else if (RIGHT == 64) {
            app.image(imageHon, this.xVal + 32, verExplosion);
            app.image(imageEndright, this.xVal + 64, verExplosion);

        } else {
            ;
        }

        if (TOP == 32) {
            app.image(imageVer, xVal, verExplosion - 32);

        } else if (TOP == 64) {
            app.image(imageVer,xVal, verExplosion - 32);
            app.image(imageEndtop,xVal, verExplosion - 64);

        } else {
            ;
        }

        if (BOTTOM== 32) {
            app.image(imageVer, xVal, verExplosion + 32);

        } else if (BOTTOM == 64) {
            app.image(imageVer, xVal, verExplosion + 32);
            app.image(imageEndbottom, xVal, verExplosion + 64);

        } else {
            ;
        }
    }

    /**
     * given the PApplet object to draw bomb's before exploding, and 
     * cycle to draw the next image in the array of the countdown of the bomb 
     * before exploding so that it looks animated
     * @param app PApplet object used to draw images
     * @param cycle counter to draw the next image in the cycle of the bomb before explosion
     */
    public void draw(PApplet app, int cycle) {
        app.image(bombBefore[cycle], xVal, yVal+15);

    }
}