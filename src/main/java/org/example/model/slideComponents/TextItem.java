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
	private String text;
	private List<TextLayout> cachedLayouts = null;
	private float lastScale = -1;
	private Style lastStyle = null;

	private static final String EMPTYTEXT = "No Text Given";

// a textitem of level level, with the text string
	public TextItem(int level, String string) {
		super(level);
		text = string;
	}

// an empty textitem
	public TextItem() {
		this(0, EMPTYTEXT);
	}

// give the text
	public String getText() {
		return text == null ? "" : text;
	}

// geef de AttributedString voor het item
	public AttributedString getAttributedString(Style style, float scale) {
		String text = getText();
		if (text.isEmpty()) {
			text = EMPTYTEXT;
		}
		AttributedString attrStr = new AttributedString(text);
		attrStr.addAttribute(TextAttribute.FONT, style.getFont(scale), 0, text.length());
		return attrStr;
	}

// give the bounding box of the item
	public Rectangle getBoundingBox(Graphics g, ImageObserver observer,
			float scale, Style myStyle) {
		List<TextLayout> layouts = getLayouts(g, myStyle, scale);
		int xsize = 0, ysize = (int) (myStyle.getLeading() * scale);
		Iterator<TextLayout> iterator = layouts.iterator();
		while (iterator.hasNext()) {
			TextLayout layout = iterator.next();
			Rectangle2D bounds = layout.getBounds();
			if (bounds.getWidth() > xsize) {
				xsize = (int) bounds.getWidth();
			}
			if (bounds.getHeight() > 0) {
				ysize += bounds.getHeight();
			}
			ysize += layout.getLeading() + layout.getDescent();
		}
		return new Rectangle((int) (myStyle.getIndent() *scale), 0, xsize, ysize );
	}

// draw the item
	public void draw(int x, int y, float scale, Graphics g,
			Style myStyle, ImageObserver o) {
		if (text == null || text.length() == 0) {
			return;
		}
		List<TextLayout> layouts = getLayouts(g, myStyle, scale);
		Point pen = new Point(x + (int)(myStyle.getIndent() * scale),
				y + (int) (myStyle.getLeading() * scale));
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(myStyle.getColor());
		Iterator<TextLayout> it = layouts.iterator();
		while (it.hasNext()) {
			TextLayout layout = it.next();
			pen.y += layout.getAscent();
			layout.draw(g2d, pen.x, pen.y);
			pen.y += layout.getDescent();
		}
	  }

	private List<TextLayout> getLayouts(Graphics g, Style s, float scale) {
		// Return cached layouts if they exist and scale/style hasn't changed
		if (cachedLayouts != null && lastScale == scale && lastStyle == s) {
			return cachedLayouts;
		}

		List<TextLayout> layouts = new ArrayList<TextLayout>();
		AttributedString attrStr = getAttributedString(s, scale);
    	Graphics2D g2d = (Graphics2D) g;
    	FontRenderContext frc = g2d.getFontRenderContext();
    	LineBreakMeasurer measurer = new LineBreakMeasurer(attrStr.getIterator(), frc);
    	float wrappingWidth = (Slide.WIDTH - s.getIndent()) * scale;
    	while (measurer.getPosition() < getText().length()) {
    		TextLayout layout = measurer.nextLayout(wrappingWidth);
    		layouts.add(layout);
    	}

		// Cache the layouts
		cachedLayouts = layouts;
		lastScale = scale;
		lastStyle = s;
    	return layouts;
	}

	// Add a method to clear the layouts cache
	public void clearLayoutCache() {
		cachedLayouts = null;
		lastScale = -1;
		lastStyle = null;
	}

	public String toString() {
		return "java.com.TextItem[" + getLevel()+","+getText()+"]";
	}
}
