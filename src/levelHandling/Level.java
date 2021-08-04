package levelHandling;

import Loader.LevelLoader;
import SpriteSheet.SpriteSheet;
import SpriteSheet.SpriteSheetMaster;
import entities.Coin;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Level {

    private static ArrayList<ArrayList<Cube>> levelCubes = new ArrayList<>();
    private static ArrayList<Rectangle2D> collisionBoxes = new ArrayList<>();
    private static ArrayList<ArrayList<Coin>> coins = new ArrayList<>();
    private static Point2D spawnPoint;
    private static Rectangle2D finish;

    /**
     *
     * basic constructor of a Level
     * loads the data (collision boxes, cubeID, coins, spawn point and finish)
     * from XML file
     *
     * @param levelName - the name of the level to be loaded
     */
    public Level(String levelName) {
        // loading the level and its details
        LevelLoader.loadLevelData("levels/" + levelName + ".litidata"); // replace this line with LevelLoader.loadLevelData("levels/" + levelName + ".litidata");
        levelCubes = LevelLoader.getLevelCubes();
        collisionBoxes = LevelLoader.getCollisionBoxes();
        coins = LevelLoader.getCoins();
        spawnPoint = LevelLoader.getSpawnPoint();
        finish = LevelLoader.getFinish();

        // loading the necessary sprite sheets for the level and the player
        SpriteSheetMaster.addSpriteSheetToMap("dirtGrassSky", new SpriteSheet("C:\\Users\\thoma\\Documents\\Programming\\Projects\\SimonSays_JumpnRun\\res\\spritesheets\\dirt_gras.png"));
    }

    /**
     *
     * @return the 2D list of all the cube objects in the current level containing their respective cube data
     */
    public static ArrayList<ArrayList<Cube>> getLevelCubes() {
        return levelCubes;
    }

    /**
     *
     * @return the point where the player enters the level
     */
    public static Point getSpawnLocation() {
        return new Point((int) spawnPoint.getX(), (int) spawnPoint.getY());
    }

    /**
     *
     * @return an arraylist containing arraylists of all the locations of
     *          5-er, 10-er and 20-er coins in this order of lists
     */
    public static ArrayList<ArrayList<Coin>> getCoins() {
        return coins;
    }

    /**
     *
     * @return an ArrayList of rectangles containing all the collision boxes
     *          with which the player can collide
     */
    public static ArrayList<Rectangle2D> getCollisionBoxes() {
        return collisionBoxes;
    }

    /**
     *
     * @return a rectangle representing the hitbox of the finish
     */
    public static Rectangle2D getFinish() {
        return finish;
    }
}
