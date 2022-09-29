package demolition;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PFont;
import java.util.*;
import java.io.*;


public class MapGame {
    private List <Integer> yellowStartLocation = new ArrayList<>();
    private List <Integer>  redStartLocation = new ArrayList<>();
    private int Playerx;
    private int Playery;
    private int Goalx;
    private int Goaly;

    private PImage[] setUpMap;
    private PImage clock;
    private PImage playerLives;
    public static String[][] mapping = new String[13][15];

    public MapGame() {

    }

    /**
     * return starting x-coordinate location of player in the map in pixel.
     * @return Playerx*32
     */
    public int getPlayerx() {
        return Playerx * 32;
    }

    /**
     * return starting y-coordinate location of player in the map in pixel.
     * @return Playery* 32 + 32 + 13
     */
    public int getPlayery() {
        // +13 so that the character is standing at the end of the 32 pixel
        return Playery * 32 + 32 + 13;
    }

    /**
     * return starting x-coordinate location of the goal in the map in pixel.
     * @return Goalx* 32
     */
    public int getGoalx() {
        return Goalx * 32;
    }

    /**
     * return starting y-coordinate location of the goal in the map in pixel.
     * @return Goaly* 32 + 32 + 13
     */
    public int getGoaly() {
        return Goaly * 32 + 32 + 13;
    }

    /**
     * return list of starting x,y coordinates (in index) location of red enemies.
     * @return redStartLocation
     */
    public List<Integer> getRedEnemies() {
        return redStartLocation;
    }

    /**
     * return list of starting x,y coordinates (in index) location of yellow enemies.
     * @return yellowStartLocation
     */
    public List<Integer> getYellowEnemies() {
        return yellowStartLocation;
    }

    /**
     * given the path, this attempts to read in the level text and transfer them to 2d array
     * @param path to the map file
     * @return mapping
     */
    public static String[][] readMapFile(String path) {
        try {
            File loadMapFile = new File(path);
            Scanner scan = new Scanner(loadMapFile);
            while (scan.hasNextLine()){
                for (int i = 0; i < 13; i++){
                    String row = scan.nextLine();
                    for (int j=0; j < 15; j++){
                        mapping[i][j] = Character.toString(row.charAt(j));
                        

                    }
                }
            }
            scan.close();
            
        }catch(FileNotFoundException e) {
            e.printStackTrace();

        }catch(IndexOutOfBoundsException e) {
            System.out.println("ERROR: INVALID MAP FILE");
            System.exit(0);
        }

        return mapping;
    }

    /**
     * given the PApplet object, this attempts to load all the images into the setUpMap array based on the 
     * information got from the 2d array mapping. This is also used to getting all the starting location of 
     * goal, enemies and player.
     * @param app PApplet object to load images of the map
     */    
    public void loadMap(PApplet app) {
        int value = 0;
        // 195: the total picture of map
        setUpMap = new PImage[195];
        redStartLocation = new ArrayList<Integer>();
        yellowStartLocation = new ArrayList<Integer>();

        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 15; j++) {
                if (mapping[i][j].equals("W")) {
                    setUpMap[value] = app.loadImage("src/main/resources/wall/solid.png");
                
                }else if (mapping[i][j].equals("B")) {
                    setUpMap[value] = app.loadImage("src/main/resources/broken/broken.png");
                
                }else if (mapping[i][j].equals(" ")) {
                    setUpMap[value] = app.loadImage("src/main/resources/empty/empty.png");
                    
                }else if (mapping[i][j].equals("G")) {
                    setUpMap[value] = app.loadImage("src/main/resources/goal/goal.png");
                    Goaly = i;
                    Goalx = j;
                }else if (mapping[i][j].equals("P")) {
                    setUpMap[value] = app.loadImage("src/main/resources/empty/empty.png");
                    Playery = i;
                    Playerx = j;

                }else if (mapping[i][j].equals("Y")) {
                    setUpMap[value] = app.loadImage("src/main/resources/empty/empty.png");
                    yellowStartLocation.add(j);
                    yellowStartLocation.add(i);

                }else if (mapping[i][j].equals("R")) {
                    setUpMap[value] = app.loadImage("src/main/resources/empty/empty.png");
                    redStartLocation.add(j);
                    redStartLocation.add(i);


                }
                value+=1;
            }
        }
        playerLives = app.loadImage("src/main/resources/icons/player.png");
        clock = app.loadImage("src/main/resources/icons/clock.png");

    }

    /**
     * given the PApplet object, this attempts to draw all the images based on the information 
     * got from the PImage array.
     * @param app PApplet object to draw the map
     */    
    public void draw(PApplet app) {
        int y_value = 64;
        int index = 0;
        int pixel = 0;

        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 15; j++) {
                app.image(setUpMap[index],pixel,y_value);
                index+=1;
                pixel+=32;

            }
            y_value+=32;
            pixel=0;
        }
        app.image(playerLives,128,20);
        app.image(clock,256,20);
        
    }
}