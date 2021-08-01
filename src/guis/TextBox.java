package guis;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

import static org.apache.commons.text.WordUtils.wrap;

public class TextBox {

    private final int x;
    private final int y;
    private final int width;
    private final Color textColor;
    private final Font font;
    private String[] lines;
    private final int verticalPixelsBetweenLines;

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
     * @param widthInChars - the amount of characters allowed per line before
     *                     inserting a newline
     * @param textColor - the color of the text
     * @param textFont - the font of the text
     * @param text - the text to be split and displayed
     * @param verticalPixelsBetweenLines - the line spacing in pixels
     * @see org.apache.commons.text.WordUtils
     * @see Color
     * @see Font
     */
    public TextBox(int x, int y, int widthInChars, Color textColor, Font textFont, String text, int verticalPixelsBetweenLines) {
        this.x = x;
        this.y = y;
        this.width = widthInChars;
        this.textColor = textColor;
        this.font = textFont;
        this.verticalPixelsBetweenLines = verticalPixelsBetweenLines;

        this.textMaxHeight = getTextMaxHeight(text, font);

        lines = wrap(text, width).split("\\n");
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
        lines = wrap(text, width).split("\\n");
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
        for (int i = 0; i < lines.length; i++) {
            g.drawString(lines[i], x, y + i * (verticalPixelsBetweenLines + textMaxHeight));
        }
    }
}
