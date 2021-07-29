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

    /**
     *
     * handling the keys pressed
     * if the key pressed is not yet in the arraylist containing all the keys
     * which are currently pressed, it is added
     *
     * @param keyEvent - the event thrown when pressing a key
     */
    @Override
    public void keyPressed(KeyEvent keyEvent) {
        // prevention of adding the same key multiple times
        if (!keysPressedInFrame.contains(keyEvent.getKeyCode())) {
            keysPressedInFrame.add(keyEvent.getKeyCode());
        }
    }

    /**
     *
     * handling the keys released
     * if a key is released, it is removed from the list of currently
     * pressed keys
     *
     * @param e - the event thrown when releasing a key
     */
    @Override
    public void keyReleased(KeyEvent e) {
        // Integer.valueOf needed, since otherwise the code would try to remove the object at index i, instead of the object with value i
        keysPressedInFrame.remove(Integer.valueOf(e.getKeyCode()));
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    /**
     *
     * handling the mouse button pressed
     * if left click is pressed, mousePressed is set to true
     *
     * @param e - the event thrown when pressing a mouse button
     */
    @Override
    public void mousePressed(MouseEvent e) {
        // checking if it was a left-click
        if (e.getButton() == 1) {
            mousePressed = true;
        }
    }

    /**
     *
     * handling the mouse button released
     * if left click is released, mousePressed is set to false
     * any mouse button that was released in that frame is
     * added to that ArrayList
     *
     * @param e - the event thrown when releasing a mouse button
     */
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
     * updating the variable containing the position of the mouse cursor
     */
    public static void updateMousePos() {
        setMousePos(MouseInfo.getPointerInfo().getLocation());
    }

    /**
     *
     * removing all the keys released in the previous tick using this function
     * which is called at the beginning of each tick
     * <p>
     * removing all the mouse buttons from the array using the
     * ArrayList.removeAll() function and passing the array itself as a
     * collection since removing it using a loop would throw a
     * java.util.ConcurrentModificationException
     */
    public static void updateKeysReleased() {
        ArrayList<Integer> mouseButtonsToRemove = new ArrayList<>(mouseButtonsReleasedInFrame);
        mouseButtonsReleasedInFrame.removeAll(mouseButtonsToRemove);
    }

    /**
     *
     * @return the ArrayList containing all the mouse buttons which were
     * released in the current tick/frame
     */
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
