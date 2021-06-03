package player;

import levelHandling.Cube;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Player {

    private final Rectangle2D.Double playerRect = new Rectangle2D.Double();
    private final float gravityAccel = 0.005f;
    private float yVelocity;
    private double xChange = 0;
    private double yChange = 0;
    private static final int cubeSizePixels = 40;
    private static final int playerWidth = 1;
    private static final int playerHeight = 2;

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
        playerRect.setRect(p.x, p.y - 1, playerWidth, playerHeight);
        health = 100;
        lives = 1;
    }

    /**
     *
     * applies gravity to the player
     */
    public void applyGravity() {
        yVelocity += gravityAccel;
        if (yVelocity >= 0.2f) {
            yVelocity = 0.2f;
        }
        yChange = yVelocity;
    }

    /**
     *
     * checks for any collision of the player with a dirt-tile
     *
     * @param levelCubes - all the cubes from the level
     */
    public void checkCollisions(ArrayList<ArrayList<Cube>> levelCubes) {
        for (int i = 0; i < levelCubes.size(); i++) {
            for (int j = 0; j < levelCubes.size(); j++) {
                if (playerRect.intersects(levelCubes.get(i).get(j).getRectangle()) && levelCubes.get(i).get(j).getCubeID() == 1 && yVelocity >= 0) {
                    playerRect.setRect(playerRect.getX(), levelCubes.get(i).get(j).getRectangle().getY() - playerHeight, playerWidth, playerHeight);
                    yVelocity = 0;
                }
            }
        }
    }

    /**
     *
     * allows the player to jump upwards
     */
    public void jump() {
        yVelocity = -0.15f;
    }

    /**
     *
     * @param direction - the character, either 'l' or 'r' for left or right respectively indicating the direction of the player's movement
     *                    'n' refers to 'none' or i.o.w. no change at all
     */
    public void move(Character direction) {
        if (direction == 'r') {
            xChange = 0.05;
        } else if (direction == 'l') {
            xChange = -0.05;
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
}
