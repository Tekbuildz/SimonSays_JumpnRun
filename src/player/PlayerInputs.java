package player;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Vector;

public class PlayerInputs implements KeyListener, MouseListener {

    private static ArrayList<Integer> keyPressed = new ArrayList<>();
    private static Point mousePos = new Point();

    /**
     *
     * @return the key which is currently pressed
     */
    public static ArrayList<Integer> getKeyPressed() {
        return keyPressed;
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        // prevention of adding the same key multiple times
        if (!keyPressed.contains(keyEvent.getKeyCode())) {
            keyPressed.add(keyEvent.getKeyCode());
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        // Integer.valueOf needed, since otherwise the code would try to remove the object at index i, instead of the object with value i
        keyPressed.remove(Integer.valueOf(keyEvent.getKeyCode()));
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
