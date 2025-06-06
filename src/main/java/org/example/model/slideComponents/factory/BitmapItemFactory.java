package org.example.model.slideComponents.factory;

import org.example.model.slideComponents.BitmapItem;
import org.example.model.slideComponents.SlideItem;
import org.example.model.slideComponents.decorator.BoldDecorator;
import org.example.model.slideComponents.decorator.ItalicDecorator;

public class BitmapItemFactory implements AbstractSlideItemFactory {
    
    @Override
    public SlideItem createItem(int level, String imagePath) {
        return new BitmapItem(level, imagePath);
    }
    
    @Override
    public SlideItem createBoldItem(int level, String imagePath) {
        return this.createItem(level, imagePath);
    }
    
    @Override
    public SlideItem createItalicItem(int level, String imagePath) {
        return this.createItem(level, imagePath);
    }
    
    @Override
    public SlideItem createBoldItalicItem(int level, String imagePath) {
        return this.createItem(level, imagePath);
    }
} 