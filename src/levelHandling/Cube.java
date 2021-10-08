package levelHandling;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 *
 * the Cube class contains all the information necessary about a cube such as
 * its position and ID
 *
 * @author Thomas Bundi
 * @version 1.2
 * @since 0.3
 */
public class Cube {


    private final int cubeID;
    private final int pixelSIZE = 40;
    private final int SIZE = 1;

    private final Rectangle2D rectangle;

    /**
     *
     * basic constructor for the cube
     * constructor adds a cubeID to the cube representing the type of this
     * cube (air, dirt, etc.)
     * it creates a rectangle using the remaining parameters to represent
     * its location in the coordinate system
     *
     * @param cubeID - the type of this cube
     * @param x      - the x coordinate of the cube measured in
     *               cubes from the left screen side
     * @param y      - the y coordinate of the cube measured in
     *               cubes from the bottom screen side
     */
    public Cube(int cubeID, int x, int y) {
        this.cubeID = cubeID;

        // the rectangle describing the cube, where x, y, SIZE are measured in tiles
        this.rectangle = new Rectangle(x, y, SIZE, SIZE);
    }

    /**
     *
     * @return the type of this specific cube (e.g. dirt, air etc.)
     */
    public int getCubeID() {
        return cubeID;
    }

    /**
     *
     * @return the x coordinate of the cube measured in
     *          cubes from the left screen side
     */
    public double getX() {
        return rectangle.getX();
    }

    /**
     *
     * @param x - the x coordinate of the cube
     */
    public void setX(int x) {
        this.rectangle.setRect(x, rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
    }

    /**
     *
     * @return the y coordinate of the cube measured in
     *          cubes from the bottom screen side
     */
    public double getY() {
        return rectangle.getY();
    }

    /**
     *
     * @param y - the y coordinate of the cube
     */
    public void setY(int y) {
        this.rectangle.setRect(rectangle.getX(), y, rectangle.getWidth(), rectangle.getHeight());
    }

    /**
     *
     * @return the length of a side of the cube
     */
    public int getPixelSIZE() {
        return pixelSIZE;
    }
}
