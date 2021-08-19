package gameLoop;

import Loader.DataLoader;
import Loader.ImageLoader;
import SpriteSheet.ResourceMaster;
import SpriteSheet.SpriteSheet;
import display.DisplayManager;
import display.Renderer;
import gamestates.GameState;
import gamestates.StateMaster;
import levelHandling.Level;
import player.Player;
import player.PlayerInputs;

public class Main implements Runnable{

    private Renderer renderer;

    private static final boolean running = true;

    public static Player player;
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
        new Level("flioLevel");
        DataLoader.loadPlayerData("player");

        player = new Player(Level.getSpawnLocation(), DataLoader.getLives(), DataLoader.getCoins(), DataLoader.getEntityKills());

        renderer = new Renderer(DisplayManager.getWIDTH(), DisplayManager.getHEIGHT());

        DisplayManager display = new DisplayManager();
        display.createDisplay(renderer);

        Thread thread = new Thread(this);
        thread.start();

        StateMaster.setState(new GameState());
    }

    /**
     *
     * loads all sprite sheets and images required for the game to a map
     */
    public static void loadAllResources() {
        ResourceMaster.addImageToMap("player_jump", ImageLoader.loadImage("res\\images\\player_jump.png"));
        ResourceMaster.addImageToMap("player_idle", ImageLoader.loadImage("res\\images\\player_idle.png"));
        ResourceMaster.addImageToMap("led", ImageLoader.loadImage("res\\images\\item_led.png"));
        ResourceMaster.addImageToMap("pcb", ImageLoader.loadImage("res\\images\\item_pcb.png"));
        ResourceMaster.addImageToMap("screw", ImageLoader.loadImage("res\\images\\item_screw.png"));
        ResourceMaster.addImageToMap("simon_says", ImageLoader.loadImage("res\\images\\simon_says.png"));

        ResourceMaster.addSpriteSheetToMap("coin_5", new SpriteSheet("res\\spritesheets\\coin_5_spritesheet.png", 40, 40));
        ResourceMaster.addSpriteSheetToMap("coin_10", new SpriteSheet("res\\spritesheets\\coin_10_spritesheet.png", 40, 40));
        ResourceMaster.addSpriteSheetToMap("coin_20", new SpriteSheet("res\\spritesheets\\coin_20_spritesheet.png", 40, 40));
        ResourceMaster.addSpriteSheetToMap("player_die", new SpriteSheet("res\\spritesheets\\player_die_spritesheet.png", 40, 60));
        ResourceMaster.addSpriteSheetToMap("player_walk", new SpriteSheet("res\\spritesheets\\player_walk_spritesheet.png", 40, 60));
        ResourceMaster.addSpriteSheetToMap("snail_walk", new SpriteSheet("res\\spritesheets\\snail_walk_spritesheet.png", 60, 25));
        ResourceMaster.addSpriteSheetToMap("dirt_gras", new SpriteSheet("res\\spritesheets\\dirt_gras.png", 40, 40));
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
