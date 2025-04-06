package org.example.model.slideComponents.factory;

import org.example.model.slideComponents.BitmapItem;
import org.example.model.slideComponents.SlideItem;
import org.example.model.slideComponents.TextItem;
import org.example.model.slideComponents.decorator.BoldDecorator;
import org.example.model.slideComponents.decorator.ItalicDecorator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SlideItemFactoryTest
{

    @BeforeAll
    static void setUp()
    {
        // Initialize any required resources
    }

    @Test
    void createTextItem_returnsTextItemInstance()
    {
        SlideItem item = SlideItemFactory.createTextItem(1, "Test Text");
        assertTrue(item instanceof TextItem);
        assertEquals(1, item.getLevel());
    }

    @Test
    void createTextItem_withEmptyText_returnsTextItemWithEmptyText()
    {
        SlideItem item = SlideItemFactory.createTextItem(1, "");
        assertTrue(item instanceof TextItem);
        assertEquals("", ((TextItem) item).getText());
    }

    @Test
    void createTextItem_withNullText_returnsTextItemWithNullText()
    {
        SlideItem item = SlideItemFactory.createTextItem(1, null);
        assertTrue(item instanceof TextItem);
        assertNull(((TextItem) item).getText());
    }

    @Test
    void createBoldTextItem_returnsBoldDecorator()
    {
        SlideItem item = SlideItemFactory.createBoldTextItem(1, "Bold Text");
        assertTrue(item instanceof BoldDecorator);
        assertEquals(1, item.getLevel());
    }

    @Test
    void createItalicTextItem_returnsItalicDecorator()
    {
        SlideItem item = SlideItemFactory.createItalicTextItem(1, "Italic Text");
        assertTrue(item instanceof ItalicDecorator);
        assertEquals(1, item.getLevel());
    }

    @Test
    void createBoldItalicTextItem_returnsBoldDecoratorWrappingItalicDecorator()
    {
        SlideItem item = SlideItemFactory.createBoldItalicTextItem(1, "Bold Italic Text");
        assertTrue(item instanceof BoldDecorator);

        SlideItem innerItem = ((BoldDecorator) item).getDecoratedItem();
        assertTrue(innerItem instanceof ItalicDecorator);
        assertEquals(1, item.getLevel());
    }

    @Test
    void createBitmapItem_returnsBitmapItemInstance()
    {
        SlideItem item = SlideItemFactory.createBitmapItem(1, "test.jpg");
        assertTrue(item instanceof BitmapItem);
        assertEquals(1, item.getLevel());
    }

    @Test
    void createBitmapItem_withEmptyPath_returnsBitmapItemWithEmptyPath()
    {
        SlideItem item = SlideItemFactory.createBitmapItem(1, "");
        assertTrue(item instanceof BitmapItem);
        assertEquals("", ((BitmapItem) item).getName());
    }

    @Test
    void createBitmapItem_withNullPath_returnsBitmapItemWithNullPath()
    {
        SlideItem item = SlideItemFactory.createBitmapItem(1, null);
        assertTrue(item instanceof BitmapItem);
        assertNull(((BitmapItem) item).getName());
    }

    @Test
    void createFormattedTextItem_withNoFormatting_returnsTextItem()
    {
        SlideItem item = SlideItemFactory.createFormattedTextItem(1, "Plain Text", false, false);
        assertTrue(item instanceof TextItem);
        assertFalse(item instanceof BoldDecorator);
        assertFalse(item instanceof ItalicDecorator);
    }

    @Test
    void createFormattedTextItem_withBoldOnly_returnsBoldDecorator()
    {
        SlideItem item = SlideItemFactory.createFormattedTextItem(1, "Bold Text", true, false);
        assertTrue(item instanceof BoldDecorator);
        assertFalse(((BoldDecorator) item).getDecoratedItem() instanceof ItalicDecorator);
    }

    @Test
    void createFormattedTextItem_withItalicOnly_returnsItalicDecorator()
    {
        SlideItem item = SlideItemFactory.createFormattedTextItem(1, "Italic Text", false, true);
        assertTrue(item instanceof ItalicDecorator);
        assertFalse(((ItalicDecorator) item).getDecoratedItem() instanceof BoldDecorator);
    }

    @Test
    void createFormattedTextItem_withBoldAndItalic_returnsBoldDecoratorWrappingItalicDecorator()
    {
        SlideItem item = SlideItemFactory.createFormattedTextItem(1, "Bold Italic Text", true, true);
        assertTrue(item instanceof BoldDecorator);
        assertTrue(((BoldDecorator) item).getDecoratedItem() instanceof ItalicDecorator);
    }

    @Test
    void createFormattedTextItem_withEmptyText_returnsItemWithEmptyText()
    {
        SlideItem plainItem = SlideItemFactory.createFormattedTextItem(1, "", false, false);
        assertTrue(plainItem instanceof TextItem);
        assertEquals("", ((TextItem) plainItem).getText());

        SlideItem boldItem = SlideItemFactory.createFormattedTextItem(1, "", true, false);
        assertTrue(boldItem instanceof BoldDecorator);
        assertEquals("", ((TextItem) ((BoldDecorator) boldItem).getDecoratedItem()).getText());
    }

    @Test
    void createFormattedTextItem_withNullText_returnsItemWithNullText()
    {
        SlideItem plainItem = SlideItemFactory.createFormattedTextItem(1, null, false, false);
        assertTrue(plainItem instanceof TextItem);
        assertNull(((TextItem) plainItem).getText());

        SlideItem italicItem = SlideItemFactory.createFormattedTextItem(1, null, false, true);
        assertTrue(italicItem instanceof ItalicDecorator);
        assertNull(((TextItem) ((ItalicDecorator) italicItem).getDecoratedItem()).getText());
    }

    @Test
    void getFactory_withTextParameter_returnsTextItemFactory()
    {
        AbstractSlideItemFactory factory = SlideItemFactory.getFactory(true);
        assertTrue(factory instanceof TextItemFactory);
    }

    @Test
    void getFactory_withBitmapParameter_returnsBitmapItemFactory()
    {
        AbstractSlideItemFactory factory = SlideItemFactory.getFactory(false);
        assertTrue(factory instanceof BitmapItemFactory);
    }
} 