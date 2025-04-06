package org.example.model;

import org.example.model.slideComponents.SlideItem;
import org.example.model.slideComponents.TextItem;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

class SlideTest {
    
    private Slide slide;
    
    @BeforeAll
    static void setUpClass() {
        // Initialize styles array
        Style.createStyles();
    }
    
    @BeforeEach
    void setUp() {
        this.slide = new Slide();
        slide.setTitle("Test Slide");
    }
    
    @Test
    void append_withSlideItem_addsItemToSlide() {
        SlideItem item = new TextItem(1, "Test Item");
        slide.append(item);
        
        assertEquals(1, slide.getSize());
        assertSame(item, slide.getSlideItem(0));
    }
    
    @Test
    void append_withLevelAndText_createsAndAddsTextItem() {
        slide.append(1, "Test Text");
        
        assertEquals(1, slide.getSize());
        SlideItem item = slide.getSlideItem(0);
        assertTrue(item instanceof TextItem);
        assertEquals("Test Text", ((TextItem)item).getText());
        assertEquals(1, item.getLevel());
    }
    
    @Test
    void getTitle_afterInitialization_returnsSetTitle() {
        assertEquals("Test Slide", slide.getTitle());
    }
    
    @Test
    void setTitle_withNewValue_updatesTitle() {
        slide.setTitle("New Title");
        assertEquals("New Title", slide.getTitle());
    }
    
    @Test
    void getSlideItems_withMultipleItems_returnsAllItems() {
        slide.append(new TextItem(1, "Item 1"));
        slide.append(new TextItem(2, "Item 2"));
        
        Vector<SlideItem> items = slide.getSlideItems();
        assertEquals(2, items.size());
        assertEquals("Item 1", ((TextItem)items.elementAt(0)).getText());
        assertEquals("Item 2", ((TextItem)items.elementAt(1)).getText());
    }
    
    @Test
    void getSize_afterAddingItems_returnsCorrectCount() {
        assertEquals(0, slide.getSize());
        
        slide.append(new TextItem(1, "Item 1"));
        assertEquals(1, slide.getSize());
        
        slide.append(new TextItem(2, "Item 2"));
        assertEquals(2, slide.getSize());
    }
    
    @Test
    void getBounds_forSlideWithItems_returnsNonEmptyRectangle() {
        // Test that the bounds method returns a non-null Rectangle
        assertNotNull(slide.getBounds());
        assertTrue(slide.getBounds().getWidth() > 0);
        assertTrue(slide.getBounds().getHeight() > 0);
    }
}
