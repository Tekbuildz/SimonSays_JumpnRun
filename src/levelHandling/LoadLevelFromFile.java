package levelHandling;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public abstract class LoadLevelFromFile {

    private static List<List<Cube>> levelCubes = new ArrayList<>();

    public static void loadLevelData(String fileName) {
        FileReader fileReader;
        try {

            fileReader = new FileReader(fileName);

            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            int currentLine = 0;
            int currentColumn;
            // looping through every character in the level file and creating a new Cube object for it
            while ((line = bufferedReader.readLine()) != null) {
                levelCubes.add(new ArrayList<>());
                char[] levelRow = line.toCharArray();
                currentColumn = 0;

                for (char cube:levelRow) {
                    levelCubes.get(currentLine).add(new Cube(Character.getNumericValue(cube), currentColumn, currentLine));
                    currentColumn++;
                }

                currentLine++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<List<Cube>> getLevelCubes() {
        return levelCubes;
    }
}
