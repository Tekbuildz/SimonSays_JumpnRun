package gamestates;

import java.awt.*;

public abstract class State {

    /**
     *
     * updating all the game logic and listeners each tick
     */
    public abstract void update();

    /**
     *
     * @param g - the graphics object used to paint onto the screen
     */
    public abstract void render(Graphics2D g);

}
