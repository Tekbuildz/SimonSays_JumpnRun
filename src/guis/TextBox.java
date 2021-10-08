package guis;

import toolbox.UIConstraints;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

import static org.apache.commons.text.WordUtils.wrap;

/**
 *
 * the TextBox class contains a width which is not displayed but acts as a
 * horizontal boundary for the text, and it displays text with word wrap
 * according to this width
 *
 * @author Thomas Bundi
 * @version 0.5
 * @since 1.7
 */
public class TextBox {

    private final int x;
    private int y;
    private int originalYPos;
    private final int totalWidth;
    private final Color textColor;
    private final Font font;
    private String[] lines;
    private final int verticalPixelsBetweenLines;
    private final int uiConstraint;

    private final int textMaxHeight;

    /**
     *
     * basic constructor of a TextBox
     * using an external library from apache.org, text can easily be wrapped
     * determined by the specified amount of characters per line. This method
     * then will take a string and insert newlines in the whitespaces just
     * before the character-maximum
     * <p>
     * This text will then be displayed on screen on multiple lines if needed
     *
     * @param x - the x coordinate of the first character in the first line
     * @param y - the y coordinate of the first character in the first line
     * @param widthInPixels - the width in pixels constraining the amount of
     *                      characters allowed per line before inserting a
     *                      newline
     * @param textColor - the color of the text
     * @param textFont - the font of the text
     * @param text - the text to be split and displayed
     * @param verticalPixelsBetweenLines - the line spacing in pixels
     * @param uiConstraint - the ui constraint of the text box, determines
     *                     where the text is bound to (left, center)
     * @see org.apache.commons.text.WordUtils
     * @see Color
     * @see Font
     */
    public TextBox(int x, int y, int widthInPixels, Color textColor, Font textFont, String text, int verticalPixelsBetweenLines, int uiConstraint) {
        this.x = x;
        this.y = y;
        this.originalYPos = y;
        this.totalWidth = widthInPixels;
        this.textColor = textColor;
        this.font = textFont;
        this.verticalPixelsBetweenLines = verticalPixelsBetweenLines;
        this.uiConstraint = uiConstraint;

        this.textMaxHeight = getTextMaxHeight(text, font);

        lines = wrap(text, totalWidth / getTextMaxWidth("a", font)).split("\\n");
    }

    /**
     *
     * returns the maximum height of the string
     * this function uses a FontRenderContext to retrieve information about the
     * dimensions of a string
     * <p>
     * an alternative method to get the dimensions is using an instance of
     * the Graphics2D class and using its
     * getFontMetrics().getStringBounds().getHeight() function
     *
     * @param text - the text to be displayed
     * @param font - the font used to display the text
     * @return - the maximum height of the string
     * @see AffineTransform
     * @see FontRenderContext
     * @see Graphics2D
     * @see Font
     */
    private int getTextMaxHeight(String text, Font font) {
        AffineTransform affineTransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affineTransform, true, true);
        return (int) font.getStringBounds(text, frc).getHeight();
    }

    /**
     *
     * returns the maximum width of the string
     * this function uses a FontRenderContext to retrieve information about the
     * dimensions of a string
     * <p>
     * an alternative method to get the dimensions is using an instance of
     * the Graphics2D class and using its
     * getFontMetrics().getStringBounds().getWidth() function
     *
     * @param text - the text to be displayed
     * @param font - the font used to display the text
     * @return - the maximum height of the string
     * @see AffineTransform
     * @see FontRenderContext
     * @see Graphics2D
     * @see Font
     */
    private int getTextMaxWidth(String text, Font font) {
        AffineTransform affineTransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affineTransform, true, true);
        return (int) font.getStringBounds(text, frc).getWidth();
    }

    /**
     *
     * sets the text of the TextBox
     * this function again uses the wrap() function from the external
     * apache library to easily wrap text at whitespaces before a specified
     * amount of characters is reached
     * <p>
     * then using regular expressions or in short regex, the string can be
     * split at every newline the apache function has inserted anc create
     * multiple substrings which can then be displayed on the screen as
     * separate lines
     *
     * @param text - the new text to be displayed
     * @see org.apache.commons.text.WordUtils
     * @see String
     */
    public void setText(String text) {
        lines = wrap(text, totalWidth / getTextMaxWidth("a", font)).split("\\n");
    }

    /**
     *
     * changes the y coordinate depending on the shift of the text
     * mostly used to create scrollable text
     *
     * @param y - new yShift of the text
     */
    public void setYShift(int y) {
        this.y = originalYPos + y;
    }

    /**
     *
     * draws the TextBox to the screen
     * using the individual lines split using regex, each is displayed on the
     * screen as a separate line with the font and textColor specified in
     * the constructor
     *
     * @param g - the graphics object used to paint onto the screen
     * @see Graphics2D
     * @see Color
     * @see Font
     */
    public void draw(Graphics2D g) {
        g.setColor(textColor);
        g.setFont(font);
        if (uiConstraint == UIConstraints.UI_LEFT_BOUND_CONSTRAINT) {
            for (int i = 0; i < lines.length; i++) {
                g.drawString(lines[i], x, y + i * (verticalPixelsBetweenLines + textMaxHeight));
            }
        } else if (uiConstraint == UIConstraints.UI_CENTER_BOUND_CONSTRAINT) {
            for (int i = 0; i < lines.length; i++) {
                g.drawString(lines[i], x + getCenterBoundOffset(lines[i], font, totalWidth), y + i * (verticalPixelsBetweenLines + textMaxHeight));
            }
        } else if (uiConstraint == UIConstraints.UI_RIGHT_BOUND_CONSTRAINT) {
            for (int i = 0; i < lines.length; i++) {
                g.drawString(lines[i], x + getRightBoundOffset(lines[i], font, totalWidth), y + i * (verticalPixelsBetweenLines + textMaxHeight));
            }
        }
    }

    /**
     *
     * this function is used to get the x offset of a CENTER_BOUND_CONSTRAINT
     * it first calculates the length of the given string in pixels, then
     * subtracts the length of the string from the totalWidth to get the total
     * offset of both the left and right side of the string combined and in
     * order to get only one half, the value is divided by two
     *
     * @param text - the string to be centered
     * @param font - the font of the string (contains the size of the text)
     * @param totalWidth - the totalWidth in which the text can be rendered
     * @return the x offset of a string needed in order to center it
     */
    private int getCenterBoundOffset(String text, Font font, int totalWidth) {
        int lineWidth = getTextMaxWidth(text, font);
        return (totalWidth - lineWidth) / 2;
    }

    /**
     *
     * this function is used to get the x offset of a RIGHT_BOUND_CONSTRAINT
     * it first calculates the length of the given string in pixels, then
     * subtracts the length of the string from the totalWidth to get the total
     * offset
     *
     * @param text - the string to be bound to the right side of the
     *             rectangle in which the string can be rendered
     * @param font - the font of the string (contains the size of the text)
     * @param totalWidth - the totalWidth in which the text can be rendered
     * @return - the x offset of a string needed in order to right-bind it
     */
    private int getRightBoundOffset(String text, Font font, int totalWidth) {
        int lineWidth = getTextMaxWidth(text, font);
        return totalWidth - lineWidth;
    }

    /**
     *
     * calculates the height of the entire text
     * using the height of a single line, the number of lines and the number
     * of pixels between each line, the function calculates the height of the
     * rectangle enclosing the TextBox
     *
     * @return the height of rectangle enclosing the TextBox
     */
    public int getTextHeight() {
        return (textMaxHeight + verticalPixelsBetweenLines) * lines.length;
    }

    /**
     *
     * @return the original position of the TextBox
     */
    public int getOriginalYPos() {
        return originalYPos;
    }
}
