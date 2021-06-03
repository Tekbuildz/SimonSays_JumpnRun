package gameLoop;

import display.DisplayManager;
import display.Renderer;
import gamestates.GameState;
import gamestates.State;
import gamestates.StateMaster;
import levelHandling.Cube;
import levelHandling.Level;
import player.Player;
import player.PlayerInputs;

import java.util.ArrayList;

public class Main implements Runnable{

    private DisplayManager display;
    private Level level;
    private Thread thread;
    private Renderer renderer;

    private static int currentFPS;

    private static final boolean running = true;

    public static Player player;
    public static ArrayList<ArrayList<Cube>> temp;



    public Main() {
        setup();
    }

    public static void main(String[] args) {
        new Main();
    }

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
     * updating the game logic
     */
    private void update() {
        // updating all the game logic
        PlayerInputs.updateMousePos();
        if (StateMaster.getState() != null) {
            StateMaster.getState().update();
        }
    }

    @Override
    // main game loop
    // changed version of the second loop found here: https://gamedev.stackexchange.com/questions/160329/java-game-loop-efficiency
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 120.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        int frames = 0;
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
            frames++;

            // updating the frames on the screen every 0.5 seconds
            if (System.currentTimeMillis() - timer > 500) {
                currentFPS = frames;
                timer += 500;
                frames = 0;
            }
        }
    }

    public static int getCurrentFPS() {
        return currentFPS;
    }
}
