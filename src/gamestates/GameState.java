package gamestates;

import SpriteSheet.SpriteSheetMaster;
import display.DisplayManager;
import gameLoop.Main;
import levelHandling.Cube;
import levelHandling.Level;
import player.Player;
import player.PlayerInputs;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.List;

public class GameState extends State {

    private final int HEIGHT = DisplayManager.getHEIGHT();
    private int yOffset = DisplayManager.getHEIGHT() - (Level.getLevelCubes().size() * Level.getLevelCubes().get(0).get(0).getPixelSIZE());

    @Override
    public void update() {
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
        Image[] dirtGrassSky = SpriteSheetMaster.getSpriteSheetFromMap("dirtGrassSky").getSpriteImages();
        for (int i = Level.getLevelCubes().size() - 1; i >= 0; i--) {
            List<Cube> list = Level.getLevelCubes().get(i);
            for (int j = list.size() - 1; j >= 0; j--) {
                Cube cube = list.get(j);
                g.drawImage(dirtGrassSky[cube.getCubeID()], (int) cube.getX() * cube.getPixelSIZE(), (int) -(Level.getLevelCubes().size() - cube.getY()) * cube.getPixelSIZE(), null, null);
            }
        }
    }

    /**
     * drawing the player to the screen
     */
    private void drawPlayer(Graphics2D g) {
        g.setColor(Color.BLUE);
        Rectangle2D.Double playerRect = new Rectangle2D.Double(Main.player.getX(), -(Level.getLevelCubes().size() * Main.player.getCubeSize() - Main.player.getY()), Player.getPlayerWidth(), Player.getPlayerHeight());
        g.fill(playerRect);
    }

    /**
     *
     * calls the functions in the player class corresponding to the input from the player
     */
    private void handleMovement() {
        // clearing any previous movement
        Main.player.move('n');
        // moving upon key press accordingly
        if (PlayerInputs.getKeyPressed().contains(KeyEvent.VK_A)) {
            Main.player.move('l');
        }
        if (PlayerInputs.getKeyPressed().contains(KeyEvent.VK_D)) {
            Main.player.move('r');
        }
        if (PlayerInputs.getKeyPressed().contains(KeyEvent.VK_SPACE)) {
            Main.player.jump();
        }
    }
}
