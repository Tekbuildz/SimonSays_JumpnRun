package levelHandling;

import Loader.LevelLoader;
import SimonSays.SimonSaysMaster;
import entities.Coin;
import entities.Item;
import entities.mob.Mob;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Level {

    private final ArrayList<ArrayList<Cube>> levelCubes;
    private final ArrayList<Rectangle2D> collisionBoxes;
    private final ArrayList<ArrayList<Coin>> coins;
    private final ArrayList<Item> items;
    private final ArrayList<Mob> mobs;
    private final SimonSaysMaster simonSaysMaster;
    private final Point2D spawnPoint;
    private final Rectangle2D finish;
    private final LevelLoader levelLoader;
    public int level;

    /**
     *
     * basic constructor of a Level
     * loads the data (collision boxes, cubeID, coins, spawn point and finish)
     * from XML file
     *
     * @param levelName - the name of the level to be loaded
     */
    public Level(String levelName) {
        // checking if the levelName only consists of digits and not letters
        char[] levelNameChars = levelName.toCharArray();
        for (char c:levelNameChars) {
            if (!Character.isDigit(c)) {
                System.err.println("The levelName contains letters and not only digits");
                System.exit(-1);
            }
        }
        level = Integer.parseInt(levelName);
        // loading the level and its details
        levelLoader = new LevelLoader("levels/" + levelName + ".litidata");
        //levelLoader = new LevelLoader("levels/1.litidata");
        levelCubes = levelLoader.getLevelCubes();
        collisionBoxes = levelLoader.getCollisionBoxes();
        coins = levelLoader.getCoins();
        items = levelLoader.getItems();
        mobs = levelLoader.getMobs();
        simonSaysMaster = new SimonSaysMaster(levelLoader.getSimonSays());
        spawnPoint = levelLoader.getSpawnPoint();
        finish = levelLoader.getFinish();
    }

    /**
     *
     * @return the 2D list of all the cube objects in the current level containing their respective cube data
     */
    public ArrayList<ArrayList<Cube>> getLevelCubes() {
        return levelCubes;
    }

    /**
     *
     * @return the point where the player enters the level
     */
    public Point getSpawnLocation() {
        return new Point((int) spawnPoint.getX(), (int) spawnPoint.getY());
    }

    /**
     *
     * @return an arraylist containing arraylists of all the locations of
     *          5-er, 10-er and 20-er coins in this order of lists
     */
    public ArrayList<ArrayList<Coin>> getCoins() {
        return coins;
    }

    /**
     *
     * @return an arraylist containing all the items with their bounds
     */
    public ArrayList<Item> getItems() {
        return items;
    }

    /**
     *
     * @return an arraylist containing all the mob objects
     */
    public ArrayList<Mob> getMobs() {
        return mobs;
    }

    /**
     *
     * @return an ArrayList of rectangles containing all the collision boxes
     *          with which the player can collide
     */
    public ArrayList<Rectangle2D> getCollisionBoxes() {
        return collisionBoxes;
    }

    /**
     *
     * @return a rectangle representing the hitbox of the finish
     */
    public Rectangle2D getFinish() {
        return finish;
    }

    /**
     *
     * @return an instance of the SimonSaysMaster class handling all its anims
     */
    public SimonSaysMaster getSimonSaysMaster() {
        return simonSaysMaster;
    }

    /**
     *
     * @return an instance of the LevelLoader class containing all the
     *          information about the level (hitboxes, mobs etc.)
     */
    public LevelLoader getLevelLoader() {
        return levelLoader;
    }
}
