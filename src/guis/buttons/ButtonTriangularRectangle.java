package guis.buttons;

import player.PlayerInputs;
import toolbox.BasicColors;

import java.awt.*;

public class ButtonTriangularRectangle extends Button {

    private final String text;

    private Color fillColor;
    private Color hoverColor;
    private Color pressedColor;
    private Color textColor;
    private Font font;

    private boolean mouseOverButton;
    private boolean buttonWasReleased;

    private final Polygon triangularRectangle;

    private int x, y;
    private int width, height;
    private int triangularCutoffSize;

    // button has shape of a rectangle with its edges cut off in a triangular shape
    public ButtonTriangularRectangle(int x, int y, int width, int height, int triangularCutoffSize, String text) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.triangularCutoffSize = triangularCutoffSize;
        this.text = text;

        fillColor = BasicColors.BUTTON_FILL_COLOR;
        hoverColor = BasicColors.BUTTON_HOVER_COLOR;
        pressedColor = BasicColors.BUTTON_PRESSED_COLOR;
        textColor = BasicColors.BUTTON_TEXT_COLOR;

        triangularRectangle = new Polygon(
                new int[] {
                        x + triangularCutoffSize, x + width - triangularCutoffSize, x + width, x + width, x + width -triangularCutoffSize, x + triangularCutoffSize, x, x
                },
                new int[] {
                        y, y, y + triangularCutoffSize, y + height - triangularCutoffSize, y + height, y + height, y + height - triangularCutoffSize, y + triangularCutoffSize
                },
                8);
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
        mouseOverButton = triangularRectangle.contains(PlayerInputs.getMousePos());

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
        g.fill(triangularRectangle);

        if (font != null) g.setFont(font);
        g.setColor(textColor);

        FontMetrics fm = g.getFontMetrics();
        // using font metrics to get information about the string and its width and height, where maxAscent is the height of the heighest char in the string
        g.drawString(text, x + width / 2 - fm.stringWidth(text) / 2, y + height / 2 + fm.getMaxAscent() / 2);
    }
}
