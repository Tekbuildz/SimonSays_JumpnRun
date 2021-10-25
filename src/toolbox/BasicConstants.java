package toolbox;

import display.DisplayManager;

import java.awt.*;

/**
 *
 * the BasicGUIConstants class holds as the name indicates the UI constants
 * which are used more than once, such as basic colors or fonts
 *
 * @author Thomas Bundi
 * @version 0.4
 * @since 2.4
 */
public class BasicConstants {

    public static final double RSF = DisplayManager.getWIDTH() / 1920f;
    public static final String DEFAULT_PATH = System.getProperty("user.home") + "/SimonSays_JAR/";

    // ------------ OTHER COLORS ------------
    public static final Color GUI_OVERLAY_DEFAULT_COLOR = new Color(34, 52, 92);
    public static final Color HEALTH_BAR_GREEN_COLOR = new Color(23, 176, 38);
    public static final Color TRANSPARENT_DARKENING_COLOR = new Color(0, 0, 0, 120);
    public static final Color MONEY_YELLOW_COLOR = new Color(217, 156, 2);

    // ------------ BUTTON COLORS ------------
    public static final Color BUTTON_FILL_COLOR = new Color(17, 23, 36);
    public static final Color BUTTON_HOVER_COLOR = new Color(32, 41, 64);
    public static final Color BUTTON_PRESSED_COLOR = new Color(27, 35, 53);
    public static final Color BUTTON_TEXT_COLOR = new Color(12, 244, 243);

    // ------------ FONTS ------------
    public static final Font DEFAULT_BUTTON_FONT = new Font("Calibri", Font.PLAIN, (int) (30 * RSF));
    public static final Font TITLE_FONT = new Font("Calibri", Font.BOLD, (int) (50 * RSF));
    public static final Font DEFAULT_TEXT_FONT = new Font("Calibri", Font.BOLD, (int) (30 * RSF));
}
