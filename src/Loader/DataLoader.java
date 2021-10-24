package Loader;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * the DataLoader class loads data from the player.xml file and stores it in
 * variables
 *
 * @author Thomas Bundi
 * @version 0.8
 * @since 2.4
 */
public class DataLoader {

    private static int coins = 0;
    private static int items = 0;
    private static int lives = 0;
    private static HashMap<String, Integer> entityKills = new HashMap<>();
    private static HashMap<String, Integer> levelTimes = new HashMap<>();

    /**
     *
     * loads the player statistics from an XML file
     * by searching through the file with the STAX parser and
     * checking each StartElement
     *
     * @param fileName - the name of the XML file to be read from
     */
    public static void loadPlayerData(String fileName)  {
        entityKills = new HashMap<>();
        levelTimes = new HashMap<>();

        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(System.getProperty("user.home") + "/SimonSays_JAR/player.xml");
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            XMLEventReader eventReader = inputFactory.createXMLEventReader(fileInputStream);

            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();
                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();

                    switch (startElement.getName().getLocalPart()) {
                        case "coins":
                            Attribute coinAmount = startElement.getAttributeByName(new QName("amount"));
                            coins = Integer.parseInt(coinAmount.getValue());
                            break;

                        case "items":
                            Attribute itemAmount = startElement.getAttributeByName(new QName("amount"));
                            items = Integer.parseInt(itemAmount.getValue());
                            break;

                        case "lives":
                            Attribute livesAmount = startElement.getAttributeByName(new QName("amount"));
                            lives = Integer.parseInt(livesAmount.getValue());
                            break;

                        case "entity_kills":
                            Iterator<Attribute> entities = startElement.getAttributes();
                            while (entities.hasNext()) {
                                Attribute entity = entities.next();
                                entityKills.put(entity.getName().getLocalPart(), Integer.parseInt(entity.getValue()));
                            }
                            break;

                        case "level_data":
                            Iterator<Attribute> iterator = startElement.getAttributes();
                            while (iterator.hasNext()) {
                                Attribute time = iterator.next();
                                levelTimes.put(time.getName().getLocalPart(), Integer.parseInt(time.getValue()));
                            }
                    }
                }
            }
        } catch (FileNotFoundException | XMLStreamException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @return how many coins the player had when last saved to disc
     */
    public static int getCoins() {
        return coins;
    }

    /**
     *
     * @return how many items the player had when last saved to disc
     */
    public static int getItems() {
        return items;
    }

    /**
     *
     * @return how many lives the player had when last saved to disc
     */
    public static int getLives() {
        return lives;
    }

    /**
     *
     * @return how many entities of each type the player had killed when
     *          last saved to disc
     */
    public static HashMap<String, Integer> getEntityKills() {
        return entityKills;
    }

    /**
     *
     * @return an arraylist containing all current fastest times
     */
    public static HashMap<String, Integer> getLevelTimes() {
        return levelTimes;
    }
}
