package entities.mob;

import SpriteSheet.ResourceMaster;
import gameLoop.Main;
import toolbox.ImageProcessing;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * Snail class which extends to the Mob class and therefore overrides its
 * functions, contains all the information necessary to draw and handle a
 * snail in the game
 *
 * @author Thomas Bundi
 * @version 0.8
 * @since 2.9
 */
public class Snail extends Mob{

    private int health = 100;
    private final Rectangle2D.Double bounds;
    private final Rectangle2D.Double originalBounds;
    private float xSpeed = 0.3f;
    private float ySpeed = 0;
    private final Image[] sprites = ResourceMaster.getSpriteSheetFromMap("snail_walk").getSpriteImages();

    private boolean hasCollisions = true;

    /**
     *
     * basic constructor of a snail
     * width and height of the snail are predefined to 60 and 24 respectively
     *
     * @param x - the initial x coordinate of the snail
     * @param y - the initial y coordinate of the snail
     */
    public Snail(int x, int y) {
        int width = 60;
        // using 24 even though the sprite sheet has a height of 25 to make
        // the animation look like the snail is digging itself into the ground
        int height = 24;
        // since when creating the snail object, it is placed at the height of a tile
        // it would float in the air if the gap between a tile-size of 40 - its height
        // wouldn't be subtracted
        this.bounds = new Rectangle2D.Double(x, y + 40 - height, width, height);
        this.originalBounds = new Rectangle2D.Double(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
    }

    @Override
    public String getType() {
        return "snail";
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
            // making the snail fall out of the screen in a parabola shape
            bounds.x += 3 * xSpeed;
            bounds.y += ySpeed;
        }
    }

    @Override
    public void draw(Graphics2D g, int x, int y) {
        if (xSpeed > 0) {
            g.drawImage(sprites[Main.currentEntityImage % 8], x, y, null);
        } else {
            g.drawImage(ImageProcessing.flipImageHorizontally((BufferedImage) sprites[Main.currentEntityImage % 8]), x, y, null);
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
        health = 100;
    }

    @Override
    public void resetBounds() {
        this.bounds.setRect(originalBounds.getBounds2D());
    }
}
