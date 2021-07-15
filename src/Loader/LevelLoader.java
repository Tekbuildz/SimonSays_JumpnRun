package Loader;

import levelHandling.Cube;

import javax.imageio.ImageIO;
import javax.xml.namespace.QName;
import javax.xml.stream.*;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

public class LevelLoader {

    private static ArrayList<ArrayList<Cube>> levelCubes;
    private static ArrayList<Rectangle2D> collisionBoxes;
    private static Point2D spawnPoint;

    public static void loadLevelData(String fileName) {
        levelCubes = new ArrayList<>();
        collisionBoxes = new ArrayList<>();

        // XML parser reading the xml-data in the .litidata file which contains information about the map and the collision boxes
        try {
            FileInputStream fileInputStream = new FileInputStream(fileName);
            XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
            XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(fileInputStream);

            // looping through all the xml events (<> || </>)
            while (xmlEventReader.hasNext()) {
                XMLEvent event = xmlEventReader.nextEvent();
                // checking if the event is a start element (<> and not </>)
                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();
                    // if it is a start element, checking for the name between <>
                    switch (startElement.getName().getLocalPart()) {
                        case "object":
                            Attribute objectType = startElement.getAttributeByName(new QName("type"));
                            if (objectType != null) {
                                // handling type collision box
                                if (objectType.getValue().equals("COLLISIONBOX")) {
                                    collisionBoxes.add(new Rectangle2D.Double(
                                            Integer.parseInt(startElement.getAttributeByName(new QName("x")).getValue()),
                                            Integer.parseInt(startElement.getAttributeByName(new QName("y")).getValue()),
                                            Integer.parseInt(startElement.getAttributeByName(new QName("width")).getValue()),
                                            Integer.parseInt(startElement.getAttributeByName(new QName("height")).getValue())
                                    ));
                                }
                                // handling type spawn point
                                else if (objectType.getValue().equals("SPAWNPOINT")) {
                                    spawnPoint = new Point2D.Double(
                                            Integer.parseInt(startElement.getAttributeByName(new QName("x")).getValue()),
                                            Integer.parseInt(startElement.getAttributeByName(new QName("y")).getValue())
                                    );
                                }
                            }
                            break;

                        case "data":
                            // processing the data containing information which area of the level contains which image
                            String level = "";
                            event = xmlEventReader.nextEvent();
                            // looping through all the events containing characters or i.o.w. are part of the level data
                            while (event.isCharacters()){
                                // string level is a string containing the information
                                level = level.concat(event.asCharacters().toString());
                                event = xmlEventReader.nextEvent();
                            }
                            level = level.replace(",", "");
                            String[] horizontalLevelSections = level.split("\\n");
                            for (int i = 0; i < horizontalLevelSections.length; i++) {
                                levelCubes.add(new ArrayList<>());
                                char[] hlsChars = horizontalLevelSections[i].toCharArray();
                                for (int j = 0; j < hlsChars.length; j++) {
                                    // subtracting 1 from the cubeID because Utiliti starts enumerating at 1 and the array starts at 0
                                    levelCubes.get(i).add(new Cube(Character.getNumericValue(hlsChars[j]) - 1, j, i));
                                }
                            }
                            break;

                    }
                }
            }
        } catch (XMLStreamException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

//    /**
//     *
//     * @param fileName - the name of the file to be read from
//     * @return an 2D list of cube objects where each of them represents a 40x40 pixel area on the screen
//     */
//    public static ArrayList<ArrayList<Cube>> loadLevelData(String fileName) {
//        ArrayList<ArrayList<Cube>> levelCubes = new ArrayList<>();
//
//        // reading all the information from the text file into a 2D list
//        FileReader fileReader;
//        try {
//
//            fileReader = new FileReader(fileName);
//
//            BufferedReader bufferedReader = new BufferedReader(fileReader);
//
//            String line;
//            int currentLine = 0;
//            int currentColumn;
//            // looping through every character in the level file and creating a new Cube object for it
//            while ((line = bufferedReader.readLine()) != null) {
//                char[] levelRow = line.toCharArray();
//
//                // prevents comments in the text file from being loaded
//                if (Character.isDigit(levelRow[0])) {
//                    levelCubes.add(new ArrayList<>());
//                    currentColumn = 0;
//                    for (char cube:levelRow) {
//                        // creating a new cube object for every digit in the text file
//                        levelCubes.get(currentLine).add(new Cube(Character.getNumericValue(cube), currentColumn, currentLine));
//                        currentColumn++;
//                    }
//
//                    currentLine++;
//                }
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        // processing all the cubes and checking which neighbouring cubes are air cubes
//        for (int rows = 0; rows < levelCubes.size(); rows++) {
//            for (int cols = 0; cols < levelCubes.get(rows).size(); cols++) {
//                Cube cube = levelCubes.get(rows).get(cols);
//
//                // checking for any special cases where a cube to the left, right, up or down doesn't exist
//                if (rows == 0) {
//                    // top left of the screen
//                    if (cols == 0) {
//                        cube.setAirAtSides(new boolean[]{true, false, false, true});
//                    }
//                    // top right of the screen
//                    else if (cols == levelCubes.get(rows).size() - 1) {
//                        cube.setAirAtSides(new boolean[]{false, false, true, true});
//                    }
//                    // all other cases in the uppermost row
//                    else {
//                        cube.setAirAtSides(new boolean[]{true, false, true, true});
//                    }
//                } else if (rows == levelCubes.size() - 1) {
//                    // bottom left of the screen
//                    if (cols == 0) {
//                        cube.setAirAtSides(new boolean[]{true, true, false, false});
//                    }
//                    // bottom right of the screen
//                    else if (cols == levelCubes.get(rows).size() - 1) {
//                        cube.setAirAtSides(new boolean[]{false, true, true, false});
//                    }
//                    // all other cases in the bottommost row
//                    else {
//                        cube.setAirAtSides(new boolean[]{true, true, true, false});
//                    }
//                }
//                // all other cases in the rightmost row
//                else if (cols == levelCubes.get(rows).size() - 1) {
//                    cube.setAirAtSides(new boolean[]{false, true, true, true});
//                }
//                // all other cases in the leftmost row
//                else if (cols == 0) {
//                    cube.setAirAtSides(new boolean[]{true, true, false, true});
//                }
//                // all other cases which aren't special
//                else {
//                    cube.setAirAtSides(new boolean[]{true, true, true, true});
//                }
//
//                if (cube.getAirAtSides()[0] || cube.getAirAtSides()[1] || cube.getAirAtSides()[2] || cube.getAirAtSides()[3]) {
//                    cube.setImageID(getDirtImageID(cube.getAirAtSides()));
//                } else {
//                    // cube.setImageID(the number in the txt file +14 (due to the multiple dirt images));
//                    cube.setImageID(cube.getImageID() + 14);
//                }
//            }
//        }
////        for (ArrayList<Cube> cubes:levelCubes) {
////            for (Cube cube:cubes) {
////                System.out.print(cube.getX() + ", ");
////            }
////            System.out.println();
////        }
////        System.out.println(levelCubes.get(0).get(0).getPixelSIZE());
//        return levelCubes;
//    }

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
    private static boolean[] checkAdjacentCubes(ArrayList<ArrayList<Cube>> levelCubes, int row, int col, boolean right, boolean top, boolean left, boolean bottom) {
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

    public static ArrayList<ArrayList<Cube>> getLevelCubes() {
        return levelCubes;
    }

    public static ArrayList<Rectangle2D> getCollisionBoxes() {
        return collisionBoxes;
    }

    public static Point2D getSpawnPoint() {
        return spawnPoint;
    }
}
