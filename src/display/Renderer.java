package display;

import gameLoop.Main;
import levelHandling.Cube;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
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
        Graphics2D graphics2D = (Graphics2D) graphics.create();

        //drawTitleScreen(graphics2D);

        graphics2D.setColor(Color.WHITE);
        graphics2D.fillRect(0, 0, WIDTH, HEIGHT);

        // using the translate() function, the cubes can more easily be drawn from the bottom up
        graphics2D.translate(0, HEIGHT);
        drawLevel(graphics2D);

        drawPlayer(graphics2D);
        // using the translate function with "-HEIGHT" because the origin is always viewed from the current origin
        // of the current coordinate system which is located at 0, HEIGHT (viewed from the actual screen => not visible)
        // in order to change the coordinate system back from the bottom left to the top left of the screen, the
        // height of the screen needs to be subtracted
        graphics2D.translate(0, -HEIGHT);


        drawFPS(graphics2D);
    }

    /**
     *
     * drawing the title screen
     */
    private void drawTitleScreen(Graphics2D g) {
        //g.drawImage(titleScreenBackground, 0, 0, null);
    }

    /**
     *
     * drawing all the level-rectanlges
     */
    private void drawLevel(Graphics2D g) {
        for (int i = Main.temp.size() - 1; i >= 0; i--) {
            List<Cube> list = Main.temp.get(i);
            for (int j = list.size() - 1; j >= 0; j--) {
                Cube cube = list.get(j);
                if (cube.getCubeID() == 1) {
                    g.setColor(new Color(129, 91, 55));
                    Rectangle2D.Double currentRect = new Rectangle2D.Double(cube.getX() * cube.getSIZE(), -(Main.temp.size() - cube.getY()) * cube.getSIZE(), cube.getSIZE(), cube.getSIZE());
                    g.fill(currentRect);
                    //g.fillRect(cube.getX() * cube.getSIZE(), -(Main.temp.size() - cube.getY()) * cube.getSIZE(), cube.getSIZE(), cube.getSIZE());

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

    /**
     *
     * drawing the fps to the screen
     */
    private void drawFPS(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.setFont(FPSFont);
        g.drawString("FPS: " + currentFPS, 10, 20);
    }

    /**
     * drawing the player to the screen
     */
    private void drawPlayer(Graphics2D g) {
        g.setColor(Color.BLUE);
        float playerX = Main.player.getX();
        float playerY = Main.player.getY();
        //Rectangle2D.Double playerRect = new Rectangle2D.Double((playerX * Main.player.getCubeSize()), (playerY - 10) * Main.player.getCubeSize(), Main.player.getCubeSize(), 2 * Main.player.getCubeSize());
        Rectangle2D.Double playerRect = new Rectangle2D.Double((playerX * Main.player.getCubeSize()), -(Main.temp.size() - playerY + 10) * Main.player.getCubeSize(), Main.player.getCubeSize(), 2 * Main.player.getCubeSize());
        //graphics2D.draw(playerRect);
        g.fill(playerRect);
    }
}
