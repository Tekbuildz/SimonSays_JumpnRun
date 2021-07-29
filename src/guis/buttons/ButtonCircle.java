package guis.buttons;

import player.PlayerInputs;
import toolbox.BasicColors;

import java.awt.*;
import java.awt.geom.Ellipse2D;

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

    // button has shape of a circle
    public ButtonCircle (int x, int y, int diameter, String text) {
        this.text = text;

        fillColor = BasicColors.BUTTON_FILL_COLOR;
        hoverColor = BasicColors.BUTTON_HOVER_COLOR;
        pressedColor = BasicColors.BUTTON_PRESSED_COLOR;
        textColor = BasicColors.BUTTON_TEXT_COLOR;

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

    @Override
    public void update() {
        buttonWasReleased = false;
        mouseOverButton = ellipse.contains(PlayerInputs.getMousePos());

        if (mouseOverButton && PlayerInputs.getMouseButtonsReleasedInFrame().contains(1)) {
            buttonWasReleased = true;
        }
    }

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