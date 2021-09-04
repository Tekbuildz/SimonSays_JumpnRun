package gamestates;

import SpriteSheet.ResourceMaster;
import display.DisplayManager;
import gameLoop.Main;
import guis.HealthBar;
import guis.TextBox;
import levelHandling.Level;
import toolbox.BasicGUIConstants;
import toolbox.UIConstraints;

import java.awt.*;

public class LoadingLevelState extends State{

    private final int CEIAtStart; // currentEntityImageAtStart
    private final HealthBar loadingBar;
    private final TextBox loadingText;
    private final Level level;

    public LoadingLevelState(Level level) {
        this.level = level;
        int HBWidth = (int) (400 * BasicGUIConstants.rsf);
        int HBHeight = (int) (60 * BasicGUIConstants.rsf);
        loadingBar = new HealthBar(DisplayManager.getWIDTH() / 2 - HBWidth / 2, DisplayManager.getHEIGHT() / 2 + HBHeight, HBWidth, HBHeight, 0, BasicGUIConstants.HEALTH_BAR_GREEN_COLOR, Color.BLACK, 5);
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
