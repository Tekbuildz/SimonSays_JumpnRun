package guis.buttons;

import player.PlayerInputs;
import toolbox.BasicGUIConstants;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 *
 * the ButtonCircle class creates all the objects necessary to display and
 * operate a button in the shape of a circle
 * <p>
 * extends to the Button class and hence overrides the update / render function
 *
 * @author Thomas Bundi
 * @version 0.5
 * @since 1.7
 */
public class ButtonCircle extends Button {

    private final String text;

    private Color fillColor;
    private Color hoverColor;
    private Color pressedColor;
    private Color textColor;
    private Font font;

    private boolean mouseOverButton;
    private boolean buttonWasReleased;

    private final Ellipse2D.Double ellipse;

    /**
     *
     * the basic constructor of a circular-shaped button
     *
     * @param x - the x coordinate of the button
     * @param y - the y coordinate of the button
     * @param diameter - the diameter of the circular-shaped button
     * @param text - the text displayed inside of the button
     *             (acting as a description of what the button does)
     */
    public ButtonCircle (int x, int y, int diameter, String text) {
        this.text = text;

        fillColor = BasicGUIConstants.BUTTON_FILL_COLOR;
        hoverColor = BasicGUIConstants.BUTTON_HOVER_COLOR;
        pressedColor = BasicGUIConstants.BUTTON_PRESSED_COLOR;
        textColor = BasicGUIConstants.BUTTON_TEXT_COLOR;

        ellipse = new Ellipse2D.Double(x, y, diameter, diameter);
    }

    @Override
    public void setTextFont(Font font) {
        this.font = font;
    }

    @Override
    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    @Override
    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    @Override
    public void setHoverColor(Color hoverColor) {
        this.hoverColor = hoverColor;
    }

    @Override
    public void setPressedColor(Color pressedColor) {
        this.pressedColor = pressedColor;
    }

    @Override
    public boolean isButtonWasReleased() {
        return buttonWasReleased;
    }

    /**
     *
     * updating the state of the button
     * checking if the mouse is hovering over the button
     * checking if the mouse was released over the
     * button in the previous tick/frame
     */
    @Override
    public void update() {
        buttonWasReleased = false;
        mouseOverButton = ellipse.contains(PlayerInputs.getMousePos());

        if (mouseOverButton && PlayerInputs.getMouseButtonsReleasedInFrame().contains(1)) {
            buttonWasReleased = true;
        }
    }

    /**
     *
     * drawing the button
     * changing the color according the position and action of the mouse
     * drawing the button and its string to the screen
     *
     * @param g - the graphics object used to paint onto the screen
     */
    @Override
    public void draw(Graphics2D g) {
        g.setColor(fillColor);

        if (mouseOverButton) {
            g.setColor(hoverColor);
            if (PlayerInputs.isMousePressed()) {
                g.setColor(pressedColor);
            }
        }
        g.fill(ellipse);

        if (font != null) g.setFont(font);
        g.setColor(textColor);

        FontMetrics fm = g.getFontMetrics();
        // using font metrics to get information about the string and its width and height, where maxAscent is the height of the highest char in the string
        g.drawString(text, (int) ellipse.getCenterX() - fm.stringWidth(text) / 2, (int) ellipse.getCenterY() + fm.getMaxAscent() / 2);
    }
}
