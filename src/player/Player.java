package player;

public class Player {

    private int x;
    private int y;
    private int width;
    private int height;

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
     * @param x - the x coordinate of the player
     * @param y - the y coordinate of the player
     */
    public Player(int x, int y) {
        this.x = x;
        this.y = y;

        width = 1;
        height = 2;
        health = 0;
        lives = 0;
        jumpHeight = 2;
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
     * @return the x coordinate of the player
     */
    public int getX() {
        return x;
    }

    /**
     *
     * @param x - sets the x coordinate of the player
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     *
     * @return the y coordinate of the player
     */
    public int getY() {
        return y;
    }

    /**
     *
     * @param y - sets the y coordinate of the player
     */
    public void setY(int y) {
        this.y = y;
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
