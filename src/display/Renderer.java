package display;

import gamestates.StateMaster;

import javax.swing.*;
import java.awt.*;

public class Renderer extends JPanel{

    private final int WIDTH;
    private final int HEIGHT;
    private final Font FPSFont = new Font("Calibri", Font.PLAIN, 20);

    public Renderer(int WIDTH, int HEIGHT) {
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
    }

    public void paint(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics.create();

        graphics2D.clearRect(0, 0, WIDTH, HEIGHT);

        if (StateMaster.getState() != null) {
            StateMaster.getState().render(graphics2D);
        }
    }
}
