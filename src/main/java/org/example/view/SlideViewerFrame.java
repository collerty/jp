package org.example.view;

import com.formdev.flatlaf.intellijthemes.FlatNordIJTheme;
import org.example.controller.KeyController;
import org.example.controller.MenuController;
import org.example.model.Presentation;
import org.example.style.StyleConstants;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Dimension;
import java.awt.BorderLayout;


public class SlideViewerFrame extends JFrame
{
    private static final long serialVersionUID = 3227L;

    private static final String JABTITLE = "Jabberpoint 1.6 - OU";
    private final SlideViewerComponent slideViewerComponent;
    private final SlideThumbnailPanel thumbnailPanel;
    private final HeaderPanel headerPanel;
    private JPanel mainPanel;
    private final JPanel headerWrapper;

    public SlideViewerFrame(Presentation presentation)
    {
        this(JABTITLE, presentation);
    }

    public SlideViewerFrame(String title, Presentation presentation)
    {
        super(title);
        try
        {
            UIManager.setLookAndFeel(new FlatNordIJTheme());  // Apply the FlatNord theme
        } catch (UnsupportedLookAndFeelException e)
        {
            e.printStackTrace();
        }
        this.slideViewerComponent = new SlideViewerComponent(presentation, this);
        this.thumbnailPanel = new SlideThumbnailPanel(presentation);
        this.headerPanel = new HeaderPanel(presentation);
        presentation.setShowView(this.slideViewerComponent);
        presentation.setThumbnailPanel(this.thumbnailPanel);
        presentation.setSlideViewerFrame(this);

        this.mainPanel = new JPanel(new BorderLayout());
        this.mainPanel.setBackground(StyleConstants.BACKGROUND);

        this.headerWrapper = new JPanel(new BorderLayout());
        this.headerWrapper.setBackground(StyleConstants.BACKGROUND);
        this.headerWrapper.setBorder(BorderFactory.createEmptyBorder(0, 24, 0, 24)); // Match slide panel's left padding
        this.headerWrapper.add(this.headerPanel, BorderLayout.CENTER);

        this.setupWindow(this.slideViewerComponent, presentation);
    }

    public JPanel getMainPanel()
    {
        return this.mainPanel;
    }

    public void setMainPanel(JPanel mainPanel)
    {
        this.mainPanel = mainPanel;
    }

    public SlideViewerComponent getSlideViewerComponent()
    {
        return this.slideViewerComponent;
    }

    public SlideThumbnailPanel getThumbnailPanel()
    {
        return this.thumbnailPanel;
    }

    public void setupWindow(SlideViewerComponent
                                    slideViewerComponent, Presentation presentation)
    {
        this.setTitle(JABTITLE);
        this.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                presentation.exit(0);
            }
        });


        this.mainPanel.add(this.thumbnailPanel, BorderLayout.WEST);

        this.mainPanel.add(slideViewerComponent.getScrollPane(), BorderLayout.CENTER);

        this.getContentPane().add(this.mainPanel);

        this.setJMenuBar(new MenuController(this, presentation));
        this.addKeyListener(new KeyController(presentation));

        this.setSize(new Dimension(StyleConstants.WINDOW_WIDTH + this.thumbnailPanel.getPreferredSize().width,
                             StyleConstants.WINDOW_HEIGHT + this.headerPanel.getPreferredSize().height));
        this.setVisible(true);
        this.requestFocus();
        this.requestFocusInWindow();
        this.toFront();
    }


    public void enterFullScreen()
    {
        this.mainPanel.remove(this.thumbnailPanel);
        this.mainPanel.repaint();

        this.slideViewerComponent.enterFullScreen();
    }

    public void exitFullScreen()
    {
        this.mainPanel.add(this.thumbnailPanel, BorderLayout.WEST);
        this.mainPanel.revalidate();
        this.mainPanel.repaint();
        this.slideViewerComponent.exitFullScreen();
    }

    public void enterEditMode()
    {
        this.mainPanel.add(this.headerWrapper, BorderLayout.NORTH);
        this.mainPanel.revalidate();
        this.mainPanel.repaint();
    }

    public void exitEditMode()
    {
        this.mainPanel.remove(this.headerWrapper);
        this.mainPanel.revalidate();
        this.mainPanel.repaint();
    }
}
