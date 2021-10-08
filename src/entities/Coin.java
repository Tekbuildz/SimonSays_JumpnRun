package entities;

import java.awt.geom.Rectangle2D;

/**
 *
 * coin class which only contains the basic information about the coin, like if
 * it was previously collected, its location and its value
 * <p>
 * the animation is handled in the GameState class
 *
 * @author Thomas Bundi
 * @version 0.3
 * @since 2.4
 */
public class Coin {

    private final Rectangle2D.Double bounds;
    private boolean wasCollected = false;
    private final int value;

    /**
     *
     * alternate constructor of a Coin
     * is an entity in a level that can be collected by the player and adds
     * money to his bank according to the value of the coin
     *
     * @param x - the x coordinate of the hitbox of the coin
     * @param y - the y coordinate of the hitbox of the coin
     * @param width - the width of the hitbox of the coin
     * @param height - the height of the hitbox of the coin
     * @param value - how much the coin is worth
     */
    public Coin(int x, int y, int width, int height, int value) {
        this(new Rectangle2D.Double(x, y, width, height), value);
    }

    /**
     *
     * basic constructor of a Coin
     * is an entity in a level that can be collected by the player and adds
     * money to his bank according to the value of the coin
     *
     * @param collisionBox - the rectangle representing the hitbox of the coin
     * @param value - how much the coin is worth
     */
    public Coin(Rectangle2D.Double collisionBox, int value) {
        this.bounds = collisionBox;
        this.value = value;
    }

    /**
     *
     * @return the hitbox of the coin
     */
    public Rectangle2D.Double getBounds() {
        return bounds;
    }

    /**
     *
     * @return how much the coin is worth
     */
    public int getValue() {
        return value;
    }

    /**
     *
     * @return whether the player already collected the coin or not
     */
    public boolean isWasCollected() {
        return wasCollected;
    }

    /**
     *
     * @param wasCollected - if the player collected the coin
     */
    public void setWasCollected(boolean wasCollected) {
        this.wasCollected = wasCollected;
    }
}
