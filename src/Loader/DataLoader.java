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

public class DataLoader {

    private static int coins = 0;
    private static int lives = 0;
    private static final HashMap<String, Integer> entityKills = new HashMap<>();
    private static final ArrayList<Long> levelTimes = new ArrayList<>();

    /**
     *
     * loads the player statistics from an XML file
     *
     * @param fileName - the name of the XML file to be read from
     */
    public static void loadPlayerData(String fileName)  {

        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream("data/" + fileName + ".xml");
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
                                levelTimes.add(Long.parseLong(iterator.next().getValue()));
                            }
                            break;
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
    public static ArrayList<Long> getLevelTimes() {
        return levelTimes;
    }
}
