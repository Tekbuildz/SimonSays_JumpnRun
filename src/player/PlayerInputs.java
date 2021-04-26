package player;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

public class PlayerInputs implements KeyListener, MouseListener {

    static int keyPressed;
    static Point mousePos = new Point();

    /**
     *
     * @return the key which is currently pressed
     */
    public static int getKeyPressed() {
        return keyPressed;
    }

    private static void setKeyPressed(int keyCode) {
        keyPressed = keyCode;
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.getKeyChar() == 'a' || keyEvent.getKeyChar() == 'd' || keyEvent.getKeyChar() == ' ') {
            setKeyPressed(keyEvent.getKeyCode());
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        if (getKeyPressed() == KeyEvent.VK_A || getKeyPressed() == KeyEvent.VK_D || getKeyPressed() == KeyEvent.VK_SPACE) {
            // the ASCII code '0' is equal to 'null'
            setKeyPressed(0);
        }
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    /**
     *
     * updating the mousePos variable
     */
    public static void updateMousePos() {
        setMousePos(MouseInfo.getPointerInfo().getLocation());
    }

    /**
     *
     * @param p - setting the variable mousePos to the Point p
     */
    private static void setMousePos(Point p) {
        mousePos.setLocation(p);
    }

    /**
     *
     * @return the current position of the mouse cursor
     */
    public static Point getMousePos() {
        return mousePos;
    }
}
