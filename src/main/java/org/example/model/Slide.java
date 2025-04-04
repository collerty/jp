package org.example.model;

import org.example.model.slideComponents.SlideItem;
import org.example.model.slideComponents.TextItem;
import org.example.model.slideComponents.decorator.BoldDecorator;
import org.example.model.slideComponents.decorator.ItalicDecorator;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import java.util.Vector;
import java.awt.Graphics2D;

public class Slide
{
    public final static int WIDTH = 960;
    public final static int HEIGHT = 720;
    protected String title; // title is saved separately
    protected Vector<SlideItem> items; // slide items are saved in a Vector

    public Slide()
    {
        items = new Vector<SlideItem>();
    }

    // Add a slide item
    public void append(SlideItem anItem)
    {
        items.addElement(anItem);
    }

    // give the title of the slide
    public String getTitle()
    {
        return title;
    }

    // change the title of the slide
    public void setTitle(String newTitle)
    {
        title = newTitle;
    }

    // Create java.com.TextItem of String, and add the java.com.TextItem
    public void append(int level, String message)
    {
        append(new TextItem(level, message));
    }

    // give the  java.com.SlideItem
    public SlideItem getSlideItem(int number)
    {
        return (SlideItem) items.elementAt(number);
    }

    // give all SlideItems in a Vector
    public Vector<SlideItem> getSlideItems()
    {
        return items;
    }

    // give the size of the java.com.Slide
    public int getSize()
    {
        return items.size();
    }

    // draw the slide
    public void draw(Graphics g, Rectangle area, ImageObserver view)
    {
        float scale = getScale(area);
        int y = area.y;
        // Title is handled separately
        SlideItem slideItem = new TextItem(0, getTitle());
        Style style = Style.getStyle(slideItem.getLevel());
        slideItem.draw(area.x, y, scale, g, style, view);
        y += slideItem.getBoundingBox(g, view, scale, style).height;
        for (int number = 0; number < getSize(); number++)
        {
            slideItem =(SlideItem) getSlideItems().elementAt(number);

            style = Style.getStyle(slideItem.getLevel());

            slideItem.draw(area.x, y, scale, g, style, view);
            y += slideItem.getBoundingBox(g, view, scale, style).height;
        }
    }

    // Give the scale for drawing
    private float getScale(Rectangle area)
    {
        return Math.min(((float) area.width) / ((float) WIDTH), ((float) area.height) / ((float) HEIGHT));
    }

    // Calculate the actual bounds of the slide content
    public Rectangle getBounds()
    {
        Rectangle bounds = new Rectangle(0, 0, WIDTH, HEIGHT);
        int maxWidth = WIDTH;
        int totalHeight = 0;

        // Create a temporary graphics context for measurements
        java.awt.image.BufferedImage tempImage = new java.awt.image.BufferedImage(1, 1, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = tempImage.createGraphics();
        ImageObserver observer = null; // We don't need an observer for measurements

        // Add title height
        SlideItem titleItem = new TextItem(0, getTitle());
        Style titleStyle = Style.getStyle(titleItem.getLevel());
        Rectangle titleBounds = titleItem.getBoundingBox(g2d, observer, 1.0f, titleStyle);
        totalHeight += titleBounds.height;

        // Add content heights
        for (int number = 0; number < getSize(); number++)
        {
            SlideItem item = getSlideItems().elementAt(number);
            Style style = Style.getStyle(item.getLevel());
            Rectangle itemBounds = item.getBoundingBox(g2d, observer, 1.0f, style);
            totalHeight += itemBounds.height;
        }

        // Clean up
        g2d.dispose();
        tempImage.flush();

        // Update bounds with actual content size
        bounds.height = totalHeight;
        return bounds;
    }
}
