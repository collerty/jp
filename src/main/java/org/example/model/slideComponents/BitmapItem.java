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
  private final String imageName;
  
  protected static final String FILE = "File ";
  protected static final String NOTFOUND = " not found";

// level is equal to item-level; name is the name of the file with the Image
public BitmapItem(int level, String name) {
	super(level);
    this.imageName = name;
	if (name != null) {
		try {
			// First try to load from file system
			File imageFile = new File(name);
			if (imageFile.exists()) {
                this.bufferedImage = ImageIO.read(imageFile);
			} else {
				// If not found in file system, try classpath
				InputStream imgStream = this.getClass().getClassLoader().getResourceAsStream(name);
				if (imgStream == null) {
					throw new IOException(FILE + this.imageName + NOTFOUND);
				}
                this.bufferedImage = ImageIO.read(imgStream);
			}
		} catch (IOException e) {
			System.err.println(FILE + this.imageName + NOTFOUND);
			e.printStackTrace();
		}
	}
}


	public BitmapItem() {
		this(0, null);
	}

	public String getName() {
		return this.imageName;
	}

	public Rectangle getBoundingBox(Graphics g, ImageObserver observer, float scale, Style myStyle) {
		return new Rectangle((int) (myStyle.getIndent() * scale), 0,
				(int) (this.bufferedImage.getWidth(observer) * scale),
				((int) (myStyle.getLeading() * scale)) +
				(int) (this.bufferedImage.getHeight(observer) * scale));
	}

	public void draw(int x, int y, float scale, Graphics g, Style myStyle, ImageObserver observer) {
		int width = x + (int) (myStyle.getIndent() * scale);
		int height = y + (int) (myStyle.getLeading() * scale);
		g.drawImage(this.bufferedImage, width, height,(int) (this.bufferedImage.getWidth(observer)*scale),
                (int) (this.bufferedImage.getHeight(observer)*scale), observer);
	}

	public String toString() {
		return "java.com.BitmapItem[" + this.getLevel() + "," + this.imageName + "]";
	}
}
