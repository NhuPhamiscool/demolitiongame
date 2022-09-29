package demolition;

import processing.core.PApplet;
import processing.core.PImage;
import java.util.*;

public class yellowEnemies extends Enemies {
    /**
     * Constructor for a yellowEnemies, requires x, y values, an image and num of lives
     * @param xVal in pixel
     * @param yVal in pixel
     * @param image the image of the yellow enemies
     * @param lives the lives that the yellow enemies has
     */
    public yellowEnemies (int xVal, int yVal, PImage image, int lives) {
        super( xVal, yVal, image, lives);
    }

    /**
     * control the direction of yellowEnemies and assign the corresponding images
     */
    public void moving () {
        if (isBlocked.equals("left")) {
            moveLeft();
            imageTodraw = left;
        } else if (isBlocked.equals("right")) {
            moveRight();
            imageTodraw = right;
        } else if (isBlocked.equals("down")) {
            moveDown();
            imageTodraw = down;
        } else if (isBlocked.equals("up")) {
            moveUp();
            imageTodraw = up;
        }
        
    }

    /**
     * checking the x,y coordinates of yellowEnemies' left moves to see if 
     * they encouter any walls. Set isBlocked to be "up", and return false if it does, otherwise 
     * decrement the xVal by 32 and return true.
     * @return true or false,
     */
    public boolean moveLeft() {
        int rowLeft = (yVal/32) - 1;
        int colLeft = (xVal - 32)/32;

        if (wallsCheck [rowLeft][colLeft].equals("W") ||  wallsCheck [rowLeft][colLeft].equals("B")) {
            isBlocked = "up";
        } else {
            xVal-=32;
            return true;
        }
        return false;
            
    }

    /**
     * checking the x,y coordinates of yellowEnemies' right moves to see if 
     * they encouter any walls. Set isBlocked to be "down", and return false if it does, otherwise 
     * increment the xVal by 32 and return true.
     * @return true or false,
     */
    public boolean moveRight() {
        int rowRight = (yVal/32)-1;
        int colRight = (xVal + 32)/32;

        if (wallsCheck [rowRight][colRight].equals("W") ||  wallsCheck [rowRight][colRight].equals("B")) {            
            isBlocked = "down";

        } else {
            xVal += 32;
            return true;
        }
        return false;
    }

    /**
     * checking the x,y coordinates of yellowEnemies' downward moves to see if 
     * they encouter any walls. Set isBlocked to be "left", and return false if it does, otherwise 
     * increment the y value by 32, and return true.
     * @return true or false,
     */
    public boolean moveDown() {
        int rowDown = (yVal + 32)/32 - 1;
        int colDown = xVal/32;

        if (wallsCheck [rowDown][colDown].equals("W") ||  wallsCheck [rowDown][colDown].equals("B")) {
            isBlocked = "left";
        } else {
            yVal+=32;
            return true;
        }
        return false;
    }

    /**
     * checking the x,y coordinates of yellowEnemies' upward moves to see if 
     * they encouter any walls. Set isBlocked to be "right", and return false if it does, otherwise 
     * decrement the y value by 32, and return true.
     * @return true or false,
     */
    public boolean moveUp() {
        int rowUp = (yVal - 32)/32 - 1;
        int colUp = xVal/32;

        if (wallsCheck [rowUp][colUp].equals("W") ||  wallsCheck [rowUp][colUp].equals("B")) {
            isBlocked = "right";
        } else {
            yVal -= 32;
            return true;
        }
        return false;
    }

    /**
     * load images of every direction of yellowEnemies
     * @param app PApplet object used to load the images of the yellow enemies
     */
    public void loadImage (PApplet app) {
        for (int i=0; i < 4; i++) {
            this.down[i] = app.loadImage("src/main/resources/yellow_enemy/yellow_down"+(i+1)+".png");
            this.up[i] = app.loadImage("src/main/resources/yellow_enemy/yellow_up"+(i+1)+".png");
            this.right[i] = app.loadImage("src/main/resources/yellow_enemy/yellow_right"+(i+1)+".png");
            this.left[i] = app.loadImage("src/main/resources/yellow_enemy/yellow_left"+(i+1)+".png");
        }
    }
}