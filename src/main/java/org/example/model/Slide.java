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
    protected String title;
    protected Vector<SlideItem> items;

    public Slide()
    {
        this.items = new Vector<SlideItem>();
    }

    public void append(SlideItem anItem)
    {
        this.items.addElement(anItem);
    }

    public String getTitle()
    {
        return this.title;
    }

    public void setTitle(String newTitle)
    {
        this.title = newTitle;
    }

    public void append(int level, String message)
    {
        this.append(new TextItem(level, message));
    }

    public SlideItem getSlideItem(int number)
    {
        return (SlideItem) this.items.elementAt(number);
    }

    public Vector<SlideItem> getSlideItems()
    {
        return this.items;
    }

    public int getSize()
    {
        return this.items.size();
    }

    public void draw(Graphics g, Rectangle area, ImageObserver view)
    {
        float scale = this.getScale(area);
        int y = area.y;
        // Title is handled separately
        SlideItem slideItem = new TextItem(0, this.getTitle());
        Style style = Style.getStyle(slideItem.getLevel());
        slideItem.draw(area.x, y, scale, g, style, view);
        y += slideItem.getBoundingBox(g, view, scale, style).height;
        for (int number = 0; number < this.getSize(); number++)
        {
            slideItem =(SlideItem) this.getSlideItems().elementAt(number);

            style = Style.getStyle(slideItem.getLevel());

            slideItem.draw(area.x, y, scale, g, style, view);
            y += slideItem.getBoundingBox(g, view, scale, style).height;
        }
    }

    private float getScale(Rectangle area)
    {
        return Math.min(((float) area.width) / ((float) WIDTH), ((float) area.height) / ((float) HEIGHT));
    }

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
        SlideItem titleItem = new TextItem(0, this.getTitle());
        Style titleStyle = Style.getStyle(titleItem.getLevel());
        Rectangle titleBounds = titleItem.getBoundingBox(g2d, observer, 1.0f, titleStyle);
        totalHeight += titleBounds.height;

        // Add content heights
        for (int number = 0; number < this.getSize(); number++)
        {
            SlideItem item = this.getSlideItems().elementAt(number);
            Style style = Style.getStyle(item.getLevel());
            Rectangle itemBounds = item.getBoundingBox(g2d, observer, 1.0f, style);
            totalHeight += itemBounds.height;
        }

        g2d.dispose();
        tempImage.flush();

        bounds.height = totalHeight;
        return bounds;
    }
}
