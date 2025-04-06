package org.example.view;

import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;

class TextBubbleBorder extends AbstractBorder
{
    private Color color;
    private int thickness = 4;
    private int radii = 8;
    private int pointerSize = 7;
    private Insets insets = null;
    private BasicStroke stroke = null;
    private int strokePad;
    private int pointerPad = 4;
    private boolean left = true;
    RenderingHints hints;

    TextBubbleBorder(Color color) {
        this(color, 4, 8, 7);
    }

    TextBubbleBorder(Color color, int thickness, int radii, int pointerSize) {
        this.thickness = thickness;
        this.radii = radii;
        this.pointerSize = pointerSize;
        this.color = color;

        this.stroke = new BasicStroke(thickness);
        this.strokePad = thickness / 2;

        this.hints = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int pad = radii + this.strokePad;
        int bottomPad = pad + pointerSize + this.strokePad;
        this.insets = new Insets(pad, pad, bottomPad, pad);
    }

    TextBubbleBorder(Color color, int thickness, int radii, int pointerSize, boolean left) {
        this(color, thickness, radii, pointerSize);
        this.left = left;
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return this.insets;
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        return this.getBorderInsets(c);
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g;

        int bottomLineY = height - this.thickness - this.pointerSize;

        RoundRectangle2D.Double bubble = new RoundRectangle2D.Double(
                0 + this.strokePad,
                0 + this.strokePad,
                width - this.thickness,
                bottomLineY,
                this.radii,
                this.radii);

        Polygon pointer = new Polygon();

        if (this.left) {
            // left point
            pointer.addPoint(
                    this.strokePad + this.radii + this.pointerPad,
                    bottomLineY);
            // right point
            pointer.addPoint(
                    this.strokePad + this.radii + this.pointerPad + this.pointerSize,
                    bottomLineY);
            // bottom point
            pointer.addPoint(
                    this.strokePad + this.radii + this.pointerPad + (this.pointerSize / 2),
                    height - this.strokePad);
        } else {
            // left point
            pointer.addPoint(
                    width - (this.strokePad + this.radii + this.pointerPad),
                    bottomLineY);
            // right point
            pointer.addPoint(
                    width - (this.strokePad + this.radii + this.pointerPad + this.pointerSize),
                    bottomLineY);
            // bottom point
            pointer.addPoint(
                    width - (this.strokePad + this.radii + this.pointerPad + (this.pointerSize / 2)),
                    height - this.strokePad);
        }

        Area area = new Area(bubble);
        area.add(new Area(pointer));

        g2.setRenderingHints(this.hints);

        // Paint the BG color of the parent, everywhere outside the clip
        // of the text bubble.
        Component parent = c.getParent();
        if (parent != null) {
            Color bg = parent.getBackground();
            Rectangle rect = new Rectangle(0, 0, width, height);
            Area borderRegion = new Area(rect);
            borderRegion.subtract(area);
            g2.setClip(borderRegion);
            g2.setColor(bg);
            g2.fillRect(0, 0, width, height);
            g2.setClip(null);
        }

        g2.setColor(this.color);
        g2.setStroke(this.stroke);
        g2.draw(area);
    }
}