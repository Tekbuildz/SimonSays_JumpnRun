package guis;

import guis.outlines.TriangularRectangle;

import java.awt.*;

public class HealthBar {

    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final int triangularCutoffsize;
    private Color fillColor;

    private Color bgColor;
    private boolean hasBGColor;

    private final TriangularRectangle outline;
    private TriangularRectangle fill;
    private TriangularRectangle bg;
    private int fillLevel;

    /**
     *
     * basic constructor of a HealthBar
     * this gui component adds a health bar where the fillLevel is used to
     * changed the area of the health bar which is filled with a specified
     * color
     *
     * @param x - the x coordinate of the gui component
     * @param y - the y coordinate of the gui component
     * @param width - the width of the gui component
     * @param height - the height of the gui component
     * @param triangularCutoffSize - the length of the isosceles triangle which
     *                             is cut off (used in instance of a
     *                             TriangularRectangle)
     * @param fillColor - the color which fills up the health bar according
     *                  to the fillLevel
     * @param outlineColor - the color of the outline of the gui component
     * @param strokeWeight - the width/thickness of the outline-line
     */
    public HealthBar(int x, int y, int width, int height, int triangularCutoffSize, Color fillColor, Color outlineColor, float strokeWeight) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.triangularCutoffsize = triangularCutoffSize;
        this.fillColor = fillColor;

        this.hasBGColor = false;

        outline = new TriangularRectangle(x, y, width, height, triangularCutoffSize, new Color(0, 0, 0, 0), outlineColor, strokeWeight);
        fill = new TriangularRectangle(x, y, width, height, triangularCutoffSize, fillColor);
    }

    /**
     *
     * sets the fillLevel of the health bar
     * the fillLevel is an integer ranging from 0 to 100 (determined by
     * the amount of health left of the player)
     *
     * @param fillLevel - the new fillLevel of the health bar
     */
    public void setFillLevel(int fillLevel) {
        this.fillLevel = fillLevel;
    }

    /**
     *
     * sets the background color of the health bar
     * especially useful if the gui fill-color should be different to the
     * background color of the health bar since the polygon of it has a
     * transparent fillColor
     *
     * @param bgColor - the background color of the health bar
     */
    public void setBGColor(Color bgColor) {
        this.hasBGColor = true;
        bg = new TriangularRectangle(x, y, width, height, triangularCutoffsize, bgColor);
    }

    /**
     *
     * changes the fillColor of the health bar
     * can be used to make the health bar turn red when reaching 0 health
     *
     * @param fillColor - the new color which fills the health bar
     */
    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    /**
     *
     * updates the instance of TriangularRectangle which fills the health bar
     * to match the fillLevel
     */
    public void update() {
        fill = new TriangularRectangle(x, y, width * fillLevel / 100, height, triangularCutoffsize, fillColor);
    }

    /**
     *
     * drawing the health bar to the screen
     *
     * @param g - the graphics object used to paint onto the screen
     */
    public void draw(Graphics2D g) {
        if (this.hasBGColor) {
            bg.draw(g);
        }
        fill.draw(g);
        outline.draw(g);
    }
}
