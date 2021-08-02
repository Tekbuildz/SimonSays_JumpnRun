package gameLoop;

import display.DisplayManager;
import display.Renderer;
import gamestates.GameState;
import gamestates.StateMaster;
import levelHandling.Level;
import player.Player;
import player.PlayerInputs;

public class Main implements Runnable{

    private DisplayManager display;
    private Level level;
    private Thread thread;
    private Renderer renderer;

    private static final boolean running = true;

    public static Player player;

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
     * creating a new player
     * a new thread and setting the current state
     */
    private void setup() {
        level = new Level("Level_1");

        player = new Player(Level.getSpawnLocation());

        renderer = new Renderer(DisplayManager.getWIDTH(), DisplayManager.getHEIGHT());

        display = new DisplayManager();
        display.createDisplay(renderer);

        thread = new Thread(this);
        thread.start();

        StateMaster.setState(new GameState());
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
        long timer = System.currentTimeMillis();
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
        }
    }
}
