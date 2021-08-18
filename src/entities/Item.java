package entities;

import java.awt.geom.Rectangle2D;

public class Item {

    private final Rectangle2D.Double bounds;
    private boolean wasCollected = false;
    private final int imageArrayIndex;

    /**
     *
     * basic constructor of an Item
     * is an entity in a level that can be collected by the player
     *
     * @param x - the x coordinate of the item
     * @param y - the y coordinate of the item
     * @param width - the width of the hitbox of the item
     * @param height - the height of the hitbox of the item
     * @param imageArrayIndex - a random integer representing the index in the
     *                        imageArray containing the images of the LED, PCB
     *                        and screw
     */
    public Item(int x, int y, int width, int height, int imageArrayIndex) {
        this.bounds = new Rectangle2D.Double(x, y, width, height);
        this.imageArrayIndex = imageArrayIndex;
    }

    /**
     *
     * @return the bounds enclosing the item and representing the hitbox of it
     */
    public Rectangle2D.Double getBounds() {
        return bounds;
    }

    /**
     *
     * @return whether the item was previously collected or not
     */
    public boolean isWasCollected() {
        return wasCollected;
    }

    /**
     *
     * @param wasCollected - the new state of the boolean (used when resetting)
     */
    public void setWasCollected(boolean wasCollected) {
        this.wasCollected = wasCollected;
    }

    /**
     *
     * @return the index in the imageArray deciding on the type of the item
     *          (LED, PCB, screw)
     */
    public int getImageArrayIndex() {
        return imageArrayIndex;
    }
}
