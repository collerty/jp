package org.example.view;

import org.example.model.Presentation;
import org.example.model.Slide;

import java.awt.*;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class SlideViewerComponent extends JComponent
{

    private Slide slide; // current slide
    private Font labelFont = null; // font for labels
    private Presentation presentation = null; // the presentation
    private JFrame frame = null;
    private boolean isFullscreen = false; // Track fullscreen state
    private JScrollPane scrollPane; // Scroll pane for overflow content

    private static final long serialVersionUID = 227L;

    private static final Color SLIDE_BORDER = new Color(200, 200, 200);
    private static final int BORDER_WIDTH = 2;
    private static final Color BGCOLOR = new Color(46, 52, 64); // Dark background
    private static final Color COLOR = new Color(216, 222, 233);
    private static final String FONTNAME = "Dialog";
    private static final int FONTSTYLE = Font.BOLD;
    private static final int FONTHEIGHT = 10;
    private static final int XPOS = 1100; // I dont know what that does
    private static final int YPOS = 20;
    private static final int SLIDE_MARGIN = 40; // Margin around the slide

    public SlideViewerComponent(Presentation pres, JFrame frame)
    {
        setBackground(BGCOLOR);
        presentation = pres;
        labelFont = new Font(FONTNAME, FONTSTYLE, FONTHEIGHT);
        this.frame = frame;
        setDoubleBuffered(false); // Disable double buffering for faster updates

        // Create scroll pane
        scrollPane = new JScrollPane(this);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null); // Remove default border

        // Set faster scrolling
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Default is 8
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setBlockIncrement(100); // Scroll by 100 pixels when clicking the track
        scrollPane.getHorizontalScrollBar().setBlockIncrement(100);
    }

    public Dimension getPreferredSize()
    {
        // Calculate the maximum dimensions needed for the content
        int maxWidth = Slide.WIDTH;
        int maxHeight = Slide.HEIGHT;

        if (slide != null)
        {
            // Get the actual content bounds
            Rectangle bounds = slide.getBounds();
            maxWidth = Math.max(maxWidth, bounds.width);
            maxHeight = Math.max(maxHeight, bounds.height);
        }

        return new Dimension(maxWidth + (2 * SLIDE_MARGIN), maxHeight + (2 * SLIDE_MARGIN));
    }

    public void update(Presentation presentation, Slide data)
    {
        this.presentation = presentation;
        this.slide = data;

        // Force a complete repaint regardless of datarevalidate();
        repaint();

        // Update frame title
        if (frame != null)
        {
            frame.setTitle(presentation.getTitle());
        }
        else
        {
            System.out.println("  - Frame is null, cannot update title");
        }

        System.out.println("SlideViewerComponent.update() completed");
    }

    // draw the slide
    public void paintComponent(Graphics g)
    {
        // Clear the background first
        g.setColor(BGCOLOR);
        g.fillRect(0, 0, getSize().width, getSize().height);

        // If no slide or invalid slide number, just show empty background
        if (presentation.getSlideNumber() < 0 || slide == null || presentation.getSize() == 0)
        {
            // Draw a message indicating no slides
            g.setFont(labelFont);
            g.setColor(COLOR);
            g.drawString("No slides available", XPOS, YPOS);
            return;
        }

        if (isFullscreen)
        {
            // In fullscreen mode, fill the entire screen with white
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getSize().width, getSize().height);

            // Calculate scaling to fit the screen while maintaining aspect ratio
            double scaleX = (double) getWidth() / Slide.WIDTH;
            double scaleY = (double) getHeight() / Slide.HEIGHT;
            double scale = Math.min(scaleX, scaleY);

            // Calculate scaled dimensions
            int scaledWidth = (int) (Slide.WIDTH * scale);
            int scaledHeight = (int) (Slide.HEIGHT * scale);

            // Center the scaled slide
            int x = (getWidth() - scaledWidth) / 2;
            int y = (getHeight() - scaledHeight) / 2;

            // Draw the slide content scaled
            Rectangle area = new Rectangle(x, y, scaledWidth, scaledHeight);
            slide.draw(g, area, this);
        }
        else
        {
            // In normal mode, draw the slide with margins
            int x = (getWidth() - Slide.WIDTH) / 2;
            int y = (getHeight() - Slide.HEIGHT) / 2;

            // Draw white background for the slide
            g.setColor(Color.WHITE);
            g.fillRect(x, y, Slide.WIDTH, Slide.HEIGHT);

            // Draw slide border
            g.setColor(SLIDE_BORDER);
            g.drawRect(x, y, Slide.WIDTH, Slide.HEIGHT);

            // Draw slide content
            Rectangle area = new Rectangle(x, y, Slide.WIDTH, Slide.HEIGHT);
            slide.draw(g, area, this);
        }
    }

    public JScrollPane getScrollPane()
    {
        return scrollPane;
    }

    public void enterFullScreen()
    {
        if (frame != null)
        {
            isFullscreen = true;
            // Disable scrolling in fullscreen mode
            if (scrollPane != null)
            {
                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            }
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
            isFullscreen = false;
            // Restore scrolling in normal mode
            if (scrollPane != null)
            {
                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            }
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
