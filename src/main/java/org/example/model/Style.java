package org.example.model;

import java.awt.Color;
import java.awt.Font;
import java.io.InputStream;
import java.io.IOException;
import java.awt.FontFormatException;

public class Style
{
    private static Style[] styles; // Array of styles

    // Constants for colors
    private static final Color SNOW_WHITE = new Color(216, 222, 233);
    private static final Color BLUE = new Color(50, 100, 255);
    private static final Color RED = new Color(255, 75, 85);

    // Style properties
    private int indent;
    private Color color;
    private Font font;
    private int fontSize;
    private int leading;

    // Method to create styles
    public static void createStyles()
    {
        styles = new Style[5];

        // Creating styles for different levels
        styles[0] = new Style(0, RED, 48, 20);    // style for item-level 0
        styles[1] = new Style(20, BLUE, 40, 10);   // style for item-level 1
        styles[2] = new Style(50, SNOW_WHITE, 36, 10);  // style for item-level 2
        styles[3] = new Style(70, SNOW_WHITE, 30, 10);  // style for item-level 3
        styles[4] = new Style(90, SNOW_WHITE, 24, 10);  // style for item-level 4
    }

    // Method to fetch the style based on the level
    public static Style getStyle(int level)
    {
        if (level >= styles.length)
        {
            level = styles.length - 1;  // Ensure that level stays within bounds
        }
        return styles[level];
    }

    // Constructor to initialize the style and load the font
    public Style(int indent, Color color, int points, int leading)
    {
        this.indent = indent;
        this.color = color;
        this.fontSize = points;
        this.leading = leading;
        this.font = loadFont("Inter/Inter-VariableFont_opsz,wght.ttf", fontSize); // Load the variable font
    }

    // Method to load the font from resources
    private Font loadFont(String fontPath, int fontSize)
    {
        try
        {
            // Load the font from the resources folder using getResourceAsStream
            InputStream fontStream = getClass().getClassLoader().getResourceAsStream(fontPath);
            if (fontStream == null)
            {
                System.err.println("Font not found at path: " + fontPath);
                return new Font("Helvetica", Font.PLAIN, fontSize);  // Fallback font if not found
            }
            Font font = Font.createFont(Font.TRUETYPE_FONT, fontStream);
            return font.deriveFont(Font.PLAIN, fontSize); // Return the font with specified size
        } catch (FontFormatException | IOException e)
        {
            e.printStackTrace();
            return new Font("Helvetica", Font.PLAIN, fontSize); // Fallback in case of error
        }
    }

    // Getter methods for private fields:
    public int getIndent()
    {
        return indent;
    }

    public Color getColor()
    {
        return color;
    }

    public Font getFont()
    {
        return font;
    }

    public int getLeading()
    {
        return leading;
    }

    // Method to convert style to a string representation
    @Override
    public String toString()
    {
        return "[" + indent + ", " + color + "; " + fontSize + "pt on " + leading + "]";
    }

    // Method to get a scaled font based on the given scale
    public Font getFont(float scale)
    {
        return font.deriveFont(fontSize * scale);  // Scale the font size
    }
}
