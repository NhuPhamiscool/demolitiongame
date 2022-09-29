package demolition;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PFont;
import java.io.*;
import java.util.*;
import processing.data.JSONObject;
import processing.data.JSONArray;



public class App extends PApplet {

    public static final int WIDTH = 480;
    public static final int HEIGHT = 480;
    public static final int FPS = 60;

    public boolean setBomb = false;
    public Bomb bomb;
    
    private int counterBomb = 1;
    private String[][] mapPlay;
    private int drawCount = 0;
    private int timeCount = 0;
    private int BombCount = 0;
    private boolean isTrue = false;


    private States keyIsPressed = States.RELEASED;
    public Player mainPlayer;
    private List<Enemies> redEnemies;
    private List<yellowEnemies> yellowEnemies;
    private PFont font;
    private MapGame map = new MapGame();
    private List<Integer> redLocation = new ArrayList<>();
    private List<Integer> yellowLocation = new ArrayList<>();
   
    private JSONObject json;
    private JSONArray arrayJson;
    public List<String> PATH = new ArrayList<>();
    private List<Integer> PathTime = new ArrayList<>();
    private int displayedLives;
    private int displayedTime;
    private int nextLevel = 0;
    private int nextLeveltime = 0;
    
    public App() {
    }
    
    /**
     * setting up the dimension of the game
     */    
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * the place to load image, create objects to ready for the game
     */    
    public void setup() {
        frameRate(FPS);
        readConfigFile("config.json");
        displayedTime = PathTime.get(nextLeveltime);
        mapPlay = MapGame.readMapFile(PATH.get(nextLevel));

        map.loadMap(this);
        font = createFont("src/main/resources/PressStart2P-Regular.ttf", 20);
        textFont(font);
        fill(0,0,0);

        mainPlayer = new Player(map.getPlayerx(), map.getPlayery(),this.loadImage("src/main/resources/player/player1.png"), displayedLives);
        // getting the red location from file
        redLocation = map.getRedEnemies();
        // create a list of red enemies
        redEnemies = new ArrayList<Enemies>();
        // set up the position of the red enemies on the map
        for (int i = 0; i < redLocation.size(); ) {
            redEnemies.add(new Enemies(redLocation.get(i) * 32, redLocation.get(i+1) *32+32+13,this.loadImage("src/main/resources/red_enemy/red_down1.png"), 1));
            i+=2;
        }

        yellowLocation = map.getYellowEnemies();
        yellowEnemies = new ArrayList<yellowEnemies>();

        for (int i = 0; i < yellowLocation.size(); ) {
            yellowEnemies.add(new yellowEnemies(yellowLocation.get(i) * 32, yellowLocation.get(i+1) *32+32+13,this.loadImage("src/main/resources/yellow_enemy/yellow_down1.png"), 1));
            i+=2;
        }
        // loading enemies images
        for (int i = 0; i < yellowEnemies.size(); i++) {
            yellowEnemies.get(i).loadImage(this);
        }

        for (int i = 0; i < redEnemies.size(); i++) {
            redEnemies.get(i).loadImage(this);
        }
        mainPlayer.loadImage(this);
        Bomb.explosionLoad(this);


    }
    
    /**
     * the place where the main loop of the game happens. 
     */ 
    public void draw() {
        background(255, 140, 0);

        if (mainPlayer.isGameOver() || displayedTime == 0) {
            text("GAME OVER", 150 ,225);
        
        }
        else if (mainPlayer.isWin(map.getGoalx(), map.getGoaly())) {
            // if theres other levels
            if (nextLevel < PATH.size() - 1) {
                nextLevel+=1;
                resetLevel();
                mainPlayer.winning = false;

            } else {
                text("YOU WIN", 150 ,225);
                delay(300);
            }
            map.loadMap(this);

        } else {
            text(String.valueOf(displayedLives), 170 ,47);
            text(displayedTime, 290 ,47);
            map.draw(this);
        
            bombPlaceHandle();

            mainPlayer.enemiesCollision(redEnemies, yellowEnemies);
            
           
            if (mainPlayer.isAlive() == false) {
                resetLevel();
                displayedLives = mainPlayer.getLives();

            }
            
            mainPlayer.draw(this);

            if (drawCount == 25) {
                Enemies.movingEnem(yellowEnemies);
                Enemies.movingEnem(redEnemies);
                drawCount = 0;
            }

            if (redEnemies.size() != 0 && redEnemies != null) {
                Enemies.drawEnem(redEnemies, this);
            }
            if (yellowEnemies.size() != 0 && yellowEnemies != null) {
                Enemies.drawEnem(yellowEnemies, this);
            }
            

            if (timeCount == 30 && displayedTime > 0) {
                displayedTime-=1;
                if (displayedTime < 0) {
                    displayedTime = 0;
                }
                timeCount = 0;
            }
        
            map.loadMap(this);
            drawCount+=1;
            timeCount+=1;
        }
    
    }

    /**
     * detect whether arrow keys are released or not yet
     */ 
    public void keyReleased() {
        if (keyCode == DOWN){
            keyIsPressed = States.RELEASED; 

        } else if (keyCode == UP) {
            keyIsPressed = States.RELEASED;

        } else if (keyCode == LEFT) {
            keyIsPressed = States.RELEASED;

        } else if (keyCode == RIGHT) {
            keyIsPressed = States.RELEASED;
        }
    }

    /**
     * detect whether arrow keys are pressed so that can assign the corresponding main player's acts
     */
    public void keyPressed() {
        if (keyCode == DOWN && keyIsPressed == States.RELEASED) {
            mainPlayer.moveDown();
            keyIsPressed = States.PRESSED;

        }else if (keyCode == UP && keyIsPressed == States.RELEASED) {
            mainPlayer.moveUp();
            keyIsPressed = States.PRESSED;

        }else if (keyCode == LEFT && keyIsPressed == States.RELEASED) {
            mainPlayer.moveLeft();
            keyIsPressed = States.PRESSED;
            
        }else if (keyCode == RIGHT && keyIsPressed == States.RELEASED) {
            mainPlayer.moveRight();
            keyIsPressed = States.PRESSED;
            
        }else if (keyCode == 32) {
            setBomb = true;
            bomb = new Bomb(mainPlayer.getxVal(), mainPlayer.getyVal(), mapPlay);
        }
    }

    /**
     * read in config file and save the paths of all the level texts into a list 
     * @param configFile the path to the config file
     */
    public void readConfigFile(String configFile) {
        try {
            json = loadJSONObject(configFile);
            arrayJson = (JSONArray) json.get("levels");
            displayedLives =  Integer.parseInt(String.valueOf(json.get("lives")));
            JSONObject firstMAP;

            for (int i = 0; i < arrayJson.size(); i++) {
                firstMAP = (JSONObject) arrayJson.get(i);
                PATH.add(String.valueOf(firstMAP.get("path")));
                PathTime.add(Integer.parseInt(String.valueOf(firstMAP.get("time"))));
            }

        }catch(Exception e) {
            e.printStackTrace();

        }
    }

    /**
     * this is for handling when the bomb is placed (player, enemies' bomb collision,
     * explosion draw) when setbomb equals true
     */
    public void bombPlaceHandle () {
        if (setBomb){
            this.bomb.draw(this,counterBomb);

            if (BombCount == 9) {
                counterBomb+= 1;
                BombCount = 0;
            }
            BombCount+=1;
            isTrue = true;

        }
        if (counterBomb + 1 > 8 && isTrue == true) {
            this.bomb.explodeDraw(this);

            if (counterBomb == 8) {
                mainPlayer.bombCollision(bomb);
                Enemies.collisionBomb(yellowEnemies, bomb);
                Enemies.collisionBomb(redEnemies, bomb);
            }
            setBomb = false;

            if (counterBomb > 100) {
                counterBomb = 0;
                isTrue = false;

            }else {
                counterBomb += 10;
            }
        }
    }

    /**
     * this is for reseting the level when the player either reach the goal and theres still level
     * afterwards or when the player is killed but still have lives.
     */
    public void resetLevel() {
        mapPlay = MapGame.readMapFile(PATH.get(nextLevel));

        if (nextLeveltime < PathTime.size() - 1) {
            nextLeveltime += 1;
            displayedTime = PathTime.get(nextLeveltime);
        }
        map.loadMap(this);
        map.draw(this);
        mainPlayer.xVal = map.getPlayerx();
        mainPlayer.yVal = map.getPlayery();

        redLocation = new ArrayList<Integer>();
        redLocation = map.getRedEnemies();
        redEnemies = new ArrayList<Enemies>();

        if (redLocation.size() != 0) {

            for (int i = 0; i < redLocation.size(); ) {
                redEnemies.add(new Enemies(redLocation.get(i) * 32,  redLocation.get(i+1) *32+32+13, this.loadImage("src/main/resources/red_enemy/red_down1.png"), 1));
                i+=2;

            } 
            for (int i = 0; i < redEnemies.size(); i++) {
                redEnemies.get(i).loadImage(this);
            }
        }
        
        
        yellowLocation = new ArrayList<Integer>();
        yellowLocation = map.getYellowEnemies();
        yellowEnemies = new ArrayList<yellowEnemies>();

        if (yellowLocation.size() != 0) {
            for (int i = 0; i < yellowLocation.size(); ) {
                yellowEnemies.add(new yellowEnemies(yellowLocation.get(i) * 32, yellowLocation.get(i+1) *32+32+13,this.loadImage("src/main/resources/yellow_enemy/yellow_down1.png"), 1));
                i+=2;
            }
            for (int i = 0; i < yellowEnemies.size(); i++) {
                yellowEnemies.get(i).loadImage(this);
            }

        } 
   
    }

    public static void main(String[] args) {
        PApplet.main("demolition.App");
        
    }

}

/**
 * used for keeping track of key pressed and key released
 */
enum States {
    PRESSED,
    RELEASED;
}