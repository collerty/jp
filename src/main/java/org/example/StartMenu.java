package org.example;

import com.formdev.flatlaf.intellijthemes.FlatNordIJTheme;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class StartMenu extends JFrame {
    private Presentation presentation;
    private JFrame parent;

    // Constants for menu options
    private static final String NEW = "New";
    private static final String OPEN = "Open";
    private static final String DEMO = "Demo";
    private static final String BROWSE = "Browse file system";

    public StartMenu(JFrame parent) {
        this.parent = parent;
        this.presentation = new Presentation();

        // Set up the frame
        setTitle("JabberPoint - Start Menu");
        setLayout(new FlowLayout());
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create buttons for each action
        JButton newButton = new JButton(NEW);
        JButton openButton = new JButton(OPEN);
        JButton demoButton = new JButton(DEMO);

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

    private void loadNewPresentation() {
        presentation.clear();
        SlideViewerFrame viewerFrame = new SlideViewerFrame("JabberPoint", presentation);
        presentation.setShowView(viewerFrame.getSlideViewerComponent());
        presentation.setSlideNumber(0);
        viewerFrame.getSlideViewerComponent().revalidate();
        viewerFrame.getSlideViewerComponent().repaint();
        dispose();
    }

    private void loadDemoPresentation() {
        try {
            presentation.clear();
            SlideViewerFrame viewerFrame = new SlideViewerFrame("JabberPoint Demo", presentation);
            presentation.setShowView(viewerFrame.getSlideViewerComponent());
            
            Accessor.getDemoAccessor().loadFile(presentation, "");
            presentation.setSlideNumber(0);
            
            viewerFrame.getSlideViewerComponent().revalidate();
            viewerFrame.getSlideViewerComponent().repaint();
            
            dispose();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "IO Error: " + ex, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openPresentationFile() {
        try {
            UIManager.setLookAndFeel(new FlatNordIJTheme());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Open Presentation File");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Presentation Files (*.xml)", "xml"));

        int userSelection = fileChooser.showOpenDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            presentation.clear();
            
            SlideViewerFrame viewerFrame = new SlideViewerFrame("JabberPoint - " + selectedFile.getName(), presentation);
            presentation.setShowView(viewerFrame.getSlideViewerComponent());
            
            Accessor xmlAccessor = new XMLAccessor();
            try {
                xmlAccessor.loadFile(presentation, selectedFile.getAbsolutePath());
                presentation.setSlideNumber(0);
                
                viewerFrame.getSlideViewerComponent().revalidate();
                viewerFrame.getSlideViewerComponent().repaint();
                
                dispose();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "IO Error: " + ex, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
