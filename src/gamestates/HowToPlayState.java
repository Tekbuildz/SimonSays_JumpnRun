package gamestates;

import Resources.ResourceMaster;
import display.DisplayManager;
import guis.TextBox;
import guis.buttons.ButtonTriangularRectangle;
import guis.outlines.OutlinedPolygon;
import player.PlayerInputs;
import toolbox.BasicConstants;
import toolbox.UIConstraints;

import java.awt.*;
import java.util.ArrayList;

/**
 *
 * the HowToPlayState handles all the objects which are displayed when
 * viewing the How To Play page in the game
 * <p>
 * extends to the State class, hence overrides the update and render function
 *
 * @author Thomas Bundi
 * @version 0.7
 * @since 3.6
 */
public class HowToPlayState extends State{

    private int totalYShift = 0;
    private final int xDisplacement = DisplayManager.getWIDTH() / 16 * 3;
    private final int initialYDisplacement = DisplayManager.getHEIGHT() / 12;
    private final int textWidth = DisplayManager.getWIDTH() / 16 * 11;
    private final int sectionYSpace = (int) (70 * BasicConstants.RSF);

    private final ArrayList<TextBox> htpTexts = new ArrayList<>();

    private final OutlinedPolygon backgroundPoly = new OutlinedPolygon(
            new int[] {DisplayManager.getWIDTH() / 8, DisplayManager.getWIDTH() / 8 * 7, DisplayManager.getWIDTH() / 8 * 7, DisplayManager.getWIDTH() / 8},
            new int[] {-100, -100, DisplayManager.getHEIGHT() + 100, DisplayManager.getHEIGHT() + 100},
            4,
            BasicConstants.GUI_OVERLAY_DEFAULT_COLOR,
            Color.BLACK,
            5
    );
    private final ButtonTriangularRectangle backButton = new ButtonTriangularRectangle(-20, -20, DisplayManager.getWIDTH() / 8 + 40, (int) (100 * BasicConstants.RSF), 0, "<-");

    // title
    private final TextBox header = new TextBox(
            xDisplacement,
            initialYDisplacement,
            textWidth,
            BasicConstants.BUTTON_TEXT_COLOR,
            new Font("Calibri", Font.BOLD, (int) (70 * BasicConstants.RSF)),
            "How To Play - A Quick Guide", 0,
            UIConstraints.UI_CENTER_BOUND_CONSTRAINT
    );
    // part 1: controls
    private final TextBox controlsTitle = new TextBox(
            xDisplacement,
            header.getOriginalYPos() + header.getTextHeight() + (int) (30 * BasicConstants.RSF), // adding some more space between the main title and the first paragraph
            textWidth,
            BasicConstants.BUTTON_TEXT_COLOR,
            BasicConstants.TITLE_FONT,
            "1. Controls", 0,
            UIConstraints.UI_LEFT_BOUND_CONSTRAINT
    );
    private final TextBox controlsText = new TextBox(
            xDisplacement,
            controlsTitle.getOriginalYPos() + controlsTitle.getTextHeight(),
            textWidth,
            BasicConstants.BUTTON_TEXT_COLOR,
            BasicConstants.DEFAULT_TEXT_FONT,
            "The player can be moved around the level using the A and D key or the left and right arrow keys. To jump, a tap of the space bar is required.",
            5, UIConstraints.UI_LEFT_BOUND_CONSTRAINT
    );

    // part 2: collectables
    private final TextBox collectablesTitle = new TextBox(
            xDisplacement,
            controlsText.getOriginalYPos() + controlsText.getTextHeight() + sectionYSpace,
            textWidth,
            BasicConstants.BUTTON_TEXT_COLOR,
            BasicConstants.TITLE_FONT,
            "2. Collectables", 0,
            UIConstraints.UI_LEFT_BOUND_CONSTRAINT
    );
    private final TextBox collectablesText = new TextBox(
            xDisplacement,
            collectablesTitle.getOriginalYPos() + collectablesTitle.getTextHeight(),
            DisplayManager.getWIDTH() / 16 * 8,
            BasicConstants.BUTTON_TEXT_COLOR,
            BasicConstants.DEFAULT_TEXT_FONT,
            "One of the objectives of the game is to collect every item in the level. Such items can either be coins of different values, 5, 10 or 20, represented by their appearance by bronze, silver and gold respectively. There is also another type of item in form of either a Screw, an LED or a PCB. ",
            0, UIConstraints.UI_LEFT_BOUND_CONSTRAINT
    );

    // part 3: simon says
    private final TextBox ssTitle = new TextBox(
            xDisplacement,
            collectablesText.getOriginalYPos() + collectablesText.getTextHeight() + sectionYSpace,
            textWidth,
            BasicConstants.BUTTON_TEXT_COLOR,
            BasicConstants.TITLE_FONT,
            "3. Simon Says",
            0, UIConstraints.UI_LEFT_BOUND_CONSTRAINT
    );
    private final TextBox ssText = new TextBox(
            xDisplacement,
            ssTitle.getOriginalYPos() + ssTitle.getTextHeight(),
            DisplayManager.getWIDTH() / 16 * 7,
            BasicConstants.BUTTON_TEXT_COLOR,
            BasicConstants.DEFAULT_TEXT_FONT,
            "A Simon Says is a memory game occurring throughout each level, where the player has to remember a given sequence and re-enter it in the same order as it was given. To access one, simply navigate the players hitbox over a Simon Says block in the level and cancel any movement. The User Interface [to the right] should automatically open. Then, the first sequence is given and the player needs to replicate it by pressing the buttons which lit up previously in the same order. Should the player fail, the UI is closed and all further Simon Says blocks are no longer accessible.",
            0, UIConstraints.UI_LEFT_BOUND_CONSTRAINT
    );
    private final TextBox ssText2 = new TextBox(
            xDisplacement,
            ssText.getOriginalYPos() + ssText.getTextHeight(),
            textWidth,
            BasicConstants.BUTTON_TEXT_COLOR,
            BasicConstants.DEFAULT_TEXT_FONT,
            "But if the player succeeds, a box to the right of the play-field is marked with a green tick and the next sequence begins. The UI closes as soon as the second sequence is finished and this one needs to be entered at the second Simon Says block. There, after entering the second sequence correctly, a third one is displayed and the UI is closed. The third and last one has to be entered in the last block. Be aware though, that each sequence can only be played once, therefore missing a sequence will require you to restart the level in case you are aiming for a perfect run.",
            0, UIConstraints.UI_LEFT_BOUND_CONSTRAINT
    );

    // part 4: hostile enemies
    private final TextBox enemiesTitle = new TextBox(
            xDisplacement,
            ssText2.getOriginalYPos() + ssText2.getTextHeight() + sectionYSpace,
            textWidth,
            BasicConstants.BUTTON_TEXT_COLOR,
            BasicConstants.TITLE_FONT,
            "4. Hostile MOBs",
            0, UIConstraints.UI_LEFT_BOUND_CONSTRAINT
    );
    private final TextBox enemiesText = new TextBox(
            xDisplacement,
            enemiesTitle.getOriginalYPos() + enemiesTitle.getTextHeight(),
            textWidth,
            BasicConstants.BUTTON_TEXT_COLOR,
            BasicConstants.DEFAULT_TEXT_FONT,
            "Currently, there are two different hostile mobs in the game: a Snail and a Wolf. They have different properties such as speed, health and damage done when intersecting with a player. A snail has a lower speed, lower health and lower damage output than the wolf. In order for the player to deal damage to a mob, he has to be falling onto an enemy. This also allows for a hit towards the mob even if the player only hits the side of the hitbox of the mob and not the top. But walking into the enemy will deal damage to the player. Be aware though, that if two enemies are behind each other, the player will only deal damage to one but take damage from the other one.",
            0, UIConstraints.UI_LEFT_BOUND_CONSTRAINT
    );

    // part 5: personal records
    private final TextBox pbTitle = new TextBox(
            xDisplacement,
            enemiesText.getOriginalYPos() + enemiesText.getTextHeight() + sectionYSpace,
            textWidth,
            BasicConstants.BUTTON_TEXT_COLOR,
            BasicConstants.TITLE_FONT,
            "5. Personal Records",
            0, UIConstraints.UI_LEFT_BOUND_CONSTRAINT
    );
    private final TextBox pbText = new TextBox(
            xDisplacement,
            pbTitle.getOriginalYPos() + pbTitle.getTextHeight(),
            textWidth,
            BasicConstants.BUTTON_TEXT_COLOR,
            BasicConstants.DEFAULT_TEXT_FONT,
            "The game saves personal bests of each level which are displayed on the level selection screen beneath the corresponding level. The ones which were not completed yet are marked as so. One thing to note is that the time is only saved if all the objectives of the level were fulfilled. This means that all the coins and items need to be collected, all the mobs have to be killed and all the Simon Says have to be solved correctly.",
            0, UIConstraints.UI_LEFT_BOUND_CONSTRAINT
    );

    // end: have fun
    private final TextBox GLHF = new TextBox(
            xDisplacement,
            pbText.getOriginalYPos() + pbText.getTextHeight() + sectionYSpace,
            DisplayManager.getWIDTH() / 16 * 10,
            BasicConstants.BUTTON_TEXT_COLOR,
            BasicConstants.TITLE_FONT,
            "Good Luck and Have Fun!",
            0, UIConstraints.UI_CENTER_BOUND_CONSTRAINT
    );

    /**
     *
     * basic constructor of the HowToPlayState
     * adds all the texts to an ArrayList for easier handling of the yShift
     * adds all the color and font settings to the Return-Button
     */
    public HowToPlayState() {
        htpTexts.add(header);
        htpTexts.add(controlsTitle);
        htpTexts.add(controlsText);
        htpTexts.add(collectablesTitle);
        htpTexts.add(collectablesText);
        htpTexts.add(ssTitle);
        htpTexts.add(ssText);
        htpTexts.add(ssText2);
        htpTexts.add(enemiesTitle);
        htpTexts.add(enemiesText);
        htpTexts.add(pbTitle);
        htpTexts.add(pbText);
        htpTexts.add(GLHF);

        backButton.setTextFont(new Font("Calibri", Font.PLAIN, 50));
        backButton.setTextColor(BasicConstants.BUTTON_TEXT_COLOR);
        backButton.setFillColor(BasicConstants.BUTTON_FILL_COLOR.brighter().brighter().brighter());
        backButton.setHoverColor(BasicConstants.BUTTON_HOVER_COLOR.brighter().brighter().brighter());
        backButton.setPressedColor(BasicConstants.BUTTON_PRESSED_COLOR.brighter().brighter().brighter());
    }

    /**
     *
     * usage of the MouseWheelListener, to apply ability to scroll down
     * through the quick explanation page
     */
    @Override
    public void update() {
        backButton.update();
        if (backButton.isButtonWasReleased()) {
            StateMaster.setState(new MainMenuState());
        }

        double mouseWheelMovement = PlayerInputs.getMouseWheelMovedInFrame();
        if (mouseWheelMovement != 0) {
            totalYShift -= mouseWheelMovement * 50;
            if (totalYShift > 0) {
                totalYShift = 0;
            } else if (totalYShift < -(GLHF.getOriginalYPos() + GLHF.getTextHeight() - DisplayManager.getHEIGHT())) {
                totalYShift = -(GLHF.getOriginalYPos() + GLHF.getTextHeight() - DisplayManager.getHEIGHT());
            }

            for (TextBox tb:htpTexts) {
                tb.setYShift(totalYShift);
            }
        }
    }

    @Override
    public void render(Graphics2D g) {
        g.drawImage(ResourceMaster.getImageFromMap("title_screen_background"), 0, 0, null);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        backButton.draw(g);
        backgroundPoly.draw(g);

        for (TextBox tb:htpTexts) {
            tb.draw(g);
        }

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        // drawing the image showing all the items and coins
        g.drawImage(ResourceMaster.getImageFromMap("all_items"), DisplayManager.getWIDTH() / 16 * 11, collectablesTitle.getOriginalYPos() + collectablesTitle.getTextHeight() / 2 + totalYShift, null);

        // drawing the image showing the simon says UI overlay
        g.drawImage(ResourceMaster.getImageFromMap("ss_ui"), DisplayManager.getWIDTH() / 16 * 10, ssTitle.getOriginalYPos() + ssTitle.getTextHeight() + totalYShift, null);
    }
}
