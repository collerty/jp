package org.example.model.slideComponents;

import org.example.model.Slide;
import org.example.model.Style;

import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.TextLayout;
import java.awt.font.TextAttribute;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageObserver;
import java.text.AttributedString;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;


public class TextItem extends SlideItem
{
    private final String text;
    private List<TextLayout> cachedLayouts = null;
    private float lastScale = -1;
    private Style lastStyle = null;

    private static final String EMPTY_TEXT = "No Text Given";

    // a textitem of level level, with the text string
    public TextItem(int level, String string)
    {
        super(level);
        this.text = string;
    }

    // an empty textitem
    public TextItem()
    {
        this(0, EMPTY_TEXT);
    }

    // give the text
    public String getText()
    {
        return this.text;
    }

    public AttributedString getAttributedString(Style style, float scale)
    {
        String displayText = this.getText();
        if (displayText == null || displayText.isEmpty())
        {
            displayText = EMPTY_TEXT;
        }
        AttributedString attrStr = new AttributedString(displayText);
        attrStr.addAttribute(TextAttribute.FONT, style.getFont(scale), 0, displayText.length());
        return attrStr;
    }

    // give the bounding box of the item
    public Rectangle getBoundingBox(Graphics g, ImageObserver observer,
                                    float scale, Style myStyle)
    {
        List<TextLayout> layouts = this.getLayouts(g, myStyle, scale);
        int xsize = 0, ysize = (int) (myStyle.getLeading() * scale);
        Iterator<TextLayout> iterator = layouts.iterator();
        while (iterator.hasNext())
        {
            TextLayout layout = iterator.next();
            Rectangle2D bounds = layout.getBounds();
            if (bounds.getWidth() > xsize)
            {
                xsize = (int) bounds.getWidth();
            }
            if (bounds.getHeight() > 0)
            {
                ysize += (int) bounds.getHeight();
            }
            ysize += (int) (layout.getLeading() + layout.getDescent());
        }
        return new Rectangle((int) (myStyle.getIndent() * scale), 0, xsize, ysize);
    }

    // draw the item
    public void draw(int x, int y, float scale, Graphics g,
                     Style myStyle, ImageObserver o)
    {
        if (this.text == null || this.text.isEmpty())
        {
            return;
        }
        List<TextLayout> layouts = this.getLayouts(g, myStyle, scale);
        Point pen = new Point(x + (int) (myStyle.getIndent() * scale),
                y + (int) (myStyle.getLeading() * scale));
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(myStyle.getColor());
        Iterator<TextLayout> it = layouts.iterator();
        while (it.hasNext())
        {
            TextLayout layout = it.next();
            pen.y += (int) layout.getAscent();
            layout.draw(g2d, pen.x, pen.y);
            pen.y += (int) layout.getDescent();
        }
    }

    private List<TextLayout> getLayouts(Graphics g, Style s, float scale)
    {
        // Return cached layouts if they exist and scale/style hasn't changed
        if (this.cachedLayouts != null && this.lastScale == scale && this.lastStyle == s)
        {
            return this.cachedLayouts;
        }

        List<TextLayout> layouts = new ArrayList<TextLayout>();
        AttributedString attrStr = this.getAttributedString(s, scale);
        Graphics2D g2d = (Graphics2D) g;
        FontRenderContext frc = g2d.getFontRenderContext();
        LineBreakMeasurer measurer = new LineBreakMeasurer(attrStr.getIterator(), frc);
        float wrappingWidth = (Slide.WIDTH - s.getIndent()) * scale;
        String textValue = this.getText();
        int textLength = textValue == null ? 0 : textValue.length();
        while (measurer.getPosition() < textLength)
        {
            TextLayout layout = measurer.nextLayout(wrappingWidth);
            layouts.add(layout);
        }

        // Cache the layouts
        this.cachedLayouts = layouts;
        this.lastScale = scale;
        this.lastStyle = s;
        return layouts;
    }

    public void clearLayoutCache()
    {
        this.cachedLayouts = null;
        this.lastScale = -1;
        this.lastStyle = null;
    }

    public String toString()
    {
        return "java.com.TextItem[" + this.getLevel() + "," + this.getText() + "]";
    }
}
