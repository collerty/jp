package org.example.model.slideComponents.factory;

import org.example.model.slideComponents.SlideItem;
import org.example.model.slideComponents.TextItem;
import org.example.model.slideComponents.decorator.BoldDecorator;
import org.example.model.slideComponents.decorator.ItalicDecorator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TextItemFactoryTest
{

    private final TextItemFactory factory = new TextItemFactory();

    @Test
    void createItem_returnsTextItemInstance()
    {
        SlideItem item = this.factory.createItem(1, "Test Text");
        assertTrue(item instanceof TextItem);
        assertEquals(1, item.getLevel());
        assertEquals("Test Text", ((TextItem) item).getText());
    }

    @Test
    void createItem_withEmptyText_returnsTextItemWithEmptyText()
    {
        SlideItem item = this.factory.createItem(1, "");
        assertTrue(item instanceof TextItem);
        assertEquals("", ((TextItem) item).getText());
    }

    @Test
    void createItem_withNullText_returnsTextItemWithNullText()
    {
        SlideItem item = this.factory.createItem(1, null);
        assertTrue(item instanceof TextItem);
        assertNull(((TextItem) item).getText());
    }

    @Test
    void createItem_withWhitespaceText_returnsTextItemWithWhitespaceText()
    {
        SlideItem item = this.factory.createItem(1, "  \t  ");
        assertTrue(item instanceof TextItem);
        assertEquals("  \t  ", ((TextItem) item).getText());
    }

    @Test
    void createFormattedItem_withNoFormatting_returnsTextItem()
    {
        SlideItem item = this.factory.createFormattedItem(1, "Plain Text", false, false);
        assertTrue(item instanceof TextItem);
        assertFalse(item instanceof BoldDecorator);
        assertFalse(item instanceof ItalicDecorator);
    }

    @Test
    void createFormattedItem_withBoldOnly_returnsBoldDecorator()
    {
        SlideItem item = this.factory.createFormattedItem(1, "Bold Text", true, false);
        assertTrue(item instanceof BoldDecorator);
        
        SlideItem textItem = ((BoldDecorator)item).getDecoratedItem();
        assertTrue(textItem instanceof TextItem);
        assertEquals("Bold Text", ((TextItem)textItem).getText());
    }
    
    @Test
    void createFormattedItem_withItalicOnly_returnsItalicDecorator() {
        SlideItem item = this.factory.createFormattedItem(1, "Italic Text", false, true);
        assertTrue(item instanceof ItalicDecorator);
        
        SlideItem textItem = ((ItalicDecorator)item).getDecoratedItem();
        assertTrue(textItem instanceof TextItem);
        assertEquals("Italic Text", ((TextItem)textItem).getText());
    }
    
    @Test
    void createFormattedItem_withEmptyText_returnsItemWithEmptyText() {
        // Test with different formatting combinations
        SlideItem plainItem = this.factory.createFormattedItem(1, "", false, false);
        assertTrue(plainItem instanceof TextItem);
        assertEquals("", ((TextItem)plainItem).getText());
        
        SlideItem boldItem = this.factory.createFormattedItem(1, "", true, false);
        assertTrue(boldItem instanceof BoldDecorator);
        assertEquals("", ((TextItem)((BoldDecorator)boldItem).getDecoratedItem()).getText());
    }
    
    @Test
    void createFormattedItem_withNullText_returnsItemWithNullText() {
        SlideItem plainItem = this.factory.createFormattedItem(1, null, false, false);
        assertTrue(plainItem instanceof TextItem);
        assertNull(((TextItem)plainItem).getText());
        
        SlideItem italicItem = this.factory.createFormattedItem(1, null, false, true);
        assertTrue(italicItem instanceof ItalicDecorator);
        assertNull(((TextItem)((ItalicDecorator)italicItem).getDecoratedItem()).getText());
    }
} 