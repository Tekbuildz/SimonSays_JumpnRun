package player;

import levelHandling.Cube;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Player {

    private Rectangle2D.Double rectangle = new Rectangle2D.Double();
    private float gravityAccel = 0.01f;
    private float velocity;
    private static final int cubeSize = 40;

    /**
     *
     * the amount of health the player has left of the current life
     * if the health reaches the maximum, another life cannot be gained this way
     * if the health reaches 0, a life is removed from the players total number of lives
     */
    private int health;
    private int lives;

    /**
     * the jumpHeight is measured in cubes
     */
    private float jumpHeight;


    /**
     *
     * @param p - the starting point of the player in the level
     */

    public Player(Point p) {
        rectangle.setRect(p.x, p.y, getCubeSize(), getCubeSize());
        health = 0;
        lives = 0;
        jumpHeight = 2;
    }

    public void applyGravity() {
        velocity += gravityAccel;
        rectangle.setRect(rectangle.getX(), rectangle.getY() + velocity, rectangle.getWidth(), rectangle.getHeight());
        //System.out.println(velocity + ", " + gravityAccel);
    }

    public void checkCollisions(ArrayList<ArrayList<Cube>> levelCubes) {
        for (int i = 0; i < levelCubes.size(); i++) {
            for (int j = 0; j < levelCubes.size(); j++) {
                if (!rectangle.intersects(levelCubes.get(i).get(j).getRectangle()) && levelCubes.get(i).get(j).getCubeID() == 1) {
                    //System.out.println(levelCubes.get(i).get(j).getRectangle());
                    rectangle.setRect(rectangle.getX(), levelCubes.get(i).get(j).getRectangle().getY() - getCubeSize(), rectangle.getWidth(), rectangle.getHeight());
                    velocity = 0;
                }
            }
        }
    }

    /**
     *
     * @return the side length of a cube
     */
    public int getCubeSize() {
        return cubeSize;
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
        return (float) rectangle.getX();
    }
//
//    /**
//     *
//     * @param x - the new x position of the player
//     */
//    public void setX(float x) {
//        this.x = x;
//    }
//
    /**
     *
     * @return the y position of the player
     */
    public float getY() {
        return (float) rectangle.getY();
    }
//
//    /**
//     *
//     * @param y - the new y position of the player
//     */
//    public void setY(float y) {
//        this.y = y;
//    }

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
     * @return the maximum height the player can jump
     */
    public float getJumpHeight() {
        return jumpHeight;
    }

    /**
     *
     * @param jumpHeight - sets the maximum height the player can jump
     */
    public void setJumpHeight(float jumpHeight) {
        this.jumpHeight = jumpHeight;
    }
}
