package guis.outlines;

import toolbox.BasicColors;

import java.awt.*;

public class TriangularRectangle extends Outline{

    private final Polygon polygon;
    private Polyline outline;
    private boolean hasOutline = true;

    private Color fillColor;

    private int x, y;
    private int width, height;
    private int triangularCutoffSize;

    public TriangularRectangle(int x, int y, int width, int height, int triangularCutoffSize) {
        this(x, y, width, height, triangularCutoffSize, null, 0f);
    }

    public TriangularRectangle(int x, int y, int width, int height, int triangularCutoffSize, Color outlineColor, float strokeWeight) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.triangularCutoffSize = triangularCutoffSize;

        this.fillColor = BasicColors.GUI_OVERLAY_DEFAULT_COLOR;

        polygon = new Polygon(
                new int[]{x + triangularCutoffSize, x + width - triangularCutoffSize, x + width, x + width, x + width - triangularCutoffSize, x + triangularCutoffSize, x, x},
                new int[]{y, y, y + triangularCutoffSize, y + height - triangularCutoffSize, y + height, y + height, y + height - triangularCutoffSize, y + triangularCutoffSize},
                8
        );

        if (outlineColor != null && strokeWeight > 0) {
            outline = new Polyline(
                    new int[]{x + triangularCutoffSize, x + width - triangularCutoffSize, x + width, x + width, x + width - triangularCutoffSize, x + triangularCutoffSize, x, x},
                    new int[]{y, y, y + triangularCutoffSize, y + height - triangularCutoffSize, y + height, y + height, y + height - triangularCutoffSize, y + triangularCutoffSize},
                    8,
                    outlineColor,
                    strokeWeight
            );
        } else {
            this.hasOutline = false;
        }
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public void draw(Graphics2D g) {
        g.fill(polygon);

        if (hasOutline) outline.draw(g);
    }
}