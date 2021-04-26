package levelHandling;

import Loader.Loader;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Level {

    private static ArrayList<ArrayList<Cube>> levelCubes = new ArrayList<>();

    public Level(String levelName) {
        levelCubes = Loader.loadLevelData("levels/" + levelName + ".txt");
    }

    /**
     *
     * @return the 2D list of all the cube objects in the current level containing their respective cube data
     */
    public static ArrayList<ArrayList<Cube>> getLevelCubes() {
        return levelCubes;
    }

    public static Point getSpawnLocation() {
        Point spawn = new Point();
        for (int i = 0; i < levelCubes.size(); i++) {
            for (int j = 0; j < levelCubes.get(i).size(); j++) {
                if (levelCubes.get(i).get(j).getCubeID() == 2) {
                    spawn.setLocation(j, i);
                    return spawn;
                }
            }
        }
        System.err.println("Could not load spawn location of the player!");
        return null;
    }
}
