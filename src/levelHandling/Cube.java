package levelHandling;

import java.awt.*;

public class Cube {

    private final int cubeID;
    private final int SIZE = 40;
    private Rectangle rectangle;
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
    public int getX() {
        return rectangle.x;
    }

    /**
     *
     * @param x - the x coordinate of the cube
     */
    public void setX(int x) {
        this.rectangle.x = x;
    }

    /**
     *
     * @return the y coordinate of the cube measured in cubes from the bottom screen side
     */
    public int getY() {
        return rectangle.y;
    }

    /**
     *
     * @param y - the y coordinate of the cube
     */
    public void setY(int y) {
        this.rectangle.y = y;
    }

    /**
     *
     * @return the length of a side of the cube
     */
    public int getSIZE() {
        return SIZE;
    }
}
