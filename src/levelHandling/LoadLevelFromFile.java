package levelHandling;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LoadLevelFromFile {


    public static List<List<Cube>> loadLevelData(String fileName) {
        List<List<Cube>> levelCubes = new ArrayList<>();

        // reading all the information from the text file into a 2D list
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

        // processing all the cubes and checking which neighbouring cubes are air cubes
        for (int rows = 0; rows < levelCubes.size(); rows++) {
            for (int cols = 0; cols < levelCubes.get(rows).size(); cols++) {
                Cube cube = levelCubes.get(rows).get(cols);

                // checking for any special cases where a cube to the left, right, up or down doesn't exist
                if (rows == 0) {
                    // top left of the screen
                    if (cols == 0) {
                        cube.setAirAtSides(checkAdjacentCubes(levelCubes, rows, cols, true, false, false, true));
                    }
                    // top right of the screen
                    else if (cols == levelCubes.get(rows).size() - 1) {
                        cube.setAirAtSides(checkAdjacentCubes(levelCubes, rows, cols, false, false, true, true));
                    }
                    // all other cases in the uppermost row
                    else {
                        cube.setAirAtSides(checkAdjacentCubes(levelCubes, rows, cols, true, false, true, true));
                    }
                } else if (rows == levelCubes.size() - 1) {
                    // bottom left of the screen
                    if (cols == 0) {
                        cube.setAirAtSides(checkAdjacentCubes(levelCubes, rows, cols, true, true, false, false));
                    }
                    // bottom right of the screen
                    else if (cols == levelCubes.get(rows).size() - 1) {
                        cube.setAirAtSides(checkAdjacentCubes(levelCubes, rows, cols, false, true, true, false));
                    }
                    // all other cases in the bottommost row
                    else {
                        cube.setAirAtSides(checkAdjacentCubes(levelCubes, rows, cols, true, true, true, false));
                    }
                }
                // all other cases in the rightmost row
                else if (cols == levelCubes.get(rows).size() - 1) {
                    cube.setAirAtSides(checkAdjacentCubes(levelCubes, rows, cols, false, true, true, true));
                }
                // all other cases in the leftmost row
                else if (cols == 0) {
                    cube.setAirAtSides(checkAdjacentCubes(levelCubes, rows, cols, true, true, false, true));
                }
                // all other cases which aren't special
                else {
                    cube.setAirAtSides(checkAdjacentCubes(levelCubes, rows, cols, true, true, true, true));
                }

                // check if rows = 0
                    // check if cols = 0
                    // check if cols = levelCubes.get(rows).size()
                // check else if cols = 0

                // check if rows = levelCubes.size()
                    // check if cols = 0
                    // check if cols = levelCubes.get(rows).size()
                // check else if cols = levelCubes.get(rows).size()
            }
        }

        return levelCubes;
    }

    /**
     *
     * the parameters right, top, left and bottom are passed to this method since row or col + or - 1 could result
     * in a NullPointerException from the levelCubes list
     *
     * @param levelCubes    - the 2D list where all the cubes are stored in
     * @param row           - the row of the current cube to be checked
     * @param col           - the column of the current cube to be checked
     * @param right         - if the cube to the right should be checked for an air cube
     * @param top           - if the cube to the top should be checked for an air cube
     * @param left          - if the cube to the left should be checked for an air cube
     * @param bottom        - if the cube to the bottom should be checked for an air cube
     * @return an array containing the information to which sides air cubes are located
     */
    private static boolean[] checkAdjacentCubes(List<List<Cube>> levelCubes, int row, int col, boolean right, boolean top, boolean left, boolean bottom) {
        boolean[] adjacentAirSides = new boolean[4];

        if (right) {
            adjacentAirSides[0] = levelCubes.get(row).get(col + 1).getCubeID() == 0;
        }
        if (top) {
            adjacentAirSides[1] = levelCubes.get(row - 1).get(col).getCubeID() == 0;
        }
        if (left) {
            adjacentAirSides[2] = levelCubes.get(row).get(col - 1).getCubeID() == 0;
        }
        if (bottom) {
            adjacentAirSides[3] = levelCubes.get(row + 1).get(col).getCubeID() == 0;
        }

        return adjacentAirSides;
    }
}
