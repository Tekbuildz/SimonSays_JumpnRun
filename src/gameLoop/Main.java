package gameLoop;

import display.DisplayManager;
import levelHandling.Cube;
import levelHandling.Level;
import levelHandling.LoadLevelFromFile;

import javax.swing.*;
import java.util.List;

public class Main extends JPanel {

    private DisplayManager display;
    private Level level;
    public static List<List<Cube>> temp;

    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        setup();
    }

    private void setup() {
        temp = LoadLevelFromFile.getLevelCubes();

        display = new DisplayManager();
        display.createDisplay();

        level = new Level();
    }
}
