package display;

import javax.swing.*;
import java.awt.*;

public class DisplayManager extends JPanel implements Runnable {

    private final GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    private final int WIDTH = graphicsDevice.getDisplayMode().getWidth();
    private final int HEIGHT = graphicsDevice.getDisplayMode().getHeight();
    private JFrame frame;
    private Renderer renderer;
    private Thread thread;

    private final boolean running = true;

    public void createDisplay() {
        frame = new JFrame("Jump 'n' Run");
        // removing the title-bar of the application-window
        // https://stackoverflow.com/questions/52148325/java-show-fullscreen-swing-application-with-taskbar-without-titlebar
        frame.setUndecorated(true);
        frame.setBounds(0, 0, WIDTH, HEIGHT);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // making the application full screen
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // adding the JPanel and therefore any painting to the frame
        renderer = new Renderer(WIDTH, HEIGHT);
        frame.add(renderer);

        thread = new Thread(this);
        thread.start();
    }

    /**
     *
     * updating the game logic
     */
    private void update() {
        // updating all the game logic
    }

    @Override
    // main game loop
    // changed version of the second loop found here: https://gamedev.stackexchange.com/questions/160329/java-game-loop-efficiency
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
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
                renderer.updateCurrentFPS(frames);
                timer += 500;
                frames = 0;
            }
        }
    }
}
