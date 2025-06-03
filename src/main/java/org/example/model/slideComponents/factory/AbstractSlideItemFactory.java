package org.example.model.slideComponents.factory;

import org.example.model.slideComponents.SlideItem;


public interface AbstractSlideItemFactory {
    SlideItem createItem(int level, String content);

    SlideItem createBoldItem(int level, String content);

    SlideItem createItalicItem(int level, String content);

    SlideItem createBoldItalicItem(int level, String content);
} 