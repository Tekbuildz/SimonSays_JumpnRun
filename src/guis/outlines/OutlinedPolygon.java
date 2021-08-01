package guis.outlines;

import java.awt.*;

public class OutlinedPolygon {

    private final Color fillColor;

    private final Polygon polygon;
    private final Polyline polyline;

    /**
     *
     * basic constructor of an OutlinedPolygon
     * advanced version of the regular java.awt.Polygon, where an outline is
     * added with specific outlineColor and thickness of the outline-line
     *
     * @param xpoints - the x coordinates of the points connected by the
     *                polygon
     * @param ypoints - the y coordinates of the points connected by the
     *                polygon
     * @param npoints - number of points connected by the polygon
     * @param fillColor - the fill-color of the polygon
     * @param outlineColor - the color of the outline-line
     * @param strokeWeight - the width/thickness of the outline-line
     * @see Color
     * @see Polygon
     * @see Polyline
     */
    public OutlinedPolygon(int[] xpoints, int[] ypoints, int npoints, Color fillColor, Color outlineColor, float strokeWeight) {

        this.fillColor = fillColor;

        polygon = new Polygon(xpoints, ypoints, npoints);
        polyline = new Polyline(xpoints, ypoints, npoints, outlineColor, strokeWeight);
    }

    /**
     *
     * drawing the outlined polygon to the screen
     *
     * @param g - the graphics object used to paint onto the screen
     * @see Graphics2D
     * @see Color
     * @see Polygon
     * @see Polyline
     */
    public void draw(Graphics2D g) {
        g.setColor(fillColor);
        g.fill(polygon);
        polyline.draw(g);
    }

}
