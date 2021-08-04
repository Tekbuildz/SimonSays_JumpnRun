package gamestates;

import entities.Coin;
import toolbox.DataSaver;
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
import toolbox.BasicGUIConstants;
import toolbox.UIConstraints;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class GameState extends State {


    private boolean drawPauseMenuOverlay = false;
    private boolean drawDeathOverlay = false;
    private boolean drawLevelFinishedOverlay = false;

    private boolean gameInterrupted = false;

    // not having to stretch WIDTH and HEIGHT since they contain the size of the current display and are not hard-coded
    private final int WIDTH = DisplayManager.getWIDTH();
    private final int HEIGHT = DisplayManager.getHEIGHT();

    // rsf = resolutionStretchFactor (converting the points from 1920x1080 screen to the resolution of the current main screen
    private final double rsf = WIDTH / 1920f;
    private final int overlayButtonsWidth = (int) (180 * rsf);
    private final int overlayButtonsHeight = (int) (50 * rsf);


    // ----------------- PAUSE MENU OVERLAY -----------------
    private final HashMap<String, Button> pauseMenuOverlayButtons = new HashMap<>();
    private final TriangularRectangle pauseMenuOverlayPolygon = new TriangularRectangle(WIDTH / 2 - overlayButtonsWidth, HEIGHT / 2 - overlayButtonsHeight * 3, overlayButtonsWidth * 2, overlayButtonsHeight * 6, (int) (20 * rsf), BasicGUIConstants.GUI_OVERLAY_DEFAULT_COLOR, Color.BLACK, 2f);

    // ----------------- DEATH OVERLAY -----------------
    private final HashMap<String, Button> deathOverlayButtons = new HashMap<>();
    private final TriangularRectangle deathOverlayPolygon = new TriangularRectangle(WIDTH / 2 - overlayButtonsWidth, HEIGHT / 2 - overlayButtonsHeight * 3, overlayButtonsWidth * 2, overlayButtonsHeight * 6, (int) (15 * rsf), BasicGUIConstants.GUI_OVERLAY_DEFAULT_COLOR, Color.BLACK, 2f);
    private final TextBox deathOverlayYouDied = new TextBox(WIDTH / 2 - overlayButtonsWidth, HEIGHT / 2 - (int) (overlayButtonsHeight * 1.5), overlayButtonsWidth * 2, Color.WHITE, new Font("Calibri", Font.PLAIN, (int) (40 * rsf)), "You Died!", 5, UIConstraints.UI_CENTER_BOUND_CONSTRAINT);

    // ----------------- GAME FINISHED OVERLAY -----------------
    private final HashMap<String, Button> levelFinishedOverlayButtons = new HashMap<>();
    private final HashMap<String, TextBox> levelFinishedTexts = new HashMap<>();
    private final TriangularRectangle levelFinishedOverlayPolygon = new TriangularRectangle(WIDTH / 2 - overlayButtonsWidth * 2, HEIGHT / 2 - overlayButtonsHeight * 5, overlayButtonsWidth * 4, overlayButtonsHeight * 10, (int) (20 * rsf), BasicGUIConstants.GUI_OVERLAY_DEFAULT_COLOR, Color.BLACK, 2f);

    // ----------------- GENERAL OVERLAY -----------------
    private final ButtonCircle pauseButton = new ButtonCircle((int) (20 * rsf), (int) (10 * rsf), (int) (60 * rsf), "II");
    private final OutlinedPolygon bottomOverlay = new OutlinedPolygon(new int[]{0, (int) (280 * rsf), (int) (400 * rsf), WIDTH - (int) (400 * rsf), WIDTH - (int) (280 * rsf), WIDTH, WIDTH, 0}, new int[]{HEIGHT - (int) (150 * rsf), HEIGHT - (int) (150 * rsf), HEIGHT - (int) (50 * rsf), HEIGHT - (int) (50 * rsf), HEIGHT - (int) (150 * rsf), HEIGHT - (int) (150 * rsf), HEIGHT, HEIGHT}, 8, BasicGUIConstants.GUI_OVERLAY_DEFAULT_COLOR, Color.BLACK, 3.5f);
    private final OutlinedPolygon topLeftOverlay = new OutlinedPolygon(new int[]{0, (int) (140 * rsf), (int) (80 * rsf), 0}, new int[]{0, 0, (int) (80 * rsf), (int) (80 * rsf)}, 4, BasicGUIConstants.GUI_OVERLAY_DEFAULT_COLOR, Color.BLACK, 3.5f);
    private final OutlinedPolygon topRightOverlay = new OutlinedPolygon(new int[]{WIDTH, WIDTH - (int) (240 * rsf), WIDTH - (int) (180 * rsf), WIDTH}, new int[]{0, 0, (int) (80 * rsf), (int) (80 * rsf)}, 4, BasicGUIConstants.GUI_OVERLAY_DEFAULT_COLOR, Color.BLACK, 3.5f);

    private final HealthBar health = new HealthBar((int) (80 * rsf), HEIGHT - (int) (110 * rsf), (int) (180 * rsf), (int) (30 * rsf), (int) (10 * rsf), BasicGUIConstants.HEALTH_BAR_GREEN_COLOR, Color.BLACK, 4f);
    private final OutlinedEllipse faiOutline = new OutlinedEllipse((int) (40 * rsf), HEIGHT - (int) (110 * rsf), (int) (30 * rsf), (int) (30 * rsf), BasicGUIConstants.GUI_OVERLAY_DEFAULT_COLOR.darker(), Color.BLACK, 3f); // faiOutline = firstAidImageOutline
    private final OutlinedEllipse livesOutline = new OutlinedEllipse((int) (40 * rsf), HEIGHT - (int) (60 * rsf), (int) (30 * rsf), (int) (30 * rsf), BasicGUIConstants.GUI_OVERLAY_DEFAULT_COLOR.darker(), Color.BLACK, 3f);

    private final TextBox lives = new TextBox((int) (80 * rsf), HEIGHT - (int) (35 * rsf), (int) (180 * rsf), BasicGUIConstants.HEALTH_BAR_GREEN_COLOR, new Font("Calibri", Font.PLAIN, (int) (30 * rsf)), "", 5, UIConstraints.UI_RIGHT_BOUND_CONSTRAINT);
    private final TextBox coins = new TextBox(WIDTH - (int) (260 * rsf), HEIGHT - (int) (60 * rsf), (int) (240 * rsf), BasicGUIConstants.MONEY_YELLOW_COLOR, new Font("Calibri", Font.PLAIN, (int) (60 * rsf)), "$ " + Main.player.getCoins(), 0, UIConstraints.UI_LEFT_BOUND_CONSTRAINT);


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
        pauseMenuOverlayButtons.put("restartButton", new ButtonTriangularRectangle(WIDTH / 2 - overlayButtonsWidth / 2, HEIGHT / 2 - overlayButtonsHeight / 2, overlayButtonsWidth, overlayButtonsHeight, (int) (10 * rsf), "Restart"));
        pauseMenuOverlayButtons.put("exitGameButton", new ButtonTriangularRectangle(WIDTH / 2 - overlayButtonsWidth / 2, HEIGHT / 2 + overlayButtonsHeight, overlayButtonsWidth, overlayButtonsHeight, (int) (10 * rsf), "Exit Game"));
        for (Button button: pauseMenuOverlayButtons.values()) {
            button.setTextFont(BasicGUIConstants.DEFAULT_BUTTON_FONT);
        }

        // ----------------- DEATH OVERLAY -----------------
        deathOverlayButtons.put("restartButton", new ButtonTriangularRectangle(WIDTH / 2 - overlayButtonsWidth / 2, HEIGHT / 2 - overlayButtonsHeight / 2, overlayButtonsWidth, overlayButtonsHeight, (int) (10 * rsf), "Restart"));
        deathOverlayButtons.put("exitGameDeathButton", new ButtonTriangularRectangle(WIDTH / 2 - overlayButtonsWidth / 2, HEIGHT / 2 + overlayButtonsHeight, overlayButtonsWidth, overlayButtonsHeight, (int) (10 * rsf), "Titlescreen"));
        for (Button button: deathOverlayButtons.values()) {
            button.setTextFont(BasicGUIConstants.DEFAULT_BUTTON_FONT);
        }

        // ----------------- GAME FINISHED OVERLAY -----------------
        levelFinishedOverlayButtons.put("restartButton", new ButtonTriangularRectangle(WIDTH / 2 - (int) (overlayButtonsWidth * 1.5), HEIGHT / 2 + (int) (overlayButtonsHeight * 3.5), overlayButtonsWidth, overlayButtonsHeight, (int) (10 * rsf), "Restart"));
        levelFinishedOverlayButtons.put("continueButton", new ButtonTriangularRectangle(WIDTH / 2 + (int) (overlayButtonsWidth * 0.5), HEIGHT / 2 + (int) (overlayButtonsHeight * 3.5), overlayButtonsWidth, overlayButtonsHeight, (int) (10 * rsf), "Continue"));
        for (Button button: levelFinishedOverlayButtons.values()) {
            button.setTextFont(BasicGUIConstants.DEFAULT_BUTTON_FONT);
        }
        levelFinishedTexts.put("wellDone", new TextBox(WIDTH / 2 - overlayButtonsWidth * 2, HEIGHT / 2 - (int) (overlayButtonsHeight * 3.5), overlayButtonsWidth * 4, BasicGUIConstants.BUTTON_TEXT_COLOR, new Font("Calibri", Font.PLAIN, (int) (50 * rsf)), "Well Done!", 0, UIConstraints.UI_CENTER_BOUND_CONSTRAINT));
        levelFinishedTexts.put("coinsCollected", new TextBox(WIDTH / 2 - (int) (overlayButtonsWidth * 1.5), HEIGHT / 2 - overlayButtonsHeight * 2, overlayButtonsWidth * 3, BasicGUIConstants.BUTTON_TEXT_COLOR, BasicGUIConstants.DEFAULT_BUTTON_FONT, "", 0, UIConstraints.UI_LEFT_BOUND_CONSTRAINT));
        levelFinishedTexts.put("moneyEarned", new TextBox(WIDTH / 2 - (int) (overlayButtonsWidth * 1.5), HEIGHT / 2 - overlayButtonsHeight, overlayButtonsWidth * 3, BasicGUIConstants.BUTTON_TEXT_COLOR, BasicGUIConstants.DEFAULT_BUTTON_FONT, "", 0, UIConstraints.UI_LEFT_BOUND_CONSTRAINT));
        levelFinishedTexts.put("itemsCollected", new TextBox(WIDTH / 2 - (int) (overlayButtonsWidth * 1.5), HEIGHT / 2, overlayButtonsWidth * 3, BasicGUIConstants.BUTTON_TEXT_COLOR, BasicGUIConstants.DEFAULT_BUTTON_FONT, "", 0, UIConstraints.UI_LEFT_BOUND_CONSTRAINT));
        levelFinishedTexts.put("timeTaken", new TextBox(WIDTH / 2 - (int) (overlayButtonsWidth * 1.5), HEIGHT / 2 + overlayButtonsHeight, overlayButtonsWidth * 3, BasicGUIConstants.BUTTON_TEXT_COLOR, BasicGUIConstants.DEFAULT_BUTTON_FONT, "", 0, UIConstraints.UI_LEFT_BOUND_CONSTRAINT));
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
        if (drawPauseMenuOverlay) {
            for (Button button : pauseMenuOverlayButtons.values()) {
                button.update();
                if (button.isButtonWasReleased()) {
                    switch (Objects.requireNonNull(toolbox.HashMap.getKey(pauseMenuOverlayButtons, button))) {
                        case "continueButton":
                            gameInterrupted = false;
                            drawPauseMenuOverlay = false;
                            break;

                        case "restartButton":
                            resetLevel();
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

        if (drawDeathOverlay) {
            for (Button button : deathOverlayButtons.values()) {
                button.update();
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

        if (drawLevelFinishedOverlay) {
            for (Button button:levelFinishedOverlayButtons.values()) {
                button.update();
                if (button.isButtonWasReleased()) {
                    switch (Objects.requireNonNull(toolbox.HashMap.getKey(levelFinishedOverlayButtons, button))) {
                        case "restartButton":
                            resetLevel();
                            break;
                        case "continueButton":
                            DataSaver.saveData();
                            // StateMaster.setState(new MainMenuState());
                            System.exit(0);
                            break;
                    }
                }
            }
        }

        if (PlayerInputs.getKeysReleasedInFrame().contains(KeyEvent.VK_ESCAPE)) {
            drawPauseMenuOverlay = !drawPauseMenuOverlay;
        }

        if (!gameInterrupted) {
            Main.player.applyGravity();
            Main.player.updatePlayerRectCoords();
            coins.setText(String.valueOf(Main.player.getCoins()));
            Main.player.checkCoinCollision(Level.getCoins());
            if (Level.getFinish().intersects(Main.player.getPlayerRect())) {
                gameInterrupted = true;
                drawLevelFinishedOverlay = true;

                int totalCoinsInLevel = 0;
                int totalCoinsCollected = 0;
                int totalMoneyEarned = 0;
                int totalMoneyAvailable = 0;
                for (ArrayList<Coin> list:Level.getCoins()) {
                    totalCoinsInLevel += list.size();
                    for (Coin coin:list) {
                        totalMoneyAvailable += coin.getValue();
                        if (coin.isWasCollected()) {
                            totalCoinsCollected++;
                            totalMoneyEarned += coin.getValue();
                        }
                    }
                }

                levelFinishedTexts.get("coinsCollected").setText(   "Coins Collected:           " + totalCoinsCollected + "/" + totalCoinsInLevel);
                levelFinishedTexts.get("moneyEarned").setText(      "Money earned:              " + totalMoneyEarned + "/" + totalMoneyAvailable);
                levelFinishedTexts.get("itemsCollected").setText(   "Items Collected:           0 / 0");
                levelFinishedTexts.get("timeTaken").setText(        "Time elapsed:              ");
            }

            handleMovement();

            pauseButton.update();
            if (pauseButton.isButtonWasReleased() && !drawPauseMenuOverlay) {
                gameInterrupted = true;
                drawPauseMenuOverlay = true;
            }

            health.setFillLevel(Main.player.getHealth());
            health.update();
            lives.setText(Main.player.getLives() + "x");

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
        if (drawLevelFinishedOverlay) {
            drawLevelFinishedOverlay(g);
        }
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    }

    /**
     *
     * drawing all the level-rectangles and all the coins
     *
     * @param g - the graphics object used to paint onto the screen
     */
    private void drawLevel(Graphics2D g) {
        // drawing the level rectangles
        Image[] dirtGrassSky = SpriteSheetMaster.getSpriteSheetFromMap("dirtGrassSky").getSpriteImages();
        for (List<Cube> list:Level.getLevelCubes()) {
            for (Cube cube:list) {
                // simulating player movement by shifting the level to the side and keeping the player centered
                if (Main.player.getX() >= (float) (DisplayManager.getWIDTH() / 2 + Main.player.getCubeSize() / 2)) {
                    g.drawImage(dirtGrassSky[cube.getCubeID()], (int) cube.getX() * cube.getPixelSIZE() - ((int) Main.player.getX() - DisplayManager.getWIDTH() / 2 - Main.player.getCubeSize() / 2), (int) -(Level.getLevelCubes().size() - cube.getY()) * cube.getPixelSIZE(), null, null);
                } else {
                    g.drawImage(dirtGrassSky[cube.getCubeID()], (int) cube.getX() * cube.getPixelSIZE(), (int) -(Level.getLevelCubes().size() - cube.getY()) * cube.getPixelSIZE(), null, null);
                }
            }
        }

        // drawing the coins
        // replace later with showing image/animation
        g.setColor(BasicGUIConstants.MONEY_YELLOW_COLOR);
        for (List<Coin> list:Level.getCoins()) {
            for (Coin coin:list) {
                if (!coin.isWasCollected()) {
                    Rectangle2D rectangle2D;
                    if (Main.player.getX() >= (float) (DisplayManager.getWIDTH() / 2 + Main.player.getCubeSize() / 2)) {
                        rectangle2D = new Rectangle2D.Double(coin.getCollisionBox().getX() - ((int) (Main.player.getX() - DisplayManager.getWIDTH() / 2 - Main.player.getCubeSize() / 2)), -(Level.getLevelCubes().size() * Main.player.getCubeSize() - coin.getCollisionBox().getY()), coin.getCollisionBox().getWidth(), coin.getCollisionBox().getHeight());
                    } else {
                        rectangle2D = new Rectangle2D.Double(coin.getCollisionBox().getX(), -(Level.getLevelCubes().size() * Main.player.getCubeSize() - coin.getCollisionBox().getY()), coin.getCollisionBox().getWidth(), coin.getCollisionBox().getHeight());
                    }

                    g.fill(rectangle2D);
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
        lives.draw(g);

        coins.draw(g);
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
        g.setColor(BasicGUIConstants.TRANSPARENT_DARKENING_COLOR);
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
        g.setColor(BasicGUIConstants.TRANSPARENT_DARKENING_COLOR);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        deathOverlayPolygon.draw(g);
        deathOverlayYouDied.draw(g);

        for (Button button: deathOverlayButtons.values()) {
            button.draw(g);
        }
    }

    /**
     *
     * drawing the overlay if the player has collided with the finish-rectangle
     * the function darkens the screen and adds the options to retry the level
     * or to continue in which case the data is saved to the disc
     * <p>
     * the overlay also shows the statistics of the level the player has just
     * completed including how many of the total available coins the player has
     * collected, how many items he has collected and how much time has elapsed
     * until the player reached the end of the level
     *
     * @param g - the graphics object used to paint onto the screen
     */
    private void drawLevelFinishedOverlay(Graphics2D g) {
        g.setColor(BasicGUIConstants.TRANSPARENT_DARKENING_COLOR);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        levelFinishedOverlayPolygon.draw(g);
        for (TextBox box:levelFinishedTexts.values()) {
            box.draw(g);
        }

        for (Button button:levelFinishedOverlayButtons.values()) {
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
        Main.player = new Player(Level.getSpawnLocation(), Main.player.getLives(), Main.player.getBackupCoins());
        drawDeathOverlay = false;
        drawPauseMenuOverlay = false;
        drawLevelFinishedOverlay = false;
        gameInterrupted = false;

        for (ArrayList<Coin> list:Level.getCoins()) {
            for (Coin coin:list) {
                coin.setWasCollected(false);
            }
        }
    }
}
