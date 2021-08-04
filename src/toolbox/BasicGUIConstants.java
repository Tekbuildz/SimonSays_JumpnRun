package toolbox;

import display.DisplayManager;

import java.awt.*;

public class BasicGUIConstants {

    private static final double rsf = DisplayManager.getWIDTH() / 1920f;

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
    public static final Font DEFAULT_BUTTON_FONT = new Font("Calibri", Font.PLAIN, (int) (30 * rsf));
}
