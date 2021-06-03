package display;

import gameLoop.Main;
import gamestates.StateMaster;

import javax.swing.*;
import java.awt.*;

public class Renderer extends JPanel {

    private final int WIDTH;
    private final int HEIGHT;
    private final Font FPSFont = new Font("Calibri", Font.PLAIN, 20);

    private Image titleScreenBackground;

    public Renderer(int WIDTH, int HEIGHT) {
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;

        //this.titleScreenBackground = Loader.loadImage("titleBackground");
    }

    public void paint(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics.create();

        //drawTitleScreen(graphics2D);

        graphics2D.clearRect(0, 0, WIDTH, HEIGHT);

        if (StateMaster.getState() != null) {
            StateMaster.getState().render(graphics2D);
        }

        drawFPS(graphics2D);
//        graphics2D.setColor(Color.WHITE);
//        graphics2D.fillRect(0, 0, WIDTH, HEIGHT);
    }

    /**
     *
     * drawing the fps to the screen
     */
    private void drawFPS(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.setFont(FPSFont);
        g.drawString("FPS: " + Main.getCurrentFPS(), 10, 20);
    }
}
