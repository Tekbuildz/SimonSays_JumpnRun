package dataProcessing;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import player.Player;
import toolbox.BasicConstants;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Objects;

import static toolbox.BasicConstants.DEFAULT_PATH;

/**
 *
 * the DataSaver class saves the player statistics to disc and also saves a
 * new personal best time in a level if one was achieved
 *
 * @author Thomas Bundi
 * @version 1.1
 * @since 2.4
 */
public class DataSaver {

    /**
     *
     * saves all the player statistics to player.xml
     * using the DOM parser to save the data to a xml file
     */
    public static void saveData(int levelIndex, long timeInMS, Player player) {

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        try {
            // basic document
            DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element rootElement = document.createElement("data");
            document.appendChild(rootElement);

            // --------------------------------------------------------------------------------------------------------- PLAYER STATS
            Element playerStats = document.createElement("player_stats");
            rootElement.appendChild(playerStats);

            // coins
            Element coins = document.createElement("coins");
            playerStats.appendChild(coins);
            coins.setAttribute("amount", String.valueOf(player.getCoins()));

            // items
            Element items = document.createElement("items");
            playerStats.appendChild(items);
            items.setAttribute("amount", String.valueOf(DataLoader.getItems() + player.getNumberOfItemsCollected()));

            // lives
            Element lives = document.createElement("lives");
            playerStats.appendChild(lives);
            lives.setAttribute("amount", String.valueOf(player.getLives()));

            // entity kills
            Element entityKills = document.createElement("entity_kills");
            playerStats.appendChild(entityKills);
            entityKills.setAttribute("snail", String.valueOf(player.getEntityKills().get("snail")));
            entityKills.setAttribute("wolf", String.valueOf(player.getEntityKills().get("wolf")));
            // ---------------------------------------------------------------------------------------------------------

            // --------------------------------------------------------------------------------------------------------- LEVEL DATA
            // if hashmap with levels is empty, add all the elements to the player.xml
            // if hashmap not empty, get values of hashmap
            Element levelData = document.createElement("level_data");
            rootElement.appendChild(levelData);

            // reading the number of files currently in the level's folder, hence reading the number of levels
            File directory = new File(DEFAULT_PATH + "levels");
            int fileCount = -1;
            if (directory.exists()) {
                if (directory.isDirectory()) {
                    fileCount = Objects.requireNonNull(directory.list()).length;
                } else {
                    System.err.println("The specified location is not a directory!");
                    System.exit(0);
                }
            } else {
                System.err.println("The specified directory doesn't exist!");
                System.exit(0);
            }


            if (fileCount > DataLoader.getLevelTimes().size()) {
                for (int i = 0; i < DataLoader.getLevelTimes().size(); i++) {
                    levelData.setAttribute("level_" + (i + 1), String.valueOf(DataLoader.getLevelTimes().get("level_" + (i + 1))));
                }
                for (int i = DataLoader.getLevelTimes().size(); i < fileCount; i++) {
                    levelData.setAttribute("level_" + (i + 1), "0");
                }
            }

            for (int i = 0; i < DataLoader.getLevelTimes().size(); i++) {
                if (levelIndex == (i + 1) && (DataLoader.getLevelTimes().get("level_" + (i + 1)) > timeInMS || DataLoader.getLevelTimes().get("level_" + (i + 1)) == 0) && timeInMS > 0) {
                    levelData.setAttribute("level_" + (i + 1), String.valueOf(timeInMS));
                }
                else {
                    levelData.setAttribute("level_" + (i + 1), String.valueOf(DataLoader.getLevelTimes().get("level_" + (i + 1))));
                }
            }
            // ---------------------------------------------------------------------------------------------------------

            // writing the data in an XML file
            FileOutputStream fileOutputStream = new FileOutputStream(BasicConstants.DEFAULT_PATH + "player.xml");

            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(fileOutputStream);

            transformer.transform(source, result);
        } catch (ParserConfigurationException | FileNotFoundException | TransformerException e) {
            e.printStackTrace();
        }
    }
}
