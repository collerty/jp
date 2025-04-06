package org.example.model;

import org.example.model.slideComponents.BitmapItem;
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

class BitmapItemTest
{

    private static String testImagePath;

    @BeforeAll
    static void setUpClass()
    {
        Style.createStyles();

        testImagePath = "src/test/resources/test.jpg";

        File testFile = new File(testImagePath);
        if (!testFile.exists())
        {
            System.err.println("Warning: Test image not found at " + testImagePath);
        }
    }

    @Test
    void toString_afterInitialization_containsLevelAndFilename()
    {
        BitmapItem item = new BitmapItem(3, testImagePath);
        String result = item.toString();
        assertTrue(result.contains("3"));
        assertTrue(result.contains("test.jpg"));
    }

    @Test
    void getName_afterInitialization_returnsCorrectFilename()
    {
        BitmapItem item = new BitmapItem(1, testImagePath);
        assertTrue(item.getName().endsWith("test.jpg"));
    }

}