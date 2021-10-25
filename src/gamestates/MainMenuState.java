package gamestates;

import Resources.ResourceMaster;
import display.DisplayManager;
import guis.TextBox;
import guis.buttons.Button;
import guis.buttons.ButtonTriangularRectangle;
import toolbox.BasicConstants;
import toolbox.UIConstraints;

import java.awt.*;
import java.util.HashMap;
import java.util.Objects;

/**
 *
 * the MainMenuState handles all the objects which need to be drawn to the
 * title screen such as the title itself and the different buttons
 * <p>
 * extends to the State class, hence overrides the update and render function
 *
 * @author Thomas Bundi
 * @version 0.6
 * @since 1.1
 */
public class MainMenuState extends State {

    private final TextBox title = new TextBox(0, (int) (250 * BasicConstants.RSF), DisplayManager.getWIDTH(), BasicConstants.GUI_OVERLAY_DEFAULT_COLOR, new Font("Calibri", Font.PLAIN, 120), "Simon Says: Jump!", 0, UIConstraints.UI_CENTER_BOUND_CONSTRAINT);
    private final HashMap<String, Button> buttons = new HashMap<>();

    /**
     *
     * basic constructor of the MainMenuState
     * creates all buttons and adds them to an ArrayList for easier handling
     */
    public MainMenuState() {
        int buttonWidth = (int) (250 * BasicConstants.RSF);
        int buttonHeight = (int) (60 * BasicConstants.RSF);
        int buttonCutoffSize = (int) (10 * BasicConstants.RSF);
        buttons.put("playButton", new ButtonTriangularRectangle(DisplayManager.getWIDTH() / 2 - buttonWidth / 2, DisplayManager.getHEIGHT() / 2 - buttonHeight / 4 * 11, buttonWidth, buttonHeight, buttonCutoffSize, "Play"));
        buttons.put("statisticsButton", new ButtonTriangularRectangle(DisplayManager.getWIDTH() / 2 - buttonWidth / 2, DisplayManager.getHEIGHT() / 2 - buttonHeight / 4 * 5, buttonWidth, buttonHeight, buttonCutoffSize, "Statistics"));
        buttons.put("howToPlayButton", new ButtonTriangularRectangle(DisplayManager.getWIDTH() / 2 - buttonWidth / 2, DisplayManager.getHEIGHT() / 2 + buttonHeight / 4, buttonWidth, buttonHeight, buttonCutoffSize, "How To Play"));
        buttons.put("quitButton", new ButtonTriangularRectangle(DisplayManager.getWIDTH() / 2 - buttonWidth / 2, DisplayManager.getHEIGHT() / 2 + buttonHeight / 4 * 7, buttonWidth, buttonHeight, buttonCutoffSize, "Quit"));

        for (Button button:buttons.values()) {
            // rearranging colors to make them fit more the background, since without an outline or background polygon,
            // the default fillColor seems nearly black
            button.setTextFont(BasicConstants.DEFAULT_BUTTON_FONT);
            button.setTextColor(BasicConstants.BUTTON_TEXT_COLOR);
            button.setFillColor(BasicConstants.BUTTON_FILL_COLOR.brighter().brighter().brighter());
            button.setHoverColor(BasicConstants.BUTTON_HOVER_COLOR.brighter().brighter().brighter());
            button.setPressedColor(BasicConstants.BUTTON_PRESSED_COLOR.brighter().brighter().brighter());
        }
    }


    @Override
    public void update() {
        for (Button button:buttons.values()) {
            button.update();
            if (button.isButtonWasReleased()) {
                switch (Objects.requireNonNull(toolbox.HashMap.getKey(buttons, button))) {
                    case "playButton":
                        StateMaster.setState(new LevelSelectionMenuState());
                        break;

                    case "howToPlayButton":
                        StateMaster.setState(new HowToPlayMenuState());
                        break;

                    case "statisticsButton":
                        StateMaster.setState(new StatisticsMenuState());
                        break;

                    case "quitButton":
                        System.exit(0);
                        break;
                }
            }
        }
    }

    @Override
    public void render(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(ResourceMaster.getImageFromMap("title_screen_background"), 0, 0, null);
        title.draw(g);
        for (Button button:buttons.values()) {
            button.draw(g);
        }
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    }
}
