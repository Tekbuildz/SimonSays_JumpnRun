package gameLoop;

import display.DisplayManager;

import javax.swing.*;

public class Main extends JPanel {

    private DisplayManager display;

    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        setup();
    }

    private void setup() {
        display = new DisplayManager();
        display.createDisplay();
    }
}
