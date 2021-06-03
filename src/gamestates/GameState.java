package gamestates;

import display.DisplayManager;
import gameLoop.Main;
import levelHandling.Cube;
import levelHandling.Level;
import player.PlayerInputs;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.util.List;

public class GameState extends State {

    private final int HEIGHT = DisplayManager.getHEIGHT();

    @Override
    public void update() {
        Main.player.checkCollisions(Level.getLevelCubes());
        Main.player.applyGravity();
        Main.player.updatePlayerRectCoords();

        handleMovement();
    }

    @Override
    public void render(Graphics2D g) {
        g.translate(0, HEIGHT);
        drawLevel(g);
        drawPlayer(g);
        // using the translate function with "-HEIGHT" because the origin is always viewed from the current origin
        // of the current coordinate system which is located at 0, HEIGHT (viewed from the actual screen => not visible)
        // in order to change the coordinate system back from the bottom left to the top left of the screen, the
        // height of the screen needs to be subtracted
        g.translate(0, -HEIGHT);

    }

    /**
     *
     * drawing all the level-rectangles
     */
    private void drawLevel(Graphics2D g) {
        for (int i = Level.getLevelCubes().size() - 1; i >= 0; i--) {
            List<Cube> list = Level.getLevelCubes().get(i);
            for (int j = list.size() - 1; j >= 0; j--) {
                Cube cube = list.get(j);
                if (cube.getCubeID() == 1) {
                    g.setColor(new Color(129, 91, 55));
                    Rectangle2D.Double currentRect = new Rectangle2D.Double(cube.getX() * cube.getPixelSIZE(), -(Level.getLevelCubes().size() - cube.getY()) * cube.getPixelSIZE(), cube.getPixelSIZE(), cube.getPixelSIZE());
                    g.fill(currentRect);
                }
            }
        }
    }

    /**
     * drawing the player to the screen
     */
    private void drawPlayer(Graphics2D g) {
        g.setColor(Color.BLUE);
        float playerX = Main.player.getX();
        float playerY = Main.player.getY();
        //Rectangle2D.Double playerRect = new Rectangle2D.Double((playerX * Main.player.getCubeSize()), (playerY - 10) * Main.player.getCubeSize(), Main.player.getCubeSize(), 2 * Main.player.getCubeSize());
        Rectangle2D.Double playerRect = new Rectangle2D.Double((playerX * Main.player.getCubeSize()), -(Level.getLevelCubes().size() - playerY) * Main.player.getCubeSize(), Main.player.getCubeSize(), 2 * Main.player.getCubeSize());
        //graphics2D.draw(playerRect);
        g.fill(playerRect);
    }

    /**
     *
     * calls the functions in the player class corresponding to the input from the player
     */
    private void handleMovement() {
        if (PlayerInputs.getKeyPressed() == KeyEvent.VK_A) {
            Main.player.move('l');
        } else if (PlayerInputs.getKeyPressed() == KeyEvent.VK_D) {
            Main.player.move('r');
        } else if (PlayerInputs.getKeyPressed() == KeyEvent.VK_SPACE) {
            Main.player.jump();
            Main.player.move('n');
        } else {
            Main.player.move('n');
        }
    }
}
