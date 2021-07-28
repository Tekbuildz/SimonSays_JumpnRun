package display;

import player.PlayerInputs;

import javax.swing.*;
import java.awt.*;

public class DisplayManager extends JPanel {

//    private static final int WIDTH = 1920;
//    private static final int HEIGHT = 1080;

    private static final GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    private static final int WIDTH = gd.getDisplayMode().getWidth();
    private static final int HEIGHT = gd.getDisplayMode().getHeight();

    /**
     *
     * creating a display, adding listeners to it
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

        // adding the JPanel and therefore any painting to the frame
        frame.add(renderer);

        // adding listeners to the frame
        PlayerInputs pi = new PlayerInputs();
        frame.addKeyListener(pi);
        frame.addMouseListener(pi);
    }

    public static int getWIDTH() {
        return WIDTH;
    }

    public static int getHEIGHT() {
        return HEIGHT;
    }
}
