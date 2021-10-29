package dataProcessing;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import toolbox.BasicConstants;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * class serves to check, whether all necessary data files exist
 * if they don't, the class will create them
 */
public class FileChecker {

    private static final ArrayList<String> fileNames = new ArrayList<>();

    /**
     *
     * called when launching the game, starts process of checking for missing
     * files
     */
    public static void start() {
//        fileNames.add("player.xml");
        for (int i = 1; i < getNumberOfLevels() + 1; i++) {
            fileNames.add("leveldata/" + i + ".xml");
        }
        ArrayList<String> nonExistingFiles = getNonExistingFiles();
        if (nonExistingFiles.size() != 0) {
            createLevelDataFile(nonExistingFiles);
        }

        if (!new File(BasicConstants.DEFAULT_PATH + "player.xml").exists()) {
            createPlayerFile();
        }
    }

    /**
     *
     * @return the number of files inside the levels-directory
     */
    private static int getNumberOfLevels() {
        File directory = new File(BasicConstants.DEFAULT_PATH + "levels");
        int fileCount = 0;
        if (directory.isDirectory() && directory.exists()) {
            fileCount = Objects.requireNonNull(directory.list()).length;
        } else System.err.println("The path: " + BasicConstants.DEFAULT_PATH + "levels is not a directory or doesn't exist!");
        return fileCount;
    }

    /**
     *
     * @return a list containing the names of all files which do not exist
     *          in the directory and need to be generated
     */
    private static ArrayList<String> getNonExistingFiles() {
        ArrayList<String> nonExistingFiles = new ArrayList<>();
        for (String fileName: fileNames) {
            File file = new File(BasicConstants.DEFAULT_PATH + fileName);
            if (!file.exists()) {
                nonExistingFiles.add(fileName);
            }
        }
        return nonExistingFiles;
    }

    /**
     *
     * creates the missing files
     *
     * @param filesToBeGenerated - a list containing the names of all files to
     *                           be generated
     */
    private static void createLevelDataFile(ArrayList<String> filesToBeGenerated) {
        for (String fileName: filesToBeGenerated) {
            File file = new File(BasicConstants.DEFAULT_PATH + fileName);
            try {
                file.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(file, false);
            } catch (IOException e) {
                e.printStackTrace();
            }
            populateCreatedFile(BasicConstants.DEFAULT_PATH + fileName);
        }
    }

    /**
     *
     * populates files created in the createLevelDataFile() function with basic
     * skeleton for data-saving
     *
     * @param fileToBePopulated - name of the file to be populated
     */
    private static void populateCreatedFile(String fileToBePopulated) {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element rootElement = document.createElement("data");
            document.appendChild(rootElement);

            Element time = document.createElement("time");
            time.setAttribute("value", "-1");
            rootElement.appendChild(time);

            Element faults = document.createElement("faults");
            faults.setAttribute("value", "-1");
            rootElement.appendChild(faults);

            Element simonSaysFaults = document.createElement("simon_says_faults");
            simonSaysFaults.setAttribute("value", "-1");
            rootElement.appendChild(simonSaysFaults);

            Element coinFaults = document.createElement("coin_faults");
            coinFaults.setAttribute("value", "-1");
            rootElement.appendChild(coinFaults);

            Element itemFaults = document.createElement("item_faults");
            itemFaults.setAttribute("value", "-1");
            rootElement.appendChild(itemFaults);

            Element mobFaults = document.createElement("mob_faults");
            mobFaults.setAttribute("value", "-1");
            rootElement.appendChild(mobFaults);

            Element totalFaults = document.createElement("total_faults");
            totalFaults.setAttribute("value", "-1");
            rootElement.appendChild(totalFaults);

            FileOutputStream fileOutputStream = new FileOutputStream(fileToBePopulated);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(fileOutputStream);

            transformer.transform(source, result);

        } catch (ParserConfigurationException | FileNotFoundException | TransformerException e) {
            e.printStackTrace();
        }
    }

    private static void createPlayerFile() {
        String fileName = BasicConstants.DEFAULT_PATH + "player.xml";
        File file = new File(fileName);
        try {
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file, false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            Document document = documentBuilder.newDocument();
            Element rootElement = document.createElement("data");
            document.appendChild(rootElement);

            Element playerStats = document.createElement("player_stats");
            rootElement.appendChild(playerStats);

            Element coins = document.createElement("coins");
            coins.setAttribute("amount", "0");
            playerStats.appendChild(coins);

            Element items = document.createElement("items");
            items.setAttribute("amount", "0");
            playerStats.appendChild(items);

            Element lives = document.createElement("lives");
            lives.setAttribute("amount", "1");
            playerStats.appendChild(lives);

            Element entityKills = document.createElement("entity_kills");
            entityKills.setAttribute("snail", "0");
            entityKills.setAttribute("wolf", "0");
            playerStats.appendChild(entityKills);

            FileOutputStream fileOutputStream = new FileOutputStream(fileName);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
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
