package org.example.model.slideComponents.factory;

import org.example.model.slideComponents.SlideItem;
import org.example.model.slideComponents.BitmapItem;
import org.example.model.slideComponents.factory.AbstractSlideItemFactory;

public class BitmapItemFactory implements AbstractSlideItemFactory
{

    @Override
    public SlideItem createItem(int level, String content)
    {
        return new BitmapItem(level, content);
    }
}
