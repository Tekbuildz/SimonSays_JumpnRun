package guis.buttons;

import player.PlayerInputs;
import toolbox.BasicGUIConstants;

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

    private final int x, y;
    private final int width, height;

    /**
     *
     * the basic constructor of the triangular-rectangle-button
     * this button consists of a basic rectangle of which the
     * edges were cut off in a triangular shape
     * these are isosceles triangles
     *
     * @param x - the x coordinate of the button
     * @param y - the y coordinate of the button
     * @param width - the width of the rectangle enclosing the button
     * @param height - the height of the rectangle enclosing the button
     * @param triangularCutoffSize - length of one of the sides
     *                             that are equal of the isosceles triangle
     * @param text - the text displayed in the button
     *             (acting as a description of its action)
     */
    public ButtonTriangularRectangle(int x, int y, int width, int height, int triangularCutoffSize, String text) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;

        fillColor = BasicGUIConstants.BUTTON_FILL_COLOR;
        hoverColor = BasicGUIConstants.BUTTON_HOVER_COLOR;
        pressedColor = BasicGUIConstants.BUTTON_PRESSED_COLOR;
        textColor = BasicGUIConstants.BUTTON_TEXT_COLOR;

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
        mouseOverButton = triangularRectangle.contains(PlayerInputs.getMousePos());

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
        g.fill(triangularRectangle);

        if (font != null) g.setFont(font);
        g.setColor(textColor);

        FontMetrics fm = g.getFontMetrics();
        // using font metrics to get information about the string and its width and height, where maxAscent is the height of the heighest char in the string
        g.drawString(text, x + width / 2 - fm.stringWidth(text) / 2, y + height / 2 + fm.getMaxAscent() / 2);
    }
}
