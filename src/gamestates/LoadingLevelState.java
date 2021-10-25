package gamestates;

import Resources.ResourceMaster;
import display.DisplayManager;
import gameLoop.Main;
import guis.HealthBar;
import guis.TextBox;
import levelHandling.Level;
import toolbox.BasicConstants;
import toolbox.UIConstraints;

import java.awt.*;

/**
 *
 * the LoadingLevelState shows a health bar with constantly increasing fill-
 * level to simulate a progress-bar until the level starts
 * <p>
 * extends to the State class, hence overrides the update and render function
 *
 * @author Thomas Bundi
 * @version 0.5
 * @since 3.6
 */
public class LoadingLevelState extends State{

    private final int CEIAtStart; // currentEntityImageAtStart
    private final HealthBar loadingBar;
    private final TextBox loadingText;
    private final Level level;

    /**
     *
     * basic constructor of the LoadingLevelState
     * contains a simple health bar with increasing fillLevel acting
     * as a progress-bar
     *
     * @param level - the level object passed onto the GameState
     */
    public LoadingLevelState(Level level) {
        this.level = level;
        int HBWidth = (int) (400 * BasicConstants.RSF);
        int HBHeight = (int) (60 * BasicConstants.RSF);
        loadingBar = new HealthBar(DisplayManager.getWIDTH() / 2 - HBWidth / 2, DisplayManager.getHEIGHT() / 2 + HBHeight, HBWidth, HBHeight, 0, BasicConstants.HEALTH_BAR_GREEN_COLOR, Color.BLACK, 5);
        loadingText = new TextBox(DisplayManager.getWIDTH() / 2 - HBWidth / 2, DisplayManager.getHEIGHT() / 2, HBWidth, Color.BLACK, new Font("Calibri", Font.BOLD, 60), "Loading ...", 0, UIConstraints.UI_CENTER_BOUND_CONSTRAINT);
        CEIAtStart = Main.currentEntityImage;
    }

    @Override
    public void update() {
        if (Main.currentEntityImage - CEIAtStart <= 50) {
            loadingBar.setFillLevel(2 * (Main.currentEntityImage - CEIAtStart));
            loadingBar.update();
        } else {
            StateMaster.setState(new GameState(level));
        }
    }

    @Override
    public void render(Graphics2D g) {
        g.drawImage(ResourceMaster.getImageFromMap("title_screen_background"), 0, 0, null);

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        loadingText.draw(g);
        loadingBar.draw(g);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    }
}
