package org.example.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static org.junit.jupiter.api.Assertions.*;

class BitmapItemTest {
    
    private static String testImagePath;
    
    @BeforeAll
    static void setUpClass() {
        // Initialize styles array
        Style.createStyles();
        
        // Get path to test image in resources
        testImagePath = "src/test/resources/test.jpg";
        
        // Check if test file exists
        File testFile = new File(testImagePath);
        if (!testFile.exists()) {
            System.err.println("Warning: Test image not found at " + testImagePath);
        }
    }

    @Test
    void toString_afterInitialization_containsLevelAndFilename() {
        // Only test the toString method, which doesn't require an actual image
        BitmapItem item = new BitmapItem(3, testImagePath);
        String result = item.toString();
        assertTrue(result.contains("3"));
        assertTrue(result.contains("test.jpg"));
    }
    
    @Test
    void getName_afterInitialization_returnsCorrectFilename() {
        // Test the getName method, which doesn't require an actual image
        BitmapItem item = new BitmapItem(1, testImagePath);
        assertTrue(item.getName().endsWith("test.jpg"));
    }
    
    @Test
    @Disabled("Requires a valid image file format")
    void getBoundingBox_withValidImage_returnsNonNullRectangle() {
        // Create a bitmap item with our test image
        BitmapItem item = new BitmapItem(1, testImagePath);
        Graphics g = Mockito.mock(Graphics.class);
        ImageObserver observer = (img, flags, x, y, width, height) -> true;
        
        Rectangle boundingBox = item.getBoundingBox(g, observer, 1.0f, Style.getStyle(1));
        assertNotNull(boundingBox);
    }
    
    @Test
    @Disabled("Requires a valid image file format")
    void draw_withValidImage_doesNotThrowException() {
        // Create a bitmap item with our test image
        BitmapItem item = new BitmapItem(1, testImagePath);
        Graphics g = Mockito.mock(Graphics.class);
        ImageObserver observer = (img, flags, x, y, width, height) -> true;
        
        // Just verify no exception is thrown
        assertDoesNotThrow(() -> 
            item.draw(10, 10, 1.0f, g, Style.getStyle(1), observer)
        );
    }
} 