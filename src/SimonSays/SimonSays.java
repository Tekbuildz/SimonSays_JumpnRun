package SimonSays;

import gameLoop.Main;

import java.awt.geom.Rectangle2D;

public class SimonSays {

    // sequence of this SimonSays where:
    // X2X1X
    // XXXXX
    // X3X4X
    // is the integer assigned to each button
    private boolean isColliding = false;
    private boolean isCompleted = false;
    private final Rectangle2D.Double bounds;

    /**
     *
     * creates a new SimonSays game where the player has to remember the
     * sequence the previous SimonSays showed
     *
     * @param x - the x coordinate of the SimonSays
     * @param y - the y coordinate of the SimonSays
     * @param width - the width of the SimonSays
     * @param height - the height of the SimonSays
     */
    public SimonSays(int x, int y, int width, int height) {
        bounds = new Rectangle2D.Double(x, y, width, height);
    }

    /**
     *
     * checks whether the SS collides with the player
     */
    public void update() {
        isColliding = Main.player.getPlayerRect().intersects(bounds) && Main.player.xSpeed == 0;
    }

    /**
     *
     * @return whether the SS was completed
     */
    public boolean isCompleted() {
        return isCompleted;
    }

    /**
     *
     * @param completed - sets whether the SS is completed
     */
    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    /**
     *
     * @return whether the SS is colliding with the player
     */
    public boolean isColliding() {
        return isColliding;
    }

    /**
     *
     * @return the rectangle enclosing the SS in the level
     */
    public Rectangle2D.Double getBounds() {
        return bounds;
    }
}
