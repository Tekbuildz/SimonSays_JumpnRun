package player;

import levelHandling.Level;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Player {

    private final Rectangle2D.Double playerRect = new Rectangle2D.Double();
    private final float gravityAccel = 0.1f;
    private double xSpeed = 0;
    private double ySpeed = 0;
    private static final int cubeSizePixels = 40;
    private static final int playerWidth = 40;
    private static final int playerHeight = 79;

    // the amount of health the player has left of the current life
    // if the health reaches the maximum, another life cannot be gained this way
    // if the health reaches 0, a life is removed from the players total number of lives
    private int health;
    private int lives;


    /**
     *
     * basic constructor of the player
     * creates a rectangle representing the hitbox of the player
     * setting the amount of health and number of lives
     *
     * @param p - the starting point of the player in the level
     */
    public Player(Point p) {
        playerRect.setRect(p.x, p.y, playerWidth, playerHeight);
        health = 100;
        lives = 1;
    }

    /**
     *
     * applies gravity to the player
     * (roughly representing gravity similar to
     * the one on the earth)
     * checks if the player is standing on the ground or hitting the ceiling in
     * which case the vertical momentum/speed has to be canceled
     */
    public void applyGravity() {
        ySpeed += gravityAccel;
        if (ySpeed >= 6) {
            ySpeed = 6;
        }
        if (hasVerticalCollision(Level.getCollisionBoxes(), ySpeed)) ySpeed = 0;
    }

    /**
     *
     * handling the jumping-ability of the player
     * sets the ySpeed to a reasonable negative value (where the player can jump roughly 1.5 tiles high) if the player collides and
     * therefore stands on the ground and has no vertical speed
     */
    public void jump() {
        // if statement checks if the velocity is 0 which is the case if the player is either colliding with the ceiling or the floor
        // checking the vertical collision with a positive velocity to eliminate the possibility for the player to jump when colliding with the ceiling
        if (ySpeed == 0 && hasVerticalCollision(Level.getCollisionBoxes(), 1)) {
            ySpeed = -3.5f;
        }
    }

    /**
     *
     * checks if the player would collide with a wall in the upcoming tick
     * using a copy of the playerRect called movedRect to which the xVelocity
     * depending on the direction of the movement was added, the function
     * checks whether the player intersects with a wall or not
     *
     * @param collisionBoxes - the ArrayList containing all the collision boxes
     *                       with which the player can collide
     * @param direction - the direction in which the player will move, should
     *                  the move be legal, it is given according to the
     *                  keyboard inputs from the player
     * @return whether the player intersects with a wall or not
     */
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

    /**
     *
     * checks if the player would collide with a ceiling/floor in the next tick
     * using a copy of the playerRect called movedRect to which the yVelocity
     * was added, the function checks whether the player collides with a
     * ceiling/floor
     *
     * @param collisionBoxes - the ArrayList containing all the collision boxes
     *                       with which the player can collide
     * @param yVelocity - the velocity which would be added to the player's
     *                  coordinates if the move is legal
     * @return whether the player intersects with another rectangle or not
     */
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
     * handles the movement of the player according to the keyboard inputs
     * the xSpeed is set according to the key pressed in which case different
     * characters are passed to the function
     *
     * @param direction - the character, either 'l' or 'r' for left or right respectively indicating the direction of the player's movement
     *                    'n' refers to 'none' or i.o.w. no change at all
     */
    public void move(Character direction) {
        if (direction == 'r') {
            if (!hasHorizontalCollision(Level.getCollisionBoxes(), direction)) {
                xSpeed = 2;
            }
        } else if (direction == 'l') {
            if (!hasHorizontalCollision(Level.getCollisionBoxes(), direction)) {
                xSpeed = -2;
            }
        } else if (direction == 'n') {
            xSpeed = 0;
        }
    }

    /**
     *
     * updating the location of the player bounding box using the change-values
     * since there is no Rectangle2D.Double.setX() function and therefore
     * the entire rectangle has to be set again
     */
    public void updatePlayerRectCoords() {
        playerRect.setRect(playerRect.getX() + xSpeed, playerRect.getY() + ySpeed, playerWidth, playerHeight);
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

    /**
     *
     * @return the width of the player in pixels
     */
    public static int getPlayerWidth() {
        return playerWidth;
    }

    /**
     *
     * @return the height of the player in pixels
     */
    public static int getPlayerHeight() {
        return playerHeight;
    }
}
