package levelHandling;

import Loader.Loader;

import java.util.ArrayList;
import java.util.List;

public class Level {

    private static List<List<Cube>> levelCubes = new ArrayList<>();

    public Level() {
        levelCubes = Loader.loadLevelData("levels/Level_1.txt");
    }

    /**
     *
     * @return the 2D list of all the cube objects in the current level containing their respective cube data
     */
    public static List<List<Cube>> getLevelCubes() {
        return levelCubes;
    }
}
