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
                char[] levelRow = line.toCharArray();

                // prevents comments in the text file from being loaded
                if (Character.isDigit(levelRow[0])) {
                    levelCubes.add(new ArrayList<>());
                    currentColumn = 0;
                    for (char cube:levelRow) {
                        // creating a new cube object for every digit in the text file
                        levelCubes.get(currentLine).add(new Cube(Character.getNumericValue(cube), currentColumn, currentLine));
                        currentColumn++;
                    }

                    currentLine++;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<List<Cube>> getLevelCubes() {
        return levelCubes;
    }
}
