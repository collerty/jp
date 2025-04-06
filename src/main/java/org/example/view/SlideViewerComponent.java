package org.example.view;

import org.example.model.Presentation;
import org.example.model.Slide;
import org.example.style.StyleConstants;

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

    public SlideViewerComponent(Presentation pres, JFrame frame)
    {
        this.setBackground(StyleConstants.BACKGROUND);
        this.presentation = pres;
        this.labelFont = StyleConstants.LABEL_FONT;
        this.frame = frame;
        this.setDoubleBuffered(false); // Disable double buffering for faster updates

        this.setupScrollPane();
    }

    private void setupScrollPane() {
        // Create scroll pane
        this.scrollPane = new JScrollPane(this);
        this.scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.scrollPane.setBorder(null); // Remove default border

        // Set faster scrolling
        this.scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Default is 8
        this.scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        this.scrollPane.getVerticalScrollBar().setBlockIncrement(100); // Scroll by 100 pixels when clicking the track
        this.scrollPane.getHorizontalScrollBar().setBlockIncrement(100);
    }

    public Dimension getPreferredSize()
    {
        // Calculate the maximum dimensions needed for the content
        int maxWidth = Slide.WIDTH;
        int maxHeight = Slide.HEIGHT;

        if (this.slide != null)
        {
            // Get the actual content bounds
            Rectangle bounds = this.slide.getBounds();
            maxWidth = Math.max(maxWidth, bounds.width);
            maxHeight = Math.max(maxHeight, bounds.height);
        }

        return new Dimension(maxWidth + (2 * StyleConstants.SLIDE_MARGIN), maxHeight + (2 * StyleConstants.SLIDE_MARGIN));
    }

    public void update(Presentation presentation, Slide data)
    {
        this.presentation = presentation;
        this.slide = data;

        // Force a complete repaint
        this.revalidate();
        this.repaint();

        // Update frame title
        if (this.frame != null)
        {
            this.frame.setTitle(presentation.getTitle());
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
        this.clearBackground(g);

        if (this.presentation.getSlideNumber() < 0 || this.slide == null || this.presentation.getSize() == 0)
        {
            this.drawEmptyMessage(g);
            return;
        }

        if (this.isFullscreen)
        {
            this.drawFullscreenSlide(g);
        }
        else
        {
            this.drawNormalSlide(g);
        }
    }

    private void clearBackground(Graphics g) {
        g.setColor(StyleConstants.BACKGROUND);
        g.fillRect(0, 0, this.getSize().width, this.getSize().height);
    }

    private void drawEmptyMessage(Graphics g) {
        g.setFont(this.labelFont);
        g.setColor(StyleConstants.TEXT_ON_DARK);
        g.drawString("No slides available", this.getWidth() / 2 - 50, this.getHeight() / 2);
    }

    private void drawFullscreenSlide(Graphics g) {
        // In fullscreen mode, fill the entire screen with white
        g.setColor(StyleConstants.SURFACE);
        g.fillRect(0, 0, this.getSize().width, this.getSize().height);

        // Calculate scaling to fit the screen while maintaining aspect ratio
        double scaleX = (double) this.getWidth() / Slide.WIDTH;
        double scaleY = (double) this.getHeight() / Slide.HEIGHT;
        double scale = Math.min(scaleX, scaleY);

        // Calculate scaled dimensions
        int scaledWidth = (int) (Slide.WIDTH * scale);
        int scaledHeight = (int) (Slide.HEIGHT * scale);

        // Center the scaled slide
        int x = (this.getWidth() - scaledWidth) / 2;
        int y = (this.getHeight() - scaledHeight) / 2;

        // Draw the slide content scaled
        Rectangle area = new Rectangle(x, y, scaledWidth, scaledHeight);
        this.slide.draw(g, area, this);
    }

    private void drawNormalSlide(Graphics g) {
        // In normal mode, draw the slide with margins
        int x = (this.getWidth() - Slide.WIDTH) / 2;
        int y = (this.getHeight() - Slide.HEIGHT) / 2;

        // Draw white background for the slide
        g.setColor(StyleConstants.SURFACE);
        g.fillRect(x, y, Slide.WIDTH, Slide.HEIGHT);

        // Draw slide border
        g.setColor(StyleConstants.BORDER);
        g.drawRect(x, y, Slide.WIDTH, Slide.HEIGHT);

        // Draw slide content
        Rectangle area = new Rectangle(x, y, Slide.WIDTH, Slide.HEIGHT);
        this.slide.draw(g, area, this);
    }

    public JScrollPane getScrollPane()
    {
        return this.scrollPane;
    }

    public void enterFullScreen()
    {
        if (this.frame != null)
        {
            this.isFullscreen = true;
            // Disable scrolling in fullscreen mode
            if (this.scrollPane != null)
            {
                this.scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                this.scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            }
            this.frame.dispose(); // Dispose to apply changes
            this.frame.setUndecorated(true); // Remove title bar and borders
            this.frame.setResizable(false);

            GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            if (gd.isFullScreenSupported())
            {
                gd.setFullScreenWindow(this.frame);
            }
            else
            {
                // If full-screen is not supported, maximize manually
                this.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                this.frame.setVisible(true);
            }
        }
    }

    public void exitFullScreen()
    {
        if (this.frame != null)
        {
            this.isFullscreen = false;
            // Restore scrolling in normal mode
            if (this.scrollPane != null)
            {
                this.scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                this.scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            }
            GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            if (gd.getFullScreenWindow() == this.frame)
            {
                gd.setFullScreenWindow(null); // Exit fullscreen mode
            }

            this.frame.dispose(); // Dispose so changes can apply
            this.frame.setUndecorated(false);
            this.frame.setResizable(true);
            this.frame.setVisible(true);
            this.frame.setExtendedState(JFrame.NORMAL);
        }
    }
}
