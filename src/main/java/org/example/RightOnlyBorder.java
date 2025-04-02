package org.example;

import javax.swing.border.AbstractBorder;
import java.awt.*;

class RightOnlyBorder extends AbstractBorder
{
    private Color color;
    private int thickness;

    public RightOnlyBorder(Color color, int thickness)
    {
        this.color = color;
        this.thickness = thickness;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height)
    {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(thickness));
        g2d.drawLine(x + width - 1, y, x + width - 1, y + height - 1);
        g2d.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c)
    {
        return new Insets(0, 0, 0, thickness);
    }


}
