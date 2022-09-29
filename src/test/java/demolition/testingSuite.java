package demolition;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import processing.core.PApplet;
import processing.core.PImage;

public class testingSuite extends PApplet {
    
    @Test
    public void simpleTest() {
        assertEquals(480, App.HEIGHT);
    }

    // @Test
    public void MapGameTest() {
        MapGame testMap = new MapGame();
        App app = new App();
        app.readConfigFile("config.json");
        MapGame.readMapFile(app.PATH.get(0));
        testMap.loadMap(this);

        // testing if the goal, the player's, the enemies' x, y coordinates are read in correctly
        assertEquals(416, testMap.getGoalx());
        assertEquals(397, testMap.getGoaly());
        assertEquals(32, testMap.getPlayerx());
        assertEquals(77, testMap.getPlayery());

        // red enemies location
        List<Integer> actualRed = testMap.getRedEnemies();
        // column 8, row 5
        List<Integer> expectedRed = Arrays.asList(8, 5);
        assertEquals(actualRed, expectedRed);

        List<Integer> actualYellow = testMap.getYellowEnemies();
        List<Integer> expectedYellow = Arrays.asList(5, 9);
        assertEquals(actualYellow, expectedYellow);

    }
    
    @Test
    public void Bombtest() {
        MapGame testMap = new MapGame();
        App app = new App();
        String[][] test = testMap.readMapFile("level1.txt");
        assertNotNull(new Bomb(32, 64, test));
        Bomb bombTest = new Bomb(32, 64, test);

        // testing if the wall method dectect the walls correctly
        // 1: thick wall; -1:broken wall; 0: neither
        assertEquals(1, bombTest.wall(1, 0));
        assertEquals(-1, bombTest.wall(4, 1));
        assertEquals(0, bombTest.wall(1, 2));

        assertEquals(32, bombTest.getxVal());
        assertEquals(64, bombTest.getyVal());

        assertEquals(64, bombTest.drawBottom());
        assertEquals(0, bombTest.drawTop());
        assertEquals(0, bombTest.drawLeft());
        assertEquals(64, bombTest.drawRight());

    }

    @Test
    public void redEnemiesTest() {
        assertNotNull(new Enemies(256, 205, null, 1));
        Bomb bombTest = new Bomb(256, 205, null);
        Enemies red = new Enemies(256, 205, null, 1);
        List <Enemies> redE = new ArrayList<Enemies>();
        redE.add(red);

        yellowEnemies yel = new yellowEnemies(160, 333, null, 1);
        List <yellowEnemies> yellE = new ArrayList<yellowEnemies>();
        yellE.add(yel);

        // testing if the location of enemies is within the bomb explosion
        assertEquals(true, Enemies.collisionBomb(redE, bombTest));
        // testing if enemies' ways is blocked
        assertEquals(true, red.moveLeft());
        assertEquals(true, red.moveRight());
        assertEquals(false, red.moveUp());
        assertEquals(false, red.moveDown());
        Enemies.movingEnem(redE);
        Enemies.movingEnem(yellE);

        assertEquals(true, yel.moveLeft());
        assertEquals(true, yel.moveRight());
        assertEquals(true, yel.moveUp());
        assertEquals(true, yel.moveDown());

    }

    @Test
    public void playerTest() {
        assertNotNull(new Player(32, 64, null, 3));
        Player player = new Player(32, 77, null, 3);
        Bomb bombTest = new Bomb(32, 77, null);
        
        assertEquals(32, player.getxVal());
        assertEquals(77, player.getyVal());
        assertEquals(3, player.getLives());

        assertEquals(false, player.isGameOver());
        player.bombCollision(bombTest);
        assertEquals(false, player.isAlive());
        assertEquals(false, player.isGameOver());

        // reach the goal
        player.xVal = 256;
        player.yVal = 456;
        assertEquals(true, player.isWin(256, 456));
        
        // case when explosion happens within 2 grid space
        Bomb bombTest1 = new Bomb(32, 77, null);
        Player player1 = new Player(64, 77, null, 3);
        bombTest1.RIGHT = 64;
        player1.bombCollision(bombTest1);
        assertEquals(false, player1.isAlive());

         // case when explosion happens within 1 grid space
        Bomb bombTest2 = new Bomb(64, 141, null);
        Player player2 = new Player(32, 141, null, 3);
        bombTest2.LEFT = 32;
        player2.bombCollision(bombTest2);
        assertEquals(false, player2.isAlive());




    }

    @Test
    public void yellowEnemiesTest() {
        assertNotNull(new yellowEnemies(160, 333, null, 1));
        Bomb bombTest = new Bomb(160, 333, null);
        yellowEnemies yel = new yellowEnemies(160, 333, null, 1);
        List <yellowEnemies> yellE = new ArrayList<yellowEnemies>();
        yellE.add(yel);
        assertEquals(true, Enemies.collisionBomb(yellE, bombTest));

    }

    @Test
    public void appTest() {
        App app = new App();

        // Set the program to not loop automatically
        app.noLoop();

        // Tell PApplet to create the worker threads for the program
        PApplet.runSketch(new String[] {"App"}, app);

        // Call App.setup() to load in sprites
        app.setup();
        MapGame testMap = new MapGame();
        app.readConfigFile("config.json");
        MapGame.readMapFile(app.PATH.get(0));

        // Set a 1 second delay to ensure all resources are loaded
        app.delay(1000);
        Player player = new Player(32, 77, null, 3);
        
        // Call draw to update the program.
        app.draw();

        // left arrow key
        keyCode = 37;
        app.keyPressed();
        app.keyReleased();
        assertEquals(app.mainPlayer.getxVal(), 32);

        // right arrow key
        keyCode = 39;
        app.keyPressed();
        app.keyReleased();

        // down arrow key
        keyCode = 40;
        app.keyPressed();
        app.keyReleased();

        // space bar key
        keyCode = 32;
        app.keyPressed();
        app.keyReleased();
        app.bombPlaceHandle();
        app.resetLevel();

        // Call draw again to move onto the next frame
        app.draw();

    }
}
