package guis.outlines;

import java.awt.*;

public class TriangularRectangle {

    private final Polygon polygon;
    private Polyline outline;
    private boolean hasOutline = true;

    private Color fillColor;

    private int x, y;
    private int width, height;
    private int triangularCutoffSize;

    /**
     *
     * alternate constructor of a triangular-rectangle-shape missing outline
     * this shape consists of a basic rectangle of which the
     * edges were cut off in a triangular shape
     * these are isosceles triangles
     *
     * @param x - the x coordinate of the shape
     * @param y - the y coordinate of the shape
     * @param width - the width of the rectangle enclosing the shape
     * @param height - the height of the rectangle enclosing the shape
     * @param triangularCutoffSize - length of one of the sides
     *                             that are equal of the isosceles triangle
     * @param fillColor - the color of the polygon
     */
    public TriangularRectangle(int x, int y, int width, int height, int triangularCutoffSize, Color fillColor) {
        this(x, y, width, height, triangularCutoffSize, fillColor, null, 0f);
    }

    /**
     *
     * basic constructor of a triangular-rectangle-shape
     * this version of the constructor also contains an outline of the shape
     * to help enclosing the area further
     * <p>
     * this shape consists of a basic rectangle of which the
     * edges were cut off in a triangular shape
     * these are isosceles triangles
     *
     * @param x - the x coordinate of the shape
     * @param y - the y coordinate of the shape
     * @param width - the width of the rectangle enclosing the shape
     * @param height - the height of the rectangle enclosing the shape
     * @param triangularCutoffSize - length of one of the sides
     *                             that are equal of the isosceles triangle
     * @param fillColor - the color of the polygon
     * @param outlineColor - the color of the outline enclosing the polygon
     * @param strokeWeight - the width/thickness of the outline-line
     */
    public TriangularRectangle(int x, int y, int width, int height, int triangularCutoffSize, Color fillColor, Color outlineColor, float strokeWeight) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.triangularCutoffSize = triangularCutoffSize;

        this.fillColor = fillColor;

        polygon = new Polygon(
                new int[]{x + triangularCutoffSize, x + width - triangularCutoffSize, x + width, x + width, x + width - triangularCutoffSize, x + triangularCutoffSize, x, x},
                new int[]{y, y, y + triangularCutoffSize, y + height - triangularCutoffSize, y + height, y + height, y + height - triangularCutoffSize, y + triangularCutoffSize},
                8
        );

        if (outlineColor != null && strokeWeight > 0) {
            outline = new Polyline(
                    new int[]{x + triangularCutoffSize, x + width - triangularCutoffSize, x + width, x + width, x + width - triangularCutoffSize, x + triangularCutoffSize, x, x},
                    new int[]{y, y, y + triangularCutoffSize, y + height - triangularCutoffSize, y + height, y + height, y + height - triangularCutoffSize, y + triangularCutoffSize},
                    8,
                    outlineColor,
                    strokeWeight
            );
        } else {
            this.hasOutline = false;
        }
    }

    /**
     *
     * drawing the triangular-rectangle-shape to the screen
     * optionally, if the outlineColor and the strokeWeight were defined
     * when calling the constructor, and therefore, an outline is requested,
     * it is drawn as well
     *
     * @param g - the graphics object used to paint onto the screen
     */
    public void draw(Graphics2D g) {
        g.setColor(fillColor);
        g.fill(polygon);

        if (hasOutline) outline.draw(g);
    }
}
