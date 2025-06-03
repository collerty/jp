package org.example.model.slideComponents.factory;

import org.example.model.slideComponents.SlideItem;

public class SlideItemFactory {
    
    private static final TextItemFactory textItemFactory = new TextItemFactory();
    private static final BitmapItemFactory bitmapItemFactory = new BitmapItemFactory();
    
    public static SlideItem createTextItem(int level, String text) {
        return textItemFactory.createItem(level, text);
    }
    
    public static SlideItem createBoldTextItem(int level, String text) {
        return textItemFactory.createBoldItem(level, text);
    }
    
    public static SlideItem createItalicTextItem(int level, String text) {
        return textItemFactory.createItalicItem(level, text);
    }
    
    public static SlideItem createBoldItalicTextItem(int level, String text) {
        return textItemFactory.createBoldItalicItem(level, text);
    }
    
    public static SlideItem createBitmapItem(int level, String imagePath) {
        return bitmapItemFactory.createItem(level, imagePath);
    }
    
    public static SlideItem createFormattedTextItem(int level, String text, boolean bold, boolean italic) {
        return textItemFactory.createFormattedTextItem(level, text, bold, italic);
    }

    public static AbstractSlideItemFactory getFactory(boolean isText) {
        return isText ? textItemFactory : bitmapItemFactory;
    }
} 