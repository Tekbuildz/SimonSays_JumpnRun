package display;

import gameLoop.Main;
import levelHandling.Cube;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DisplayManager extends JPanel implements Runnable {

    private final GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    private final int WIDTH = graphicsDevice.getDisplayMode().getWidth();
    private final int HEIGHT = graphicsDevice.getDisplayMode().getHeight();
    private JFrame frame;
    private Thread thread;

    private final boolean running = true;
    private int currentFPS = 0;
    private final Font FPSFont = new Font("Calibri", Font.PLAIN, 20);

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
        frame.add(this);

        thread = new Thread(this);
        thread.start();
    }

    private void update() {
        // updating all the game logic
    }

    public void paint(Graphics g) {
        // temporary, replaced by background image
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // drawing the FPS on the screen
        g.setColor(Color.BLACK);
        g.setFont(FPSFont);
        g.drawString("FPS: " + currentFPS, 10, 20);

        g.translate(0, HEIGHT);

        for (int i = Main.temp.size() - 1; i >= 0; i--) {
            List<Cube> list = Main.temp.get(i);
            for (int j = list.size() - 1; j >= 0; j--) {
                Cube cube = list.get(j);
                if (cube.getCubeID() == 1) {
                    g.setColor(new Color(129, 91, 55));
                    g.fillRect(cube.getX() * cube.getSIZE(), -(Main.temp.size() - cube.getY()) * cube.getSIZE(), cube.getSIZE(), cube.getSIZE());
                }
            }
        }
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
                repaint();
            frames++;

            // updating the frames on the screen every 0.5 seconds
            if (System.currentTimeMillis() - timer > 500) {
                currentFPS = frames;
                timer += 500;
                frames = 0;
            }
        }
    }
}
