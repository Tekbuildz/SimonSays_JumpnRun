package levelHandling;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Cube {

    /**
     *
     * the cubeID represents the type of this cube (air, dirt, etc.)
     */
    private final int cubeID;
    private final int SIZE = 40;

    /**
     *
     * the imageID represents the image which this cube will show on the screen (air, dirt, dirt with grass on whatever side, etc.)
     */
    private int imageID;
    private Rectangle2D rectangle;
    // private static final int SIZE = DisplayManager.HEIGHT / 27;

    /**
     * contains information about whether the adjacent cubes are air cubes or not
     * index 0 = right
     * index 1 = top
     * index 2 = left
     * index 3 = bottom
     */
    private boolean[] airAtSides;

    /**
     *
     * @param cubeID - the type of this cube
     * @param x      - the x coordinate of the cube measured in cubes from the left screen side
     * @param y      - the y coordinate of the cube measured in cubes from the bottom screen side
     */
    public Cube(int cubeID, int x, int y) {
        this.cubeID = cubeID;
        this.rectangle = new Rectangle(x, y, SIZE, SIZE);

        airAtSides = new boolean[4];
    }

    /**
     *
     * @return the ID of the image which is rendered at this cubes position
     */
    public int getImageID() {
        return imageID;
    }

    /**
     *
     * @param imageID - the ID of the image which is rendered at this cubes position
     *                (only called once when loading the level from the LoadLevelFromFile.java class)
     */
    public void setImageID(int imageID) {
        this.imageID = imageID;
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
     * @param airAtSides - sets the information about whether the adjacent cubes are air cubes or not (for indices see
     *                   the information above)
     */
    public void setAirAtSides(boolean[] airAtSides) {
        this.airAtSides = airAtSides;
    }

    /**
     *
     * @return whether the adjacent cubes are air cubes
     */
    public boolean[] getAirAtSides() {
        return airAtSides;
    }

    /**
     *
     * @return the x coordinate of the cube measured in cubes from the left screen side
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
     * @return the y coordinate of the cube measured in cubes from the bottom screen side
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
    public int getSIZE() {
        return SIZE;
    }

    /**
     *
     * @return the rectangle of the cube
     */
    public Rectangle2D getRectangle() {
        return rectangle;
    }
}
