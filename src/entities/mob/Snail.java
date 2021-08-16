package entities.mob;

import SpriteSheet.ResourceMaster;
import gameLoop.Main;
import levelHandling.Level;
import toolbox.ImageProcessing;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Snail extends Mob{

    private int health = 100;
    private Rectangle2D.Double bounds;
    private float speed = 0.3f;
    private final Image[] sprites = ResourceMaster.getSpriteSheetFromMap("snail_walk").getSpriteImages();

    private boolean show = true;

    /**
     *
     * basic constructor of a snail, extends to the Mob class
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
    }

    @Override
    public void update() {
        if (show) {
            if (!hasHorizontalCollision(Level.getCollisionBoxes())) {
                bounds.x += speed;
            } else {
                speed = -speed;
            }
        }
    }

    @Override
    public void draw(Graphics2D g, int x, int y) {
        if (show) {
            if (speed > 0) {
                g.drawImage(sprites[Main.currentEntityImage % 8], x, y, null);
            } else {
                g.drawImage(ImageProcessing.flipImageHorizontally((BufferedImage) sprites[Main.currentEntityImage % 8]), x, y, null);
            }
        }
    }

    /**
     *
     * checks if the snail would collide with a wall if it moved by its current
     * speed further along
     *
     * @param collisionBoxes - an arraylist containing rectangles representing
     *                       all the collision boxes of the level
     * @return whether the snail would collide if it moved by its current speed
     *          further
     */
    private boolean hasHorizontalCollision(ArrayList<Rectangle2D> collisionBoxes) {
        boolean doesIntersect = false;
        Rectangle2D movedRect = bounds.getBounds2D();
        if (speed > 0) {
            movedRect.setRect(movedRect.getX() + 2, movedRect.getY(), movedRect.getWidth(), movedRect.getHeight());
        }
        else if (speed < 0) {
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
    public void hit() {
        health -= 100;
        if (health <= 0) {
            show = false;
        }
    }

    @Override
    public boolean isShown() {
        return show;
    }

    // no need for vertical collisions since mob only walks from side to side in trench
    // hence y value never has to be changed

    // potentially add small health-bar above mob
}
