package gameLoop;

import Loader.DataLoader;
import Loader.ImageLoader;
import SpriteSheet.ResourceMaster;
import SpriteSheet.SpriteSheet;
import display.DisplayManager;
import display.Renderer;
import gamestates.MainMenuState;
import gamestates.StateMaster;
import player.PlayerInputs;

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
        ResourceMaster.addImageToMap("title_screen_background", ImageLoader.loadImage("res\\images\\titleScreenBackground.png"));
        ResourceMaster.addImageToMap("all_items", ImageLoader.loadImage("res\\images\\allItems.png"));
        ResourceMaster.addImageToMap("ss_ui", ImageLoader.loadImage("res\\images\\SS_UI.png"));

        // ------------------------------------------------------------------------------------------------------------- IMAGES FOR GAMESTATE
        ResourceMaster.addImageToMap("player_jump", ImageLoader.loadImage("res\\images\\player_jump.png"));
        ResourceMaster.addImageToMap("player_idle", ImageLoader.loadImage("res\\images\\player_idle.png"));
        ResourceMaster.addImageToMap("led", ImageLoader.loadImage("res\\images\\item_led.png"));
        ResourceMaster.addImageToMap("pcb", ImageLoader.loadImage("res\\images\\item_pcb.png"));
        ResourceMaster.addImageToMap("screw", ImageLoader.loadImage("res\\images\\item_screw.png"));
        ResourceMaster.addImageToMap("simon_says", ImageLoader.loadImage("res\\images\\simon_says.png"));
        ResourceMaster.addImageToMap("game_background", ImageLoader.loadImage("res\\images\\game_background.png"));
        ResourceMaster.addImageToMap("mushroom_idle_up", ImageLoader.loadImage("res\\images\\mushroom_idle_up.png"));
        ResourceMaster.addImageToMap("mushroom_idle_down", ImageLoader.loadImage("res\\images\\mushroom_idle_down.png"));

        ResourceMaster.addSpriteSheetToMap("coin_5", new SpriteSheet("res\\spritesheets\\coin_5_spritesheet.png", 40, 40));
        ResourceMaster.addSpriteSheetToMap("coin_10", new SpriteSheet("res\\spritesheets\\coin_10_spritesheet.png", 40, 40));
        ResourceMaster.addSpriteSheetToMap("coin_20", new SpriteSheet("res\\spritesheets\\coin_20_spritesheet.png", 40, 40));
        ResourceMaster.addSpriteSheetToMap("player_die", new SpriteSheet("res\\spritesheets\\player_die_spritesheet.png", 40, 60));
        ResourceMaster.addSpriteSheetToMap("player_walk", new SpriteSheet("res\\spritesheets\\player_walk_spritesheet.png", 40, 60));
        ResourceMaster.addSpriteSheetToMap("snail_walk", new SpriteSheet("res\\spritesheets\\snail_walk_spritesheet.png", 60, 25));
        ResourceMaster.addSpriteSheetToMap("wolf_walk", new SpriteSheet("res\\spritesheets\\wolf_walk_spritesheet.png", 80, 44));
        ResourceMaster.addSpriteSheetToMap("dirt_gras", new SpriteSheet("res\\spritesheets\\blocktiles.png", 40, 40));
        ResourceMaster.addSpriteSheetToMap("mushroom_squish", new SpriteSheet("res\\spritesheets\\mushroom_squish_spritesheet.png", 40, 40));
        ResourceMaster.addSpriteSheetToMap("mushroom_desquish", new SpriteSheet("res\\spritesheets\\mushroom_desquish_spritesheet.png", 40, 40));
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
