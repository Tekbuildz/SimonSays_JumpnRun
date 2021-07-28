package guis.buttons;

import player.PlayerInputs;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class ButtonCircle extends Button {

    private final String text;

    private Color fillColor;
    private Color hoverColor;
    private Color textColor;
    private Color pressedColor;
    private Font font;

    private boolean mouseOverButton;
    private boolean buttonWasReleased;

    private final Ellipse2D.Double ellipse;

    // button has shape of a circle
    public ButtonCircle (int x, int y, int diameter, String text) {
        this.text = text;

        fillColor = Color.WHITE;
        textColor = Color.WHITE;

        ellipse = new Ellipse2D.Double(x, y, diameter, diameter);
    }

    @Override
    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
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

    @Override
    public void update() {
        buttonWasReleased = false;
        mouseOverButton = ellipse.contains(PlayerInputs.getMousePos());

        if (mouseOverButton && PlayerInputs.getMouseButtonsReleasedInFrame().contains(1)) {
            System.out.println("button released");
            buttonWasReleased = true;
        }
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(fillColor);
        if (hoverColor != null && pressedColor != null) {
            if (PlayerInputs.isMousePressed() && mouseOverButton) {
                g.setColor(pressedColor);
            }
            else if (mouseOverButton) {
                g.setColor(hoverColor);
            }
        } else {
            System.err.println("Hover Color or Pressed Color for the button with String '" + text + "' is not defined!");
            System.exit(-1);
        }
        g.fill(ellipse);

        if (font != null) g.setFont(font);
        g.setColor(textColor);

        FontMetrics fm = g.getFontMetrics();
        // using font metrics to get information about the string and its width and height, where maxAscent is the height of the highest char in the string
        g.drawString(text, (int) ellipse.getCenterX() - fm.stringWidth(text) / 2, (int) ellipse.getCenterY() + fm.getMaxAscent() / 2);
    }
}
