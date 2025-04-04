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
    private SlideViewerComponent slideViewerComponent;
    private SlideThumbnailPanel thumbnailPanel;
    private HeaderPanel headerPanel;
    private JPanel mainPanel;
    private JPanel headerWrapper;

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
        presentation.setShowView(slideViewerComponent);
        presentation.setThumbnailPanel(thumbnailPanel);
        presentation.setSlideViewerFrame(this); // Set the frame reference

        // Create main content panel with BorderLayout
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(StyleConstants.BACKGROUND);

        // Setting up the header panel
        this.headerWrapper = new JPanel(new BorderLayout());
        this.headerWrapper.setBackground(StyleConstants.BACKGROUND);
        this.headerWrapper.setBorder(BorderFactory.createEmptyBorder(0, 24, 0, 24)); // Match slide panel's left padding
        this.headerWrapper.add(this.headerPanel, BorderLayout.CENTER);

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
        setSize(new Dimension(StyleConstants.WINDOW_WIDTH + thumbnailPanel.getPreferredSize().width, 
                             StyleConstants.WINDOW_HEIGHT + headerPanel.getPreferredSize().height));
        setVisible(true);
        requestFocus();
        requestFocusInWindow();
        toFront();
    }


    public void enterFullScreen()
    {
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
