package gamestates;

import dataProcessing.DataLoader;
import Resources.ResourceMaster;
import display.DisplayManager;
import guis.TextBox;
import guis.buttons.ButtonTriangularRectangle;
import guis.outlines.OutlinedPolygon;
import toolbox.BasicConstants;
import toolbox.UIConstraints;

import java.awt.*;
import java.util.HashMap;

/**
 *
 * the StatisticsMenuState class handles all the objects required to display
 * the statistics to the screen
 * <p>
 * extends to the State class, hence overrides the update and render function
 *
 * @author Thomas Bundi
 * @version 0.6
 * @since 3.2
 */
public class StatisticsMenuState extends State{

    private final OutlinedPolygon backgroundPoly = new OutlinedPolygon(
            new int[] {DisplayManager.getWIDTH() / 8, DisplayManager.getWIDTH() / 8 * 7, DisplayManager.getWIDTH() / 8 * 7, DisplayManager.getWIDTH() / 8},
            new int[] {-100, -100, DisplayManager.getHEIGHT() + 100, DisplayManager.getHEIGHT() + 100},
            4,
            BasicConstants.GUI_OVERLAY_DEFAULT_COLOR,
            Color.BLACK,
            5
    );

    private final HashMap<String, Integer> stats = new HashMap<>();

    private final TextBox title = new TextBox(0, DisplayManager.getHEIGHT() / 8, DisplayManager.getWIDTH(), BasicConstants.BUTTON_TEXT_COLOR, new Font("Calibri", Font.PLAIN, 80), "Statistics", 0, UIConstraints.UI_CENTER_BOUND_CONSTRAINT);
    private final TextBox[] descriptions = new TextBox[4];
    private final TextBox[] values = new TextBox[4];
    private final ButtonTriangularRectangle backButton = new ButtonTriangularRectangle(-20, -20, DisplayManager.getWIDTH() / 8 + 40, (int) (100 * BasicConstants.RSF), 0, "<-");

    /**
     *
     * basic constructor of the StatisticsMenuState
     * reads all statistics values and adds them to TextBoxes
     */
    public StatisticsMenuState() {
        stats.put("money", DataLoader.getCoins());
        stats.put("items", DataLoader.getItems());
        stats.put("snail_kill", DataLoader.getEntityKills().get("snail"));
        stats.put("wolf_kill", DataLoader.getEntityKills().get("wolf"));

        int textX = DisplayManager.getWIDTH() / 4;
        int textYBase = DisplayManager.getHEIGHT() / 4;
        int textHeightDifference = (int) (80 * BasicConstants.RSF);
        int textWidth = DisplayManager.getWIDTH() / 2;
        Font font = new Font("Calibri", Font.PLAIN, 40);

        descriptions[0] = new TextBox(textX, textYBase, textWidth, BasicConstants.BUTTON_TEXT_COLOR, font, "Total money in bank: ", 0, UIConstraints.UI_LEFT_BOUND_CONSTRAINT);
        descriptions[1] = new TextBox(textX, textYBase + textHeightDifference, textWidth, BasicConstants.BUTTON_TEXT_COLOR, font, "Total items collected: ", 0, UIConstraints.UI_LEFT_BOUND_CONSTRAINT);
        descriptions[2] = new TextBox(textX, textYBase + textHeightDifference * 2, textWidth, BasicConstants.BUTTON_TEXT_COLOR, font, "Number of snails killed: ", 0, UIConstraints.UI_LEFT_BOUND_CONSTRAINT);
        descriptions[3] = new TextBox(textX, textYBase + textHeightDifference * 3, textWidth, BasicConstants.BUTTON_TEXT_COLOR, font, "Number of wolfs killed: ", 0, UIConstraints.UI_LEFT_BOUND_CONSTRAINT);

        values[0] = new TextBox(textX, textYBase, textWidth, BasicConstants.BUTTON_TEXT_COLOR, font, String.valueOf(DataLoader.getCoins()), 0, UIConstraints.UI_RIGHT_BOUND_CONSTRAINT);
        values[1] = new TextBox(textX, textYBase + textHeightDifference, textWidth, BasicConstants.BUTTON_TEXT_COLOR, font, String.valueOf(DataLoader.getItems()), 0, UIConstraints.UI_RIGHT_BOUND_CONSTRAINT);
        values[2] = new TextBox(textX, textYBase + textHeightDifference * 2, textWidth, BasicConstants.BUTTON_TEXT_COLOR, font, String.valueOf(DataLoader.getEntityKills().get("snail")), 0, UIConstraints.UI_RIGHT_BOUND_CONSTRAINT);
        values[3] = new TextBox(textX, textYBase + textHeightDifference * 3, textWidth, BasicConstants.BUTTON_TEXT_COLOR, font, String.valueOf(DataLoader.getEntityKills().get("wolf")), 0, UIConstraints.UI_RIGHT_BOUND_CONSTRAINT);

        backButton.setTextFont(new Font("Calibri", Font.PLAIN, 50));
        backButton.setTextColor(BasicConstants.BUTTON_TEXT_COLOR);
        backButton.setFillColor(BasicConstants.BUTTON_FILL_COLOR.brighter().brighter().brighter());
        backButton.setHoverColor(BasicConstants.BUTTON_HOVER_COLOR.brighter().brighter().brighter());
        backButton.setPressedColor(BasicConstants.BUTTON_PRESSED_COLOR.brighter().brighter().brighter());
    }

    @Override
    public void update() {
        backButton.update();
        if (backButton.isButtonWasReleased()) {
            StateMaster.setState(new MainMenuState());
        }
    }

    @Override
    public void render(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // drawing background image
        g.drawImage(ResourceMaster.getImageFromMap("title_screen_background"), 0, 0, null);
        backButton.draw(g);
        backgroundPoly.draw(g);

        title.draw(g);
        for (int i = 0; i < descriptions.length; i++) {
            descriptions[i].draw(g);
            values[i].draw(g);
        }

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    }
}
