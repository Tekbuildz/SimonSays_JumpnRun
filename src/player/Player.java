package player;

import levelHandling.Level;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Player {

    private final Rectangle2D.Double playerRect = new Rectangle2D.Double();
    private final float gravityAccel = 0.1f;
    private double xChange = 0;
    private double yChange = 0;
    private static final int cubeSizePixels = 40;
    private static final int playerWidth = 40;
    private static final int playerHeight = 79;

    /**
     *
     * the amount of health the player has left of the current life
     * if the health reaches the maximum, another life cannot be gained this way
     * if the health reaches 0, a life is removed from the players total number of lives
     */
    private int health;
    private int lives;


    /**
     *
     * @param p - the starting point of the player in the level
     */
    public Player(Point p) {
        playerRect.setRect(p.x, p.y - 800, playerWidth, playerHeight);
        health = 100;
        lives = 1;
    }

    /**
     *
     * applies gravity to the player
     */
    public void applyGravity() {
        yChange += gravityAccel;
        if (yChange >= 6) {
            yChange = 6;
        }
        if (hasVerticalCollision(Level.getCollisionBoxes(), yChange)) yChange = 0;
    }

    /**
     *
     * allows the player to jump upwards
     */
    public void jump() {
        // if statement checks if the velocity is 0 which is the case if the player is either colliding with the ceiling or the floor
        // checking the vertical collision with a positive velocity to eliminate the possibility for the player to jump when colliding with the ceiling
        if (yChange == 0 && hasVerticalCollision(Level.getCollisionBoxes(), 1)) {
            yChange = -3.5f;
        }
    }

    private boolean hasHorizontalCollision(ArrayList<Rectangle2D> collisionBoxes, Character direction) {
        boolean doesIntersect = false;
        Rectangle2D movedRect = playerRect.getBounds2D();
        if (direction == 'r') {
            movedRect.setRect(movedRect.getX() + 2, movedRect.getY(), movedRect.getWidth(), movedRect.getHeight());
        }
        else if (direction == 'l') {
            movedRect.setRect(movedRect.getX() - 2, movedRect.getY(), movedRect.getWidth(), movedRect.getHeight());
        }
        for (Rectangle2D collisionBox:collisionBoxes) {
            if (movedRect.intersects(collisionBox)) doesIntersect = true;
        }

        return doesIntersect;
    }

    private boolean hasVerticalCollision(ArrayList<Rectangle2D> collisionBoxes, double yVelocity) {
        boolean doesIntersect = false;
        Rectangle2D movedRect = playerRect.getBounds2D();
        movedRect.setRect(movedRect.getX(), movedRect.getY() + yVelocity, movedRect.getWidth(), movedRect.getHeight());
        for (Rectangle2D collisionBox:collisionBoxes) {
            if (movedRect.intersects(collisionBox)) doesIntersect = true;
        }
        return doesIntersect;
    }

    /**
     *
     * @param direction - the character, either 'l' or 'r' for left or right respectively indicating the direction of the player's movement
     *                    'n' refers to 'none' or i.o.w. no change at all
     */
    public void move(Character direction) {
        if (direction == 'r') {
            if (!hasHorizontalCollision(Level.getCollisionBoxes(), direction)) {
                xChange = 2;
            }
        } else if (direction == 'l') {
            if (!hasHorizontalCollision(Level.getCollisionBoxes(), direction)) {
                xChange = -2;
            }
        } else if (direction == 'n') {
            xChange = 0;
        }
    }

    /**
     *
     * updating the location of the player bounding box using change-values since there is no Rectangle2D.Double.setX()
     * function and therefore the entire rectangle would have to be set again, removing the possibility to move in two
     * directions at once using multiple functions
     */
    public void updatePlayerRectCoords() {
        playerRect.setRect(playerRect.getX() + xChange, playerRect.getY() + yChange, playerWidth, playerHeight);
    }

    /**
     *
     * @return the side length of a cube
     */
    public int getCubeSize() {
        return cubeSizePixels;
    }

    /**
     *
     * increases the number of lives by one
     */
    public void increaseLives() {
        lives++;
    }

    /**
     *
     * @return the x position of the player
     */
    public float getX() {
        return (float) playerRect.getX();
    }

    /**
     *
     * @return the y position of the player
     */
    public float getY() {
        return (float) playerRect.getY();
    }

    /**
     *
     * @return the health left of the current life
     */
    public int getHealth() {
        return health;
    }

    /**
     *
     * @param health - increases the health of the current life by the value of the parameter
     */
    public void increaseHealth(int health) {
        this.health += health;
    }

    /**
     *
     * @return the total number of lives the player has
     */
    public int getLives() {
        return lives;
    }

    /**
     *
     * increases the total number of lives by one
     */
    public void increaseNumOfLives() {
        lives++;
    }

    public static int getPlayerWidth() {
        return playerWidth;
    }

    public static int getPlayerHeight() {
        return playerHeight;
    }
}
