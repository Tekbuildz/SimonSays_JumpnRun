package guis.outlines;

import java.awt.*;
import java.awt.geom.Line2D;

public class Polyline {
    private final int[] xpoints;
    private final int[] ypoints;
    private final int npoints;
    private final Color fillColor;
    private final float strokeWeight;

    private final Line2D.Double[] lines;

    /**
     *
     * basic constructor of the PolyLine with specific fillColor and thickness
     * using points to create an array of lines forging the PolyLine
     * using the points, the function creates multiple lines between each
     * of the points that should be connected to then form a PolyLine
     *
     * @param xpoints - the x coordinates of the points to be connected
     * @param ypoints - the y coordinates of the points to be connected
     * @param npoints - the number of points to be connected
     * @param fillColor - the color of the line
     * @param strokeWeight - the thickness of the line
     */
    public Polyline(int[] xpoints, int[] ypoints, int npoints, Color fillColor, float strokeWeight) {
        this.xpoints = xpoints;
        this.ypoints = ypoints;
        this.npoints = npoints;
        this.fillColor = fillColor;
        this.strokeWeight = strokeWeight;

        lines = new Line2D.Double[npoints];

        if (xpoints.length != ypoints.length || xpoints.length != npoints) {
            System.err.println("The number of xpoints do not match the number of ypoints or npoints");
            System.exit(-1);
        }
        else {
            // adding 1 to npoints to complete the outline, otherwise one line would be missing,
            // hence the modulo operator needs to be used in order to prevent an IndexOutOfBoundsException
            for (int i = 0; i < npoints; i++) {
                lines[i] = new Line2D.Double(xpoints[i], ypoints[i], xpoints[(i + 1) % npoints], ypoints[(i + 1) % npoints]);
            }
        }
    }

    /**
     *
     * drawing the PolyLine
     * the function draws all the lines in the lines array forming a PolyLine
     *
     * @param g - the graphics object used to paint onto the screen
     */
    public void draw(Graphics2D g) {
        g.setColor(fillColor);
        g.setStroke(new BasicStroke(strokeWeight));
        for (int i = 0; i < lines.length; i++) {
            g.draw(lines[i]);
        }
    }
}
