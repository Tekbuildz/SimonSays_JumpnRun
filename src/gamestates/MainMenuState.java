package gamestates;

import java.awt.*;

public class MainMenuState extends State {


    @Override
    public void update() {
        // checking any possible updates (in gameState this would be mouse inputs etc.)
    }

    @Override
    public void render(Graphics2D g) {
        // rendering all the main menu buttons and stuff
        drawTitleScreen(g);
    }

    /**
     *
     * drawing the title screen
     */
    private void drawTitleScreen(Graphics2D g) {
        //g.drawImage(titleScreenBackground, 0, 0, null);
    }
}
