package SimonSays;

import display.DisplayManager;
import guis.CheckBox;
import guis.outlines.OutlinedPolygon;
import guis.outlines.TriangularRectangle;
import player.Player;
import player.PlayerInputs;
import toolbox.BasicGUIConstants;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class SimonSaysMaster {

    // ------------------------------------------------------------------------- SIMON_SAYS VARIABLES
    private static final Color[] opColors = new Color[] { // op = OutlinedPolygon
            new Color(180,18,36),
            new Color(34,150,76),
            new Color(200,150,14),
            new Color(63,72,170)
    };
    // stores all the sequences of all the SimonSays of a level
    private int[][] sequences;
    private final SimonSays[] simonSays;
    // keeps track which is the current SimonSays, since each of the three have to be treated differently: //SS = SIMON_SAYS!!
    // 1: show sequence, solve sequence, show sequence
    // 2: solve sequence, show sequence
    // 3: solve sequence
    // ↓ NOTE: set to -1 since counter is incremented before executing task, hence when 0 is required, currentSSCounter would otherwise already be at 1 ↓
    private int currentSSCounter = -1;
    // keeps track which of the sequence is currently active since it may differ from the current SimonSays
    private int currentSequenceCounter = 0;
    // keeps track of the current action to be displayed
    private int currentAnimation = 0;
    // keeps track whether currently an animation is being played or not
    private boolean isPlaying = false;
    // alters its value every cycle and hence adds a pause every other cycle
    private boolean doPause = true;
    // timer to keep track of the time since the previous update
    private long animationTimer;
    // adds an initial delay of a second before an animation of a SimonSays starts
    // in order to prevent an instant lit-up tile
    private static final int INITIAL_DELAY_IN_S = 1;

    // keeping track of user input
    private final ArrayList<Integer> playerInputs = new ArrayList<>();

    // ------------------------------------------------------------------------- GUI VARIABLES
    private final TriangularRectangle bgTR = new TriangularRectangle(DisplayManager.getWIDTH() / 2 - DisplayManager.getHEIGHT() / 3 - (int) (BasicGUIConstants.rsf * 50), DisplayManager.getHEIGHT() / 6 - (int) (BasicGUIConstants.rsf * 50), DisplayManager.getHEIGHT() + (int) (BasicGUIConstants.rsf * 50), DisplayManager.getHEIGHT() / 3 * 2  + (int) (BasicGUIConstants.rsf * 100), DisplayManager.getHEIGHT() / 10, BasicGUIConstants.GUI_OVERLAY_DEFAULT_COLOR, Color.BLACK, 15);
    private final TriangularRectangle fgTR = new TriangularRectangle(DisplayManager.getWIDTH() / 2 - DisplayManager.getHEIGHT() / 3, DisplayManager.getHEIGHT() / 6, DisplayManager.getHEIGHT() / 3 * 2, DisplayManager.getHEIGHT() / 3 * 2, DisplayManager.getHEIGHT() / 12, BasicGUIConstants.BUTTON_TEXT_COLOR, Color.BLACK, 15);
    private final TriangularRectangle centerTR = new TriangularRectangle(DisplayManager.getWIDTH() / 2 - DisplayManager.getHEIGHT() / 12, DisplayManager.getHEIGHT() / 12 * 5, DisplayManager.getHEIGHT() / 6, DisplayManager.getHEIGHT() / 6, DisplayManager.getHEIGHT() / 48, Color.GRAY, Color.BLACK, 8);

    private final int centerLineWidthHalf = (int) (15 * BasicGUIConstants.rsf);
    private final OutlinedPolygon[] ops = new OutlinedPolygon[] {
            new OutlinedPolygon(
                    new int[] {DisplayManager.getWIDTH() / 2 + centerLineWidthHalf, DisplayManager.getWIDTH() / 2 + DisplayManager.getHEIGHT() / 3 - DisplayManager.getHEIGHT() / 12, DisplayManager.getWIDTH() / 2 + DisplayManager.getHEIGHT() / 3, DisplayManager.getWIDTH() / 2 + DisplayManager.getHEIGHT() / 3, DisplayManager.getWIDTH() / 2 + centerLineWidthHalf},
                    new int[] {DisplayManager.getHEIGHT() / 6, DisplayManager.getHEIGHT() / 6, DisplayManager.getHEIGHT() / 6 + DisplayManager.getHEIGHT() / 12, DisplayManager.getHEIGHT() / 2 - centerLineWidthHalf, DisplayManager.getHEIGHT() / 2 - centerLineWidthHalf},
                    5, opColors[0], Color.BLACK, 10
            ),
            new OutlinedPolygon(
                    new int[] {DisplayManager.getWIDTH() / 2 - DisplayManager.getHEIGHT() / 3 + DisplayManager.getHEIGHT() / 12, DisplayManager.getWIDTH() / 2 - centerLineWidthHalf, DisplayManager.getWIDTH() / 2 - centerLineWidthHalf, DisplayManager.getWIDTH() / 2 - DisplayManager.getHEIGHT() / 3, DisplayManager.getWIDTH() / 2 - DisplayManager.getHEIGHT() / 3},
                    new int[] {DisplayManager.getHEIGHT() / 6, DisplayManager.getHEIGHT() / 6, DisplayManager.getHEIGHT() / 2 - centerLineWidthHalf, DisplayManager.getHEIGHT() / 2 - centerLineWidthHalf, DisplayManager.getHEIGHT() / 6 + DisplayManager.getHEIGHT() / 12},
                    5, opColors[1], Color.BLACK, 10
            ),
            new OutlinedPolygon(
                    new int[] {DisplayManager.getWIDTH() / 2 - DisplayManager.getHEIGHT() / 3, DisplayManager.getWIDTH() / 2 - centerLineWidthHalf, DisplayManager.getWIDTH() / 2 - centerLineWidthHalf, DisplayManager.getWIDTH() / 2 - DisplayManager.getHEIGHT() / 3 + DisplayManager.getHEIGHT() / 12, DisplayManager.getWIDTH() / 2 - DisplayManager.getHEIGHT() / 3},
                    new int[] {DisplayManager.getHEIGHT() / 2 + centerLineWidthHalf, DisplayManager.getHEIGHT() / 2 + centerLineWidthHalf, DisplayManager.getHEIGHT() / 6 * 5, DisplayManager.getHEIGHT() / 6 * 5, DisplayManager.getHEIGHT() / 6 * 5 - DisplayManager.getHEIGHT() / 12},
                    5, opColors[2], Color.BLACK, 10
            ),
            new OutlinedPolygon(
                    new int[] {DisplayManager.getWIDTH() / 2 + centerLineWidthHalf, DisplayManager.getWIDTH() / 2 + DisplayManager.getHEIGHT() / 3, DisplayManager.getWIDTH() / 2 + DisplayManager.getHEIGHT() / 3, DisplayManager.getWIDTH() / 2 + DisplayManager.getHEIGHT() / 3 - DisplayManager.getHEIGHT() / 12, DisplayManager.getWIDTH() / 2 + centerLineWidthHalf},
                    new int[] {DisplayManager.getHEIGHT() / 2 + centerLineWidthHalf, DisplayManager.getHEIGHT() / 2 + centerLineWidthHalf, DisplayManager.getHEIGHT() / 6 * 5 - DisplayManager.getHEIGHT() / 12, DisplayManager.getHEIGHT() / 6 * 5, DisplayManager.getHEIGHT() / 6 * 5},
                    5, opColors[3], Color.BLACK, 10
            )
    };

    public final CheckBox[] checkBoxes = new CheckBox[] {
            new CheckBox(DisplayManager.getWIDTH() / 2 + DisplayManager.getHEIGHT() / 3 + (int) (BasicGUIConstants.rsf * 120), DisplayManager.getHEIGHT() / 4, DisplayManager.getHEIGHT() / 8, DisplayManager.getHEIGHT() / 8, BasicGUIConstants.GUI_OVERLAY_DEFAULT_COLOR, 15),
            new CheckBox(DisplayManager.getWIDTH() / 2 + DisplayManager.getHEIGHT() / 3 + (int) (BasicGUIConstants.rsf * 120), DisplayManager.getHEIGHT() / 2 - DisplayManager.getHEIGHT() / 16, DisplayManager.getHEIGHT() / 8, DisplayManager.getHEIGHT() / 8, BasicGUIConstants.GUI_OVERLAY_DEFAULT_COLOR, 15),
            new CheckBox(DisplayManager.getWIDTH() / 2 + DisplayManager.getHEIGHT() / 3 + (int) (BasicGUIConstants.rsf * 120), DisplayManager.getHEIGHT() / 2 + DisplayManager.getHEIGHT() / 8, DisplayManager.getHEIGHT() / 8, DisplayManager.getHEIGHT() / 8, BasicGUIConstants.GUI_OVERLAY_DEFAULT_COLOR, 15)
    };

    /**
     *
     * basic constructor of a SimonSaysMaster
     * upon call, creates a new set of sequences
     *
     * @param simonSays - an array containing all the SS objects generated
     *                  when loading the level
     */
    public SimonSaysMaster(SimonSays[] simonSays) {
        sequences = new int[3][0];
        for (int i = 0; i < sequences.length; i++) {
            sequences[i] = new int[ThreadLocalRandom.current().nextInt(3, 6)];
            for (int j = 0; j < sequences[i].length; j++) {
                sequences[i][j] = ThreadLocalRandom.current().nextInt(0, 4);
            }
        }
        this.simonSays = simonSays;
    }

    /**
     *
     * handles all the updates concerning the SimonSays part of the game
     * <p>
     * if isPlaying is true, the variable currentAnimation is increased in
     * regular intervals which then causes the drawSimonSaysOverlay() function
     * to highlight a different button
     * <p>
     * updates all the SS objects and making them check if they collide with
     * the player
     * <p>
     * depending on the current SS active, the overlay acts differently, since
     * the first SS has to show a sequence, check if the player enters it
     * correctly, then show a second one and close
     * the second SS has to check if the player enters the second sequence
     * correctly and then show a third sequence and close
     * the third SS has to check if the player enters the third sequence
     * correctly
     */
    public void update(Player player) {

        // distinguishing between the SimonSays since not each one has to
        // execute the same actions
        switch (currentSSCounter) {
            case -1:
                for (SimonSays simonSays:simonSays) {
                    if (simonSays.isColliding()) {
                        startAnimation();
                        currentSSCounter++;
                    }
                }
                break;

            // a case has an empty body without a break statement,
            // it will execute the same code which the following has
            // here: case 0 executes also the code from case 1
            case 0:

            case 1:
                // returns true if the player has made equal many inputs as the current sequence contains actions
                if (playerInputs.size() == sequences[currentSSCounter].length) {
                    // checking if the inputs were correct and if so, continuing the SimonSays
                    if (checkSequenceResult(playerInputs, sequences, currentSequenceCounter)) {
                        checkBoxes[currentSequenceCounter].setState(CheckBox.TICKED);
                        currentSequenceCounter++;
                        startAnimation();
                    }
                    // if the inputs were wrong, the SimonSays is interrupted and all further ones are disabled
                    // cannot be converted to else statement since in the upper version,
                    // currentSequenceCounter is increased and therefore, the inputs wouldn't match the sequence
                    // because the sequence was changed
                    if (!checkSequenceResult(playerInputs, sequences, currentSSCounter)) {
                        // sets all CheckBoxes to CROSSED
                        for (int i = currentSSCounter; i < sequences.length; i++) {
                            checkBoxes[i].setState(CheckBox.CROSSED);
                        }
                        // sets all SimonSays to completed to be ignored later on
                        for (SimonSays simonSays: simonSays) {
                            simonSays.setCompleted(true);
                        }
                    }

                    // as soon as the second animation of the first SS is completed,
                    // the SS is exited and the inputs are cleared
                    if (!isPlaying) {
                        ArrayList<Integer> collectionToRemove = new ArrayList<>(playerInputs);
                        playerInputs.removeAll(collectionToRemove);

                        currentSSCounter++;
                        for (SimonSays simonSays:simonSays) {
                            if (simonSays.isColliding()) {
                                simonSays.setCompleted(true);
                            }
                        }
                    }
                }
                break;

            case 2:
                // checking the inputs again
                if (playerInputs.size() == sequences[currentSSCounter].length) {
                    if (checkSequenceResult(playerInputs, sequences, currentSequenceCounter)) {
                        checkBoxes[currentSequenceCounter].setState(CheckBox.TICKED);
                        ArrayList<Integer> collectionToRemove = new ArrayList<>(playerInputs);
                        playerInputs.removeAll(collectionToRemove);
                    }
                    else {
                        for (int i = currentSSCounter; i < sequences.length; i++) {
                            checkBoxes[i].setState(CheckBox.CROSSED);
                        }
                    }
                    for (SimonSays simonSays: simonSays) {
                        simonSays.setCompleted(true);
                    }
                }
                break;
        }
        for (SimonSays simonSays:simonSays) {
            simonSays.update(player);
        }


        // if an animation is playing, the counter should constantly increase
        if (isPlaying) {
            // executed every second
            if (System.currentTimeMillis() - animationTimer > 750) {
                // making a pause every other cycle
                doPause = !doPause;
                // resetting all the color of the buttons after being lit up by the animation
                for (int i = 0; i < ops.length; i++) {
                    ops[i].setFillColor(opColors[i]);
                }
                animationTimer += 750;
                // only if no pause is to be done in this cycle, a next action will be displayed
                if (!doPause) {
                    currentAnimation++;
                }
            }
        } else {
            // mouse interaction with SimonSays
            for (int i = 0; i < ops.length; i++) {
                // whether the player hovers over the specific button or not
                if (ops[i].contains(PlayerInputs.getMousePos())) {
                    for (SimonSays simonSays : simonSays) {
                        // checking each tile whether the player released the mouse button over it and adds it to the player inputs
                        if (PlayerInputs.getMouseButtonsReleasedInFrame().contains(1) && simonSays.isColliding() && playerInputs.size() < sequences[currentSSCounter].length) {
                            playerInputs.add(i);
                        }
                    }
                    // if the player presses the button and hovers over it, it becomes darker
                    if (PlayerInputs.isMousePressed()) {
                        ops[i].setFillColor(opColors[i].darker());
                    }
                    // if the player doesn't press the button but hovers over it, it becomes brighter
                    else {
                        ops[i].setFillColor(opColors[i].brighter());
                    }
                    // resetting the button color
                } else {
                    ops[i].setFillColor(opColors[i]);
                }
            }
        }
    }

    /**
     *
     * draws all the animations, buttons and CheckBoxes to the screen
     * if a SS object is colliding with the player and this SS isn't completed,
     * the overlay is active and if isPlaying is set to true (which is always
     * the case if startAnimation() was called), the animation is played
     * using the currentAnimation variable updated in the update function
     *
     * @param g - the graphics object used to paint onto the screen
     */
    public void drawSimonSaysOverlay(Graphics2D g) {
        for (SimonSays simonSays:simonSays) {
            if (simonSays.isColliding() && !simonSays.isCompleted()) {
                // darken background
                g.setColor(BasicGUIConstants.TRANSPARENT_DARKENING_COLOR);
                g.fillRect(0, 0, DisplayManager.getWIDTH(), DisplayManager.getHEIGHT());

                if (isPlaying) {
                    if (!doPause) {
                        if (0 <= currentAnimation && currentAnimation < sequences[currentSequenceCounter].length) {
                            ops[sequences[currentSequenceCounter][currentAnimation]].setFillColor(opColors[sequences[currentSequenceCounter][currentAnimation]].brighter());
                        } else if (currentAnimation > 0) {
                            isPlaying = false;
                        }
                    }
                }

                bgTR.draw(g);
                fgTR.draw(g);
                for (OutlinedPolygon op:ops) {
                    op.draw(g);
                }
                centerTR.draw(g);
                for (CheckBox cb:checkBoxes) {
                    cb.draw(g);
                }
            }
        }
    }

    /**
     *
     * @return an array containing all the SS objects
     */
    public SimonSays[] getSimonSays() {
        return simonSays;
    }

    /**
     *
     * resets all the variables and starts new animation
     */
    private void startAnimation() {
        if (!isPlaying) {
            doPause = true;
            isPlaying = true;
            currentAnimation = -INITIAL_DELAY_IN_S;
            animationTimer = System.currentTimeMillis();
        }
    }

    /**
     *
     * resetting all the variables if the level is reset, creating new sequence
     */
    public void resetSimonSays() {
        currentAnimation = -INITIAL_DELAY_IN_S;
        currentSequenceCounter = 0;
        currentSSCounter = -1;
        for (CheckBox cb:checkBoxes) {
            cb.setState(CheckBox.EMPTY);
        }

        for (SimonSays simonSays:simonSays) {
            simonSays.setCompleted(false);
        }
        ArrayList<Integer> collectionToRemove = new ArrayList<>(playerInputs);
        playerInputs.removeAll(collectionToRemove);

        // creating a new sequence
        sequences = new int[3][0];
        for (int i = 0; i < sequences.length; i++) {
            sequences[i] = new int[ThreadLocalRandom.current().nextInt(3, 6)];
            for (int j = 0; j < sequences[i].length; j++) {
                sequences[i][j] = ThreadLocalRandom.current().nextInt(0, 4);
            }
        }
    }

    /**
     *
     * checks the playerInputs for equality with the current sequence
     *
     * @param playerInputs - the buttons which the player has pressed
     * @param sequences - all the sequences from all the SSs
     * @param currentSequenceCounter - the current sequence active (the one to check)
     * @return whether the inputs from the player match the sequence of the
     *          currently active SS
     */
    private boolean checkSequenceResult(ArrayList<Integer> playerInputs, int[][] sequences, int currentSequenceCounter) {
        for (int i = 0; i < playerInputs.size(); i++) {
            if (playerInputs.get(i) != sequences[currentSequenceCounter][i]) {
                return false;
            }
        }
        return true;
    }
}
