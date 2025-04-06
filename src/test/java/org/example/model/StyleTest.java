package org.example.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.awt.Font;

import static org.junit.jupiter.api.Assertions.*;

class StyleTest
{

    @BeforeAll
    static void setUp()
    {
        Style.createStyles();
    }

    @Test
    void getStyle_withValidLevel_returnsCorrectStyle()
    {
        Style style0 = Style.getStyle(0);
        Style style1 = Style.getStyle(1);
        Style style4 = Style.getStyle(4);

        assertNotNull(style0);
        assertNotNull(style1);
        assertNotNull(style4);

        assertNotEquals(style0.getIndent(), style1.getIndent());
        assertNotEquals(style0.getColor(), style1.getColor());
    }

    @Test
    void getStyle_withInvalidLevel_returnsLastValidStyle()
    {
        Style style = Style.getStyle(100);
        assertNotNull(style);
        assertEquals(Style.getStyle(4).getIndent(), style.getIndent());
    }

    @Test
    void constructor_withValidParameters_createsStyleWithCorrectValues()
    {
        int indent = 30;
        Color color = new Color(100, 100, 100);
        int fontSize = 20;
        int leading = 5;

        Style style = new Style(indent, color, fontSize, leading);

        assertEquals(indent, style.getIndent());
        assertEquals(color, style.getColor());
        assertEquals(leading, style.getLeading());
        assertNotNull(style.getFont());
    }

    @Test
    void getters_afterInitialization_returnCorrectValues()
    {
        int indent = 40;
        Color color = new Color(200, 100, 50);
        int fontSize = 24;
        int leading = 8;

        Style style = new Style(indent, color, fontSize, leading);

        assertEquals(indent, style.getIndent());
        assertEquals(color, style.getColor());
        assertEquals(leading, style.getLeading());
        assertNotNull(style.getFont());
    }

    @Test
    void getFont_withScale_returnsScaledFont()
    {
        Style style = new Style(20, new Color(0, 0, 0), 30, 10);

        Font originalFont = style.getFont();
        Font scaledFont = style.getFont(2.0f);

        assertNotNull(scaledFont);
        assertTrue(scaledFont.getSize() > originalFont.getSize());
    }

    @Test
    void toString_afterInitialization_containsAllAttributes()
    {
        Style style = new Style(25, new Color(0, 0, 0), 18, 12);

        String result = style.toString();

        assertNotNull(result);
        assertTrue(result.contains("25"));
        assertTrue(result.contains("18"));
        assertTrue(result.contains("12"));
    }
} 