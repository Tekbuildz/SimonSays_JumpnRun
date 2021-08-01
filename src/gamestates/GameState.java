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

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class GameState extends State {

    public HashMap<String, Button> pauseMenuOverlayButtons = new HashMap<>();

    private boolean drawPauseMenuOverlay = false;
    private boolean drawDeathOverlay = false;

    private boolean gameInterrupted = false;

    // not having to stretch WIDTH and HEIGHT since they contain the size of the current display and are not hard-coded
    private final int WIDTH = DisplayManager.getWIDTH();
    private final int HEIGHT = DisplayManager.getHEIGHT();

    // rsf = resolutionStretchFactor (converting the points from 1920x1080 screen to the resolution of the current main screen
    private final double rsf = (double) WIDTH / 1920;


    // ----------------- PAUSE MENU OVERLAY -----------------
    private final int pauseMenuOverlayButtonsWidth = (int) (180 * rsf);
    private final int pauseMenuOverlayButtonsHeight = (int) (50 * rsf);

    private final TriangularRectangle pauseMenuOverlayPolygon = new TriangularRectangle(WIDTH / 2 - pauseMenuOverlayButtonsWidth, HEIGHT / 2 - pauseMenuOverlayButtonsHeight * 3, pauseMenuOverlayButtonsWidth * 2, pauseMenuOverlayButtonsHeight * 6, (int) (20 * rsf), BasicColors.GUI_OVERLAY_DEFAULT_COLOR, Color.BLACK, 2f);

    private final ButtonTriangularRectangle continueButton = new ButtonTriangularRectangle(WIDTH / 2 - pauseMenuOverlayButtonsWidth / 2, HEIGHT / 2 - pauseMenuOverlayButtonsHeight * 2, pauseMenuOverlayButtonsWidth, pauseMenuOverlayButtonsHeight, (int) (10 * rsf), "Continue");
    private final ButtonTriangularRectangle optionsButton = new ButtonTriangularRectangle(WIDTH / 2 - pauseMenuOverlayButtonsWidth / 2, HEIGHT / 2 - pauseMenuOverlayButtonsHeight / 2, pauseMenuOverlayButtonsWidth, pauseMenuOverlayButtonsHeight, (int) (10 * rsf), "Options");
    private final ButtonTriangularRectangle exitGameButton = new ButtonTriangularRectangle(WIDTH / 2 - pauseMenuOverlayButtonsWidth / 2, HEIGHT / 2 + pauseMenuOverlayButtonsHeight, pauseMenuOverlayButtonsWidth, pauseMenuOverlayButtonsHeight, (int) (10 * rsf), "Exit Game");


    // ----------------- GENERAL OVERLAY -----------------
    private final ButtonCircle pauseButton = new ButtonCircle((int) (20 * rsf), (int) (10 * rsf), (int) (60 * rsf), "II");
    private final OutlinedPolygon bottomOverlay = new OutlinedPolygon(new int[]{0, (int) (280 * rsf), (int) (400 * rsf), WIDTH - (int) (400 * rsf), WIDTH - (int) (280 * rsf), WIDTH, WIDTH, 0}, new int[]{HEIGHT - (int) (150 * rsf), HEIGHT - (int) (150 * rsf), HEIGHT - (int) (50 * rsf), HEIGHT - (int) (50 * rsf), HEIGHT - (int) (150 * rsf), HEIGHT - (int) (150 * rsf), HEIGHT, HEIGHT}, 8, BasicColors.GUI_OVERLAY_DEFAULT_COLOR, Color.BLACK, 3.5f);
    private final OutlinedPolygon topLeftOverlay = new OutlinedPolygon(new int[]{0, (int) (140 * rsf), (int) (80 * rsf), 0}, new int[]{0, 0, (int) (80 * rsf), (int) (80 * rsf)}, 4, BasicColors.GUI_OVERLAY_DEFAULT_COLOR, Color.BLACK, 3.5f);
    private final OutlinedPolygon topRightOverlay = new OutlinedPolygon(new int[]{WIDTH, WIDTH - (int) (240 * rsf), WIDTH - (int) (180 * rsf), WIDTH}, new int[]{0, 0, (int) (80 * rsf), (int) (80 * rsf)}, 4, BasicColors.GUI_OVERLAY_DEFAULT_COLOR, Color.BLACK, 3.5f);

    private final HealthBar health = new HealthBar((int) (80 * rsf), HEIGHT - (int) (110 * rsf), (int) (180 * rsf), (int) (30 * rsf), (int) (10 * rsf), BasicColors.HEALTH_BAR_GREEN_COLOR, Color.BLACK, 4f);
    private final OutlinedEllipse faiOutline = new OutlinedEllipse((int) (40 * rsf), HEIGHT - (int) (110 * rsf), (int) (30 * rsf), (int) (30 * rsf), BasicColors.GUI_OVERLAY_DEFAULT_COLOR.darker(), Color.BLACK, 3f); // faiOutline = firstAidImageOutline
    private final OutlinedEllipse livesOutline = new OutlinedEllipse((int) (40 * rsf), HEIGHT - (int) (60 * rsf), (int) (30 * rsf), (int) (30 * rsf), BasicColors.GUI_OVERLAY_DEFAULT_COLOR.darker(), Color.BLACK, 3f);

    private final TextBox textBox = new TextBox((int) (80 * rsf), HEIGHT - (int) (35 * rsf), 10, BasicColors.HEALTH_BAR_GREEN_COLOR, new Font("Calibri", Font.PLAIN, (int) (30 * rsf)), "", 5);

    /**
     *
     * constructor of the GameState class
     * adding buttons of this state to an ArrayList and giving them
     * a default font
     */
    public GameState() {
        pauseButton.setTextFont(new Font("Calibri", Font.PLAIN, (int) (40 * rsf)));

        pauseMenuOverlayButtons.put("continueButton", continueButton);
        pauseMenuOverlayButtons.put("optionsButton", optionsButton);
        pauseMenuOverlayButtons.put("exitGameButton", exitGameButton);
        for (Button button: pauseMenuOverlayButtons.values()) {
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
                        System.exit(0);
                        break;
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
                drawPauseMenuOverlay = true;
            }

            health.setFillLevel(Main.player.getHealth());
            health.update();
            textBox.setText(Main.player.getLives() + "x");

            if (Main.player.getY() > 2200) {
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
     * also stops the gameplay itself from progressing by interrupting the
     * updates affecting the player and the level, however not the buttons in
     * order for the player to be able to get back into the game again
     *
     * @param g - the graphics object used to paint onto the screen
     */
    private void drawPauseMenuOverlay(Graphics2D g) {
        gameInterrupted = true;
        // creating a semi-transparent overlay over the entire screen to remove focus on the game and switch it to buttons
        g.setColor(new Color(0, 0, 0, 120));
        g.fillRect(0, 0, (int) (WIDTH * rsf), (int) (HEIGHT * rsf));

        g.setColor(BasicColors.GUI_OVERLAY_DEFAULT_COLOR);
//        g.fill(pauseMenuOverlayPolygon);
        pauseMenuOverlayPolygon.draw(g);

        for (Button button: pauseMenuOverlayButtons.values()) {
            button.draw(g);
        }
    }
}
