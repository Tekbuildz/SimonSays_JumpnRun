package guis;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public class CheckBox {

    public static final int EMPTY = 0;
    public static final int TICKED = 1;
    public static final int CROSSED = 2;

    private int state;
    private final Rectangle2D.Double bounds;
    private final Color fillColor;
    private final int strokeWidth;

    // tick-mark
    private static final Color tickColor = new Color(1,172,14);
    private final Line2D.Double tickL1;
    private final Line2D.Double tickL2;

    // cross-mark
    private static final Color crossColor = new Color(240,0,0);
    private final Line2D.Double crossL1;
    private final Line2D.Double crossL2;

    /**
     *
     * basic constructor of a CheckBox
     *
     * @param x - the x coordinate of the CheckBox
     * @param y - the y coordinate of the CheckBox
     * @param width - the width of the CheckBox
     * @param height - the height of the CheckBox
     * @param fillColor - the fillColor of the CheckBox
     * @param strokeWidth - the thickness of the bounds of the CheckBox
     */
    public CheckBox(int x, int y, int width, int height, Color fillColor, int strokeWidth) {
        bounds = new Rectangle2D.Double(x, y, width, height);
        this.fillColor = fillColor;
        this.strokeWidth = strokeWidth;
        this.state = EMPTY;

        // tick-mark
        this.tickL1 = new Line2D.Double(x, y + height / 2f, x + width / 2f, y + height / 6f * 5);
        this.tickL2 = new Line2D.Double(x + width / 2f, y + height / 6f * 5, x + width, y);
        // cross-mark
        this.crossL1 = new Line2D.Double(x, y, x + width, y + height);
        this.crossL2 = new Line2D.Double(x + width, y, x, y + height);
    }

    /**
     *
     * draws the checkBox and depending on its state also a cross, tick or none
     *
     * @param g - the graphics object used to paint onto the screen
     */
    public void draw(Graphics2D g) {
        // drawing the GUI background
        g.setColor(fillColor);
        g.fill(bounds);

        // drawing the outlines
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(strokeWidth));
        g.draw(bounds);

        // drawing the state if needed
        // to add tick / cross depending on state variable
        if (state == TICKED) {
           g.setColor(tickColor);
           g.draw(tickL1);
           g.draw(tickL2);
       } else if (state == CROSSED) {
           g.setColor(crossColor);
           g.draw(crossL1);
           g.draw(crossL2);
       }
    }

    /**
     *
     * @return whether the checkBox is TICKED, EMPTY or CROSSED
     */
    public int getState() {
        return state;
    }

    /**
     *
     * @param state - the new state of the checkBox, input preferably using
     *              the static constants of this class
     */
    public void setState(int state) {
        this.state = state;
    }

    /**
     *
     * @return the rectangle enclosing the checkBox, also used for painting
     */
    public Rectangle2D.Double getBounds() {
        return bounds;
    }
}
