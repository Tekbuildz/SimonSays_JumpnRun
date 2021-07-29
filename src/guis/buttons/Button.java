package guis.buttons;

import java.awt.*;

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
