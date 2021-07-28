package guis.buttons;

import java.awt.*;

public abstract class Button {

    public abstract void update();

    public abstract void draw(Graphics2D g);

    public abstract void setTextFont(Font font);
    public abstract void setTextColor(Color textColor);
    public abstract void setFillColor(Color fillColor);
    public abstract void setHoverColor(Color hoverColor);
    public abstract void setPressedColor(Color pressedColor);

    public abstract boolean isButtonWasReleased();
}
