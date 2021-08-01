package guis.outlines;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class OutlinedEllipse {

    private final Color fillColor;
    private final Color outlineColor;
    private final float strokeWeight;
    private final Ellipse2D.Double ellipse;

    /**
     *
     * basic constructor of the OutlinedEllipse
     * advanced version of the regular java.awt.geom.Ellipse2D.Double, where
     * an outline is added with specific outlineColor and thickness of the line
     *
     * @param x - the x coordinate of the ellipse
     * @param y - the y coordinate of the ellipse
     * @param width - the width of the ellipse
     * @param height - the height of the ellipse
     * @param fillColor - the color with which the ellipse is filled
     * @param outlineColor - the color of the outline around the ellipse
     * @param strokeWeight - the width/thickness of the outline-line
     * @see Color
     * @see Ellipse2D.Double
     */
    public OutlinedEllipse(int x, int y, int width, int height, Color fillColor, Color outlineColor, float strokeWeight) {
        this.fillColor = fillColor;
        this.outlineColor = outlineColor;
        this.strokeWeight = strokeWeight;

        this.ellipse = new Ellipse2D.Double(x, y, width, height);
    }

    /**
     *
     * drawing the outlined ellipse to the screen
     *
     * @param g - the graphics object used to paint onto the screen
     * @see Graphics2D
     * @see Color
     * @see Ellipse2D.Double
     */
    public void draw(Graphics2D g) {
        g.setColor(fillColor);
        g.fill(ellipse);
        g.setColor(outlineColor);
        g.setStroke(new BasicStroke(strokeWeight));
        g.draw(ellipse);
    }
}
