package gamestates;

import Loader.DataLoader;
import SpriteSheet.ResourceMaster;
import display.DisplayManager;
import guis.TextBox;
import guis.buttons.ButtonTriangularRectangle;
import guis.outlines.OutlinedPolygon;
import guis.outlines.TriangularRectangle;
import levelHandling.Level;
import player.PlayerInputs;
import toolbox.BasicGUIConstants;
import toolbox.UIConstraints;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Objects;

/**
 *
 * the LevelSelectionMenuState handles all the objects which are displayed when
 * entering the level selection screen
 * <p>
 * extends to the State class, hence overrides the update and render function
 *
 * @author Thomas Bundi
 * @version 0.8
 * @since 2.6
 */
public class LevelSelectionMenuState extends State{

    // 3 is hardcoded, since only using 3 levels for now
    private final HashMap<String, ButtonTriangularRectangle> levelButtons = new HashMap<>();
    private final HashMap<String, TriangularRectangle> levelPBsBackground = new HashMap<>();
    private final HashMap<String, TextBox> levelPBs = new HashMap<>();
    private final int buttonSize = (int) (250 * BasicGUIConstants.rsf);
    private final ButtonTriangularRectangle returnButton;
    private final OutlinedPolygon titleOutlineBackground = new OutlinedPolygon(
            new int[] {
                    DisplayManager.getWIDTH() / 4,
                    DisplayManager.getWIDTH() / 4 * 3,
                    DisplayManager.getWIDTH() / 4 * 3,
                    DisplayManager.getWIDTH() / 4 * 3 - (int) (50 * BasicGUIConstants.rsf),
                    DisplayManager.getWIDTH() / 4 + (int) (50 * BasicGUIConstants.rsf),
                    DisplayManager.getWIDTH() / 4
            },
            new int[] {
                    0,
                    0,
                    (int) (75 * BasicGUIConstants.rsf),
                    (int) (125 * BasicGUIConstants.rsf),
                    (int) (125 * BasicGUIConstants.rsf),
                    (int) (75 * BasicGUIConstants.rsf)
            },
            6,
            BasicGUIConstants.GUI_OVERLAY_DEFAULT_COLOR,
            Color.BLACK,
            10
    );
    private final TextBox title = new TextBox(
            DisplayManager.getWIDTH() / 4,
            (int) (90 * BasicGUIConstants.rsf),
            DisplayManager.getWIDTH() / 2,
            BasicGUIConstants.BUTTON_TEXT_COLOR,
            new Font("Calibri", Font.PLAIN, 80),
            "Select a level:",
            0,
            UIConstraints.UI_CENTER_BOUND_CONSTRAINT
    );

    /**
     *
     * basic constructor of the LevelSelectionMenuState
     * loads personal bests for each level to show them under the level button
     * adds other UI related objects to lists for simplified handling
     */
    public LevelSelectionMenuState() {
        DataLoader.loadPlayerData("player");

        levelButtons.put("level_1", new ButtonTriangularRectangle(DisplayManager.getWIDTH() / 2 - buttonSize * 2, DisplayManager.getHEIGHT() / 2 - buttonSize, buttonSize, buttonSize, buttonSize / 10, "1"));
        levelButtons.put("level_2", new ButtonTriangularRectangle(DisplayManager.getWIDTH() / 2 - buttonSize / 2, DisplayManager.getHEIGHT() / 2 - buttonSize, buttonSize, buttonSize, buttonSize / 10, "2"));
        levelButtons.put("level_3", new ButtonTriangularRectangle(DisplayManager.getWIDTH() / 2 + buttonSize, DisplayManager.getHEIGHT() / 2 - buttonSize, buttonSize, buttonSize, buttonSize / 10, "3"));

        for (ButtonTriangularRectangle button:levelButtons.values()) {
            button.setTextFont(new Font("Calibri", Font.PLAIN, 120));
            button.setTextColor(BasicGUIConstants.BUTTON_TEXT_COLOR);
            button.setFillColor(BasicGUIConstants.BUTTON_FILL_COLOR.brighter().brighter().brighter());
            button.setHoverColor(BasicGUIConstants.BUTTON_HOVER_COLOR.brighter().brighter().brighter());
            button.setPressedColor(BasicGUIConstants.BUTTON_PRESSED_COLOR.brighter().brighter().brighter());
        }

        returnButton = new ButtonTriangularRectangle(-(int) (20 * BasicGUIConstants.rsf), -(int) (20 * BasicGUIConstants.rsf), (int) (300 * BasicGUIConstants.rsf), (int) (100 * BasicGUIConstants.rsf), (int) (20 * BasicGUIConstants.rsf), "<-");
        returnButton.setTextFont(new Font("Calibri", Font.BOLD, 70));
        returnButton.setTextColor(BasicGUIConstants.BUTTON_TEXT_COLOR);
        returnButton.setPressedColor(BasicGUIConstants.BUTTON_PRESSED_COLOR.brighter().brighter().brighter());
        returnButton.setHoverColor(BasicGUIConstants.BUTTON_HOVER_COLOR.brighter().brighter().brighter());
        returnButton.setFillColor(BasicGUIConstants.BUTTON_FILL_COLOR.brighter().brighter().brighter());


        levelPBsBackground.put("level_1", new TriangularRectangle(DisplayManager.getWIDTH() / 2 - buttonSize * 2, DisplayManager.getHEIGHT() / 2 + buttonSize / 4, buttonSize, buttonSize / 3, buttonSize / 20, BasicGUIConstants.GUI_OVERLAY_DEFAULT_COLOR));
        levelPBsBackground.put("level_2", new TriangularRectangle(DisplayManager.getWIDTH() / 2 - buttonSize / 2, DisplayManager.getHEIGHT() / 2 + buttonSize / 4, buttonSize, buttonSize / 3, buttonSize / 20, BasicGUIConstants.GUI_OVERLAY_DEFAULT_COLOR));
        levelPBsBackground.put("level_3", new TriangularRectangle(DisplayManager.getWIDTH() / 2 + buttonSize, DisplayManager.getHEIGHT() / 2 + buttonSize / 4, buttonSize, buttonSize / 3, buttonSize / 20, BasicGUIConstants.GUI_OVERLAY_DEFAULT_COLOR));

        levelPBs.put("level_1", new TextBox(DisplayManager.getWIDTH() / 2 - buttonSize * 2, DisplayManager.getHEIGHT() / 2 + buttonSize / 2 - 7, buttonSize, BasicGUIConstants.BUTTON_TEXT_COLOR, new Font("Calibri", Font.PLAIN, 35), "0:00.000", 0, UIConstraints.UI_CENTER_BOUND_CONSTRAINT));
        levelPBs.put("level_2", new TextBox(DisplayManager.getWIDTH() / 2 - buttonSize / 2, DisplayManager.getHEIGHT() / 2 + buttonSize / 2 - 7, buttonSize, BasicGUIConstants.BUTTON_TEXT_COLOR, new Font("Calibri", Font.PLAIN, 35), "0:00.000", 0, UIConstraints.UI_CENTER_BOUND_CONSTRAINT));
        levelPBs.put("level_3", new TextBox(DisplayManager.getWIDTH() / 2 + buttonSize, DisplayManager.getHEIGHT() / 2 + buttonSize / 2 - 7, buttonSize, BasicGUIConstants.BUTTON_TEXT_COLOR, new Font("Calibri", Font.PLAIN, 35), "0:00.000", 0, UIConstraints.UI_CENTER_BOUND_CONSTRAINT));

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
