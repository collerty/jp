package org.example.model;

import org.example.model.slideComponents.TextItem;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.text.AttributedString;

import static org.junit.jupiter.api.Assertions.*;

class TextItemTest {

    @BeforeAll
    static void setUpClass() {
        // Initialize styles array
        Style.createStyles();
    }

    @Test
    void constructor_withLevelAndText_setsPropertiesCorrectly() {
        TextItem item = new TextItem(2, "Test Text");
        assertEquals(2, item.getLevel());
        assertEquals("Test Text", item.getText());
    }

    @Test
    void constructor_withNoParameters_setsDefaultValues() {
        TextItem item = new TextItem();
        assertEquals(0, item.getLevel());
        assertEquals("No Text Given", item.getText());
    }

    @Test
    void getText_afterInitialization_returnsCorrectText() {
        TextItem item = new TextItem(1, "Sample Text");
        assertEquals("Sample Text", item.getText());
        
        // Test with null text
        TextItem nullItem = new TextItem(1, null);
        assertNull(nullItem.getText());
    }

    @Test
    void getAttributedString_withValidStyleAndScale_returnsNonNullString() {
        TextItem item = new TextItem(1, "Test");
        Style style = new Style(1, new Color(0, 0, 0), 20, 10);
        float scale = 1.0f;
        
        AttributedString result = item.getAttributedString(style, scale);
        assertNotNull(result);
        
        // Test with empty text
        TextItem emptyItem = new TextItem(1, "");
        AttributedString emptyResult = emptyItem.getAttributedString(style, scale);
        assertNotNull(emptyResult);
    }
    
    @Test
    void getBoundingBox_withValidParameters_returnsCorrectRectangle() {
        TextItem item = new TextItem(1, "Test Text");
        Style style = new Style(10, new Color(0, 0, 0), 20, 10);
        
        // Create a simple mock Graphics object
        BufferedImage bi = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.createGraphics();
        
        Rectangle boundingBox = item.getBoundingBox(g, null, 1.0f, style);
        
        assertNotNull(boundingBox);
        assertTrue(boundingBox.width > 0);
        assertTrue(boundingBox.height > 0);
        assertEquals(10, boundingBox.x); // Should match style indent
    }
    
    @Test
    void draw_withNullText_doesNotThrowException() {
        TextItem item = new TextItem(1, null);
        Style style = new Style(10, new Color(0, 0, 0), 20, 10);
        
        // Create a simple mock Graphics object
        BufferedImage bi = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.createGraphics();
        
        // This should not throw an exception
        item.draw(10, 10, 1.0f, g, style, null);
    }
    
    @Test
    void draw_withValidText_doesNotThrowException() {
        TextItem item = new TextItem(1, "Test Text");
        Style style = new Style(10, new Color(0, 0, 0), 20, 10);
        
        // Create a simple mock Graphics object
        BufferedImage bi = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.createGraphics();
        
        // This should not throw an exception
        item.draw(10, 10, 1.0f, g, style, null);
    }

    @Test
    void toString_afterInitialization_containsLevelAndText() {
        TextItem item = new TextItem(3, "Test String");
        String result = item.toString();
        assertTrue(result.contains("3"));
        assertTrue(result.contains("Test String"));
    }
    
    @Test
    void getBoundingBox_withRepeatedCalls_usesCachedLayoutsWhenPossible() {
        TextItem item = new TextItem(1, "Test Text");
        Style style = new Style(10, new Color(0, 0, 0), 20, 10);
        
        // Create a simple mock Graphics object
        BufferedImage bi = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.createGraphics();
        
        // First call should calculate layouts
        Rectangle boundingBox1 = item.getBoundingBox(g, null, 1.0f, style);
        
        // Second call should use cached layouts
        Rectangle boundingBox2 = item.getBoundingBox(g, null, 1.0f, style);
        
        assertEquals(boundingBox1.width, boundingBox2.width);
        assertEquals(boundingBox1.height, boundingBox2.height);
        
        // Different scale should recalculate layouts
        Rectangle boundingBox3 = item.getBoundingBox(g, null, 2.0f, style);
        
        assertNotEquals(boundingBox1.width, boundingBox3.width);
    }
} 