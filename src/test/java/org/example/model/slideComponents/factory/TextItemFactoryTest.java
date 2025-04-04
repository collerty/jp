package org.example.model.slideComponents.factory;

import org.example.model.slideComponents.SlideItem;
import org.example.model.slideComponents.TextItem;
import org.example.model.slideComponents.decorator.BoldDecorator;
import org.example.model.slideComponents.decorator.ItalicDecorator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TextItemFactoryTest {

    private final TextItemFactory factory = new TextItemFactory();
    
    @Test
    void createItem_returnsTextItemInstance() {
        SlideItem item = factory.createItem(1, "Test Text");
        assertTrue(item instanceof TextItem);
        assertEquals(1, item.getLevel());
        assertEquals("Test Text", ((TextItem)item).getText());
    }
    
    @Test
    void createItem_withEmptyText_returnsTextItemWithEmptyText() {
        SlideItem item = factory.createItem(1, "");
        assertTrue(item instanceof TextItem);
        assertEquals("", ((TextItem)item).getText());
    }
    
    @Test
    void createItem_withNullText_returnsTextItemWithNullText() {
        SlideItem item = factory.createItem(1, null);
        assertTrue(item instanceof TextItem);
        assertNull(((TextItem)item).getText());
    }
    
    @Test
    void createItem_withWhitespaceText_returnsTextItemWithWhitespaceText() {
        SlideItem item = factory.createItem(1, "  \t  ");
        assertTrue(item instanceof TextItem);
        assertEquals("  \t  ", ((TextItem)item).getText());
    }

    @Test
    void createBoldItem_returnsBoldDecorator() {
        SlideItem item = factory.createBoldItem(1, "Bold Text");
        assertTrue(item instanceof BoldDecorator);
        assertEquals(1, item.getLevel());
        
        SlideItem decoratedItem = ((BoldDecorator)item).getDecoratedItem();
        assertTrue(decoratedItem instanceof TextItem);
        assertEquals("Bold Text", ((TextItem)decoratedItem).getText());
    }

    @Test
    void createItalicItem_returnsItalicDecorator() {
        SlideItem item = factory.createItalicItem(1, "Italic Text");
        assertTrue(item instanceof ItalicDecorator);
        assertEquals(1, item.getLevel());
        
        SlideItem decoratedItem = ((ItalicDecorator)item).getDecoratedItem();
        assertTrue(decoratedItem instanceof TextItem);
        assertEquals("Italic Text", ((TextItem)decoratedItem).getText());
    }

    @Test
    void createBoldItalicItem_returnsBoldDecoratorWrappingItalicDecorator() {
        SlideItem item = factory.createBoldItalicItem(1, "Bold Italic Text");
        assertTrue(item instanceof BoldDecorator);
        assertEquals(1, item.getLevel());
        
        SlideItem italicItem = ((BoldDecorator)item).getDecoratedItem();
        assertTrue(italicItem instanceof ItalicDecorator);
        
        SlideItem textItem = ((ItalicDecorator)italicItem).getDecoratedItem();
        assertTrue(textItem instanceof TextItem);
        assertEquals("Bold Italic Text", ((TextItem)textItem).getText());
    }
    
    @Test
    void createFormattedTextItem_withNoFormatting_returnsTextItem() {
        SlideItem item = factory.createFormattedTextItem(1, "Plain Text", false, false);
        assertTrue(item instanceof TextItem);
        assertEquals("Plain Text", ((TextItem)item).getText());
    }
    
    @Test
    void createFormattedTextItem_withBoldOnly_returnsBoldDecorator() {
        SlideItem item = factory.createFormattedTextItem(1, "Bold Text", true, false);
        assertTrue(item instanceof BoldDecorator);
        
        SlideItem textItem = ((BoldDecorator)item).getDecoratedItem();
        assertTrue(textItem instanceof TextItem);
        assertEquals("Bold Text", ((TextItem)textItem).getText());
    }
    
    @Test
    void createFormattedTextItem_withItalicOnly_returnsItalicDecorator() {
        SlideItem item = factory.createFormattedTextItem(1, "Italic Text", false, true);
        assertTrue(item instanceof ItalicDecorator);
        
        SlideItem textItem = ((ItalicDecorator)item).getDecoratedItem();
        assertTrue(textItem instanceof TextItem);
        assertEquals("Italic Text", ((TextItem)textItem).getText());
    }
    
    @Test
    void createFormattedTextItem_withBoldAndItalic_returnsBoldDecoratorWrappingItalicDecorator() {
        SlideItem item = factory.createFormattedTextItem(1, "Bold Italic Text", true, true);
        assertTrue(item instanceof BoldDecorator);
        
        SlideItem italicItem = ((BoldDecorator)item).getDecoratedItem();
        assertTrue(italicItem instanceof ItalicDecorator);
        
        SlideItem textItem = ((ItalicDecorator)italicItem).getDecoratedItem();
        assertTrue(textItem instanceof TextItem);
        assertEquals("Bold Italic Text", ((TextItem)textItem).getText());
    }
} 