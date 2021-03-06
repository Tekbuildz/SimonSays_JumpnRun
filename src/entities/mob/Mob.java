package entities.mob;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 *
 * abstract class Mob which contains all the basic functions all the mobs have
 * to be able to do and execute
 *
 * @author Thomas Bundi
 * @version 0.7
 * @since 2.9
 */
public abstract class Mob {

    public static final float gravityAccel = 0.1f;

    /**
     *
     * updates the direction of movement of the mob and checks for collisions
     *
     * @param collisionBoxes - the boxes with which the mob can collide
     */
    public abstract void update(ArrayList<Rectangle2D> collisionBoxes);

    /**
     *
     * draws the mob using its predefined image to the screen
     * with the given coordinates and the graphics object, the image
     * corresponding to the specific mob can be displayed on screen
     *
     * @param g - the graphics object used to paint onto the screen
     * @param x - the x position on screen where the mob is drawn
     * @param y - the y position on screen where the mob is drawn
     */
    public abstract void draw(Graphics2D g, int x, int y);

    /**
     *
     * @return the type of the mob, e.g. snail, wolf
     */
    public abstract String getType();

    /**
     *
     * @return the bounds representing the hitbox of the mob
     */
    public abstract Rectangle2D.Double getBounds();

    /**
     *
     * subtracts a constant amount of health of the mob
     * function is called if the mob was hit by the player
     * changes hasCollisions boolean if the mob has no
     * health left
     *
     * @return whether the mob was killed during this hit or not
     */
    public abstract boolean hit();

    /**
     *
     * resets the variable hasCollisions
     */
    public abstract void resetCollisions();

    /**
     *
     * resets the bounds of the mob to its original bounds
     */
    public abstract void resetBounds();

    /**
     *
     * resets the health of the mob
     */
    public abstract void resetHealth();

    /**
     *
     * @return whether the mob has collisions or not (no collisions when dead)
     */
    public abstract boolean hasCollisions();
}
