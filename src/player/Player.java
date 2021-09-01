package player;

import Loader.DataLoader;
import SpriteSheet.ResourceMaster;
import entities.Coin;
import entities.Item;
import gameLoop.Main;
import levelHandling.Level;
import toolbox.ImageProcessing;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

public class Player {

    // player statistics
    private int coins;
    private int numberOfItemsCollected;
    private HashMap<String, Integer> entityKills;

    private final Rectangle2D.Double playerRect = new Rectangle2D.Double();
    private final float gravityAccel = 0.1f;
    private final int playerDamageCooldownMS = 750;
    private long systemTimeOfDamage;
    private boolean isVulnerable;
    public double xSpeed = 0;
    public double ySpeed = 0;
    public final double jumpYSpeed = -3.5f;
    private static final int cubeSizePixels = 40;
    private static final int playerWidth = 40;
    private static final int playerHeight = 60;

    // the amount of health the player has left of the current life
    // if the health reaches the maximum, another life cannot be gained this way
    // if the health reaches 0, a life is removed from the players total number of lives
    private int health;
    private int lives;

    private final int backupCoins;

    private static Image currentPlayerImage;

    // death animation vars
    public boolean isDeathAnimPlaying;
    public boolean wasDeathAnimPlayed;
    private long deathAnimTimeOfAction;
    private int currentDeathAnimSprite;
    // the amount of time in milliseconds which it takes between each sprite of the death animation
    private static final int deathAnimActionGap = 100;

    /**
     *
     * basic constructor of the player
     * creates a rectangle representing the hitbox of the player
     * setting the amount of health and number of lives
     *
     * @param p - the starting point of the player in the level
     */
     public Player(Point p, int lives, int coins, HashMap<String, Integer> entityKills) {
        playerRect.setRect(p.x, p.y, playerWidth, playerHeight);
        health = 100;
        this.lives = lives;
        this.coins = coins;
        this.entityKills = entityKills;
        this.backupCoins = coins;
        this.numberOfItemsCollected = 0;

        isDeathAnimPlaying = false;
        wasDeathAnimPlayed = false;
        currentDeathAnimSprite = 0;
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
            ySpeed = jumpYSpeed;
        }
    }

    /**
     *
     * checking if the player collides with any coin and if so, removes it
     * and adds its value to the player's bank
     *
     * @param coins - a 2D ArrayList containing all the coin objects
     */
    public void checkCoinCollision(ArrayList<ArrayList<Coin>> coins) {
        for (ArrayList<Coin> list:coins) {
            for (Coin coin:list) {
                if (playerRect.intersects(coin.getBounds())) {
                    if (!coin.isWasCollected()) {
                        this.coins += coin.getValue();
                    }
                    coin.setWasCollected(true);
                }
            }
        }
    }

    /**
     *
     * checking if the player collides with any item and if so, removes it
     * and adds it to the player's numberOfItemsCollected
     *
     * @param items - the ArrayList containing all the item objects
     */
    public void checkItemCollision(ArrayList<Item> items) {
        for (Item item:items) {
            if (playerRect.intersects(item.getBounds())) {
                if (!item.isWasCollected()) {
                    this.numberOfItemsCollected++;
                }
                item.setWasCollected(true);
            }
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
    public boolean hasVerticalCollision(ArrayList<Rectangle2D> collisionBoxes, double yVelocity) {
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
                Player.setCurrentPlayerImage(ResourceMaster.getSpriteSheetFromMap("player_walk").getSpriteImages()[Main.currentEntityImage % 6]);
            }
        } else if (direction == 'l') {
            if (!hasHorizontalCollision(Level.getCollisionBoxes(), direction)) {
                xSpeed = -2;
                Player.setCurrentPlayerImage(ImageProcessing.flipImageHorizontally((BufferedImage) ResourceMaster.getSpriteSheetFromMap("player_walk").getSpriteImages()[Main.currentEntityImage % 6]));
            }
        } else if (direction == 'n') {
            xSpeed = 0;
            Player.setCurrentPlayerImage(ResourceMaster.getImageFromMap("player_idle"));
        }
    }

    /**
     *
     * updating the location of the player bounding box using the change-values
     * updating the death animation, if the player died
     */
    public void update() {
        if (!isDeathAnimPlaying) {
            playerRect.x += xSpeed;
            playerRect.y += ySpeed;
            isVulnerable = (System.currentTimeMillis() - systemTimeOfDamage) > playerDamageCooldownMS;
        }
        else {
            if (System.currentTimeMillis() - deathAnimTimeOfAction >= deathAnimActionGap) {
                deathAnimTimeOfAction = System.currentTimeMillis();
                currentDeathAnimSprite++;
            }
            if (ResourceMaster.getSpriteSheetFromMap("player_die").getSpriteImages().length != currentDeathAnimSprite) {
                Player.setCurrentPlayerImage(ResourceMaster.getSpriteSheetFromMap("player_die").getSpriteImages()[currentDeathAnimSprite]);
            } else {
                isDeathAnimPlaying = false;
                wasDeathAnimPlayed = true;
            }
        }
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
     * @return the rectangle representing the hitbox of the player
     */
    public Rectangle2D.Double getPlayerRect() {
        return playerRect;
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
    public void removeHealth(int health) {
        if (isVulnerable) {
            this.health -= health;
            systemTimeOfDamage = System.currentTimeMillis();
        }
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
     * @return the current amount of coins the player has
     */
    public int getCoins() {
        return coins;
    }

    /**
     *
     * @return the amount of each entity the player has killed in total
     */
    public HashMap<String, Integer> getEntityKills() {
        return entityKills;
    }

    /**
     *
     * @return the coins the player had before entering the current level
     */
    public int getBackupCoins() {
        return backupCoins;
    }

    /**
     *
     * @return the image as which the player is currently displayed
     */
    public static Image getCurrentPlayerImage() {
        return currentPlayerImage;
    }

    /**
     *
     * @param currentPlayerImage - the new currentPlayerImage
     */
    public static void setCurrentPlayerImage(Image currentPlayerImage) {
        Player.currentPlayerImage = currentPlayerImage;
    }

    /**
     *
     * @param entity - the entity type of which another one was killed
     */
    public void addEntityKill(String entity) {
        entityKills.put(entity, entityKills.get(entity) + 1);
    }

    /**
     *
     * @return the number of items the player collected in the level
     */
    public int getNumberOfItemsCollected() {
        return numberOfItemsCollected;
    }

    /**
     *
     * starts the death animation if the player died from an enemy
     */
    public void startDeathAnim() {
        isDeathAnimPlaying = true;
        deathAnimTimeOfAction = System.currentTimeMillis();
    }
}
