package org.example.model;

import org.example.model.slideComponents.SlideItem;
import org.example.model.slideComponents.TextItem;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

class SlideTest
{

    private Slide slide;

    @BeforeAll
    static void setUpClass()
    {
        Style.createStyles();
    }

    @BeforeEach
    void setUp()
    {
        this.slide = new Slide();
        this.slide.setTitle("Test Slide");
    }

    @Test
    void append_withSlideItem_addsItemToSlide()
    {
        SlideItem item = new TextItem(1, "Test Item");
        this.slide.append(item);

        assertEquals(1, this.slide.getSize());
        assertSame(item, this.slide.getSlideItem(0));
    }

    @Test
    void append_withLevelAndText_createsAndAddsTextItem()
    {
        this.slide.append(1, "Test Text");

        assertEquals(1, this.slide.getSize());
        SlideItem item = this.slide.getSlideItem(0);
        assertTrue(item instanceof TextItem);
        assertEquals("Test Text", ((TextItem) item).getText());
        assertEquals(1, item.getLevel());
    }

    @Test
    void getTitle_afterInitialization_returnsSetTitle()
    {
        assertEquals("Test Slide", this.slide.getTitle());
    }

    @Test
    void setTitle_withNewValue_updatesTitle()
    {
        this.slide.setTitle("New Title");
        assertEquals("New Title", this.slide.getTitle());
    }

    @Test
    void getSlideItems_withMultipleItems_returnsAllItems()
    {
        this.slide.append(new TextItem(1, "Item 1"));
        this.slide.append(new TextItem(2, "Item 2"));

        Vector<SlideItem> items = this.slide.getSlideItems();
        assertEquals(2, items.size());
        assertEquals("Item 1", ((TextItem) items.elementAt(0)).getText());
        assertEquals("Item 2", ((TextItem) items.elementAt(1)).getText());
    }

    @Test
    void getSize_afterAddingItems_returnsCorrectCount()
    {
        assertEquals(0, this.slide.getSize());

        this.slide.append(new TextItem(1, "Item 1"));
        assertEquals(1, this.slide.getSize());

        this.slide.append(new TextItem(2, "Item 2"));
        assertEquals(2, this.slide.getSize());
    }

    @Test
    void getBounds_forSlideWithItems_returnsNonEmptyRectangle()
    {
        assertNotNull(this.slide.getBounds());
        assertTrue(this.slide.getBounds().getWidth() > 0);
        assertTrue(this.slide.getBounds().getHeight() > 0);
    }
}
