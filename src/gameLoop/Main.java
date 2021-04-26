package gameLoop;

import display.DisplayManager;
import levelHandling.Cube;
import levelHandling.Level;
import player.Player;

import javax.swing.*;
import java.util.ArrayList;

public class Main extends JPanel {

    private DisplayManager display;
    private Level level;
    public static Player player;
    public static ArrayList<ArrayList<Cube>> temp;

    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        setup();
    }

    private void setup() {
        level = new Level("Level_1");
        temp = Level.getLevelCubes();

        player = new Player(Level.getSpawnLocation());

        display = new DisplayManager();
        display.createDisplay();
    }
}
