package entities.mob;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class Mob {

    /**
     *
     * updates the direction of movement of the mob and checks for collisions
     */
    public abstract void update();

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
     * @return the bounds representing the hitbox of the mob
     */
    public abstract Rectangle2D.Double getBounds();

    /**
     *
     * removes the mob by changing a variable in order to prevent it from
     * rendering on screen and colliding with the player
     */
    public abstract void hit();

    /**
     *
     * @return whether the mob has died or not
     */
    public abstract boolean isShown();

    /**
     *
     * resets the variable show of each mob
     */
    public abstract void resetShown();
}
