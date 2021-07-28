package guis.buttons;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class ButtonRoundRectangle extends Button implements MouseListener, MouseMotionListener {

    private int x;
    private int y;
    private int width;
    private int height;
    private int arcDiameter;
    private String text;
    private int activationMethod;

    // button has shape of a rounded rectangle
    public ButtonRoundRectangle (int x, int y, int width, int height, int arcDiameter, String text, int activationMethod) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.arcDiameter = arcDiameter;
        this.text = text;
        this.activationMethod = activationMethod;
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Graphics2D g) {

    }

    @Override
    public void setTextFont(Font font) {

    }

    @Override
    public void setTextColor(Color textColor) {

    }

    @Override
    public void setFillColor(Color fillColor) {

    }

    @Override
    public void setHoverColor(Color hoverColor) {

    }

    @Override
    public void setPressedColor(Color pressedColor) {

    }

    @Override
    public boolean isButtonWasReleased() {
        return false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        System.out.println();
    }
}
