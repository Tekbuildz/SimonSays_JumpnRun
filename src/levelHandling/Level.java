package levelHandling;

import Loader.LevelLoader;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Level {

    private static ArrayList<ArrayList<Cube>> levelCubes = new ArrayList<>();
    private static ArrayList<Rectangle2D> collisionBoxes = new ArrayList<>();
    private static Point2D spawnPoint;

    public Level(String levelName) {
        //levelCubes = LevelLoader.loadLevelData("levels/" + levelName + ".txt");
        LevelLoader.loadLevelData("levels/test_hb_1_2_3.litidata");
        levelCubes = LevelLoader.getLevelCubes();
        collisionBoxes = LevelLoader.getCollisionBoxes();
        spawnPoint = LevelLoader.getSpawnPoint();
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
}
