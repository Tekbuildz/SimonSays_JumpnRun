package toolbox;

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

public class DataSaver {

    /**
     *
     * saves all the player statistics to player.xml
     * using the DOM parser to save the data to a xml file
     */
    public static void saveData() {

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        try {
            // basic document
            DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element rootElement = document.createElement("player_stats");
            document.appendChild(rootElement);

            // coins
            Element coins = document.createElement("coins");
            rootElement.appendChild(coins);
            coins.setAttribute("amount", String.valueOf(Main.player.getCoins()));

            // items
            Element items = document.createElement("items");
            rootElement.appendChild(items);
            items.setAttribute("amount", String.valueOf(Main.player.getNumberOfItemsCollected()));

            // lives
            Element lives = document.createElement("lives");
            rootElement.appendChild(lives);
            lives.setAttribute("amount", String.valueOf(Main.player.getLives()));

            // entity kills
            Element entityKills = document.createElement("entity_kills");
            rootElement.appendChild(entityKills);
            entityKills.setAttribute("snail", String.valueOf(Main.player.getEntityKills().get("snail")));
            entityKills.setAttribute("wolf", String.valueOf(Main.player.getEntityKills().get("wolf")));

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
