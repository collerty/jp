package org.example;

import com.formdev.flatlaf.intellijthemes.FlatNordIJTheme;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.BorderLayout;

/**
 * <p>The application window for a slideviewcomponent</p>
 *
 * @author Ian F. Darwin, ian@darwinsys.com, Gert Florijn, Sylvia Stuurman
 * @version 1.6 2014/05/16 Sylvia Stuurman
 */

public class SlideViewerFrame extends JFrame
{
    private static final long serialVersionUID = 3227L;

    private static final String JABTITLE = "Jabberpoint 1.6 - OU";
    public final static int WIDTH = 1200;
    public final static int HEIGHT = 800;
    private SlideViewerComponent slideViewerComponent;
    private SlideThumbnailPanel thumbnailPanel;
    private HeaderPanel headerPanel;
    private JPanel mainPanel;
    private JPanel headerWrapper;

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
        presentation.setShowView(slideViewerComponent);
//        presentation.setThumbnailPanel(thumbnailPanel);

        // Create main content panel with BorderLayout
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(46, 52, 64)); // Dark background

        // Setting up the header panel
        this.headerPanel = new HeaderPanel(presentation);

        this.headerWrapper = new JPanel(new BorderLayout());
        this.headerWrapper.setBackground(new Color(46, 52, 64));
        this.headerWrapper.setBorder(BorderFactory.createEmptyBorder(0, 24, 0, 24)); // Match slide panel's left padding
        this.headerWrapper.add(this.headerPanel, BorderLayout.CENTER);

        // Setting up the sidebar panel
        this.thumbnailPanel = new SlideThumbnailPanel(presentation);

        // Making setup window for viewing mode
        setupWindow(slideViewerComponent, presentation);
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
        return slideViewerComponent;
    }

    public SlideThumbnailPanel getThumbnailPanel()
    {
        return thumbnailPanel;
    }

    // Setup GUI
    public void setupWindow(SlideViewerComponent
                                    slideViewerComponent, Presentation presentation)
    {
        setTitle(JABTITLE);
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                presentation.exit(0);
            }
        });


        // Add thumbnail panel to the left
        mainPanel.add(thumbnailPanel, BorderLayout.WEST);

        // Add slide viewer with scroll pane to the center
        mainPanel.add(slideViewerComponent.getScrollPane(), BorderLayout.CENTER);

        // Add the main panel to the frame
        getContentPane().add(mainPanel);

        // Set up the menu and other components
        setJMenuBar(new MenuController(this, presentation));
        addKeyListener(new KeyController(presentation));

        // Set window size and make it visible
        setSize(new Dimension(WIDTH + thumbnailPanel.getPreferredSize().width, HEIGHT + headerPanel.getPreferredSize().height));
        setVisible(true);
        requestFocus();
        requestFocusInWindow();
        toFront();
    }


    public void enterFullScreen()
    {
//        this.slide
        this.mainPanel.remove(thumbnailPanel);
        this.mainPanel.repaint();

        this.slideViewerComponent.enterFullScreen();
    }

    public void exitFullScreen()
    {

        this.mainPanel.add(thumbnailPanel, BorderLayout.WEST);
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
