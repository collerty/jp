package org.example.model.slideComponents;

import org.example.model.Style;

import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;

import javax.imageio.ImageIO;

import java.io.IOException;
import java.io.InputStream;


public class BitmapItem extends SlideItem
{
  private BufferedImage bufferedImage;
  private String imageName;
  
  protected static final String FILE = "File ";
  protected static final String NOTFOUND = " not found";

// level is equal to item-level; name is the name of the file with the Image
public BitmapItem(int level, String name) {
	super(level);
	imageName = name;
	if (name != null) {
		try {
			// First try to load from file system
			File imageFile = new File(name);
			if (imageFile.exists()) {
				bufferedImage = ImageIO.read(imageFile);
			} else {
				// If not found in file system, try classpath
				InputStream imgStream = getClass().getClassLoader().getResourceAsStream(name);
				if (imgStream == null) {
					throw new IOException(FILE + imageName + NOTFOUND);
				}
				bufferedImage = ImageIO.read(imgStream);
			}
		} catch (IOException e) {
			System.err.println(FILE + imageName + NOTFOUND);
			e.printStackTrace();
		}
	}
}


// An empty bitmap-item
	public BitmapItem() {
		this(0, null);
	}

// give the filename of the image
	public String getName() {
		return imageName;
	}

// give the  bounding box of the image
	public Rectangle getBoundingBox(Graphics g, ImageObserver observer, float scale, Style myStyle) {
		return new Rectangle((int) (myStyle.getIndent() * scale), 0,
				(int) (bufferedImage.getWidth(observer) * scale),
				((int) (myStyle.getLeading() * scale)) +
				(int) (bufferedImage.getHeight(observer) * scale));
	}

// draw the image
	public void draw(int x, int y, float scale, Graphics g, Style myStyle, ImageObserver observer) {
		int width = x + (int) (myStyle.getIndent() * scale);
		int height = y + (int) (myStyle.getLeading() * scale);
		g.drawImage(bufferedImage, width, height,(int) (bufferedImage.getWidth(observer)*scale),
                (int) (bufferedImage.getHeight(observer)*scale), observer);
	}

	public String toString() {
		return "java.com.BitmapItem[" + getLevel() + "," + imageName + "]";
	}
}
