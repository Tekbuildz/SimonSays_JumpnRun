package gamestates;

import SpriteSheet.SpriteSheetMaster;
import display.DisplayManager;
import gameLoop.Main;
import guis.HealthBar;
import guis.TextBox;
import guis.buttons.Button;
import guis.buttons.ButtonCircle;
import guis.buttons.ButtonTriangularRectangle;
import guis.outlines.OutlinedEllipse;
import guis.outlines.OutlinedPolygon;
import guis.outlines.TriangularRectangle;
import levelHandling.Cube;
import levelHandling.Level;
import player.Player;
import player.PlayerInputs;
import toolbox.BasicColors;
import toolbox.UIConstraints;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class GameState extends State {


    private boolean drawPauseMenuOverlay = false;
    private boolean drawDeathOverlay = false;

    private boolean gameInterrupted = false;

    // not having to stretch WIDTH and HEIGHT since they contain the size of the current display and are not hard-coded
    private final int WIDTH = DisplayManager.getWIDTH();
    private final int HEIGHT = DisplayManager.getHEIGHT();

    // rsf = resolutionStretchFactor (converting the points from 1920x1080 screen to the resolution of the current main screen
    private final double rsf = (double) WIDTH / 1920;
    private final int overlayButtonsWidth = (int) (180 * rsf);
    private final int overlayButtonsHeight = (int) (50 * rsf);


    // ----------------- PAUSE MENU OVERLAY -----------------
    private final HashMap<String, Button> pauseMenuOverlayButtons = new HashMap<>();
    private final TriangularRectangle pauseMenuOverlayPolygon = new TriangularRectangle(WIDTH / 2 - overlayButtonsWidth, HEIGHT / 2 - overlayButtonsHeight * 3, overlayButtonsWidth * 2, overlayButtonsHeight * 6, (int) (20 * rsf), BasicColors.GUI_OVERLAY_DEFAULT_COLOR, Color.BLACK, 2f);

    // ----------------- DEATH OVERLAY -----------------
    private final HashMap<String, Button> deathOverlayButtons = new HashMap<>();
    private final TriangularRectangle deathOverlayPolygon = new TriangularRectangle(WIDTH / 2 - overlayButtonsWidth, HEIGHT / 2 - overlayButtonsHeight * 3, overlayButtonsWidth * 2, overlayButtonsHeight * 6, (int) (15 * rsf), BasicColors.GUI_OVERLAY_DEFAULT_COLOR, Color.BLACK, 2f);
    private final TextBox deathOverlayYouDied = new TextBox(WIDTH / 2 - overlayButtonsWidth, HEIGHT / 2 - (int) (overlayButtonsHeight * 1.5), overlayButtonsWidth * 2, Color.WHITE, new Font("Calibri", Font.PLAIN, (int) (40 * rsf)), "You Died!", 5, UIConstraints.UI_CENTER_BOUND_CONSTRAINT);

    // ----------------- GENERAL OVERLAY -----------------
    private final ButtonCircle pauseButton = new ButtonCircle((int) (20 * rsf), (int) (10 * rsf), (int) (60 * rsf), "II");
    private final OutlinedPolygon bottomOverlay = new OutlinedPolygon(new int[]{0, (int) (280 * rsf), (int) (400 * rsf), WIDTH - (int) (400 * rsf), WIDTH - (int) (280 * rsf), WIDTH, WIDTH, 0}, new int[]{HEIGHT - (int) (150 * rsf), HEIGHT - (int) (150 * rsf), HEIGHT - (int) (50 * rsf), HEIGHT - (int) (50 * rsf), HEIGHT - (int) (150 * rsf), HEIGHT - (int) (150 * rsf), HEIGHT, HEIGHT}, 8, BasicColors.GUI_OVERLAY_DEFAULT_COLOR, Color.BLACK, 3.5f);
    private final OutlinedPolygon topLeftOverlay = new OutlinedPolygon(new int[]{0, (int) (140 * rsf), (int) (80 * rsf), 0}, new int[]{0, 0, (int) (80 * rsf), (int) (80 * rsf)}, 4, BasicColors.GUI_OVERLAY_DEFAULT_COLOR, Color.BLACK, 3.5f);
    private final OutlinedPolygon topRightOverlay = new OutlinedPolygon(new int[]{WIDTH, WIDTH - (int) (240 * rsf), WIDTH - (int) (180 * rsf), WIDTH}, new int[]{0, 0, (int) (80 * rsf), (int) (80 * rsf)}, 4, BasicColors.GUI_OVERLAY_DEFAULT_COLOR, Color.BLACK, 3.5f);

    private final HealthBar health = new HealthBar((int) (80 * rsf), HEIGHT - (int) (110 * rsf), (int) (180 * rsf), (int) (30 * rsf), (int) (10 * rsf), BasicColors.HEALTH_BAR_GREEN_COLOR, Color.BLACK, 4f);
    private final OutlinedEllipse faiOutline = new OutlinedEllipse((int) (40 * rsf), HEIGHT - (int) (110 * rsf), (int) (30 * rsf), (int) (30 * rsf), BasicColors.GUI_OVERLAY_DEFAULT_COLOR.darker(), Color.BLACK, 3f); // faiOutline = firstAidImageOutline
    private final OutlinedEllipse livesOutline = new OutlinedEllipse((int) (40 * rsf), HEIGHT - (int) (60 * rsf), (int) (30 * rsf), (int) (30 * rsf), BasicColors.GUI_OVERLAY_DEFAULT_COLOR.darker(), Color.BLACK, 3f);

    private final TextBox textBox = new TextBox((int) (80 * rsf), HEIGHT - (int) (35 * rsf), (int) (180 * rsf), BasicColors.HEALTH_BAR_GREEN_COLOR, new Font("Calibri", Font.PLAIN, (int) (30 * rsf)), "", 5, UIConstraints.UI_RIGHT_BOUND_CONSTRAINT);

    /**
     *
     * constructor of the GameState class
     * creating and adding buttons of this state to an ArrayList and giving
     * them a default font
     */
    public GameState() {
        pauseButton.setTextFont(new Font("Calibri", Font.PLAIN, (int) (40 * rsf)));

        // ----------------- PAUSE MENU OVERLAY -----------------
        pauseMenuOverlayButtons.put("continueButton", new ButtonTriangularRectangle(WIDTH / 2 - overlayButtonsWidth / 2, HEIGHT / 2 - overlayButtonsHeight * 2, overlayButtonsWidth, overlayButtonsHeight, (int) (10 * rsf), "Continue"));
        pauseMenuOverlayButtons.put("optionsButton", new ButtonTriangularRectangle(WIDTH / 2 - overlayButtonsWidth / 2, HEIGHT / 2 - overlayButtonsHeight / 2, overlayButtonsWidth, overlayButtonsHeight, (int) (10 * rsf), "Options"));
        pauseMenuOverlayButtons.put("exitGameButton", new ButtonTriangularRectangle(WIDTH / 2 - overlayButtonsWidth / 2, HEIGHT / 2 + overlayButtonsHeight, overlayButtonsWidth, overlayButtonsHeight, (int) (10 * rsf), "Exit Game"));
        for (Button button: pauseMenuOverlayButtons.values()) {
            button.setTextFont(new Font("Calibri", Font.PLAIN, (int) (30 * rsf)));
        }

        // ----------------- DEATH OVERLAY -----------------
        deathOverlayButtons.put("restartButton", new ButtonTriangularRectangle(WIDTH / 2 - overlayButtonsWidth / 2, HEIGHT / 2 - overlayButtonsHeight / 2, overlayButtonsWidth, overlayButtonsHeight, (int) (10 * rsf), "Restart"));
        deathOverlayButtons.put("exitGameDeathButton", new ButtonTriangularRectangle(WIDTH / 2 - overlayButtonsWidth / 2, HEIGHT / 2 + overlayButtonsHeight, overlayButtonsWidth, overlayButtonsHeight, (int) (10 * rsf), "Titlescreen"));
        for (Button button: deathOverlayButtons.values()) {
            button.setTextFont(new Font("Calibri", Font.PLAIN, (int) (30 * rsf)));
        }
    }

    /**
     *
     * updating the game logic
     * updating the player's location and applying gravity to it
     * handling player input and converting it
     * into movement of the digital player
     * updating the buttons and listening for any mouse releases
     * over the buttons, then executing the action corresponding to the button
     */
    @Override
    public void update() {
        for (Button button: pauseMenuOverlayButtons.values()) {
            button.update();
            if (drawPauseMenuOverlay) {
                if (button.isButtonWasReleased()) {
                    switch (Objects.requireNonNull(toolbox.HashMap.getKey(pauseMenuOverlayButtons, button))) {
                        case "continueButton":
                            gameInterrupted = false;
                            drawPauseMenuOverlay = false;
                            break;

                        case "optionsButton":
                            // do sth else
                            break;

                        case "exitGameButton":
                            // replace this later by going back to main menu screen
                            // StateMaster.setState(new MainMenuState());
                            System.exit(0);
                            break;
                    }
                }
            }
        }

        for (Button button: deathOverlayButtons.values()) {
            button.update();
            if (drawDeathOverlay) {
                if (button.isButtonWasReleased()) {
                    switch (Objects.requireNonNull(toolbox.HashMap.getKey(deathOverlayButtons, button))) {
                        case "restartButton":
                            resetLevel();
                            break;
                        case "exitGameDeathButton":
                            resetLevel();
                            // replace this later by going back to main menu screen
                            // StateMaster.setState(new MainMenuState());
                            System.exit(0);
                            break;
                    }
                }
            }
        }

        if (PlayerInputs.getKeysPressedInFrame().contains(KeyEvent.VK_ESCAPE)) {
            if (!drawPauseMenuOverlay) drawPauseMenuOverlay = true;
        }

        if (!gameInterrupted) {
            Main.player.applyGravity();
            Main.player.updatePlayerRectCoords();

            handleMovement();

            pauseButton.update();
            if (pauseButton.isButtonWasReleased() && !drawPauseMenuOverlay) {
                gameInterrupted = true;
                drawPauseMenuOverlay = true;
            }

            health.setFillLevel(Main.player.getHealth());
            health.update();
            textBox.setText(Main.player.getLives() + "x");

            if (Main.player.getY() > 2200) {
                gameInterrupted = true;
                drawDeathOverlay = true;
            }
        }
    }

    @Override
    public void render(Graphics2D g) {
        g.translate(0, HEIGHT);
        drawLevel(g);
        drawPlayer(g);
        // using the translate function with "-HEIGHT" because the origin is always viewed from the current origin
        // of the current coordinate system which is located at 0, HEIGHT (viewed from the actual screen => not visible)
        // in order to change the coordinate system back from the bottom left to the top left of the screen, the
        // height of the screen needs to be subtracted
        g.translate(0, -HEIGHT);

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawGUIOverlay(g);

        if (drawPauseMenuOverlay) {
            drawPauseMenuOverlay(g);
        }

        if (drawDeathOverlay) {
            drawDeathOverlay(g);
        }
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    }

    /**
     *
     * drawing all the level-rectangles
     *
     * @param g - the graphics object used to paint onto the screen
     */
    private void drawLevel(Graphics2D g) {
        Image[] dirtGrassSky = SpriteSheetMaster.getSpriteSheetFromMap("dirtGrassSky").getSpriteImages();
        for (int i = Level.getLevelCubes().size() - 1; i >= 0; i--) {
            List<Cube> list = Level.getLevelCubes().get(i);
            for (int j = list.size() - 1; j >= 0; j--) {
                Cube cube = list.get(j);
                // simulating player movement by shifting the level to the side and keeping the player centered
                if (Main.player.getX() >= (float) (DisplayManager.getWIDTH()/2 + Main.player.getCubeSize()/2)){
                    g.drawImage(dirtGrassSky[cube.getCubeID()], (int) cube.getX() * cube.getPixelSIZE() - ((int) Main.player.getX() - DisplayManager.getWIDTH()/2 - Main.player.getCubeSize()/2), (int) -(Level.getLevelCubes().size() - cube.getY()) * cube.getPixelSIZE(), null, null);
                } else {
                    g.drawImage(dirtGrassSky[cube.getCubeID()], (int) cube.getX() * cube.getPixelSIZE(), (int) -(Level.getLevelCubes().size() - cube.getY()) * cube.getPixelSIZE(), null, null);
                }
            }
        }
    }

    /**
     *
     * drawing the player to the screen
     *
     * @param g - the graphics object used to paint onto the screen
     */
    private void drawPlayer(Graphics2D g) {
        g.setColor(Color.BLUE);
        Rectangle2D.Double playerRect;

        // simulating player movement by shifting the level to the side and keeping the player centered
        if (Main.player.getX() >= (float) (DisplayManager.getWIDTH()/2 + Main.player.getCubeSize()/2)){
            playerRect = new Rectangle2D.Double((float) (DisplayManager.getWIDTH()/2 + Main.player.getCubeSize()/2), -(Level.getLevelCubes().size() * Main.player.getCubeSize() - Main.player.getY()), Player.getPlayerWidth(), Player.getPlayerHeight());
        } else {
            playerRect = new Rectangle2D.Double(Main.player.getX(), -(Level.getLevelCubes().size() * Main.player.getCubeSize() - Main.player.getY()), Player.getPlayerWidth(), Player.getPlayerHeight());
        }

        g.fill(playerRect);
    }

    /**
     *
     * calls the functions in the player class
     * corresponding to the input from the player
     */
    private void handleMovement() {
        // clearing any previous movement
        Main.player.move('n');
        // moving upon key press accordingly
        if (PlayerInputs.getKeysPressedInFrame().contains(KeyEvent.VK_A)) {
            Main.player.move('l');
        }
        if (PlayerInputs.getKeysPressedInFrame().contains(KeyEvent.VK_D)) {
            Main.player.move('r');
        }
        if (PlayerInputs.getKeysPressedInFrame().contains(KeyEvent.VK_SPACE)) {
            Main.player.jump();
        }
    }

    /**
     *
     * drawing all the overlays for the GUI
     *
     * @param g - the graphics object used to paint onto the screen
     */
    private void drawGUIOverlay(Graphics2D g) {
        // basic GUI overlays
        bottomOverlay.draw(g);
        topLeftOverlay.draw(g);
        topRightOverlay.draw(g);

        // buttons
        pauseButton.draw(g);

        // health bars and additions
        health.draw(g);
        faiOutline.draw(g);
        livesOutline.draw(g);
        textBox.draw(g);
    }

    /**
     *
     * drawing the overlay when hitting the pause button or pressing ESC
     * adds an overlay over the entire screen darkening out the background
     * to center the focus of the player to the buttons in the middle
     * adds option to go to the options settings, resume the level or quit
     * to the main screen in which case the progress would not be saved
     *
     * @param g - the graphics object used to paint onto the screen
     */
    private void drawPauseMenuOverlay(Graphics2D g) {
        // creating a semi-transparent overlay over the entire screen to remove focus on the game and switch it to buttons
        g.setColor(BasicColors.TRANSPARENT_DARKENING_COLOR);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        pauseMenuOverlayPolygon.draw(g);

        for (Button button: pauseMenuOverlayButtons.values()) {
            button.draw(g);
        }
    }

    /**
     *
     * drawing the death overlay when the player
     * adds an overlay over the entire screen darkening out the background
     * to center the focus of the player to the buttons in the middle
     * adds option to reset the level and try again or return to the
     * main menu
     *
     * @param g - the graphics object used to paint onto the screen
     */
    private void drawDeathOverlay(Graphics2D g) {
        // semi-transparent overlay again
        g.setColor(BasicColors.TRANSPARENT_DARKENING_COLOR);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        deathOverlayPolygon.draw(g);
        deathOverlayYouDied.draw(g);

        for (Button button: deathOverlayButtons.values()) {
            button.draw(g);
        }
    }

    /**
     *
     * resetting the level if the player died
     * this function resets all the variables like the player's location,
     * removing any overlays and removing the interruption of the player's
     * movement
     */
    private void resetLevel() {
        Main.player = new Player(Level.getSpawnLocation());
        drawDeathOverlay = false;
        drawPauseMenuOverlay = false;
        gameInterrupted = false;
    }
}
