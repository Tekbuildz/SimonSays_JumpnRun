package display;

import gameLoop.Main;
import levelHandling.Cube;
import Loader.Loader;
import player.PlayerInputs;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Renderer extends JPanel {

    private final Font FPSFont = new Font("Calibri", Font.PLAIN, 20);
    private int currentFPS;
    private final int WIDTH;
    private final int HEIGHT;

    private Image titleScreenBackground;

    public Renderer(int WIDTH, int HEIGHT) {
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;

        //this.titleScreenBackground = Loader.loadImage("titleBackground");
    }

    public void updateCurrentFPS(int currentFPS) {
        this.currentFPS = currentFPS;
    }

    public void paint(Graphics graphics) {

        //drawTitleScreen(graphics);


        // using the translate() function, the cubes can more easily be drawn from the bottom up
        graphics.translate(0, HEIGHT);

        drawLevel(graphics);

        // using the translate function with "-HEIGHT" because the origin is always viewed from the current origin
        // of the current coordinate system which is located at 0, HEIGHT (viewed from the actual screen => not visible)
        // in order to change the coordinate system back from the bottom left to the top left of the screen, the
        // height of the screen needs to be subtracted
        graphics.translate(0, -HEIGHT);
        drawFPS(graphics);
    }

    private void drawTitleScreen(Graphics g) {
        //g.drawImage(titleScreenBackground, 0, 0, null);
    }

    private void drawLevel(Graphics g) {
        for (int i = Main.temp.size() - 1; i >= 0; i--) {
            List<Cube> list = Main.temp.get(i);
            for (int j = list.size() - 1; j >= 0; j--) {
                Cube cube = list.get(j);
                if (cube.getCubeID() == 1) {
                    g.setColor(new Color(129, 91, 55));
                    g.fillRect(cube.getX() * cube.getSIZE(), -(Main.temp.size() - cube.getY()) * cube.getSIZE(), cube.getSIZE(), cube.getSIZE());

                }

                // printing the amount of airSides each cube sees on the cube
//                g.setColor(Color.BLACK);
//                boolean[] airSides = cube.getAirAtSides();
//                int trueCount = 0;
//                for (int k = 0; k < cube.getAirAtSides().length; k++) {
//                    if (airSides[k]) {
//                        trueCount++;
//                    }
//                }
//                g.drawString(String.valueOf(trueCount), cube.getX() * cube.getSIZE() + cube.getSIZE()/2, -(Main.temp.size() - cube.getY()) * cube.getSIZE() + cube.getSIZE()/2);
            }
        }
    }

    private void drawFPS(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(FPSFont);
        g.drawString("FPS: " + currentFPS, 10, 20);
    }
}
