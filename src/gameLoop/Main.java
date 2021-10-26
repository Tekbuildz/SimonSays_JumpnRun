package gameLoop;

import dataProcessing.DataLoader;
import Resources.ResourceMaster;
import Resources.SpriteSheet;
import display.DisplayManager;
import display.Renderer;
import gamestates.MainMenuState;
import gamestates.StateMaster;
import player.PlayerInputs;
import toolbox.BasicConstants;
import toolbox.ImageProcessing;

import static toolbox.BasicConstants.DEFAULT_PATH;

/**
 *
 * Main class contains the main game loop of the game, loads all the images
 * to memory and calls the repaint function of the Renderer class
 *
 * @author Thomas Bundi
 * @version 2.7
 * @since 0.1
 */
public class Main implements Runnable{

    private Renderer renderer;

    private static final boolean running = true;

    public static long timer;
    public static int currentEntityImage;

    /**
     *
     * basic constructor calling the setup() function
     */
    public Main() {
        setup();
    }

    public static void main(String[] args) {
        new Main();
    }

    /**
     *
     * setting up the display
     * creating a new player,
     * a new thread and setting the current state
     */
    private void setup() {
        loadAllResources();
        DataLoader.loadPlayerData("player");

        renderer = new Renderer(DisplayManager.getWIDTH(), DisplayManager.getHEIGHT());

        DisplayManager display = new DisplayManager();
        display.createDisplay(renderer);

        Thread thread = new Thread(this);
        thread.start();

        StateMaster.setState(new MainMenuState());
    }

    /**
     *
     * loads all sprite sheets and images required for the game to a map
     */
    public static void loadAllResources() {
        ResourceMaster.addImageToMap("title_screen_background", ImageProcessing.loadImage(DEFAULT_PATH + "images/titleScreenBackground.png"));
        ResourceMaster.addImageToMap("all_items", ImageProcessing.loadImage(DEFAULT_PATH + "images/allitems.png"));
        ResourceMaster.addImageToMap("ss_ui", ImageProcessing.loadImage(DEFAULT_PATH + "images/SS_UI.png"));

        // ------------------------------------------------------------------------------------------------------------- IMAGES FOR GAMESTATE
        ResourceMaster.addImageToMap("player_jump", ImageProcessing.loadImage(DEFAULT_PATH + "images/player_jump.png"));
        ResourceMaster.addImageToMap("player_idle", ImageProcessing.loadImage(DEFAULT_PATH + "images/player_idle.png"));
        ResourceMaster.addImageToMap("led", ImageProcessing.loadImage(DEFAULT_PATH + "images/item_led.png"));
        ResourceMaster.addImageToMap("pcb", ImageProcessing.loadImage(DEFAULT_PATH + "images/item_pcb.png"));
        ResourceMaster.addImageToMap("screw", ImageProcessing.loadImage(DEFAULT_PATH + "images/item_screw.png"));
        ResourceMaster.addImageToMap("simon_says", ImageProcessing.loadImage(DEFAULT_PATH + "images/simon_says.png"));
        ResourceMaster.addImageToMap("game_background", ImageProcessing.loadImage(DEFAULT_PATH + "images/game_background.png"));
        ResourceMaster.addImageToMap("mushroom_idle_up", ImageProcessing.loadImage(DEFAULT_PATH + "images/mushroom_idle_up.png"));
        ResourceMaster.addImageToMap("mushroom_idle_down", ImageProcessing.loadImage(DEFAULT_PATH + "images/mushroom_idle_down.png"));

        ResourceMaster.addSpriteSheetToMap("coin_5", new SpriteSheet(DEFAULT_PATH + "spritesheets/coin_5_spritesheet.png", 40, 40));
        ResourceMaster.addSpriteSheetToMap("coin_10", new SpriteSheet(DEFAULT_PATH + "spritesheets/coin_10_spritesheet.png", 40, 40));
        ResourceMaster.addSpriteSheetToMap("coin_20", new SpriteSheet(DEFAULT_PATH + "spritesheets/coin_20_spritesheet.png", 40, 40));
        ResourceMaster.addSpriteSheetToMap("player_die", new SpriteSheet(DEFAULT_PATH + "spritesheets/player_die_spritesheet.png", 40, 60));
        ResourceMaster.addSpriteSheetToMap("player_walk", new SpriteSheet(DEFAULT_PATH + "spritesheets/player_walk_spritesheet.png", 40, 60));
        ResourceMaster.addSpriteSheetToMap("snail_walk", new SpriteSheet(DEFAULT_PATH + "spritesheets/snail_walk_spritesheet.png", 60, 25));
        ResourceMaster.addSpriteSheetToMap("wolf_walk", new SpriteSheet(DEFAULT_PATH + "spritesheets/wolf_walk_spritesheet.png", 80, 44));
        ResourceMaster.addSpriteSheetToMap("dirt_gras", new SpriteSheet(DEFAULT_PATH + "spritesheets/blocktiles.png", 40, 40));
        ResourceMaster.addSpriteSheetToMap("mushroom_squish", new SpriteSheet(DEFAULT_PATH + "spritesheets/mushroom_squish_spritesheet.png", 40, 40));
        ResourceMaster.addSpriteSheetToMap("mushroom_desquish", new SpriteSheet(DEFAULT_PATH + "spritesheets/mushroom_desquish_spritesheet.png", 40, 40));
        // -------------------------------------------------------------------------------------------------------------
    }

    /**
     *
     * updating the game logic of the currently active state
     * updating any inputs from the player which are
     * required all the time such as mouse and keyboard inputs
     */
    private void update() {
        // updating all the game logic
        PlayerInputs.updateMousePos();
        if (StateMaster.getState() != null) {
            StateMaster.getState().update();
        }

        // last to update is to clear all the keys from the arraylist containing all the keys removed in this frame/tick
        PlayerInputs.updateKeysReleased();
    }

    // main game loop
    // changed version of the second loop found here: https://gamedev.stackexchange.com/questions/160329/java-game-loop-efficiency

    /**
     *
     * main game loop updates 120 times per second (amountOfTicks)
     */
    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 120.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        timer = System.currentTimeMillis();
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                update();
                delta--;
            }
            if (running)
                renderer.repaint();

            // animation updater
            if (System.currentTimeMillis() - timer > 40) {
                timer += 40;

                currentEntityImage++;
            }
        }
    }
}
