package SimonSays;

import gameLoop.Main;
import player.Player;

import java.awt.geom.Rectangle2D;

public class SimonSays {

    // sequence of this SimonSays where:
    //      1 is the top right quadrant
    //      2 is the top left quadrant
    //      3 is the bottom left quadrant
    //      4 is the bottom right quadrant
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
    public void update(Player player) {
        isColliding = player.getPlayerRect().intersects(bounds) && player.xSpeed == 0;
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
