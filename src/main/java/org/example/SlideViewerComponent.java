package org.example;

import java.awt.*;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class SlideViewerComponent extends JComponent {
		
	private Slide slide; // current slide
	private Font labelFont = null; // font for labels
	private Presentation presentation = null; // the presentation
	private JFrame frame = null;
	
	private static final long serialVersionUID = 227L;
	
	private static final Color BGCOLOR = new Color(46, 52, 64); // Dark background
	private static final Color COLOR = new Color(216, 222, 233);
	private static final String FONTNAME = "Dialog";
	private static final int FONTSTYLE = Font.BOLD;
	private static final int FONTHEIGHT = 10;
	private static final int XPOS = 1100;
	private static final int YPOS = 20;
	private static final int SLIDE_MARGIN = 40; // Margin around the slide

	public SlideViewerComponent(Presentation pres, JFrame frame) {
		setBackground(BGCOLOR); 
		presentation = pres;
		labelFont = new Font(FONTNAME, FONTSTYLE, FONTHEIGHT);
		this.frame = frame;
		setDoubleBuffered(false); // Disable double buffering for faster updates
	}

	public Dimension getPreferredSize() {
		return new Dimension(Slide.WIDTH + (2 * SLIDE_MARGIN), Slide.HEIGHT + (2 * SLIDE_MARGIN));
	}

	public void update(Presentation presentation, Slide data) {
		if (data == null) {
			repaint(0); // Immediate repaint
			return;
		}
		this.presentation = presentation;
		this.slide = data;
		repaint(0); // Immediate repaint
		frame.setTitle(presentation.getTitle());
	}

// draw the slide
	public void paintComponent(Graphics g) {
		g.setColor(BGCOLOR);
		g.fillRect(0, 0, getSize().width, getSize().height);
		if (presentation.getSlideNumber() < 0 || slide == null) {
			return;
		}
		g.setFont(labelFont);
		g.setColor(COLOR);
		g.drawString("java.com.Slide " + (1 + presentation.getSlideNumber()) + " of " +
                 presentation.getSize(), XPOS, YPOS);
		
		// Calculate center position with margins
		int x = (getWidth() - Slide.WIDTH) / 2;
		int y = YPOS + SLIDE_MARGIN; // Add margin from top
		Rectangle area = new Rectangle(x, y, Slide.WIDTH, Slide.HEIGHT);
		slide.draw(g, area, this);
	}

	public void enterFullScreen()
	{
		if (frame != null)
		{
			frame.dispose(); // Dispose to apply changes
			frame.setUndecorated(true); // Remove title bar and borders
			frame.setResizable(false);

			GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
			if (gd.isFullScreenSupported())
			{
				gd.setFullScreenWindow(frame);
			}
			else
			{
				// If full-screen is not supported, maximize manually
				frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
				frame.setVisible(true);
			}
		}
	}
	public void exitFullScreen()
	{
		if (frame != null)
		{
			GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
			if (gd.getFullScreenWindow() == frame)
			{
				gd.setFullScreenWindow(null); // Exit fullscreen mode
			}

			frame.dispose(); // Dispose so changes can apply
			frame.setUndecorated(false);
			frame.setResizable(true);
			frame.setVisible(true);
			frame.setExtendedState(JFrame.NORMAL);
		}
	}


}
