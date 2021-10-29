package dataProcessing;

import toolbox.BasicConstants;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class LevelDataLoader {

    private static final ArrayList<Long> times = new ArrayList<>();
    private static final ArrayList<Integer> deathFaults = new ArrayList<>();
    private static final ArrayList<Integer> simonSaysFaults = new ArrayList<>();
    private static final ArrayList<Integer> coinFaults = new ArrayList<>();
    private static final ArrayList<Integer> itemFaults = new ArrayList<>();
    private static final ArrayList<Integer> mobFaults = new ArrayList<>();
    private static final ArrayList<Integer> totalFaults = new ArrayList<>();

    public static void loadAllLevelData() {
        File levelsDir = new File(BasicConstants.DEFAULT_PATH + "leveldata");
        if (!(levelsDir.exists() && levelsDir.isDirectory())) {
            System.err.println("Levels directory missing or not correctly placed!");
        }
        File[] levelsData = levelsDir.listFiles();
        assert levelsData != null;
        for (File file:levelsData) {
            if (hasCorrectFileType(file)) {
                loadDataFromFile(file);
            }
        }
    }

    /**
     *
     * checks, whether the file has the correct file-extension, therefore can
     * be used to read level-data
     *
     * @param file - the file to be checked
     * @return whether the file has the correct file-extension or not
     */
    private static boolean hasCorrectFileType(File file) {
        if (file.getName().contains(".xml")) {
            return true;
        } else {
            System.err.println("File with the name '" + file.getName() + "' is placed in wrong directory! (inside levels dir)");
            return false;
        }
    }

    private static void loadDataFromFile(File file) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            XMLEventReader eventReader = inputFactory.createXMLEventReader(fileInputStream);

            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();
                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();

                    switch (startElement.getName().getLocalPart()) {
                        case "time":
                            times.add(Long.parseLong(startElement.getAttributeByName(new QName("value")).getValue()));
                            break;

                        case "faults":
                            deathFaults.add(Integer.parseInt(startElement.getAttributeByName(new QName("value")).getValue()));
                            break;

                        case "simon_says_faults":
                            simonSaysFaults.add(Integer.parseInt(startElement.getAttributeByName(new QName("value")).getValue()));
                            break;

                        case "coin_faults":
                            coinFaults.add(Integer.parseInt(startElement.getAttributeByName(new QName("value")).getValue()));
                            break;

                        case "item_faults":
                            itemFaults.add(Integer.parseInt(startElement.getAttributeByName(new QName("value")).getValue()));
                            break;

                        case "mob_faults":
                            mobFaults.add(Integer.parseInt(startElement.getAttributeByName(new QName("value")).getValue()));
                            break;

                        case "total_faults":
                            totalFaults.add(Integer.parseInt(startElement.getAttributeByName(new QName("value")).getValue()));
                            break;
                    }
                }
            }
        } catch (FileNotFoundException | XMLStreamException e) {
            e.printStackTrace();
        }
    }

    public static long getTime(int index) {
        return times.get(index);
    }

    public static int getDeathFault(int index) {
        return deathFaults.get(index);
    }

    public static int getSimonSaysFault(int index) {
        return simonSaysFaults.get(index);
    }

    public static int getCoinFault(int index) {
        return coinFaults.get(index);
    }

    public static int getItemFault(int index) {
        return itemFaults.get(index);
    }

    public static int getMobFault(int index) {
        return mobFaults.get(index);
    }

    public static int getTotalFault(int index) {
        return totalFaults.get(index);
    }
}
