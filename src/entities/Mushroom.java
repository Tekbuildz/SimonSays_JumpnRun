package entities;

import SpriteSheet.ResourceMaster;
import player.Player;

import java.awt.*;
import java.awt.geom.Rectangle2D;

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

    public Mushroom(int x, int y) {
        xImage = x;
        yImage = y;

        hitBox = new Rectangle2D.Double(x + (40 - hitBoxWidth) / 2f, y + 40 - hitBoxHeight, hitBoxWidth, hitBoxHeight);

        currentMushroomImage = ResourceMaster.getImageFromMap("mushroom_idle_up");
    }

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

    public void draw(Graphics2D g, int x, int y) {
        g.drawImage(currentMushroomImage, x, y, null);
    }

    public void startSquishAnimation() {
        isSquishAnimationPlaying = true;
        squishAnimTime = System.currentTimeMillis();
    }

    private void startCoolDownPhase() {
        currentSquishSprite = 0;
        isCoolDownActive = true;
        isSquishAnimationPlaying = false;
        currentMushroomImage = ResourceMaster.getImageFromMap("mushroom_idle_down");
        coolDownTimeOfAction = System.currentTimeMillis();
    }

    private void startDeSquishAnimation() {
        isCoolDownActive = false;
        isDeSquishAnimationPlaying = true;
        squishAnimTime = System.currentTimeMillis();
    }

    private void resetMushroom() {
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
