package org.example.model.slideComponents.decorator;

import org.example.model.Style;
import org.example.model.slideComponents.SlideItem;

import java.awt.*;
import java.awt.image.ImageObserver;

public class ItalicDecorator extends SlideItemDecorator
{
    public ItalicDecorator(SlideItem decoratedItem)
    {
        super(decoratedItem.getLevel(), decoratedItem);
    }


    @Override
    public void draw(int x, int y, float scale, Graphics g, Style style, ImageObserver observer)
    {
        super.draw(x, y, scale, g, style, observer);
        
        style.setFont(super.modifyFont(style, scale, Font.ITALIC));
        super.getDecoratedItem().draw(x, y, scale, g, style, observer);
    }
}
