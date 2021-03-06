package display;

import gamestates.StateMaster;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 *
 * contains JPanel to which all the elements are drawn
 *
 * @author Thomas Bundi
 * @version 1.3
 * @since 0.6
 */
public class Renderer extends JPanel{

    private final int WIDTH;
    private final int HEIGHT;

    /**
     *
     * basic constructor of the Renderer
     *
     * @param WIDTH - the width of the resolution of the current main screen
     * @param HEIGHT - the height of the resolution of the current main screen
     */
    public Renderer(int WIDTH, int HEIGHT) {
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
    }

    /**
     *
     * painting all the contents of the current state on the screen
     *
     * @param graphics - the graphics object used to paint onto the screen
     */
    public void paint(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics.create();

        graphics2D.clearRect(0, 0, WIDTH, HEIGHT);
        graphics2D.setTransform(new AffineTransform());

        if (StateMaster.getState() != null) {
            StateMaster.getState().render(graphics2D);
        }

        graphics2D.dispose();
    }
}
