package toolbox;

import Loader.DataLoader;
import gameLoop.Main;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import player.Player;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.Iterator;
import java.util.Objects;

public class DataSaver {

    /**
     *
     * saves all the player statistics to player.xml
     * using the DOM parser to save the data to a xml file
     */
    public static void saveData(int levelIndex, long timeInMS) {

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
            coins.setAttribute("amount", String.valueOf(Main.player.getCoins()));

            // items
            Element items = document.createElement("items");
            playerStats.appendChild(items);
            items.setAttribute("amount", String.valueOf(Main.player.getNumberOfItemsCollected()));

            // lives
            Element lives = document.createElement("lives");
            playerStats.appendChild(lives);
            lives.setAttribute("amount", String.valueOf(Main.player.getLives()));

            // entity kills
            Element entityKills = document.createElement("entity_kills");
            playerStats.appendChild(entityKills);
            entityKills.setAttribute("snail", String.valueOf(Main.player.getEntityKills().get("snail")));
            entityKills.setAttribute("wolf", String.valueOf(Main.player.getEntityKills().get("wolf")));
            // ---------------------------------------------------------------------------------------------------------

            // --------------------------------------------------------------------------------------------------------- LEVEL DATA
            // if hashmap with levels is empty, add all the elements to the player.xml
            // if hashmap not empty, get values of hashmap
            Element levelData = document.createElement("level_data");
            rootElement.appendChild(levelData);

            if (DataLoader.getLevelTimes().isEmpty()) {
                File directory = new File("levels");
                int fileCount = Objects.requireNonNull(directory.list()).length;
                for (int i = 0; i < fileCount; i++) {
                    levelData.setAttribute("level_" + (i + 1), "0");
                }
            } else {
                for (int i = 0; i < DataLoader.getLevelTimes().size(); i++) {
                    if (i == levelIndex - 1 && timeInMS > 0) {
                        if (DataLoader.getLevelTimes().get(i) > timeInMS || DataLoader.getLevelTimes().get(i) == 0) {
                            levelData.setAttribute("level_" + (i + 1), String.valueOf(timeInMS));
                            continue;
                        }
                    }
                    levelData.setAttribute("level_" + (i + 1), String.valueOf(DataLoader.getLevelTimes().get(i)));
                }
            }
            // ---------------------------------------------------------------------------------------------------------

            // writing the data in an XML file
            FileOutputStream fileOutputStream = new FileOutputStream("data/player.xml");

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
