package entities;

import Resources.ResourceMaster;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 *
 * the mushroom class contains all elements of the mushroom, which contain its
 * location, its animation and its current image
 *
 * @author Thomas Bundi
 * @version 0.4
 * @since 4.1
 */
public class Mushroom {

    private static final int hitBoxWidth = 24;
    private static final int hitBoxHeight = 20;
    private static final int animSpriteIntervalMS = 100;
    private static final int coolDownTimeMS = 4000;

    private final int xImage, yImage;
    private final Rectangle2D.Double hitBox;
    private Image currentMushroomImage;

    private boolean isIdleUp = true;
    private long coolDownTimeOfAction;
    // potentially replace booleans with one state variable
    private boolean isSquishAnimationPlaying = false;
    private boolean isCoolDownActive = false;
    private boolean isDeSquishAnimationPlaying = false;
    private long squishAnimTime;
    private int currentSquishSprite = 0;

    /**
     *
     * basic constructor of the Mushroom class
     * the hitbox does not have a width and height of 40 pixels
     * as the mushroom itself is much smaller, hence the image
     * has a width and height of 40 pixels but not the hitbox
     *
     * @param x - the x coordinate of the mushroom
     * @param y - the y coordinate of the mushroom
     */
    public Mushroom(int x, int y) {
        xImage = x;
        yImage = y;

        hitBox = new Rectangle2D.Double(x + (40 - hitBoxWidth) / 2f, y + 40 - hitBoxHeight, hitBoxWidth, hitBoxHeight);

        currentMushroomImage = ResourceMaster.getImageFromMap("mushroom_idle_up");
    }

    /**
     *
     * updating the mushroom depending on the state of it
     * changing its current image depending on its state
     * changing its action upon collision depending on its state
     */
    public void update() {
        isIdleUp = !isSquishAnimationPlaying && !isCoolDownActive && !isDeSquishAnimationPlaying;

        if (isSquishAnimationPlaying) {
            if (currentSquishSprite < ResourceMaster.getSpriteSheetFromMap("mushroom_squish").getSpriteImages().length) {
                currentMushroomImage = ResourceMaster.getSpriteSheetFromMap("mushroom_squish").getSpriteImages()[currentSquishSprite];
                if (System.currentTimeMillis() > squishAnimTime + animSpriteIntervalMS) {
                    squishAnimTime = System.currentTimeMillis();
                    currentSquishSprite++;
                }
            } else {
                startCoolDownPhase();
            }
        }

        if (isCoolDownActive) {
            if (System.currentTimeMillis() > coolDownTimeOfAction + coolDownTimeMS) {
                startDeSquishAnimation();
            }
        }

        if (isDeSquishAnimationPlaying) {
            if (currentSquishSprite < ResourceMaster.getSpriteSheetFromMap("mushroom_desquish").getSpriteImages().length) {
                currentMushroomImage = ResourceMaster.getSpriteSheetFromMap("mushroom_desquish").getSpriteImages()[currentSquishSprite];
                if (System.currentTimeMillis() > squishAnimTime + animSpriteIntervalMS) {
                    squishAnimTime = System.currentTimeMillis();
                    currentSquishSprite++;
                }
            } else {
                resetMushroom();
            }
        }
    }

    /**
     *
     * draws the image of the mushroom in its current state
     * coordinates of image given externally to implement the horizontal
     * shift of the level to simulate player movement
     *
     * @param g - the graphics object used to paint onto the screen
     * @param x - the x coordinate at which the image should be drawn
     * @param y - the y coordinate at which the image should be drawn
     */
    public void draw(Graphics2D g, int x, int y) {
        g.drawImage(currentMushroomImage, x, y, null);
    }

    /**
     *
     * starts the animation and timers to begin the squish-animation
     */
    public void startSquishAnimation() {
        isSquishAnimationPlaying = true;
        squishAnimTime = System.currentTimeMillis();
    }

    /**
     *
     * resets all the variables used for the squish-animation, starts cooldown
     */
    private void startCoolDownPhase() {
        currentSquishSprite = 0;
        isCoolDownActive = true;
        isSquishAnimationPlaying = false;
        currentMushroomImage = ResourceMaster.getImageFromMap("mushroom_idle_down");
        coolDownTimeOfAction = System.currentTimeMillis();
    }

    /**
     *
     * resets all the variables of the cooldown phase and lauches de-squish-animation
     */
    private void startDeSquishAnimation() {
        isCoolDownActive = false;
        isDeSquishAnimationPlaying = true;
        squishAnimTime = System.currentTimeMillis();
    }

    /**
     *
     * resets all the variables of the de-squish-animation and returns to
     * its original state
     */
    public void resetMushroom() {
        isSquishAnimationPlaying = false;
        isCoolDownActive = false;
        currentSquishSprite = 0;
        isDeSquishAnimationPlaying = false;
        currentMushroomImage = ResourceMaster.getImageFromMap("mushroom_idle_up");
    }

    /**
     *
     * @return the x coordinate of the position of the image
     */
    public int getxImage() {
        return xImage;
    }

    /**
     *
     * @return the y coordinate of the position of the image
     */
    public int getyImage() {
        return yImage;
    }

    /**
     *
     * @return the collision box enclosing the mushroom
     */
    public Rectangle2D.Double getHitBox() {
        return hitBox;
    }

    /**
     *
     * @return whether the mushroom is in an idle state and can be activated
     */
    public boolean isIdleUp() {
        return isIdleUp;
    }
}
