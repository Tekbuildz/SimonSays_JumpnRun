package gamestates;

import entities.Coin;
import SimonSays.SimonSays;
import entities.mob.Mob;
import guis.CheckBox;
import toolbox.DataSaver;
import SpriteSheet.ResourceMaster;
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
import java.util.*;
import java.util.List;

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

    private long timeWithPauses;
    private long pauseStart;
    private long mSeconds;
    private long seconds;
    private long minutes;

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
    private final HashMap<String, TextBox> levelFinishedValues = new HashMap<>();
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
    private final TextBox time = new TextBox(WIDTH - (int) (180 * rsf), (int) (50 * rsf), (int) (180 * rsf), Color.WHITE, new Font("Calibri", Font.PLAIN, (int) (40 * rsf)), "00:00.000", 0, UIConstraints.UI_LEFT_BOUND_CONSTRAINT);

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

        int endTextX = WIDTH / 2 - (int) (overlayButtonsWidth * 1.5);
        int endValuesX = WIDTH / 2 + overlayButtonsWidth / 2;
        int width = overlayButtonsWidth * 3;
        levelFinishedTexts.put("coinsCollected", new TextBox(endTextX, HEIGHT / 2 - overlayButtonsHeight * 2, width, BasicGUIConstants.BUTTON_TEXT_COLOR, BasicGUIConstants.DEFAULT_BUTTON_FONT, "Coins collected:", 0, UIConstraints.UI_LEFT_BOUND_CONSTRAINT));
        levelFinishedTexts.put("moneyEarned", new TextBox(endTextX, HEIGHT / 2 - overlayButtonsHeight, width, BasicGUIConstants.BUTTON_TEXT_COLOR, BasicGUIConstants.DEFAULT_BUTTON_FONT, "Money earned:", 0, UIConstraints.UI_LEFT_BOUND_CONSTRAINT));
        levelFinishedTexts.put("itemsCollected", new TextBox(endTextX, HEIGHT / 2, width, BasicGUIConstants.BUTTON_TEXT_COLOR, BasicGUIConstants.DEFAULT_BUTTON_FONT, "Items collected:", 0, UIConstraints.UI_LEFT_BOUND_CONSTRAINT));
        levelFinishedTexts.put("simonSaysCorrect", new TextBox(endTextX, HEIGHT / 2 + overlayButtonsHeight, width, BasicGUIConstants.BUTTON_TEXT_COLOR, BasicGUIConstants.DEFAULT_BUTTON_FONT, "Simon Says correct:", 0, UIConstraints.UI_LEFT_BOUND_CONSTRAINT));
        levelFinishedTexts.put("timeTaken", new TextBox(endTextX, HEIGHT / 2 + overlayButtonsHeight * 2, width, BasicGUIConstants.BUTTON_TEXT_COLOR, BasicGUIConstants.DEFAULT_BUTTON_FONT, "Time elapsed:", 0, UIConstraints.UI_LEFT_BOUND_CONSTRAINT));

        levelFinishedValues.put("coinsCollected", new TextBox(endValuesX, HEIGHT / 2 - overlayButtonsHeight * 2, width, BasicGUIConstants.BUTTON_TEXT_COLOR, BasicGUIConstants.DEFAULT_BUTTON_FONT, "", 0, UIConstraints.UI_LEFT_BOUND_CONSTRAINT));
        levelFinishedValues.put("moneyEarned", new TextBox(endValuesX, HEIGHT / 2 - overlayButtonsHeight, width, BasicGUIConstants.BUTTON_TEXT_COLOR, BasicGUIConstants.DEFAULT_BUTTON_FONT, "", 0, UIConstraints.UI_LEFT_BOUND_CONSTRAINT));
        levelFinishedValues.put("itemsCollected", new TextBox(endValuesX, HEIGHT / 2, width, BasicGUIConstants.BUTTON_TEXT_COLOR, BasicGUIConstants.DEFAULT_BUTTON_FONT, "", 0, UIConstraints.UI_LEFT_BOUND_CONSTRAINT));
        levelFinishedValues.put("simonSaysCorrect", new TextBox(endValuesX, HEIGHT / 2 + overlayButtonsHeight, width, BasicGUIConstants.BUTTON_TEXT_COLOR, BasicGUIConstants.DEFAULT_BUTTON_FONT, "", 0, UIConstraints.UI_LEFT_BOUND_CONSTRAINT));
        levelFinishedValues.put("timeTaken", new TextBox(endValuesX, HEIGHT / 2 + overlayButtonsHeight * 2, width, BasicGUIConstants.BUTTON_TEXT_COLOR, BasicGUIConstants.DEFAULT_BUTTON_FONT, "", 0, UIConstraints.UI_LEFT_BOUND_CONSTRAINT));

        timeWithPauses = System.currentTimeMillis();
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

        Level.simonSaysMaster.update();
        for (SimonSays simonSays: Level.simonSaysMaster.getSimonSays()) {
            if (simonSays.isColliding()) {
                gameInterrupted = !simonSays.isCompleted();
                break;
            }
        }

        if (!gameInterrupted) {
            // ----------------------------------------------------------------- TIMER
            // updating the timer
            // preventing pauses from causing any interference with the timer
            if (pauseStart != 0) {
                timeWithPauses += System.currentTimeMillis() - pauseStart;
            }
            pauseStart = 0;
            mSeconds = (System.currentTimeMillis() - timeWithPauses) % 1000;
            seconds = (long) (Math.floor((System.currentTimeMillis() - timeWithPauses) / 1000f)) % 60;
            minutes = (long) (Math.floor((System.currentTimeMillis() - timeWithPauses) / 60000f)) % 60;
            if (seconds < 10) {
                time.setText(minutes + ":0" + seconds + "." + mSeconds);
            } else {
                time.setText(minutes + ":" + seconds + "." + mSeconds);
            }
            // -----------------------------------------------------------------

            // ----------------------------------------------------------------- PLAYER
            // handling player movement and updating player image when moving
            handleMovement();

            // updating player image when jumping
            if (!Main.player.hasVerticalCollision(Level.getCollisionBoxes(), Main.player.ySpeed + 1)) {
                Player.setCurrentPlayerImage(ResourceMaster.getImageFromMap("player_jump"));
            }
            // updating player
            Main.player.applyGravity();
            Main.player.update();

            coins.setText("$ " + Main.player.getCoins());
            Main.player.checkCoinCollision(Level.getCoins());
            // checking if the player finished the level
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

                int ssCorrect = 0;
                for (CheckBox cb:Level.simonSaysMaster.checkBoxes) {
                    if (cb.getState() == CheckBox.TICKED) {
                        ssCorrect++;
                    }
                }

                levelFinishedValues.get("coinsCollected").setText(totalCoinsCollected + " / " + totalCoinsInLevel);
                levelFinishedValues.get("moneyEarned").setText(totalMoneyEarned + " / " + totalMoneyAvailable);
                levelFinishedValues.get("itemsCollected").setText("0 / 0");
                levelFinishedValues.get("simonSaysCorrect").setText(ssCorrect + " / " + Level.simonSaysMaster.checkBoxes.length);
                if (seconds < 10) {
                    levelFinishedValues.get("timeTaken").setText(minutes + ":0" + seconds + "." + mSeconds);
                } else {
                    levelFinishedValues.get("timeTaken").setText(minutes + ":" + seconds + "." + mSeconds);
                }
            }

            // player attributes
            lives.setText(Main.player.getLives() + "x");
            // -----------------------------------------------------------------

            // ----------------------------------------------------------------- MOBS
            // updating all the mobs and their position
            // handling collisions between the mobs and the player
            for (Mob mob: Level.getMobs()) {
                mob.update();

                if (mob.hasCollisions()) {
                    if (Main.player.getPlayerRect().intersects(mob.getBounds()) && Main.player.ySpeed > 0) {
                        boolean entityWasKilled = mob.hit();
                        if (entityWasKilled) {
                            Main.player.addEntityKill(mob.getType());
                        }
                        Main.player.ySpeed = Main.player.jumpYSpeed;
                    } else if (Main.player.getPlayerRect().intersects(mob.getBounds()) && !(Main.player.ySpeed > 0)){
                        // make the player take damage
                        Main.player.removeHealth(25);

                        health.setFillLevel(Main.player.getHealth());
                        health.update();
                    }
                }
            }
            // -----------------------------------------------------------------

            // ----------------------------------------------------------------- VARIOUS CAUSES FOR INTERRUPTION
            // updating gui
            pauseButton.update();
            if (pauseButton.isButtonWasReleased() && !drawPauseMenuOverlay) {
                gameInterrupted = true;
                drawPauseMenuOverlay = true;
            }
            // checking death scenario
            if (Main.player.getY() > 2200) {
                gameInterrupted = true;
                drawDeathOverlay = true;
            }
            // checking if the player paused the game
            if (PlayerInputs.getKeysReleasedInFrame().contains(KeyEvent.VK_ESCAPE)) {
                drawPauseMenuOverlay = true;
                gameInterrupted = true;
            }
            // -----------------------------------------------------------------

            // ----------------------------------------------------------------- STOPPING TIMER
            // if an event caused the game to interrupt, the time at which the
            // interruption took place will be taken in order to calculate the
            // length of the pause
            if (gameInterrupted) {
                pauseStart = System.currentTimeMillis();
            }

            // checking if the player resumed the game
        } else if (PlayerInputs.getKeysReleasedInFrame().contains(KeyEvent.VK_ESCAPE)) {
            drawPauseMenuOverlay = false;
            gameInterrupted = false;
        }
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

    @Override
    public void render(Graphics2D g) {
        g.translate(0, HEIGHT);
        drawLevel(g);
        drawPlayer(g);

        drawMobs(g);
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

        Level.simonSaysMaster.drawSimonSaysOverlay(g);

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
        for (List<Cube> list:Level.getLevelCubes()) {
            for (Cube cube:list) {
                // simulating player movement by shifting the level to the side and keeping the player centered
                if (Main.player.getX() >= (float) (DisplayManager.getWIDTH() / 2 + Main.player.getCubeSize() / 2)) {
                    g.drawImage(ResourceMaster.getSpriteSheetFromMap("dirt_gras").getSpriteImages()[cube.getCubeID()], (int) cube.getX() * cube.getPixelSIZE() - ((int) Main.player.getX() - DisplayManager.getWIDTH() / 2 - Main.player.getCubeSize() / 2), (int) -(Level.getLevelCubes().size() - cube.getY()) * cube.getPixelSIZE(), null, null);
                } else {
                    g.drawImage(ResourceMaster.getSpriteSheetFromMap("dirt_gras").getSpriteImages()[cube.getCubeID()], (int) cube.getX() * cube.getPixelSIZE(), (int) -(Level.getLevelCubes().size() - cube.getY()) * cube.getPixelSIZE(), null, null);
                }
            }
        }

        // drawing the coins
        for (List<Coin> list:Level.getCoins()) {
            for (Coin coin:list) {
                if (!coin.isWasCollected()) {
                    if (Main.player.getX() >= (float) (DisplayManager.getWIDTH() / 2 + Main.player.getCubeSize() / 2)) {
                        switch (coin.getValue()) {
                            case 5:
                                g.drawImage(ResourceMaster.getSpriteSheetFromMap("coin_5").getSpriteImages()[(int) ((Main.currentEntityImage / 1.5) % 8)], (int) (coin.getCollisionBox().getX() - (Main.player.getX() - DisplayManager.getWIDTH() / 2 - Main.player.getCubeSize() / 2)), (int) -(Level.getLevelCubes().size() * Main.player.getCubeSize() - coin.getCollisionBox().getY()), null);
                                break;
                            case 10:
                                g.drawImage(ResourceMaster.getSpriteSheetFromMap("coin_10").getSpriteImages()[(int) ((Main.currentEntityImage / 1.5) % 8)], (int) (coin.getCollisionBox().getX() - (Main.player.getX() - DisplayManager.getWIDTH() / 2 - Main.player.getCubeSize() / 2)), (int) -(Level.getLevelCubes().size() * Main.player.getCubeSize() - coin.getCollisionBox().getY()), null);
                                break;
                            case 20:
                                g.drawImage(ResourceMaster.getSpriteSheetFromMap("coin_20").getSpriteImages()[(int) ((Main.currentEntityImage / 1.5) % 8)], (int) (coin.getCollisionBox().getX() - (Main.player.getX() - DisplayManager.getWIDTH() / 2 - Main.player.getCubeSize() / 2)), (int) -(Level.getLevelCubes().size() * Main.player.getCubeSize() - coin.getCollisionBox().getY()), null);
                                break;
                        }
                    } else {
                        switch (coin.getValue()) {
                            case 5:
                                g.drawImage(ResourceMaster.getSpriteSheetFromMap("coin_5").getSpriteImages()[(int) ((Main.currentEntityImage / 1.5) % 8)], (int) coin.getCollisionBox().getX(), (int) -(Level.getLevelCubes().size() * Main.player.getCubeSize() - coin.getCollisionBox().getY()), null);
                                break;
                            case 10:
                                g.drawImage(ResourceMaster.getSpriteSheetFromMap("coin_10").getSpriteImages()[(int) ((Main.currentEntityImage / 1.5) % 8)], (int) coin.getCollisionBox().getX(), (int) -(Level.getLevelCubes().size() * Main.player.getCubeSize() - coin.getCollisionBox().getY()), null);
                                break;
                            case 20:
                                g.drawImage(ResourceMaster.getSpriteSheetFromMap("coin_20").getSpriteImages()[(int) ((Main.currentEntityImage / 1.5) % 8)], (int) coin.getCollisionBox().getX(), (int) -(Level.getLevelCubes().size() * Main.player.getCubeSize() - coin.getCollisionBox().getY()), null);
                                break;
                        }
                    }
                }
            }
        }

        // drawing the SimonSays
        for (SimonSays simon: Level.simonSaysMaster.getSimonSays()) {
            g.setColor(BasicGUIConstants.TRANSPARENT_DARKENING_COLOR);
            if (Main.player.getX() >= (float) (DisplayManager.getWIDTH() / 2 + Main.player.getCubeSize() / 2)) {
                g.fill(new Rectangle2D.Double((int) (simon.getBounds().getX() - (Main.player.getX() - DisplayManager.getWIDTH() / 2 - Main.player.getCubeSize() / 2)), (int) -(Level.getLevelCubes().size() * Main.player.getCubeSize() - simon.getBounds().getY()), simon.getBounds().getWidth(), simon.getBounds().getHeight()));
            } else {
                g.fill(new Rectangle2D.Double((int) simon.getBounds().getX(), (int) -(Level.getLevelCubes().size() * Main.player.getCubeSize() - simon.getBounds().getY()), simon.getBounds().getWidth(), simon.getBounds().getHeight()));
                // g.drawImage();
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
        // simulating player movement by shifting the level to the side and keeping the player centered
        if (Main.player.getX() >= (float) (DisplayManager.getWIDTH()/2 + Main.player.getCubeSize()/2)){
            g.drawImage(Player.getCurrentPlayerImage(), DisplayManager.getWIDTH()/2 + Main.player.getCubeSize()/2, (int) -(Level.getLevelCubes().size() * Main.player.getCubeSize() - Main.player.getY()), null);
        } else {
            g.drawImage(Player.getCurrentPlayerImage(), (int) Main.player.getX(), (int) -(Level.getLevelCubes().size() * Main.player.getCubeSize() - Main.player.getY()), null);
        }
    }

    private void drawMobs(Graphics2D g) {
        for (Mob mob: Level.getMobs()) {
            if (Main.player.getX() >= (float) (DisplayManager.getWIDTH() / 2 + Main.player.getCubeSize() / 2)) {
                mob.draw(g, (int) (mob.getBounds().getX() - (Main.player.getX() - DisplayManager.getWIDTH() / 2 - Main.player.getCubeSize() / 2)), (int) -(Level.getLevelCubes().size() * Main.player.getCubeSize() - mob.getBounds().getY()));
            } else {
                mob.draw(g, (int) mob.getBounds().getX(), (int) -(Level.getLevelCubes().size() * Main.player.getCubeSize() - mob.getBounds().getY()));
            }
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
        if (!(Main.player.getHealth() > 0)) {
            health.setFillColor(new Color(0, 0, 0, 0));
            if (!Main.player.isDeathAnimPlaying && !Main.player.wasDeathAnimPlayed) {
                Main.player.startDeathAnim();
            }
            else if (!Main.player.isDeathAnimPlaying) {
                drawDeathOverlay = true;
                gameInterrupted = true;
            }
        }
        faiOutline.draw(g);
        livesOutline.draw(g);
        lives.draw(g);

        coins.draw(g);
        time.draw(g);
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
        // creating a semi-transparent overlay over the entire screen to hit focus on the game and switch it to buttons
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

        for (TextBox box:levelFinishedValues.values()){
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
        Main.player = new Player(Level.getSpawnLocation(), Main.player.getLives(), Main.player.getBackupCoins(), Main.player.getEntityKills());
        health.setFillColor(BasicGUIConstants.HEALTH_BAR_GREEN_COLOR);
        health.setFillLevel(Main.player.getHealth());
        health.update();
        drawDeathOverlay = false;
        drawPauseMenuOverlay = false;
        drawLevelFinishedOverlay = false;
        gameInterrupted = false;

        for (ArrayList<Coin> list:Level.getCoins()) {
            for (Coin coin:list) {
                coin.setWasCollected(false);
            }
        }

        for (Mob mob:Level.getMobs()) {
            mob.resetCollisions();
            mob.resetBounds();
        }

        mSeconds = 0;
        seconds = 0;
        minutes = 0;
        pauseStart = 0;
        timeWithPauses = System.currentTimeMillis();

        Level.simonSaysMaster.resetSimonSays();
    }
}
