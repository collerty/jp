package org.example.view;

import com.formdev.flatlaf.intellijthemes.FlatNordIJTheme;
import org.example.filehandler.Accessor;
import org.example.filehandler.XMLAccessor;
import org.example.model.Presentation;
import org.example.filehandler.FileHandlerStrategy;
import org.example.filehandler.XMLFileHandler;
import org.example.exception.FileOperationException;
import org.example.exception.PresentationException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;


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

        // Set up the frame
        setTitle("JabberPoint - Start Menu");
        setLayout(new FlowLayout());
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create buttons for each action
        JButton newButton = new JButton(NEW_PRESENTATION);
        JButton openButton = new JButton(OPEN_PRESENTATION);
        JButton demoButton = new JButton(DEMO_PRESENTATION);

        // Add action listeners to buttons
        newButton.addActionListener(e -> loadNewPresentation());
        openButton.addActionListener(e -> openPresentationFile());
        demoButton.addActionListener(e -> loadDemoPresentation());

        // Add buttons to the frame
        add(newButton);
        add(openButton);
        add(demoButton);

        // Ensure no button has focus when the window opens
        newButton.setFocusable(false);
        openButton.setFocusable(false);
        demoButton.setFocusable(false);

        // Request focus for the frame to avoid focusing on buttons
        this.requestFocusInWindow();

        // Show the start menu
        setVisible(true);
    }

    private void loadNewPresentation()
    {
        try
        {
            fileHandler.newFile(presentation);
            createAndSetupViewerFrame("JabberPoint");
        } catch (PresentationException e)
        {
            showError("Failed to create new presentation: " + e.getMessage(), e);
        }
    }

    private void loadDemoPresentation()
    {
        try
        {
            fileHandler.newFile(presentation);
            SlideViewerFrame viewerFrame = createAndSetupViewerFrame("JabberPoint Demo");
            presentation.setSlideViewerFrame(viewerFrame);
            Accessor.getDemoAccessor().loadFile(presentation, "");
            // Ensure we're showing the first slide
            presentation.setSlideNumber(0);
            // Force a complete refresh of the UI
            viewerFrame.getSlideViewerComponent().update(presentation, presentation.getCurrentSlide());
            viewerFrame.getSlideViewerComponent().revalidate();
            viewerFrame.getSlideViewerComponent().repaint();
        } catch (Exception e)
        {
            showError("Failed to load demo presentation: " + e.getMessage(), e);
        }
    }

    private void openPresentationFile()
    {
        try
        {
            if (fileHandler.openFile(presentation))
            {
                SlideViewerFrame viewerFrame = createAndSetupViewerFrame("JabberPoint");
                viewerFrame.getSlideViewerComponent().revalidate();
                viewerFrame.getSlideViewerComponent().repaint();
            }
        } catch (FileOperationException | PresentationException e)
        {
            showError("Failed to open presentation file: " + e.getMessage(), e);
        }
    }

    /**
     * Displays an error message dialog with the given message and exception details.
     *
     * @param message The main error message to display
     * @param e The exception that caused the error
     */
    private void showError(String message, Exception e)
    {
        String errorMessage = message;
        if (e.getCause() != null)
        {
            errorMessage += "\nCause: " + e.getCause().getMessage();
        }
        JOptionPane.showMessageDialog(this, 
            errorMessage, 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
    }


    private SlideViewerFrame createAndSetupViewerFrame(String title) throws PresentationException
    {
        try {
            SlideViewerFrame viewerFrame = new SlideViewerFrame(title, presentation);
            presentation.setShowView(viewerFrame.getSlideViewerComponent());
            dispose();
            return viewerFrame;
        } catch (Exception e) {
            throw new PresentationException("Failed to create slide viewer frame: " + e.getMessage(), e);
        }
    }
}
