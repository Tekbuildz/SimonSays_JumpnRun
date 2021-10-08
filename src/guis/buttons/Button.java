package guis.buttons;

import java.awt.*;

/**
 *
 * the abstract class Button contains all the functions which each other type
 * of button also has such as a functionality when getting triggered or
 * updating its color depending on whether the mouse is hovering above the
 * button or not
 *
 * @author Thomas Bundi
 * @version 0.3
 * @since 1.7
 */
public abstract class Button {

    /**
     *
     * updating the buttons and listen for a mouse button release over a button
     */
    public abstract void update();

    /**
     *
     * drawing the button to the screen
     *
     * @param g - the graphics object used to paint onto the screen
     */
    public abstract void draw(Graphics2D g);

    /**
     *
     * @param font - the new font of the text
     */
    public abstract void setTextFont(Font font);

    /**
     *
     * @param textColor - the new color of the text
     */
    public abstract void setTextColor(Color textColor);

    /**
     *
     * @param fillColor - the new color filling the background of the button
     */
    public abstract void setFillColor(Color fillColor);

    /**
     *
     * @param hoverColor - the new color of the button when
     *                   hovering over it with the mouse
     */
    public abstract void setHoverColor(Color hoverColor);

    /**
     *
     * @param pressedColor - the new color of the button when
     *                     pressing it with the mouse
     */
    public abstract void setPressedColor(Color pressedColor);

    /**
     *
     * @return a boolean containing if the button
     *          was released in the previous tick
     */
    public abstract boolean isButtonWasReleased();
}
