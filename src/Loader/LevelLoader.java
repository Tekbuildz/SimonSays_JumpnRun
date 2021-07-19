package Loader;

import display.DisplayManager;
import gameLoop.Main;
import levelHandling.Cube;
import levelHandling.Level;

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
                            level = level.replaceFirst("\\n", "");
                            // replacing the first newline found in the string since after <data> there is a newline, otherwise, the array would contain an empty array
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

        for (Rectangle2D collisionBox : collisionBoxes) {
            collisionBox.setRect(collisionBox.getX(), collisionBox.getY(), collisionBox.getWidth(), collisionBox.getHeight());
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
