package org.example;

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

    private Presentation presentation;
    private int selectedIndex = -1;
    private JPanel thumbnailsPanel;
    private JScrollPane scrollPane;
    private static final Color SNOW_WHITE = new Color(216, 222, 233);

    public SlideThumbnailPanel(Presentation presentation)
    {
        this.presentation = presentation;
        setBackground(BACKGROUND_COLOR);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createCompoundBorder(
                new RightOnlyBorder(SNOW_WHITE, 1),
                BorderFactory.createEmptyBorder(16, 16, 0, 0)
        ));
        setPreferredSize(new Dimension(THUMBNAIL_WIDTH + 32, 0));
        setMaximumSize(new Dimension(THUMBNAIL_WIDTH + 32, Integer.MAX_VALUE));
        setMinimumSize(new Dimension(THUMBNAIL_WIDTH + 32, 0));

        // Create a panel to hold thumbnails
        thumbnailsPanel = new JPanel();
        thumbnailsPanel.setLayout(new BoxLayout(thumbnailsPanel, BoxLayout.Y_AXIS));
        thumbnailsPanel.setBackground(BACKGROUND_COLOR);

        // Create scroll pane
        scrollPane = new JScrollPane(thumbnailsPanel);
        scrollPane.setPreferredSize(new Dimension(THUMBNAIL_WIDTH + 20, 0));
        scrollPane.setBackground(BACKGROUND_COLOR);
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);
        scrollPane.setBorder(null);

        add(scrollPane, BorderLayout.CENTER);
        updateThumbnails();
    }

    public void updateThumbnails()
    {
        thumbnailsPanel.removeAll();

        for (int i = 0; i < presentation.getSize(); i++)
        {
            Slide slide = presentation.getSlide(i);
            JPanel thumbnailPanel = createThumbnailPanel(slide, i);
            thumbnailsPanel.add(thumbnailPanel);
            thumbnailsPanel.add(Box.createVerticalStrut(10));
        }

        thumbnailsPanel.revalidate();
        thumbnailsPanel.repaint();
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
                    g.fillRect(0, 0, getWidth(), getHeight());
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
                Rectangle area = new Rectangle(0, 0, getWidth(), getHeight());
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

        return panel;
    }

    public void setSelectedIndex(int index)
    {
        this.selectedIndex = index;
        updateThumbnails();
    }

    public void setVisible(boolean visible)
    {
        this.thumbnailsPanel.setVisible(visible);
    }
}


