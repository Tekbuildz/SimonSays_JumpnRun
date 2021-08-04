package Loader;

import entities.Coin;
import levelHandling.Cube;

import javax.xml.namespace.QName;
import javax.xml.stream.*;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.util.ArrayList;

public class LevelLoader {

    private static ArrayList<ArrayList<Cube>> levelCubes;
    private static ArrayList<Rectangle2D> collisionBoxes;
    private static Point2D spawnPoint;
    private static Rectangle2D.Double finish;
    private static final ArrayList<ArrayList<Coin>> coins = new ArrayList<>();

    /**
     *
     * loads all information about the level from an XML file
     * this function uses an XML parser reading the data from a file
     * given by a FileInputStream and then checking for specific XML-Elements
     * <p>
     * Using those elements, the XML parser checks for collision boxes,
     * the spawn point and also the level with all the cubeIDs stored
     *
     *
     * @param fileName - the name of the level to be loaded of the format
     *                 fileName.litidata
     */
    public static void loadLevelData(String fileName) {
        levelCubes = new ArrayList<>();
        collisionBoxes = new ArrayList<>();

        // adding three new ArrayLists for coins of value 5, 10, 20
        for (int i = 0; i < 3; i++) {
            coins.add(new ArrayList<>());
        }

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
                                switch (objectType.getValue()) {
                                    case "COLLISIONBOX":
                                        collisionBoxes.add(new Rectangle2D.Double(
                                                Integer.parseInt(startElement.getAttributeByName(new QName("x")).getValue()),
                                                Integer.parseInt(startElement.getAttributeByName(new QName("y")).getValue()),
                                                Integer.parseInt(startElement.getAttributeByName(new QName("width")).getValue()),
                                                Integer.parseInt(startElement.getAttributeByName(new QName("height")).getValue())
                                        ));
                                        break;
                                    // handling type spawn point
                                    case "SPAWNPOINT":
                                        spawnPoint = new Point2D.Double(
                                                Integer.parseInt(startElement.getAttributeByName(new QName("x")).getValue()),
                                                Integer.parseInt(startElement.getAttributeByName(new QName("y")).getValue())
                                        );
                                        break;
                                    // handling props (coins)
                                    case "PROP":
                                        switch (startElement.getAttributeByName(new QName("name")).getValue()) {
                                            case "5":
                                                coins.get(0).add(new Coin(
                                                            Integer.parseInt(startElement.getAttributeByName(new QName("x")).getValue()),
                                                            Integer.parseInt(startElement.getAttributeByName(new QName("y")).getValue()),
                                                            Integer.parseInt(startElement.getAttributeByName(new QName("width")).getValue()),
                                                            Integer.parseInt(startElement.getAttributeByName(new QName("height")).getValue()),
                                                            5)
                                                );
                                                break;

                                            case "10":
                                                coins.get(1).add(new Coin(
                                                            Integer.parseInt(startElement.getAttributeByName(new QName("x")).getValue()),
                                                            Integer.parseInt(startElement.getAttributeByName(new QName("y")).getValue()),
                                                            Integer.parseInt(startElement.getAttributeByName(new QName("width")).getValue()),
                                                            Integer.parseInt(startElement.getAttributeByName(new QName("height")).getValue()),
                                                            10)
                                                );
                                                break;

                                            case "20":
                                                coins.get(2).add(new Coin(
                                                            Integer.parseInt(startElement.getAttributeByName(new QName("x")).getValue()),
                                                            Integer.parseInt(startElement.getAttributeByName(new QName("y")).getValue()),
                                                            Integer.parseInt(startElement.getAttributeByName(new QName("width")).getValue()),
                                                            Integer.parseInt(startElement.getAttributeByName(new QName("height")).getValue()),
                                                            20)
                                                );
                                                break;
                                        }
                                        break;

                                    case "TRIGGER":
                                        finish = new Rectangle2D.Double(
                                                Integer.parseInt(startElement.getAttributeByName(new QName("x")).getValue()),
                                                Integer.parseInt(startElement.getAttributeByName(new QName("y")).getValue()),
                                                Integer.parseInt(startElement.getAttributeByName(new QName("width")).getValue()),
                                                Integer.parseInt(startElement.getAttributeByName(new QName("height")).getValue())
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
                            // replacing the first newline found in the string since after <data> there is a newline, otherwise, the array would contain an empty array
                            level = level.replaceFirst("\\n", "");
                            String[] horizontalLevelSections = level.split("\\n");
                            for (int i = 0; i < horizontalLevelSections.length; i++) {
                                levelCubes.add(new ArrayList<>());
                                String[] cubeIDs = horizontalLevelSections[i].split("[,]");
                                for (int j = 0; j < cubeIDs.length; j++) {
                                    levelCubes.get(i).add(new Cube(Integer.parseInt(cubeIDs[j]) - 1, j, i));
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

    /**
     *
     * @return a 2D ArrayList of all the Cubes containing their cubeIDs
     */
    public static ArrayList<ArrayList<Cube>> getLevelCubes() {
        return levelCubes;
    }

    /**
     *
     * @return an ArrayList of all the collision boxes in the level
     */
    public static ArrayList<Rectangle2D> getCollisionBoxes() {
        return collisionBoxes;
    }

    /**
     *
     * @return a Point2D representing the entry point of the player
     */
    public static Point2D getSpawnPoint() {
        return spawnPoint;
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
     * @return the rectangle representing the hitbox of the finish
     */
    public static Rectangle2D.Double getFinish() {
        return finish;
    }
}
