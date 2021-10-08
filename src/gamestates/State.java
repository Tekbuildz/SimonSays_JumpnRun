package gamestates;

import java.awt.*;

/**
 *
 * the abstract class State contains the functions which all the screens need
 * to have, the update and the render function, responsible for updating and
 * drawing the objects respectively to the screen
 *
 * @author Thomas Bundi
 * @version 0.4
 * @since 1.1
 */
public abstract class State {

    /**
     *
     * updating all the game logic and listeners each tick
     */
    public abstract void update();

    /**
     *
     * painting every object to the screen
     *
     * @param g - the graphics object used to paint onto the screen
     */
    public abstract void render(Graphics2D g);

}
