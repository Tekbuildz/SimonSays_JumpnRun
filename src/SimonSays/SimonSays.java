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
    private boolean isStarted = false;
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

    public void update() {
        // if the drawOverlay boolean is still true but the player since has moved, the SimonSays cannot be accessed anymore
//        if (isColliding && !(Main.player.getPlayerRect().intersects(bounds) && Main.player.xSpeed == 0 && !wasPreviouslyUsed)) wasPreviouslyUsed = true;
        isColliding = Main.player.getPlayerRect().intersects(bounds);
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void setStarted(boolean started) {
        this.isStarted = started;
    }

    public boolean isColliding() {
        return isColliding;
    }

    public Rectangle2D.Double getBounds() {
        return bounds;
    }
}
