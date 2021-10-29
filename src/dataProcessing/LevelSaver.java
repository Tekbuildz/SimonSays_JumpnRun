package dataProcessing;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class LevelSaver {

    public static void init(String completePath, long timeInMS, int deathFaults, int simonSaysFaults, int coinFaults, int itemFaults, int mobFaults, int totalFaults) {
        if (checkIfHighScore(completePath, timeInMS, totalFaults)) {
            saveData(completePath, timeInMS, deathFaults, simonSaysFaults, coinFaults, itemFaults, mobFaults, totalFaults);
        }
    }

    /**
     *
     * function checks, whether the score achieved in the recent run is better
     * than the one prior
     *
     * @param completePath - the path to the XML file where the current high-
     *                     score of the level is saved
     * @param timeInMS - the time, the player had in this run
     * @param totalFaults - the number of faults, the player had in this run,
     *                    is a combination of all faults (deaths, mobs not
     *                    killed, etc.)
     * @return whether the score achieved in this run is better or worse, than
     *          the previous high-score
     */
    private static boolean checkIfHighScore(String completePath, long timeInMS, int totalFaults) {
        // using StringUtils from org.apache.commons.lang3 to get String between two Strings
        int levelID = 0; // cannot be 0, since first level is level 1
        String levelNum = StringUtils.substringBetween(completePath, "leveldata/", ".xml");
        if (levelNum.length() == 1) {
            if (Character.isDigit(levelNum.toCharArray()[0])) {
                levelID = Integer.parseInt(levelNum);
            } else {
                System.err.println("Problem retrieving name of Level out of completePath in LevelSaver.java!");
            }
        } else {
            System.err.println("Problem retrieving name of Level out of completePath in LevelSaver.java!");
        }
        // to use the levelID to read from the ArrayLists contained in LevelDataLoader.java, 1 needs to be subtracted
        // since in all file-names, the enumeration starts with 1, but first index is always 0
        levelID--;

        long previousTimeInMS = LevelDataLoader.getTime(levelID);
        int previousTotalFaults = LevelDataLoader.getTotalFault(levelID);
        System.out.println(previousTimeInMS);
        System.out.println(previousTotalFaults);

        // checking the faults
        // totalFaults is -1 at first creation of the file => level was never played
        if (previousTotalFaults == -1 || previousTotalFaults > totalFaults) return true;
        // checking the time if faults are equal
        else if (previousTotalFaults == totalFaults) return previousTimeInMS > timeInMS;

        // if the faults aren't lower or the faults are equal but the time is higher, returns false
        return false;
    }

    /**
     *
     * saves the high-score of a specified level, none of the parameter values
     * are already scaled for the total faults
     *
     * @param completePath - the name of the level and therefore also the file
     * @param timeInMS - the time in milliseconds, which the player took to
     *                 complete the level
     * @param deathFaults - the number of deaths which the player had
     * @param simonSaysFaults - the number of incorrectly solved Simon Says
     * @param coinFaults - the number of coins which were not collected
     * @param itemFaults - the number of items which were not collected
     * @param mobFaults - the number of mobs which weren't killed
     */
    public static void saveData(String completePath, long timeInMS, int deathFaults, int simonSaysFaults, int coinFaults, int itemFaults, int mobFaults, int totalFaults) {

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element rootElement = document.createElement("data");
            document.appendChild(rootElement);

            Element time = document.createElement("time");
            time.setAttribute("value", String.valueOf(timeInMS));
            rootElement.appendChild(time);

            // ----------------------------------------------------------------- FAULTS
            Element faults = document.createElement("faults");
            faults.setAttribute("value", String.valueOf(deathFaults));
            rootElement.appendChild(faults);

            Element simonSays = document.createElement("simon_says_faults");
            simonSays.setAttribute("value", String.valueOf(simonSaysFaults));
            rootElement.appendChild(simonSays);

            Element coins = document.createElement("coin_faults");
            coins.setAttribute("value", String.valueOf(coinFaults));
            rootElement.appendChild(coins);

            Element items = document.createElement("item_faults");
            items.setAttribute("value", String.valueOf(itemFaults));
            rootElement.appendChild(items);

            Element mobs = document.createElement("mob_faults");
            mobs.setAttribute("value", String.valueOf(mobFaults));
            rootElement.appendChild(mobs);

            Element total = document.createElement("total_faults");
            total.setAttribute("value", String.valueOf(totalFaults));
            rootElement.appendChild(total);
            // -----------------------------------------------------------------

            FileOutputStream fileOutputStream = new FileOutputStream(completePath);
            System.out.println(completePath);

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
