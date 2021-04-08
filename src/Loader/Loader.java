package Loader;

import levelHandling.Cube;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Loader {

    /**
     *
     * @param fileName - the name of the image file to be loaded
     * @return the loaded image
     */
    public static BufferedImage loadImage(String fileName) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("res/" + fileName + ".png"));
        } catch (IOException e) {
            System.err.println("The image with the name: " + fileName + "could not be loaded with error message: " + e);
        }

        return image;
    }

    /**
     *
     * @param fileName - the name of the file to be read from
     * @return an 2D list of cube objects where each of them represents a 40x40 pixel area on the screen
     */
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

                if (cube.getAirAtSides()[0] || cube.getAirAtSides()[1] || cube.getAirAtSides()[2] || cube.getAirAtSides()[3]) {
                    cube.setImageID(getDirtImageID(cube.getAirAtSides()));
                } else {
                    // cube.setImageID(the number in the txt file +14 (due to the multiple dirt images));
                    cube.setImageID(cube.getImageID() + 14);
                }
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

    /**
     *
     * @param airSides - an array of booleans which indicate whether the cube's adjacent cubes are air cubes ro not
     * @return the ID of the image
     */
    private static int getDirtImageID(boolean[] airSides) {
        // possible combinations and their image index (O = true, X = false)
        // O,O,O,O = 0
        //
        // X,O,O,O = 1
        // O,X,O,O = 2
        // O,O,X,O = 3
        // O,O,O,X = 4
        //
        // X,X,O,O = 5
        // O,X,X,O = 6
        // O,O,X,X = 7
        // X,O,O,X = 8
        //
        // X,O,X,O = 9
        // O,X,O,X = 10
        //
        // O,X,X,X = 11
        // X,O,X,X = 12
        // X,X,O,X = 13
        // X,X,X,O = 14
        //
        // X,X,X,X = 15

        if (airSides[0]) {
            if (airSides[1]) {
                if (airSides[2]) {
                    if (airSides[3]) {
                        return 0;
                    } else {
                        return 4;
                    }
                } else if (airSides[3]) {
                    return 3;
                } else {
                    return 7;
                }
            } else if (airSides[2]) {
                if (airSides[3]) {
                    return 2;
                } else {
                    return 10;
                }
            } else if (airSides[3]) {
                return 6;
            } else {
                return 11;
            }
        } else if (airSides[1]) {
            if (airSides[2]) {
                if (airSides[3]) {
                    return 1;
                } else {
                    return 8;
                }
            } else if (airSides[3]) {
                return 9;
            } else {
                return 12;
            }
        } else if (airSides[2]) {
            if (airSides[3]) {
                return 5;
            } else {
                return 13;
            }
        } else if (airSides[3]) {
            return 14;
        } else {
            return 15;
        }
    }
}
