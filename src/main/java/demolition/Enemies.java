package demolition;

import processing.core.PApplet;
import processing.core.PImage;
import java.util.*;

public class Enemies extends Player {

    protected String isBlocked = "right";
    /**
     * Constructor for a redEnemies, requires x, y values, an image and num of lives
     * @param xVal in pixel
     * @param yVal in pixel
     * @param image the image of the enemies
     * @param lives lives the enemies have
     */
    public Enemies (int xVal, int yVal, PImage image, int lives) {
        super(xVal,yVal,image,lives);
    }

    /**
     * control the direction of yellowEnemies and assign the corresponding images
     */
    public void moving () {
        if (isBlocked.equals("left")) {
            moveLeft();
            this.imageTodraw = this.left;
        } else if (isBlocked.equals("right")) {
            moveRight();
            this.imageTodraw = this.right;
        } else if (isBlocked.equals("down")) {
            moveDown();
            this.imageTodraw = this.down;
        } else if (isBlocked.equals("up")) {
            moveUp();
            this.imageTodraw = this.up;
        }
        
    }

    /**
     * checking the x,y coordinates of redEnemies' left moves to see if 
     * they encouter any walls. Set isBlocked to be "right" (this is chose randomly), and return false if it does, otherwise 
     * decrement the xVal by 32 and return true.
     * @return true or false,
     */
    public boolean moveLeft() {
        int rowLeft = (yVal/32) - 1;
        int colLeft = (xVal - 32)/32;

        if (wallsCheck [rowLeft][colLeft].equals("W") ||  wallsCheck [rowLeft][colLeft].equals("B")) {
            isBlocked = "right";
            // return false;
            
        } else {
            xVal-=32;
            return true;
        }
        return false;
            
    }

    /**
     * checking the x,y coordinates of redEnemies' right moves to see if 
     * they encouter any walls. Set isBlocked to be "down"(this is chose randomly), and return false if it does, otherwise 
     * increment the xVal by 32 and return true.
     * @return true or false,
     */
    public boolean moveRight() {
        int rowRight = (yVal/32)-1;
        int colRight = (xVal + 32)/32;

        if (wallsCheck [rowRight][colRight].equals("W") ||  wallsCheck [rowRight][colRight].equals("B")) {            
            isBlocked = "down";

        } else {
            xVal+=32;
            return true;
        }
        return false;
    }

    /**
     * checking the x,y coordinates of redEnemies' downward moves to see if 
     * they encouter any walls. Set isBlocked to be "left"(this is chose randomly) , and return false if it does, otherwise 
     * increment the y value by 32, and return true.
     * @return true or false,
     */
    public boolean moveDown() {
        int rowDown = (yVal + 32)/32 - 1;
        int colDown = xVal/32;

        if (wallsCheck [rowDown][colDown].equals("W") ||  wallsCheck [rowDown][colDown].equals("B")) {
            isBlocked = "up";

        } else {
            yVal+=32;
            return true;
        }
        return false;
    }

    /**
     * checking the x,y coordinates of redEnemies' upward moves to see if 
     * they encouter any walls. Set isBlocked to be "right"(this is chose randomly), and return false if it does, otherwise 
     * decrement the y value by 32, and return true.
     * @return true or false,
     */
    public boolean moveUp() {
        int rowUp = (yVal - 32)/32 - 1;
        int colUp = xVal/32;

        if (wallsCheck [rowUp][colUp].equals("W") ||  wallsCheck [rowUp][colUp].equals("B")) {
            isBlocked = "left";
       
        } else {
            yVal-=32;
            return true;
        }
        return false;
    }

    /**
     * load images of every direction of redEnemies
     * @param app PApplet object used to load images
     */
    public void loadImage (PApplet app) {
        for (int i=0; i < 4; i++) {
            this.down[i] = app.loadImage("src/main/resources/red_enemy/red_down"+(i+1)+".png");
            this.up[i] = app.loadImage("src/main/resources/red_enemy/red_up"+(i+1)+".png");
            this.right[i] = app.loadImage("src/main/resources/red_enemy/red_right"+(i+1)+".png");
            this.left[i] = app.loadImage("src/main/resources/red_enemy/red_left"+(i+1)+".png");
        }
    }

    /**
     * given the list of any enemies object that is lower (hiareachy) than Enemies and PApplet object
     * to draw every object in the list on the map
     * @param enem list of enemies
     * @param app PApplet object used to draw images
     */
    public static void drawEnem(List<? extends Enemies> enem, PApplet app) {
        for (int i = 0; i < enem.size(); i++) {
            if (enem.get(i).getLives() != 0 && enem.get(i) != null) {
                enem.get(i).draw(app);
            }
        }
        
    }

    /**
     * given the list of any enemies object that is lower (hiareachy) than Enemies
     * to call the moving method on every enemies on the list. So that they can move
     * at once.
     * @param enem list of enemies
     */
    public static void movingEnem(List<? extends Enemies> enem) {
        for (int i = 0; i < enem.size(); i++) {
            enem.get(i).moving();
        }
    }

    /**
     * given the list of any enemies object that is lower (hiareachy) than Enemies and bomb object
     * this will attemp to call the bombCollision method on every enemies on the list. So that 
     * it can check if any enemies get caught in the explosion
     * @param enem list of enemies
     * @param bomb bomb object 
     * @return true or false
     */
    public static boolean collisionBomb(List<? extends Enemies> enem, Bomb bomb) {
        for (int i = 0; i < enem.size(); i++) {
            enem.get(i).bombCollision(bomb);
            return true;
        }
        return false;
    }

    /**
     * given the PApplet object
     * to draw the current direction of the red enemies
     * @param app PApplet object used to draw the images of the enemies
     */
    public void draw(PApplet app) {
        app.image(this.imageTodraw[this.counterTurn] , this.xVal, this.yVal);
         
        if (counting == 8) {
            counterTurn = (counterTurn + 1) % imageTodraw.length;
            counting = 0;
        }
        counting+=1;
    }
}