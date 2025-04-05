package org.example.model.slideComponents.factory;

import org.example.model.slideComponents.SlideItem;

/**
 * Abstract Factory interface for creating slide items.
 * This interface defines the contract for concrete factories that create different types of slide items.
 */
public interface AbstractSlideItemFactory {
    /**
     * Creates a basic slide item
     * 
     * @param level the level of the item
     * @param content the content (text or path) for the item
     * @return a new SlideItem
     */
    SlideItem createItem(int level, String content);
    
    /**
     * Creates a bold version of a slide item (if applicable)
     * 
     * @param level the level of the item
     * @param content the content for the item
     * @return a bold-formatted SlideItem
     */
    SlideItem createBoldItem(int level, String content);
    
    /**
     * Creates an italic version of a slide item (if applicable)
     * 
     * @param level the level of the item
     * @param content the content for the item
     * @return an italic-formatted SlideItem
     */
    SlideItem createItalicItem(int level, String content);
    
    /**
     * Creates a bold and italic version of a slide item (if applicable)
     * 
     * @param level the level of the item
     * @param content the content for the item
     * @return a bold and italic-formatted SlideItem
     */
    SlideItem createBoldItalicItem(int level, String content);
} 