package org.example.model.slideComponents.factory;

import org.example.model.slideComponents.BitmapItem;
import org.example.model.slideComponents.SlideItem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BitmapItemFactoryTest
{

    private final BitmapItemFactory factory = new BitmapItemFactory();

    @Test
    void createItem_returnsBitmapItemInstance()
    {
        SlideItem item = this.factory.createItem(1, "test.jpg");
        assertTrue(item instanceof BitmapItem);
        assertEquals(1, item.getLevel());
        assertEquals("test.jpg", ((BitmapItem) item).getName());
    }

    @Test
    void createItem_withEmptyPath_returnsBitmapItemWithEmptyPath()
    {
        SlideItem item = this.factory.createItem(1, "");
        assertTrue(item instanceof BitmapItem);
        assertEquals("", ((BitmapItem) item).getName());
    }

    @Test
    void createItem_withNullPath_returnsBitmapItemWithNullPath()
    {
        SlideItem item = this.factory.createItem(1, null);
        assertTrue(item instanceof BitmapItem);
        assertNull(((BitmapItem) item).getName());
    }

    @Test
    void createItem_withWhitespacePath_returnsBitmapItemWithWhitespacePath()
    {
        SlideItem item = this.factory.createItem(1, "  \t  ");
        assertTrue(item instanceof BitmapItem);
        assertEquals("  \t  ", ((BitmapItem) item).getName());
    }

    @Test
    void createBoldItem_returnsBitmapItemUndecorated()
    {
        SlideItem item = this.factory.createBoldItem(1, "test.jpg");
        assertTrue(item instanceof BitmapItem);
        assertFalse(item.getClass().getSimpleName().contains("Decorator"));
        assertEquals(1, item.getLevel());
        assertEquals("test.jpg", ((BitmapItem) item).getName());
    }

    @Test
    void createItalicItem_returnsBitmapItemUndecorated()
    {
        SlideItem item = this.factory.createItalicItem(1, "test.jpg");
        assertTrue(item instanceof BitmapItem);
        assertFalse(item.getClass().getSimpleName().contains("Decorator"));
        assertEquals(1, item.getLevel());
        assertEquals("test.jpg", ((BitmapItem) item).getName());
    }

    @Test
    void createBoldItalicItem_returnsBitmapItemUndecorated()
    {
        SlideItem item = this.factory.createBoldItalicItem(1, "test.jpg");
        assertTrue(item instanceof BitmapItem);
        assertFalse(item.getClass().getSimpleName().contains("Decorator"));
        assertEquals(1, item.getLevel());
        assertEquals("test.jpg", ((BitmapItem) item).getName());
    }
} 