package org.example.model.slideComponents.decorator;

import org.example.model.Style;
import org.example.model.slideComponents.SlideItem;

import java.awt.*;
import java.awt.image.ImageObserver;

public abstract class SlideItemDecorator extends SlideItem
{
    private SlideItem decoratedItem;

    public SlideItemDecorator(int level, SlideItem decoratedItem)
    {
        this.decoratedItem = decoratedItem;
        super.setLevel(level);
    }


    public SlideItem getDecoratedItem()
    {
        return this.decoratedItem;
    }

    public void setDecoratedItem(SlideItem decoratedItem)
    {
        this.decoratedItem = decoratedItem;
    }

    public Font modifyFont(Style style, float scale, int fontStyle)
    {
        Font font = style.getFont();
        return font.deriveFont(fontStyle | font.getStyle(), scale);
    }

    @Override
    public Rectangle getBoundingBox(Graphics g, ImageObserver observer, float scale, Style style)
    {
        return this.decoratedItem.getBoundingBox(g, observer, scale, style);
    }
}
