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
    private final Presentation presentation;
    private final FileHandlerStrategy fileHandler;

    // Constants for menu options
    private static final String NEW_PRESENTATION = "New";
    private static final String OPEN_PRESENTATION = "Open";
    private static final String DEMO_PRESENTATION = "Demo";


    public StartMenu()
    {
        this.presentation = new Presentation();
        this.fileHandler = new XMLFileHandler(this);
        this.setupFrame();
        this.setupButtons();
        this.showStartMenu();
    }

    private void setupFrame()
    {
        this.setTitle("JabberPoint - Start Menu");
        this.setLayout(new FlowLayout());
        this.setSize(400, 300);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void setupButtons()
    {
        this.add(this.createMenuButton(NEW_PRESENTATION, this::loadNewPresentation));
        this.add(this.createMenuButton(OPEN_PRESENTATION, this::openPresentationFile));
        this.add(this.createMenuButton(DEMO_PRESENTATION, this::loadDemoPresentation));
    }

    private JButton createMenuButton(String text, Runnable action)
    {
        JButton button = new JButton(text);
        button.addActionListener(e -> action.run());
        button.setFocusable(false);
        return button;
    }

    private void showStartMenu()
    {
        this.requestFocusInWindow();
        this.setVisible(true);
    }

    private SlideViewerFrame createAndSetupViewerFrame(String title) throws PresentationException
    {
        try
        {
            SlideViewerFrame viewerFrame = this.createViewerFrame(title);
            this.setupViewerFrame(viewerFrame);
            return viewerFrame;
        } catch (Exception e)
        {
            throw new PresentationException("Failed to create slide viewer frame: " + e.getMessage(), e);
        }
    }

    private SlideViewerFrame createViewerFrame(String title)
    {
        return new SlideViewerFrame(title, presentation);
    }

    private void setupViewerFrame(SlideViewerFrame viewerFrame)
    {
        this.presentation.setShowView(viewerFrame.getSlideViewerComponent());
        this.dispose();
    }

    private void refreshViewerFrame(SlideViewerFrame viewerFrame)
    {
        viewerFrame.getSlideViewerComponent().revalidate();
        viewerFrame.getSlideViewerComponent().repaint();
    }

    private void loadPresentation(Runnable loader, String title, String errorMessage)
    {
        try
        {
            loader.run();
            SlideViewerFrame viewerFrame = this.createAndSetupViewerFrame(title);
            this.refreshViewerFrame(viewerFrame);
        } catch (Exception e)
        {
            this.showError(errorMessage, e);
        }
    }

    private void loadNewPresentation()
    {
        this.loadPresentation(
                () -> this.fileHandler.newFile(this.presentation),
                "JabberPoint",
                "Failed to create new presentation"
        );
    }

    private void loadDemoPresentation()
    {
        try
        {
            this.fileHandler.newFile(this.presentation);
            SlideViewerFrame viewerFrame = this.createAndSetupViewerFrame("JabberPoint Demo");
            this.presentation.setSlideViewerFrame(viewerFrame);
            Accessor.getDemoAccessor().loadFile(this.presentation, "");
            this.presentation.setSlideNumber(0);
            viewerFrame.getSlideViewerComponent().update(this.presentation, this.presentation.getCurrentSlide());
            this.refreshViewerFrame(viewerFrame);
        } catch (Exception e)
        {
            this.showError("Failed to load demo presentation", e);
        }
    }

    private void openPresentationFile()
    {
        try
        {
            if (fileHandler.openFile(presentation))
            {
                SlideViewerFrame viewerFrame = this.createAndSetupViewerFrame("JabberPoint");
                this.refreshViewerFrame(viewerFrame);
            }
        } catch (FileOperationException | PresentationException e)
        {
            this.showError("Failed to open presentation file", e);
        }
    }

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
