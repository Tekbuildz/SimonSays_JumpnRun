package gamestates;

import dataProcessing.DataLoader;
import dataProcessing.LevelSaver;
import entities.Coin;
import SimonSays.SimonSays;
import entities.Item;
import entities.Mushroom;
import entities.mob.Mob;
import guis.CheckBox;
import dataProcessing.DataSaver;
import Resources.ResourceMaster;
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
import toolbox.BasicConstants;
import toolbox.UIConstraints;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;

/**
 *
 * GameState class handles all the updates and other actions which occur in a
 * level including drawing the objects to the screen or calling their draw-
 * function
 * <p>
 * in addition, the GameState class is used as an instance where whenever a new
 * level is started, a new GameState class is created
 * <p>
 * extends to the State class, hence overrides the update and render function
 *
 * @author Thomas Bundi
 * @version 3.5
 * @since 1.1
 */
public class GameState extends State {

    private int currentEntityImage;
    private final Level level;
    private Player player;
    private final Image[] itemImages = new Image[3];

    private boolean drawPauseMenuOverlay = false;
    private boolean drawDeathOverlay = false;
    private boolean drawLevelFinishedOverlay = false;

    private boolean gameInterrupted = false;
    private boolean saveTimeForLevel = false;

    private boolean movementLeftBoundThreshold;
    private boolean movementRightBoundThreshold;

    private final int overlayButtonsWidth = (int) (180 * BasicConstants.RSF);
    private final int overlayButtonsHeight = (int) (50 * BasicConstants.RSF);

    // ----------------- PLAYER STATS -----------------
    private int deaths;
    private final int totalSimonSays = 3;
    private int simonSaysCompleted;
    private int totalCoins;
    private int coinsCollected;
    private int totalItems;
    private int itemsCollected;
    private int totalEnemies;
    private int enemiesKilled;

    private int totalMoney;
    private int moneyCollected;

    // ----------------- TIMER -------------------
    private long timeWithPauses;
    private long pauseStart;
    private long mSeconds;
    private long seconds;
    private long minutes;
    private long totalTime;

    // ----------------- PAUSE MENU OVERLAY -----------------
    private final HashMap<String, Button> pauseMenuOverlayButtons = new HashMap<>();
    private final TriangularRectangle pauseMenuOverlayPolygon = new TriangularRectangle(DisplayManager.getWIDTH() / 2 - overlayButtonsWidth, DisplayManager.getHEIGHT() / 2 - overlayButtonsHeight * 3, overlayButtonsWidth * 2, overlayButtonsHeight * 6, (int) (20 * BasicConstants.RSF), BasicConstants.GUI_OVERLAY_DEFAULT_COLOR, Color.BLACK, 2f);

    // ----------------- DEATH OVERLAY -----------------
    private final HashMap<String, Button> deathOverlayButtons = new HashMap<>();
    private final TriangularRectangle deathOverlayPolygon = new TriangularRectangle(DisplayManager.getWIDTH() / 2 - overlayButtonsWidth, DisplayManager.getHEIGHT() / 2 - overlayButtonsHeight * 3, overlayButtonsWidth * 2, overlayButtonsHeight * 6, (int) (15 * BasicConstants.RSF), BasicConstants.GUI_OVERLAY_DEFAULT_COLOR, Color.BLACK, 2f);
    private final TextBox deathOverlayYouDied = new TextBox(DisplayManager.getWIDTH() / 2 - overlayButtonsWidth, DisplayManager.getHEIGHT() / 2 - (int) (overlayButtonsHeight * 1.5), overlayButtonsWidth * 2, Color.WHITE, new Font("Calibri", Font.PLAIN, (int) (40 * BasicConstants.RSF)), "You Died!", 5, UIConstraints.UI_CENTER_BOUND_CONSTRAINT);

    // ----------------- GAME FINISHED OVERLAY -----------------
    private final HashMap<String, Button> levelFinishedOverlayButtons = new HashMap<>();
    private final HashMap<String, TextBox> levelFinishedTexts = new HashMap<>();
    private final HashMap<String, TextBox> levelFinishedValues = new HashMap<>();
    private final TriangularRectangle levelFinishedOverlayPolygon = new TriangularRectangle(DisplayManager.getWIDTH() / 2 - overlayButtonsWidth * 2, DisplayManager.getHEIGHT() / 2 - overlayButtonsHeight * 5, overlayButtonsWidth * 4, overlayButtonsHeight * 10, (int) (20 * BasicConstants.RSF), BasicConstants.GUI_OVERLAY_DEFAULT_COLOR, Color.BLACK, 2f);

    // ----------------- GENERAL OVERLAY -----------------
    private final ButtonCircle pauseButton = new ButtonCircle((int) (20 * BasicConstants.RSF), (int) (10 * BasicConstants.RSF), (int) (60 * BasicConstants.RSF), "II");
    private final OutlinedPolygon bottomOverlay = new OutlinedPolygon(new int[]{0, (int) (280 * BasicConstants.RSF), (int) (400 * BasicConstants.RSF), DisplayManager.getWIDTH() - (int) (400 * BasicConstants.RSF), DisplayManager.getWIDTH() - (int) (280 * BasicConstants.RSF), DisplayManager.getWIDTH(), DisplayManager.getWIDTH(), 0}, new int[]{DisplayManager.getHEIGHT() - (int) (150 * BasicConstants.RSF), DisplayManager.getHEIGHT() - (int) (150 * BasicConstants.RSF), DisplayManager.getHEIGHT() - (int) (50 * BasicConstants.RSF), DisplayManager.getHEIGHT() - (int) (50 * BasicConstants.RSF), DisplayManager.getHEIGHT() - (int) (150 * BasicConstants.RSF), DisplayManager.getHEIGHT() - (int) (150 * BasicConstants.RSF), DisplayManager.getHEIGHT(), DisplayManager.getHEIGHT()}, 8, BasicConstants.GUI_OVERLAY_DEFAULT_COLOR, Color.BLACK, 3.5f);
    private final OutlinedPolygon topLeftOverlay = new OutlinedPolygon(new int[]{0, (int) (140 * BasicConstants.RSF), (int) (80 * BasicConstants.RSF), 0}, new int[]{0, 0, (int) (80 * BasicConstants.RSF), (int) (80 * BasicConstants.RSF)}, 4, BasicConstants.GUI_OVERLAY_DEFAULT_COLOR, Color.BLACK, 3.5f);
    private final OutlinedPolygon topRightOverlay = new OutlinedPolygon(new int[]{DisplayManager.getWIDTH(), DisplayManager.getWIDTH() - (int) (240 * BasicConstants.RSF), DisplayManager.getWIDTH() - (int) (180 * BasicConstants.RSF), DisplayManager.getWIDTH()}, new int[]{0, 0, (int) (80 * BasicConstants.RSF), (int) (80 * BasicConstants.RSF)}, 4, BasicConstants.GUI_OVERLAY_DEFAULT_COLOR, Color.BLACK, 3.5f);

    private final HealthBar health = new HealthBar((int) (80 * BasicConstants.RSF), DisplayManager.getHEIGHT() - (int) (110 * BasicConstants.RSF), (int) (180 * BasicConstants.RSF), (int) (30 * BasicConstants.RSF), (int) (10 * BasicConstants.RSF), BasicConstants.HEALTH_BAR_GREEN_COLOR, Color.BLACK, 4f);
    private final OutlinedEllipse faiOutline = new OutlinedEllipse((int) (40 * BasicConstants.RSF), DisplayManager.getHEIGHT() - (int) (110 * BasicConstants.RSF), (int) (30 * BasicConstants.RSF), (int) (30 * BasicConstants.RSF), BasicConstants.GUI_OVERLAY_DEFAULT_COLOR.darker(), Color.BLACK, 3f); // faiOutline = firstAidImageOutline
    private final OutlinedEllipse livesOutline = new OutlinedEllipse((int) (40 * BasicConstants.RSF), DisplayManager.getHEIGHT() - (int) (60 * BasicConstants.RSF), (int) (30 * BasicConstants.RSF), (int) (30 * BasicConstants.RSF), BasicConstants.GUI_OVERLAY_DEFAULT_COLOR.darker(), Color.BLACK, 3f);

    private final TextBox lives = new TextBox((int) (80 * BasicConstants.RSF), DisplayManager.getHEIGHT() - (int) (35 * BasicConstants.RSF), (int) (180 * BasicConstants.RSF), BasicConstants.HEALTH_BAR_GREEN_COLOR, new Font("Calibri", Font.PLAIN, (int) (30 * BasicConstants.RSF)), "", 5, UIConstraints.UI_RIGHT_BOUND_CONSTRAINT);
    private final TextBox coins = new TextBox(DisplayManager.getWIDTH() - (int) (260 * BasicConstants.RSF), DisplayManager.getHEIGHT() - (int) (60 * BasicConstants.RSF), (int) (240 * BasicConstants.RSF), BasicConstants.MONEY_YELLOW_COLOR, new Font("Calibri", Font.PLAIN, (int) (60 * BasicConstants.RSF)), "", 0, UIConstraints.UI_LEFT_BOUND_CONSTRAINT);
    private final TextBox time = new TextBox(DisplayManager.getWIDTH() - (int) (180 * BasicConstants.RSF), (int) (50 * BasicConstants.RSF), (int) (180 * BasicConstants.RSF), Color.WHITE, new Font("Calibri", Font.PLAIN, (int) (40 * BasicConstants.RSF)), "00:00.000", 0, UIConstraints.UI_LEFT_BOUND_CONSTRAINT);

    /**
     *
     * constructor of the GameState class
     * creating and adding buttons of this state to an ArrayList and giving
     * them a default font
     */
    public GameState(Level level) {
        this.level = level;
        player = new Player(level.getSpawnLocation(), DataLoader.getLives(), DataLoader.getCoins(), DataLoader.getEntityKills(), level.getLevelLoader());
        coins.setText("$ " + player.getCoins());

        // adding images to image-array to make randomizing the image displayed as the item easier
        itemImages[0] = ResourceMaster.getImageFromMap("led");
        itemImages[1] = ResourceMaster.getImageFromMap("pcb");
        itemImages[2] = ResourceMaster.getImageFromMap("screw");

        pauseButton.setTextFont(new Font("Calibri", Font.PLAIN, (int) (40 * BasicConstants.RSF)));

        // ----------------- PAUSE MENU OVERLAY -----------------
        pauseMenuOverlayButtons.put("continueButton", new ButtonTriangularRectangle(DisplayManager.getWIDTH() / 2 - overlayButtonsWidth / 2, DisplayManager.getHEIGHT() / 2 - overlayButtonsHeight * 2, overlayButtonsWidth, overlayButtonsHeight, (int) (10 * BasicConstants.RSF), "Continue"));
        pauseMenuOverlayButtons.put("restartButton", new ButtonTriangularRectangle(DisplayManager.getWIDTH() / 2 - overlayButtonsWidth / 2, DisplayManager.getHEIGHT() / 2 - overlayButtonsHeight / 2, overlayButtonsWidth, overlayButtonsHeight, (int) (10 * BasicConstants.RSF), "Restart"));
        pauseMenuOverlayButtons.put("exitGameButton", new ButtonTriangularRectangle(DisplayManager.getWIDTH() / 2 - overlayButtonsWidth / 2, DisplayManager.getHEIGHT() / 2 + overlayButtonsHeight, overlayButtonsWidth, overlayButtonsHeight, (int) (10 * BasicConstants.RSF), "Exit Game"));
        for (Button button: pauseMenuOverlayButtons.values()) {
            button.setTextFont(BasicConstants.DEFAULT_BUTTON_FONT);
        }

        // ----------------- DEATH OVERLAY -----------------
        deathOverlayButtons.put("restartButton", new ButtonTriangularRectangle(DisplayManager.getWIDTH() / 2 - overlayButtonsWidth / 2, DisplayManager.getHEIGHT() / 2 - overlayButtonsHeight / 2, overlayButtonsWidth, overlayButtonsHeight, (int) (10 * BasicConstants.RSF), "Restart"));
        deathOverlayButtons.put("exitGameDeathButton", new ButtonTriangularRectangle(DisplayManager.getWIDTH() / 2 - overlayButtonsWidth / 2, DisplayManager.getHEIGHT() / 2 + overlayButtonsHeight, overlayButtonsWidth, overlayButtonsHeight, (int) (10 * BasicConstants.RSF), "Titlescreen"));
        for (Button button: deathOverlayButtons.values()) {
            button.setTextFont(BasicConstants.DEFAULT_BUTTON_FONT);
        }

        // ----------------- GAME FINISHED OVERLAY -----------------
        levelFinishedOverlayButtons.put("restartButton", new ButtonTriangularRectangle(DisplayManager.getWIDTH() / 2 - (int) (overlayButtonsWidth * 1.5), DisplayManager.getHEIGHT() / 2 + (int) (overlayButtonsHeight * 3.5), overlayButtonsWidth, overlayButtonsHeight, (int) (10 * BasicConstants.RSF), "Restart"));
        levelFinishedOverlayButtons.put("continueButton", new ButtonTriangularRectangle(DisplayManager.getWIDTH() / 2 + (int) (overlayButtonsWidth * 0.5), DisplayManager.getHEIGHT() / 2 + (int) (overlayButtonsHeight * 3.5), overlayButtonsWidth, overlayButtonsHeight, (int) (10 * BasicConstants.RSF), "Continue"));
        for (Button button: levelFinishedOverlayButtons.values()) {
            button.setTextFont(BasicConstants.DEFAULT_BUTTON_FONT);
        }
        levelFinishedTexts.put("wellDone", new TextBox(DisplayManager.getWIDTH() / 2 - overlayButtonsWidth * 2, DisplayManager.getHEIGHT() / 2 - (int) (overlayButtonsHeight * 3.5), overlayButtonsWidth * 4, BasicConstants.BUTTON_TEXT_COLOR, new Font("Calibri", Font.PLAIN, (int) (50 * BasicConstants.RSF)), "Well Done!", 0, UIConstraints.UI_CENTER_BOUND_CONSTRAINT));

        int endTextX = DisplayManager.getWIDTH() / 2 - (int) (overlayButtonsWidth * 1.5);
        int endTextY = DisplayManager.getHEIGHT() / 2 - 20;
        int endValuesX = DisplayManager.getWIDTH() / 2 + overlayButtonsWidth / 2;
        int width = overlayButtonsWidth * 3;
        levelFinishedTexts.put("coinsCollected", new TextBox(endTextX, endTextY - overlayButtonsHeight * 2, width, BasicConstants.BUTTON_TEXT_COLOR, BasicConstants.DEFAULT_BUTTON_FONT, "Coins collected:", 0, UIConstraints.UI_LEFT_BOUND_CONSTRAINT));
        levelFinishedTexts.put("moneyEarned", new TextBox(endTextX, endTextY - overlayButtonsHeight, width, BasicConstants.BUTTON_TEXT_COLOR, BasicConstants.DEFAULT_BUTTON_FONT, "Money earned:", 0, UIConstraints.UI_LEFT_BOUND_CONSTRAINT));
        levelFinishedTexts.put("itemsCollected", new TextBox(endTextX, endTextY, width, BasicConstants.BUTTON_TEXT_COLOR, BasicConstants.DEFAULT_BUTTON_FONT, "Items collected:", 0, UIConstraints.UI_LEFT_BOUND_CONSTRAINT));
        levelFinishedTexts.put("simonSaysCorrect", new TextBox(endTextX, endTextY + overlayButtonsHeight, width, BasicConstants.BUTTON_TEXT_COLOR, BasicConstants.DEFAULT_BUTTON_FONT, "Simon Says correct:", 0, UIConstraints.UI_LEFT_BOUND_CONSTRAINT));
        levelFinishedTexts.put("enemiesKilled", new TextBox(endTextX, endTextY + overlayButtonsHeight * 2, width, BasicConstants.BUTTON_TEXT_COLOR, BasicConstants.DEFAULT_BUTTON_FONT, "Enemies killed:", 0, UIConstraints.UI_LEFT_BOUND_CONSTRAINT));
        levelFinishedTexts.put("timeTaken", new TextBox(endTextX, endTextY + overlayButtonsHeight * 3, width, BasicConstants.BUTTON_TEXT_COLOR, BasicConstants.DEFAULT_BUTTON_FONT, "Time elapsed:", 0, UIConstraints.UI_LEFT_BOUND_CONSTRAINT));

        levelFinishedValues.put("coinsCollected", new TextBox(endValuesX, endTextY - overlayButtonsHeight * 2, width, BasicConstants.BUTTON_TEXT_COLOR, BasicConstants.DEFAULT_BUTTON_FONT, "", 0, UIConstraints.UI_LEFT_BOUND_CONSTRAINT));
        levelFinishedValues.put("moneyEarned", new TextBox(endValuesX, endTextY - overlayButtonsHeight, width, BasicConstants.BUTTON_TEXT_COLOR, BasicConstants.DEFAULT_BUTTON_FONT, "", 0, UIConstraints.UI_LEFT_BOUND_CONSTRAINT));
        levelFinishedValues.put("itemsCollected", new TextBox(endValuesX, endTextY, width, BasicConstants.BUTTON_TEXT_COLOR, BasicConstants.DEFAULT_BUTTON_FONT, "", 0, UIConstraints.UI_LEFT_BOUND_CONSTRAINT));
        levelFinishedValues.put("simonSaysCorrect", new TextBox(endValuesX, endTextY + overlayButtonsHeight, width, BasicConstants.BUTTON_TEXT_COLOR, BasicConstants.DEFAULT_BUTTON_FONT, "", 0, UIConstraints.UI_LEFT_BOUND_CONSTRAINT));
        levelFinishedValues.put("enemiesKilled", new TextBox(endValuesX, endTextY + overlayButtonsHeight * 2, width, BasicConstants.BUTTON_TEXT_COLOR, BasicConstants.DEFAULT_BUTTON_FONT, "", 0, UIConstraints.UI_LEFT_BOUND_CONSTRAINT));
        levelFinishedValues.put("timeTaken", new TextBox(endValuesX, endTextY + overlayButtonsHeight * 3, width, BasicConstants.BUTTON_TEXT_COLOR, BasicConstants.DEFAULT_BUTTON_FONT, "", 0, UIConstraints.UI_LEFT_BOUND_CONSTRAINT));

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
                            resetLevel();
                            StateMaster.setState(new LevelSelectionState());
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
                            StateMaster.setState(new MainMenuState());
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

                                /*
                                scaling factors of the faults:
                                faults: 15
                                simon says faults: 15
                                coin faults: 1 (value is neglected)
                                item faults: 10
                                mobs: 5
                                 */

                            int totalFaults = deaths * 15 + (totalSimonSays - simonSaysCompleted) * 15 + (totalCoins - coinsCollected) + (totalItems - itemsCollected) * 10 + (totalEnemies - enemiesKilled) * 5;
                            LevelSaver.init(BasicConstants.DEFAULT_PATH + "leveldata/" + level.level + ".xml", totalTime, deaths, totalSimonSays - simonSaysCompleted, totalCoins - coinsCollected, totalItems - itemsCollected, totalEnemies - enemiesKilled, totalFaults);

                            DataLoader.loadPlayerData();
                            resetLevel();
                            StateMaster.setState(new LevelSelectionState());
                            break;
                    }
                }
            }
        }

        level.getSimonSaysMaster().update(player);
        for (SimonSays simonSays: level.getSimonSaysMaster().getSimonSays()) {
            if (simonSays.isColliding()) {
                gameInterrupted = !simonSays.isCompleted();
                break;
            }
        }

        if (!gameInterrupted) {
            // ----------------------------------------------------------------- TIMER
            this.currentEntityImage = Main.currentEntityImage;
            // updating the timer
            // preventing pauses from causing any interference with the timer
            if (pauseStart != 0) {
                timeWithPauses += System.currentTimeMillis() - pauseStart;
            }
            pauseStart = 0;
            mSeconds = (System.currentTimeMillis() - timeWithPauses) % 1000;
            seconds = (long) (Math.floor((System.currentTimeMillis() - timeWithPauses) / 1000f)) % 60;
            minutes = (long) (Math.floor((System.currentTimeMillis() - timeWithPauses) / 60000f)) % 60;
            totalTime = System.currentTimeMillis() - timeWithPauses;
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
            if (!player.hasVerticalCollision(level.getCollisionBoxes(), player.ySpeed + 1)) {
                Player.setCurrentPlayerImage(ResourceMaster.getImageFromMap("player_jump"));
            }
            // updating player
            player.applyGravity();
            player.update();

            coins.setText("$ " + player.getCoins());
            player.checkCoinCollision(level.getCoins());
            player.checkItemCollision(level.getItems());

            movementLeftBoundThreshold = player.getX() >= (float) (DisplayManager.getWIDTH() / 2 + player.getCubeSize() / 2) && player.getX() < (float) (level.getLevelCubes().get(0).size() * player.getCubeSize() - DisplayManager.getWIDTH() / 2 + player.getCubeSize() / 2);
            movementRightBoundThreshold = player.getX() >= (float) (level.getLevelCubes().get(0).size() * player.getCubeSize() - DisplayManager.getWIDTH() / 2 + player.getCubeSize() / 2);
            // checking if the player finished the level
            if (level.getFinish().intersects(player.getPlayerRect()) && !drawLevelFinishedOverlay) {
                levelFinishedCalculations();
            }

            // player attributes
            lives.setText(player.getLives() + "x");
            // -----------------------------------------------------------------

            // ----------------------------------------------------------------- MOBS
            // updating all the mobs and their position
            // handling collisions between the mobs and the player
            for (Mob mob: level.getMobs()) {
                mob.update(level.getCollisionBoxes());

                if (mob.hasCollisions()) {
                    if (player.getPlayerRect().intersects(mob.getBounds()) && player.ySpeed > 0) {
                        boolean entityWasKilled = mob.hit();
                        if (entityWasKilled) {
                            player.addEntityKill(mob.getType());
                        }
                        player.ySpeed = player.jumpYSpeed;
                    } else if (player.getPlayerRect().intersects(mob.getBounds()) && player.ySpeed <= 0){
                        // make the player take damage
                        if (mob.getType().equals("snail")) {
                            player.removeHealth(10);
                        } else if (mob.getType().equals("wolf")) {
                            player.removeHealth(15);
                        }

                        health.setFillLevel(player.getHealth());
                        health.update();
                    }
                }
            }

            for (Mushroom mushroom:level.getMushrooms()) {
                mushroom.update();

                if (player.getPlayerRect().intersects(mushroom.getHitBox())) {
                    if (player.ySpeed > 0) {
                        if (mushroom.isIdleUp()) {
                            player.ySpeed = -6f;
                            mushroom.startSquishAnimation();
                        }
                    } else {
                        player.removeHealth(5);

                        health.setFillLevel(player.getHealth());
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
            if (player.getY() > level.getLevelCubes().size() * player.getCubeSize()) {
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
        player.move('n');
        // moving upon key press accordingly
        if (PlayerInputs.getKeysPressedInFrame().contains(KeyEvent.VK_A) || PlayerInputs.getKeysPressedInFrame().contains(KeyEvent.VK_LEFT)) {
            player.move('l');
        }
        if (PlayerInputs.getKeysPressedInFrame().contains(KeyEvent.VK_D) || PlayerInputs.getKeysPressedInFrame().contains(KeyEvent.VK_RIGHT)) {
            player.move('r');
        }
        if (PlayerInputs.getKeysPressedInFrame().contains(KeyEvent.VK_SPACE)) {
            player.jump();
        }
    }

    @Override
    public void render(Graphics2D g) {
        g.drawImage(ResourceMaster.getImageFromMap("game_background"), 0, 0, null);
        g.translate(0, DisplayManager.getHEIGHT());
        drawLevel(g);
        drawPlayer(g);

        drawMobs(g);
        // using the translate-function with "-DisplayManager.getHEIGHT()" because the origin is always viewed from the current origin
        // of the current coordinate system which is located at 0, DisplayManager.getHEIGHT() (viewed from the actual screen => not visible)
        // in order to change the coordinate system back from the bottom left to the top left of the screen, the
        // height of the screen needs to be subtracted
        g.translate(0, -DisplayManager.getHEIGHT());

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

        level.getSimonSaysMaster().drawSimonSaysOverlay(g);

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    }

    /**
     *
     * drawing all the level-rectangles, all the coins, items, SimonSays
     * and mushrooms
     *
     * @param g - the graphics object used to paint onto the screen
     */
    private void drawLevel(Graphics2D g) {
        // drawing the level rectangles
        for (List<Cube> list:level.getLevelCubes()) {
            for (Cube cube:list) {
                if (cube.getCubeID() == -1) {
                    continue;
                }
                // simulating player movement by shifting the level to the side and keeping the player centered
                if (movementLeftBoundThreshold) {
                    g.drawImage(ResourceMaster.getSpriteSheetFromMap("dirt_gras").getSpriteImages()[cube.getCubeID()], (int) cube.getX() * cube.getPixelSIZE() - ((int) player.getX() - DisplayManager.getWIDTH() / 2 - player.getCubeSize() / 2), (int) -(level.getLevelCubes().size() - cube.getY()) * cube.getPixelSIZE(), null, null);
                // continue player movement when level reached the end
                } else if (movementRightBoundThreshold) {
                    g.drawImage(ResourceMaster.getSpriteSheetFromMap("dirt_gras").getSpriteImages()[cube.getCubeID()], (int) ((cube.getX() - (level.getLevelCubes().get(0).size() - 48)) * cube.getPixelSIZE()), (int) -(level.getLevelCubes().size() - cube.getY()) * cube.getPixelSIZE(), null, null);
                } else {
                    g.drawImage(ResourceMaster.getSpriteSheetFromMap("dirt_gras").getSpriteImages()[cube.getCubeID()], (int) cube.getX() * cube.getPixelSIZE(), (int) -(level.getLevelCubes().size() - cube.getY()) * cube.getPixelSIZE(), null, null);
                }
            }
        }

        // drawing the coins
        for (List<Coin> list:level.getCoins()) {
            for (Coin coin:list) {
                if (!coin.isWasCollected()) {
                    if (movementLeftBoundThreshold) {
                        switch (coin.getValue()) {
                            case 5:
                                g.drawImage(ResourceMaster.getSpriteSheetFromMap("coin_5").getSpriteImages()[(int) ((this.currentEntityImage / 1.5) % 8)], (int) (coin.getBounds().getX() - (player.getX() - DisplayManager.getWIDTH() / 2 - player.getCubeSize() / 2)), (int) -(level.getLevelCubes().size() * player.getCubeSize() - coin.getBounds().getY()), null);
                                break;
                            case 10:
                                g.drawImage(ResourceMaster.getSpriteSheetFromMap("coin_10").getSpriteImages()[(int) ((this.currentEntityImage / 1.5) % 8)], (int) (coin.getBounds().getX() - (player.getX() - DisplayManager.getWIDTH() / 2 - player.getCubeSize() / 2)), (int) -(level.getLevelCubes().size() * player.getCubeSize() - coin.getBounds().getY()), null);
                                break;
                            case 20:
                                g.drawImage(ResourceMaster.getSpriteSheetFromMap("coin_20").getSpriteImages()[(int) ((this.currentEntityImage / 1.5) % 8)], (int) (coin.getBounds().getX() - (player.getX() - DisplayManager.getWIDTH() / 2 - player.getCubeSize() / 2)), (int) -(level.getLevelCubes().size() * player.getCubeSize() - coin.getBounds().getY()), null);
                                break;
                        }
                    } else if (movementRightBoundThreshold) {
                        switch (coin.getValue()) {
                            case 5:
                                g.drawImage(ResourceMaster.getSpriteSheetFromMap("coin_5").getSpriteImages()[(int) ((this.currentEntityImage / 1.5) % 8)], (int) (coin.getBounds().getX() - (level.getLevelCubes().get(0).size() - 48) * player.getCubeSize()), (int) -(level.getLevelCubes().size() * player.getCubeSize() - coin.getBounds().getY()), null);
                                break;
                            case 10:
                                g.drawImage(ResourceMaster.getSpriteSheetFromMap("coin_10").getSpriteImages()[(int) ((this.currentEntityImage / 1.5) % 8)], (int) (coin.getBounds().getX() - (level.getLevelCubes().get(0).size() - 48) * player.getCubeSize()), (int) -(level.getLevelCubes().size() * player.getCubeSize() - coin.getBounds().getY()), null);
                                break;
                            case 20:
                                g.drawImage(ResourceMaster.getSpriteSheetFromMap("coin_20").getSpriteImages()[(int) ((this.currentEntityImage / 1.5) % 8)], (int) (coin.getBounds().getX() - (level.getLevelCubes().get(0).size() - 48) * player.getCubeSize()), (int) -(level.getLevelCubes().size() * player.getCubeSize() - coin.getBounds().getY()), null);
                                break;
                        }
                    } else {
                        switch (coin.getValue()) {
                            case 5:
                                g.drawImage(ResourceMaster.getSpriteSheetFromMap("coin_5").getSpriteImages()[(int) ((this.currentEntityImage / 1.5) % 8)], (int) coin.getBounds().getX(), (int) -(level.getLevelCubes().size() * player.getCubeSize() - coin.getBounds().getY()), null);
                                break;
                            case 10:
                                g.drawImage(ResourceMaster.getSpriteSheetFromMap("coin_10").getSpriteImages()[(int) ((this.currentEntityImage / 1.5) % 8)], (int) coin.getBounds().getX(), (int) -(level.getLevelCubes().size() * player.getCubeSize() - coin.getBounds().getY()), null);
                                break;
                            case 20:
                                g.drawImage(ResourceMaster.getSpriteSheetFromMap("coin_20").getSpriteImages()[(int) ((this.currentEntityImage / 1.5) % 8)], (int) coin.getBounds().getX(), (int) -(level.getLevelCubes().size() * player.getCubeSize() - coin.getBounds().getY()), null);
                                break;
                        }
                    }
                }
            }
        }

        // drawing the items
        for (Item item:level.getItems()) {
            if (!item.isWasCollected()) {
                int itemYPos = (int) -(level.getLevelCubes().size() * player.getCubeSize() - item.getBounds().getY() + Math.sin(this.currentEntityImage / 8f) * 10);
                if (movementLeftBoundThreshold) {
                    g.drawImage(itemImages[item.getImageArrayIndex()], (int) (item.getBounds().getX() - (player.getX() - DisplayManager.getWIDTH() / 2 - player.getCubeSize() / 2)), itemYPos, null);
                } else if (movementRightBoundThreshold) {
                    g.drawImage(itemImages[item.getImageArrayIndex()], (int) (item.getBounds().getX() - (level.getLevelCubes().get(0).size() - 48) * player.getCubeSize()), itemYPos, null);
                } else {
                    g.drawImage(itemImages[item.getImageArrayIndex()], (int) item.getBounds().getX(), itemYPos, null);
                }
            }
        }

        // drawing the SimonSays
        for (SimonSays simon: level.getSimonSaysMaster().getSimonSays()) {
            g.setColor(BasicConstants.TRANSPARENT_DARKENING_COLOR);
            if (movementLeftBoundThreshold) {
                g.drawImage(ResourceMaster.getImageFromMap("simon_says"), (int) (simon.getBounds().getX() - (player.getX() - DisplayManager.getWIDTH() / 2 - player.getCubeSize() / 2)), (int) -(level.getLevelCubes().size() * player.getCubeSize() - simon.getBounds().getY()), null);
            } else if (movementRightBoundThreshold) {
                g.drawImage(ResourceMaster.getImageFromMap("simon_says"), (int) (simon.getBounds().getX() - (level.getLevelCubes().get(0).size() - 48) * player.getCubeSize()), (int) -(level.getLevelCubes().size() * player.getCubeSize() - simon.getBounds().getY()), null);
            } else {
                g.drawImage(ResourceMaster.getImageFromMap("simon_says"), (int) simon.getBounds().getX(), (int) -(level.getLevelCubes().size() * player.getCubeSize() - simon.getBounds().getY()), null);
           }
        }

        // drawing the mushrooms
        for (Mushroom mushroom: level.getMushrooms()) {
            if (movementLeftBoundThreshold) {
                mushroom.draw(g, (int) (mushroom.getxImage() - (player.getX() - DisplayManager.getWIDTH() / 2 - player.getCubeSize() / 2)), -(level.getLevelCubes().size() * player.getCubeSize() - mushroom.getyImage()));
            } else if (movementRightBoundThreshold) {
                mushroom.draw(g, mushroom.getxImage() - (level.getLevelCubes().get(0).size() - 48) * player.getCubeSize(), -(level.getLevelCubes().size() * player.getCubeSize() - mushroom.getyImage()));
            } else {
                mushroom.draw(g, mushroom.getxImage(), -(level.getLevelCubes().size() * player.getCubeSize() - mushroom.getyImage()));
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
        if (movementLeftBoundThreshold){
            g.drawImage(Player.getCurrentPlayerImage(), DisplayManager.getWIDTH()/2 + player.getCubeSize()/2, (int) -(level.getLevelCubes().size() * player.getCubeSize() - player.getY()), null);
        } else if (movementRightBoundThreshold) {
            g.drawImage(Player.getCurrentPlayerImage(), (int) (player.getX() - (level.getLevelCubes().get(0).size() * player.getCubeSize() - DisplayManager.getWIDTH())), (int) -(level.getLevelCubes().size() * player.getCubeSize() - player.getY()), null);
        } else {
            g.drawImage(Player.getCurrentPlayerImage(), (int) player.getX(), (int) -(level.getLevelCubes().size() * player.getCubeSize() - player.getY()), null);
        }
    }

    /**
     *
     * drawing the mobs to the screen
     *
     * @param g - the graphics object used to paint onto the screen
     */
    private void drawMobs(Graphics2D g) {
        for (Mob mob: level.getMobs()) {
            if (movementLeftBoundThreshold) {
                mob.draw(g, (int) (mob.getBounds().getX() - (player.getX() - DisplayManager.getWIDTH() / 2 - player.getCubeSize() / 2)), (int) -(level.getLevelCubes().size() * player.getCubeSize() - mob.getBounds().getY()));
            } else if (movementRightBoundThreshold) {
                mob.draw(g, (int) (mob.getBounds().getX() - (level.getLevelCubes().get(0).size() - 48) * player.getCubeSize()), (int) -(level.getLevelCubes().size() * player.getCubeSize() - mob.getBounds().getY()));
            } else {
                mob.draw(g, (int) mob.getBounds().getX(), (int) -(level.getLevelCubes().size() * player.getCubeSize() - mob.getBounds().getY()));
            }
        }
    }

    /**
     *
     * drawing all the overlays for the basic level UI
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
        if (!(player.getHealth() > 0)) {
            health.setFillColor(new Color(0, 0, 0, 0));
            if (!player.isDeathAnimPlaying && !player.wasDeathAnimPlayed) {
                player.startDeathAnim();
            }
            else if (!player.isDeathAnimPlaying) {
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
        g.setColor(BasicConstants.TRANSPARENT_DARKENING_COLOR);
        g.fillRect(0, 0, DisplayManager.getWIDTH(), DisplayManager.getHEIGHT());

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
        g.setColor(BasicConstants.TRANSPARENT_DARKENING_COLOR);
        g.fillRect(0, 0, DisplayManager.getWIDTH(), DisplayManager.getHEIGHT());

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
        g.setColor(BasicConstants.TRANSPARENT_DARKENING_COLOR);
        g.fillRect(0, 0, DisplayManager.getWIDTH(), DisplayManager.getHEIGHT());

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
        player = new Player(level.getSpawnLocation(), player.getLives(), player.getBackupCoins(), player.getEntityKills(), level.getLevelLoader());
        health.setFillColor(BasicConstants.HEALTH_BAR_GREEN_COLOR);
        health.setFillLevel(player.getHealth());
        health.update();
        drawDeathOverlay = false;
        drawPauseMenuOverlay = false;
        drawLevelFinishedOverlay = false;
        gameInterrupted = false;

        for (ArrayList<Coin> list:level.getCoins()) {
            for (Coin coin:list) {
                coin.setWasCollected(false);
            }
        }

        for (Mob mob:level.getMobs()) {
            mob.resetCollisions();
            mob.resetBounds();
            mob.resetHealth();
        }

        for (Item item:level.getItems()) {
            item.setWasCollected(false);
        }

        for (Mushroom mushroom:level.getMushrooms()) {
            mushroom.resetMushroom();
        }

        mSeconds = 0;
        seconds = 0;
        minutes = 0;
        pauseStart = 0;
        timeWithPauses = System.currentTimeMillis();

        level.getSimonSaysMaster().resetSimonSays();
    }

    private void levelFinishedCalculations() {
        gameInterrupted = true;
        drawLevelFinishedOverlay = true;


        // ----------------------- RESETTING STATS -----------------------
        deaths = 0; // TODO: will be removed and replaced when checkpoints and lives are added
        simonSaysCompleted = 0;
        totalCoins = 0;
        coinsCollected = 0;
        totalMoney = 0;
        moneyCollected = 0;
        totalItems = level.getItems().size();
        itemsCollected = 0;
        totalEnemies = 0;
        enemiesKilled = 0;

        for (ArrayList<Coin> list:level.getCoins()) {
            totalCoins += list.size();
            for (Coin coin:list) {
                totalMoney += coin.getValue();
                if (coin.isWasCollected()) {
                    coinsCollected++;
                    moneyCollected += coin.getValue();
                }
            }
        }

        for (Mob mob:level.getMobs()) {
            if (!mob.hasCollisions()) {
                enemiesKilled++;
            }
            totalEnemies++;
        }

        for (CheckBox cb:level.getSimonSaysMaster().checkBoxes) {
            if (cb.getState() == CheckBox.TICKED) {
                simonSaysCompleted++;
            }
        }

        levelFinishedValues.get("coinsCollected").setText(coinsCollected + " / " + totalCoins);
        levelFinishedValues.get("moneyEarned").setText(moneyCollected + " / " + totalMoney);
        levelFinishedValues.get("itemsCollected").setText(player.getNumberOfItemsCollected() + " / " + totalItems);
        levelFinishedValues.get("simonSaysCorrect").setText(simonSaysCompleted + " / " + totalSimonSays);
        levelFinishedValues.get("enemiesKilled").setText(enemiesKilled + " / " + totalEnemies);
        if (seconds < 10) {
            levelFinishedValues.get("timeTaken").setText(minutes + ":0" + seconds + "." + mSeconds);
        } else {
            levelFinishedValues.get("timeTaken").setText(minutes + ":" + seconds + "." + mSeconds);
        }

        // only if all the items etc. were collected, the time for the level is saved
//        if (coinsCollected == totalCoins && player.getNumberOfItemsCollected() == totalItems && simonSaysCompleted == totalSimonSays && enemiesKilled == totalEnemies) {
//            saveTimeForLevel = true;
//        }
    }
}
