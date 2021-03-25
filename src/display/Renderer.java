package display;

import gameLoop.Main;
import levelHandling.Cube;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Renderer extends JPanel {

    private final Font FPSFont = new Font("Calibri", Font.PLAIN, 20);
    private int currentFPS;
    private final int WIDTH;
    private final int HEIGHT;

    public Renderer(int WIDTH, int HEIGHT) {
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
    }

    public void updateCurrentFPS(int currentFPS) {
        this.currentFPS = currentFPS;
    }

    public void paint(Graphics g) {
        // temporary, replaced by background image
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // using the translate() function, the cubes can easily be drawn from the bottom up
        g.translate(0, HEIGHT);

        for (int i = Main.temp.size() - 1; i >= 0; i--) {
            List<Cube> list = Main.temp.get(i);
            for (int j = list.size() - 1; j >= 0; j--) {
                Cube cube = list.get(j);
                if (cube.getCubeID() == 1) {
                    g.setColor(new Color(129, 91, 55));
                    g.fillRect(cube.getX() * cube.getSIZE(), -(Main.temp.size() - cube.getY()) * cube.getSIZE(), cube.getSIZE(), cube.getSIZE());
                }
            }
        }

        // using the translate function with "-HEIGHT" because the origin is always viewed from the current origin
        // of the current coordinate system which is located at 0, HEIGHT (viewed from the actual screen => not visible)
        // in order to change the coordinate system back from the bottom left to the top left of the screen, the
        // height of the screen needs to be subtracted
        g.translate(0, -HEIGHT);

        // drawing the FPS on the screen
        g.setColor(Color.BLACK);
        g.setFont(FPSFont);
        g.drawString("FPS: " + currentFPS, 10, 20);
    }
}
