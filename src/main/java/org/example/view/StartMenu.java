package org.example.view;

import org.example.filehandler.Accessor;
import org.example.model.Presentation;
import org.example.filehandler.FileHandlerStrategy;
import org.example.filehandler.XMLFileHandler;
import org.example.exception.FileOperationException;
import org.example.exception.PresentationException;

import javax.swing.*;
import java.awt.*;


public class StartMenu extends JFrame
{
    private Presentation presentation;
    private FileHandlerStrategy fileHandler;

    // Constants for menu options
    private static final String NEW_PRESENTATION = "New";
    private static final String OPEN_PRESENTATION = "Open";
    private static final String DEMO_PRESENTATION = "Demo";


    public StartMenu()
    {
        this.presentation = new Presentation();
        this.fileHandler = new XMLFileHandler(this);
        setupFrame();
        setupButtons();
        showStartMenu();
    }

    private void setupFrame()
    {
        setTitle("JabberPoint - Start Menu");
        setLayout(new FlowLayout());
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void setupButtons()
    {
        add(createMenuButton(NEW_PRESENTATION, this::loadNewPresentation));
        add(createMenuButton(OPEN_PRESENTATION, this::openPresentationFile));
        add(createMenuButton(DEMO_PRESENTATION, this::loadDemoPresentation));
    }

    // Creates a menu button with consistent styling
    private JButton createMenuButton(String text, Runnable action)
    {
        JButton button = new JButton(text);
        button.addActionListener(e -> action.run());
        button.setFocusable(false);
        return button;
    }

    private void showStartMenu()
    {
        // Request focus for the frame to avoid focusing on buttons
        this.requestFocusInWindow();
        setVisible(true);
    }

    private SlideViewerFrame createAndSetupViewerFrame(String title) throws PresentationException
    {
        try
        {
            SlideViewerFrame viewerFrame = createViewerFrame(title);
            setupViewerFrame(viewerFrame);
            return viewerFrame;
        } catch (Exception e)
        {
            throw new PresentationException("Failed to create slide viewer frame: " + e.getMessage(), e);
        }
    }

    // Creates a new SlideViewerFrame with the given title
    private SlideViewerFrame createViewerFrame(String title)
    {
        return new SlideViewerFrame(title, presentation);
    }

    // Sets up the viewer frame and updates the presentation
    private void setupViewerFrame(SlideViewerFrame viewerFrame)
    {
        presentation.setShowView(viewerFrame.getSlideViewerComponent());
        dispose();
    }

    // Refreshes the UI components of the viewer frame
    private void refreshViewerFrame(SlideViewerFrame viewerFrame)
    {
        viewerFrame.getSlideViewerComponent().revalidate();
        viewerFrame.getSlideViewerComponent().repaint();
    }

    // Common pattern for loading presentations
    private void loadPresentation(Runnable loader, String title, String errorMessage)
    {
        try
        {
            loader.run();
            SlideViewerFrame viewerFrame = createAndSetupViewerFrame(title);
            refreshViewerFrame(viewerFrame);
        } catch (Exception e)
        {
            showError(errorMessage, e);
        }
    }

    private void loadNewPresentation()
    {
        loadPresentation(
                () -> fileHandler.newFile(presentation),
                "JabberPoint",
                "Failed to create new presentation"
        );
    }

    private void loadDemoPresentation()
    {
        try
        {
            fileHandler.newFile(presentation);
            SlideViewerFrame viewerFrame = createAndSetupViewerFrame("JabberPoint Demo");
            presentation.setSlideViewerFrame(viewerFrame);
            Accessor.getDemoAccessor().loadFile(presentation, "");
            presentation.setSlideNumber(0);
            viewerFrame.getSlideViewerComponent().update(presentation, presentation.getCurrentSlide());
            refreshViewerFrame(viewerFrame);
        } catch (Exception e)
        {
            showError("Failed to load demo presentation", e);
        }
    }

    private void openPresentationFile()
    {
        try
        {
            if (fileHandler.openFile(presentation))
            {
                SlideViewerFrame viewerFrame = createAndSetupViewerFrame("JabberPoint");
                refreshViewerFrame(viewerFrame);
            }
        } catch (FileOperationException | PresentationException e)
        {
            showError("Failed to open presentation file", e);
        }
    }

    // Shows an error dialog with the given message and exception
    private void showError(String message, Exception e)
    {
        String errorMessage = message;
        if (e.getCause() != null)
        {
            errorMessage += "\n\nTechnical details: " + e.getCause().getMessage();
        }
        JOptionPane.showMessageDialog(this, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
