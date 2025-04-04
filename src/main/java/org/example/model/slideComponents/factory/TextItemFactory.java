package org.example.model.slideComponents.factory;

import org.example.model.slideComponents.SlideItem;
import org.example.model.slideComponents.TextItem;
import org.example.model.slideComponents.decorator.BoldDecorator;
import org.example.model.slideComponents.decorator.ItalicDecorator;

/**
 * Factory for creating TextItem instances.
 * Implements the Abstract Factory pattern.
 */
public class TextItemFactory implements AbstractSlideItemFactory {
    
    @Override
    public SlideItem createItem(int level, String content) {
        return new TextItem(level, content);
    }
    
    @Override
    public SlideItem createBoldItem(int level, String content) {
        return new BoldDecorator(new TextItem(level, content));
    }
    
    @Override
    public SlideItem createItalicItem(int level, String content) {
        return new ItalicDecorator(new TextItem(level, content));
    }
    
    @Override
    public SlideItem createBoldItalicItem(int level, String content) {
        return new BoldDecorator(new ItalicDecorator(new TextItem(level, content)));
    }
    
    /**
     * Creates a formatted text item based on the given formatting options
     */
    public SlideItem createFormattedTextItem(int level, String text, boolean bold, boolean italic) {
        if (bold && italic) {
            return createBoldItalicItem(level, text);
        } else if (bold) {
            return createBoldItem(level, text);
        } else if (italic) {
            return createItalicItem(level, text);
        }
        return createItem(level, text);
    }
} 