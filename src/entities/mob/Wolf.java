package entities.mob;

import Resources.ResourceMaster;
import gameLoop.Main;
import toolbox.ImageProcessing;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * Wolf class which extends to the Mob class and therefore overrides its
 * functions, contains all the information necessary to draw and handle a
 * wolf in the game
 *
 * @author Thomas Bundi
 * @version 0.5
 * @since 3.6
 */
public class Wolf extends Mob{

    private int health = 200;
    private final Rectangle2D.Double bounds;
    private final Rectangle2D.Double originalBounds;
    private float xSpeed = 0.5f;
    private float ySpeed = 0;
    private final Image[] sprites = ResourceMaster.getSpriteSheetFromMap("wolf_walk").getSpriteImages();

    private boolean hasCollisions = true;

    /**
     *
     * basic constructor of a wolf
     * width and height are predefined to 80 and 44 respectively
     *
     * @param x - the initial x coordinate of the wolf
     * @param y - the initial y coordinate of the wolf
     */
    public Wolf(int x, int y) {
        int width = 80;
        int height = 44;
        this.bounds = new Rectangle2D.Double(x, y + 40 - height, width, height);
        this.originalBounds = new Rectangle2D.Double(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
    }

    @Override
    public String getType() {
        return "wolf";
    }

    @Override
    public void update(ArrayList<Rectangle2D> collisionBoxes) {
        if (hasCollisions) {
            if (!hasHorizontalCollision(collisionBoxes)) {
                bounds.x += xSpeed;
            } else {
                xSpeed = -xSpeed;
            }
        } else {
            ySpeed += Mob.gravityAccel;
            // making the wolf fall out of the screen in a parabola shape
            bounds.x += 3 * xSpeed;
            bounds.y += ySpeed;
        }
    }

    @Override
    public void draw(Graphics2D g, int x, int y) {
        if (xSpeed > 0) {
            g.drawImage(sprites[(Main.currentEntityImage / 2) % 8], x, y, null);
        } else {
            g.drawImage(ImageProcessing.flipImageHorizontally((BufferedImage) sprites[(Main.currentEntityImage / 2) % 8]), x, y, null);
        }
    }

    /**
     *
     * checks if the snail would collide with a wall if it moved by its current
     * speed further along
     *
     * @param collisionBoxes - an arraylist containing rectangles representing
     *                       all the collision boxes of the level
     * @return whether the snail would collide with a wall if it moved further
     */
    private boolean hasHorizontalCollision(ArrayList<Rectangle2D> collisionBoxes) {
        boolean doesIntersect = false;
        Rectangle2D movedRect = bounds.getBounds2D();
        if (xSpeed > 0) {
            movedRect.setRect(movedRect.getX() + 2, movedRect.getY(), movedRect.getWidth(), movedRect.getHeight());
        }
        else if (xSpeed < 0) {
            movedRect.setRect(movedRect.getX() - 2, movedRect.getY(), movedRect.getWidth(), movedRect.getHeight());
        }
        for (Rectangle2D collisionBox:collisionBoxes) {
            if (movedRect.intersects(collisionBox)) doesIntersect = true;
        }

        return doesIntersect;
    }

    @Override
    public Rectangle2D.Double getBounds() {
        return bounds;
    }

    @Override
    public boolean hit() {
        health -= 100;
        if (health <= 0) {
            hasCollisions = false;
            ySpeed = -4f;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void resetCollisions() {
        hasCollisions = true;
    }

    @Override
    public boolean hasCollisions() {
        return hasCollisions;
    }

    @Override
    public void resetHealth() {
        health = 200;
    }

    @Override
    public void resetBounds() {
        this.bounds.setRect(originalBounds.getBounds2D());
    }
}
