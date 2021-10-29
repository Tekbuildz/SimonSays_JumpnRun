package gamestates;

import dataProcessing.DataLoader;
import Resources.ResourceMaster;
import display.DisplayManager;
import guis.TextBox;
import guis.buttons.ButtonTriangularRectangle;
import guis.outlines.OutlinedPolygon;
import guis.outlines.TriangularRectangle;
import levelHandling.Level;
import player.PlayerInputs;
import toolbox.BasicConstants;
import toolbox.UIConstraints;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Objects;

/**
 *
 * the LevelSelectionState handles all the objects which are displayed when
 * entering the level selection screen
 * <p>
 * extends to the State class, hence overrides the update and render function
 *
 * @author Thomas Bundi
 * @version 0.8
 * @since 2.6
 */
public class LevelSelectionState extends State{

    // 3 is hardcoded, since only using 3 levels for now
    private final HashMap<String, ButtonTriangularRectangle> levelButtons = new HashMap<>();
    private final HashMap<String, TriangularRectangle> levelPBsBackground = new HashMap<>();
    private final HashMap<String, TextBox> levelPBs = new HashMap<>();
    private final int buttonSize = (int) (250 * BasicConstants.RSF);
    private final ButtonTriangularRectangle returnButton;
    private final OutlinedPolygon titleOutlineBackground = new OutlinedPolygon(
            new int[] {
                    DisplayManager.getWIDTH() / 4,
                    DisplayManager.getWIDTH() / 4 * 3,
                    DisplayManager.getWIDTH() / 4 * 3,
                    DisplayManager.getWIDTH() / 4 * 3 - (int) (50 * BasicConstants.RSF),
                    DisplayManager.getWIDTH() / 4 + (int) (50 * BasicConstants.RSF),
                    DisplayManager.getWIDTH() / 4
            },
            new int[] {
                    0,
                    0,
                    (int) (75 * BasicConstants.RSF),
                    (int) (125 * BasicConstants.RSF),
                    (int) (125 * BasicConstants.RSF),
                    (int) (75 * BasicConstants.RSF)
            },
            6,
            BasicConstants.GUI_OVERLAY_DEFAULT_COLOR,
            Color.BLACK,
            10
    );
    private final TextBox title = new TextBox(
            DisplayManager.getWIDTH() / 4,
            (int) (90 * BasicConstants.RSF),
            DisplayManager.getWIDTH() / 2,
            BasicConstants.BUTTON_TEXT_COLOR,
            new Font("Calibri", Font.PLAIN, 80),
            "Select a level:",
            0,
            UIConstraints.UI_CENTER_BOUND_CONSTRAINT
    );

    /**
     *
     * basic constructor of the LevelSelectionState
     * loads personal bests for each level to show them under the level button
     * adds other UI related objects to lists for simplified handling
     */
    public LevelSelectionState() {
        DataLoader.loadPlayerData();

        levelButtons.put("level_1", new ButtonTriangularRectangle(DisplayManager.getWIDTH() / 2 - buttonSize * 2, DisplayManager.getHEIGHT() / 2 - buttonSize, buttonSize, buttonSize, buttonSize / 10, "1"));
        levelButtons.put("level_2", new ButtonTriangularRectangle(DisplayManager.getWIDTH() / 2 - buttonSize / 2, DisplayManager.getHEIGHT() / 2 - buttonSize, buttonSize, buttonSize, buttonSize / 10, "2"));
        levelButtons.put("level_3", new ButtonTriangularRectangle(DisplayManager.getWIDTH() / 2 + buttonSize, DisplayManager.getHEIGHT() / 2 - buttonSize, buttonSize, buttonSize, buttonSize / 10, "3"));

        for (ButtonTriangularRectangle button:levelButtons.values()) {
            button.setTextFont(new Font("Calibri", Font.PLAIN, 120));
            button.setTextColor(BasicConstants.BUTTON_TEXT_COLOR);
            button.setFillColor(BasicConstants.BUTTON_FILL_COLOR.brighter().brighter().brighter());
            button.setHoverColor(BasicConstants.BUTTON_HOVER_COLOR.brighter().brighter().brighter());
            button.setPressedColor(BasicConstants.BUTTON_PRESSED_COLOR.brighter().brighter().brighter());
        }

        returnButton = new ButtonTriangularRectangle(-(int) (20 * BasicConstants.RSF), -(int) (20 * BasicConstants.RSF), (int) (300 * BasicConstants.RSF), (int) (100 * BasicConstants.RSF), (int) (20 * BasicConstants.RSF), "<-");
        returnButton.setTextFont(new Font("Calibri", Font.BOLD, 70));
        returnButton.setTextColor(BasicConstants.BUTTON_TEXT_COLOR);
        returnButton.setPressedColor(BasicConstants.BUTTON_PRESSED_COLOR.brighter().brighter().brighter());
        returnButton.setHoverColor(BasicConstants.BUTTON_HOVER_COLOR.brighter().brighter().brighter());
        returnButton.setFillColor(BasicConstants.BUTTON_FILL_COLOR.brighter().brighter().brighter());


        levelPBsBackground.put("level_1", new TriangularRectangle(DisplayManager.getWIDTH() / 2 - buttonSize * 2, DisplayManager.getHEIGHT() / 2 + buttonSize / 4, buttonSize, buttonSize / 3, buttonSize / 20, BasicConstants.GUI_OVERLAY_DEFAULT_COLOR));
        levelPBsBackground.put("level_2", new TriangularRectangle(DisplayManager.getWIDTH() / 2 - buttonSize / 2, DisplayManager.getHEIGHT() / 2 + buttonSize / 4, buttonSize, buttonSize / 3, buttonSize / 20, BasicConstants.GUI_OVERLAY_DEFAULT_COLOR));
        levelPBsBackground.put("level_3", new TriangularRectangle(DisplayManager.getWIDTH() / 2 + buttonSize, DisplayManager.getHEIGHT() / 2 + buttonSize / 4, buttonSize, buttonSize / 3, buttonSize / 20, BasicConstants.GUI_OVERLAY_DEFAULT_COLOR));

        levelPBs.put("level_1", new TextBox(DisplayManager.getWIDTH() / 2 - buttonSize * 2, DisplayManager.getHEIGHT() / 2 + buttonSize / 2 - 7, buttonSize, BasicConstants.BUTTON_TEXT_COLOR, new Font("Calibri", Font.PLAIN, 35), "0:00.000", 0, UIConstraints.UI_CENTER_BOUND_CONSTRAINT));
        levelPBs.put("level_2", new TextBox(DisplayManager.getWIDTH() / 2 - buttonSize / 2, DisplayManager.getHEIGHT() / 2 + buttonSize / 2 - 7, buttonSize, BasicConstants.BUTTON_TEXT_COLOR, new Font("Calibri", Font.PLAIN, 35), "0:00.000", 0, UIConstraints.UI_CENTER_BOUND_CONSTRAINT));
        levelPBs.put("level_3", new TextBox(DisplayManager.getWIDTH() / 2 + buttonSize, DisplayManager.getHEIGHT() / 2 + buttonSize / 2 - 7, buttonSize, BasicConstants.BUTTON_TEXT_COLOR, new Font("Calibri", Font.PLAIN, 35), "0:00.000", 0, UIConstraints.UI_CENTER_BOUND_CONSTRAINT));

        // showing all the best times under the levels which were already fully completed once
        for (int i = 0; i < DataLoader.getLevelTimes().size(); i++) {
            if (DataLoader.getLevelTimes().get("level_" + (i + 1)) != 0) {
                long minutes = (DataLoader.getLevelTimes().get("level_" + (i + 1)) / 60000) % 60;
                long seconds = (DataLoader.getLevelTimes().get("level_" + (i + 1)) / 1000) % 60;
                long mSeconds = DataLoader.getLevelTimes().get("level_" + (i + 1)) % 1000;

                String secs;
                String mSecs;
                if (seconds < 10) {
                    secs = "0" + seconds;
                } else secs = String.valueOf(seconds);
                if (mSeconds < 10) {
                    mSecs = "00" + mSeconds;
                } else if (mSeconds < 100) {
                    mSecs = "0" + mSeconds;
                } else mSecs = String.valueOf(mSeconds);

                levelPBs.get("level_" + (i + 1)).setText(minutes + ":" + secs + "." + mSecs);
            }
        }

        // showing "Not completed" if the level was either never played or not all the objectives were fulfilled
        for (int i = 0; i < levelPBs.size(); i++) {
            if (DataLoader.getLevelTimes().get("level_" + (i + 1)) == 0) {
                levelPBs.get("level_" + (i + 1)).setText("Not completed");
            }
        }
    }

    @Override
    public void update() {
        returnButton.update();
        if (returnButton.isButtonWasReleased() || PlayerInputs.getKeysReleasedInFrame().contains(KeyEvent.VK_ESCAPE)) {
            StateMaster.setState(new MainMenuState());
        }
        for (ButtonTriangularRectangle button:levelButtons.values()) {
            button.update();
            if (button.isButtonWasReleased()) {
                StateMaster.setState(new LoadingLevelState(new Level(Objects.requireNonNull(Objects.requireNonNull(toolbox.HashMap.getKey(levelButtons, button)).replace("level_", "")))));
            }
        }
    }

    @Override
    public void render(Graphics2D g) {
        g.drawImage(ResourceMaster.getImageFromMap("title_screen_background"), 0, 0, null);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        titleOutlineBackground.draw(g);
        title.draw(g);
        returnButton.draw(g);
        for (ButtonTriangularRectangle button:levelButtons.values()) {
            button.draw(g);
        }
        for (TriangularRectangle tr:levelPBsBackground.values()) {
            tr.draw(g);
        }
        for (TextBox tb:levelPBs.values()) {
            tb.draw(g);
        }
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    }
}
