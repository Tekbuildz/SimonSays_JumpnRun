package gamestates;

import java.awt.*;

public abstract class State {

    public abstract void update();

    public abstract void render(Graphics2D g);

}
