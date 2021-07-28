package player;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;

public class PlayerInputs implements KeyListener, MouseListener {

    private static final ArrayList<Integer> keysPressedInFrame = new ArrayList<>();
    private static final ArrayList<Integer> mouseButtonsReleasedInFrame = new ArrayList<>();
    private static final Point mousePos = new Point();
    private static boolean mousePressed = false;


    /**
     *
     * @return the key which is currently pressed
     */
    public static ArrayList<Integer> getKeysPressedInFrame() {
        return keysPressedInFrame;
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        // prevention of adding the same key multiple times
        if (!keysPressedInFrame.contains(keyEvent.getKeyCode())) {
            keysPressedInFrame.add(keyEvent.getKeyCode());
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        // Integer.valueOf needed, since otherwise the code would try to remove the object at index i, instead of the object with value i
        keysPressedInFrame.remove(Integer.valueOf(keyEvent.getKeyCode()));
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        // checking if it was a left-click
        if (e.getButton() == 1) {
            mousePressed = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // checking if it was a left-click
        if (e.getButton() == 1) {
            mousePressed = false;
        }

        mouseButtonsReleasedInFrame.add(e.getButton());
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    /**
     *
     * updating the mousePos variable
     */
    public static void updateMousePos() {
        setMousePos(MouseInfo.getPointerInfo().getLocation());
    }

    public static void updateKeysReleased() {
//        for (int button:mouseButtonsReleasedInFrame) {
//            // using Integer.valueOf
//            if (mouseButtonsReleasedInFrame.contains(button)) mouseButtonsReleasedInFrame.remove(Integer.valueOf(button));
//        }
        ArrayList<Integer> mouseButtonsToRemove = new ArrayList<>(mouseButtonsReleasedInFrame);
        mouseButtonsReleasedInFrame.removeAll(mouseButtonsToRemove);
    }

    public static ArrayList<Integer> getMouseButtonsReleasedInFrame() {
        return mouseButtonsReleasedInFrame;
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

    /**
     *
     * @return the state of the mouse whether it's pressed or not
     */
    public static boolean isMousePressed() {
        return mousePressed;
    }
}
