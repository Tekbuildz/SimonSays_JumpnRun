package levelHandling;

import Loader.LevelLoader;
import SpriteSheet.SpriteSheet;
import SpriteSheet.SpriteSheetMaster;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Level {

    private static ArrayList<ArrayList<Cube>> levelCubes = new ArrayList<>();
    private static ArrayList<Rectangle2D> collisionBoxes = new ArrayList<>();
    private static Point2D spawnPoint;

    public Level(String levelName) {
        // loading the level and its details
        LevelLoader.loadLevelData("levels/test_hb_1_2_3.litidata"); // replace this line with LevelLoader.loadLevelData("levels/" + levelName + ".litidata");
        levelCubes = LevelLoader.getLevelCubes();
        collisionBoxes = LevelLoader.getCollisionBoxes();
        spawnPoint = LevelLoader.getSpawnPoint();

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

    public static Point getSpawnLocation() {
        return new Point((int) spawnPoint.getX(), (int) spawnPoint.getY());
    }

    public static ArrayList<Rectangle2D> getCollisionBoxes() {
        return collisionBoxes;
    }
}
