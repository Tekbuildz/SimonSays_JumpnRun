package display;

import player.PlayerInputs;

import javax.swing.*;
import java.awt.*;

/**
 *
 * creates the frame on which the entire game is built on
 *
 * @author Thomas Bundi
 * @version 1.3
 * @since 0.2
 */
public class DisplayManager extends JPanel {

    private static final GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    private static final int WIDTH = gd.getDisplayMode().getWidth();
    private static final int HEIGHT = gd.getDisplayMode().getHeight();

    /**
     *
     * creating a basic JFrame and adding listeners to it
     */
    public void createDisplay(Renderer renderer) {
        JFrame frame = new JFrame("Jump 'n' Run");
        // removing the title-bar of the application-window
        // https://stackoverflow.com/questions/52148325/java-show-fullscreen-swing-application-with-taskbar-without-titlebar
        frame.setUndecorated(true);
        frame.setBounds(0, 0, WIDTH, HEIGHT);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // making the application full screen
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        frame.add(renderer);

        PlayerInputs pi = new PlayerInputs();
        frame.addKeyListener(pi);
        frame.addMouseListener(pi);
        frame.addMouseWheelListener(pi);
    }

    /**
     *
     * @return the width of the resolution of the current main display
     */
    public static int getWIDTH() {
        return WIDTH;
    }

    /**
     *
     * @return the height of the resolution of the current main display
     */
    public static int getHEIGHT() {
        return HEIGHT;
    }
}
