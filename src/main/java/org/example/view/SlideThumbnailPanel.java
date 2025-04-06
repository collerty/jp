package org.example.view;

import org.example.model.Presentation;
import org.example.model.Slide;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SlideThumbnailPanel extends JPanel
{
    private static final int THUMBNAIL_WIDTH = 240;
    private static final int THUMBNAIL_HEIGHT = 150;

    private static final Color SELECTED_COLOR = new Color(59, 66, 82);
    private static final Color HOVER_COLOR = new Color(67, 76, 94);
    private static final Color BACKGROUND_COLOR = new Color(46, 52, 64);

    private final Presentation presentation;
    private int selectedIndex = -1;
    private final JPanel thumbnailsPanel;
    private final JScrollPane scrollPane;
    private static final Color SNOW_WHITE = new Color(216, 222, 233);

    public SlideThumbnailPanel(Presentation presentation)
    {
        this.presentation = presentation;
        this.setBackground(BACKGROUND_COLOR);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createCompoundBorder(
                new RightOnlyBorder(SNOW_WHITE, 1),
                BorderFactory.createEmptyBorder(16, 16, 0, 0)
        ));
        this.setPreferredSize(new Dimension(THUMBNAIL_WIDTH + 32, 0));
        this.setMaximumSize(new Dimension(THUMBNAIL_WIDTH + 32, Integer.MAX_VALUE));
        this.setMinimumSize(new Dimension(THUMBNAIL_WIDTH + 32, 0));

        // Create a panel to hold thumbnails
        this.thumbnailsPanel = new JPanel();
        this.thumbnailsPanel.setLayout(new BoxLayout(this.thumbnailsPanel, BoxLayout.Y_AXIS));
        this.thumbnailsPanel.setBackground(BACKGROUND_COLOR);

        // Create scroll pane
        this.scrollPane = new JScrollPane(this.thumbnailsPanel);
        this.scrollPane.setPreferredSize(new Dimension(THUMBNAIL_WIDTH + 20, 0));
        this.scrollPane.setBackground(BACKGROUND_COLOR);
        this.scrollPane.getViewport().setBackground(BACKGROUND_COLOR);
        this.scrollPane.setBorder(null);

        this.add(this.scrollPane, BorderLayout.CENTER);
        this.updateThumbnails();
    }

    public void updateThumbnails()
    {
        this.thumbnailsPanel.removeAll();
        
        if (this.presentation.getSize() == 0) {
            JLabel emptyLabel = new JLabel("No slides available");
            emptyLabel.setForeground(SNOW_WHITE);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            this.thumbnailsPanel.add(emptyLabel);
            
            // Force a complete refresh
            this.thumbnailsPanel.revalidate();
            this.thumbnailsPanel.repaint();
            this.scrollPane.revalidate();
            this.scrollPane.repaint();
            this.revalidate();
            this.repaint();
            return;
        }
        
        for (int i = 0; i < this.presentation.getSize(); i++)
        {
            Slide slide = this.presentation.getSlide(i);
            JPanel thumbnailPanel = this.createThumbnailPanel(slide, i);
            this.thumbnailsPanel.add(thumbnailPanel);
            this.thumbnailsPanel.add(Box.createVerticalStrut(10));
            
            // Force a complete refresh after adding each thumbnail
            this.thumbnailsPanel.revalidate();
            this.thumbnailsPanel.repaint();
        }
        
        // Force a complete refresh
        this.thumbnailsPanel.revalidate();
        this.thumbnailsPanel.repaint();
        this.scrollPane.revalidate();
        this.scrollPane.repaint();
        this.revalidate();
        this.repaint();
    }

    private JPanel createThumbnailPanel(Slide slide, int index)
    {
        JPanel panel = new JPanel()
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                if (index == selectedIndex)
                {
                    g.setColor(SELECTED_COLOR);
                    g.fillRect(0, 0, this.getWidth(), this.getHeight());
                }
            }
        };

        panel.setPreferredSize(new Dimension(THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT));
        panel.setMaximumSize(new Dimension(THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setLayout(new BorderLayout());
        
        // Create thumbnail preview
        JPanel previewPanel = new JPanel()
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw slide content
                Rectangle area = new Rectangle(0, 0, this.getWidth(), this.getHeight());
                slide.draw(g2d, area, this);
            }
        };
        previewPanel.setPreferredSize(new Dimension(THUMBNAIL_WIDTH - 16, THUMBNAIL_HEIGHT - 16));
        previewPanel.setBackground(Color.WHITE);

        // Add slide number
        JLabel numberLabel = new JLabel("Slide " + (index + 1));
        numberLabel.setForeground(Color.WHITE);
        numberLabel.setHorizontalAlignment(SwingConstants.CENTER);
        numberLabel.setPreferredSize(new Dimension(THUMBNAIL_WIDTH, 20));

        panel.add(previewPanel, BorderLayout.CENTER);
        panel.add(numberLabel, BorderLayout.SOUTH);

        // Add mouse listeners
        panel.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                selectedIndex = index;
                presentation.setSlideNumber(index);
                panel.repaint();
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
                if (index != selectedIndex)
                {
                    panel.setBackground(HOVER_COLOR);
                    panel.repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                if (index != selectedIndex)
                {
                    panel.setBackground(BACKGROUND_COLOR);
                    panel.repaint();
                }
            }
        });
        
        // Force a complete refresh of the panel
        panel.revalidate();
        panel.repaint();

        return panel;
    }

    public void setSelectedIndex(int index)
    {
        this.selectedIndex = index;
        this.updateThumbnails();
    }

    public void setVisible(boolean visible)
    {
        this.thumbnailsPanel.setVisible(visible);
    }
    
    public void removeAll() {
        // Remove all components from the thumbnails panel
        this.thumbnailsPanel.removeAll();
        
        // Force a complete refresh
        this.thumbnailsPanel.revalidate();
        this.thumbnailsPanel.repaint();
        this.scrollPane.revalidate();
        this.scrollPane.repaint();
        this.revalidate();
        this.repaint();
    }
}


