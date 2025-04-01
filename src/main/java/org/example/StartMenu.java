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
    private Frame parent;

    // Constants for menu options
    private static final String NEW = "New";
    private static final String OPEN = "Open";
    private static final String DEMO = "Demo";
    private static final String BROWSE = "Browse file system";

    public StartMenu(Frame parent) {
        this.parent = parent;
        this.presentation = new Presentation();

        // Set up the frame
        setTitle("JabberPoint - Start Menu 123");
        setLayout(new FlowLayout());
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create buttons for each action
        JButton newButton = new JButton(NEW);
        JButton openButton = new JButton(OPEN);
        JButton demoButton = new JButton(DEMO);

        // Add action listeners to buttons
        newButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Create a new blank presentation or load a demo one
                loadNewPresentation();
            }
        });

        openButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open a file chooser to browse for an existing presentation
                openPresentationFile();
            }
        });

        demoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Load a demo template (for now, just test.xml)
                loadDemoPresentation();
            }
        });


        // Add buttons to the frame
        add(newButton);
        add(openButton);
        add(demoButton);

        // Ensure no button has focus when the window opens
        newButton.setFocusable(false); // Disable focus on this button
        openButton.setFocusable(false); // Disable focus on this button
        demoButton.setFocusable(false); // Disable focus on this button

        // Request focus for the frame to avoid focusing on buttons
        this.requestFocusInWindow();

        // Show the start menu
        setVisible(true);
    }

    private void loadNewPresentation() {
        presentation.setSlideNumber(0);
        new SlideViewerFrame("JabberPoint", presentation);
        dispose(); // Close the start menu
    }

    private void loadDemoPresentation() {
        SlideViewerFrame viewer = new SlideViewerFrame("JabberPoint Demo", presentation);
        try {
            Accessor.getDemoAccessor().loadFile(presentation, "");
            presentation.setSlideNumber(0);

            dispose(); // Close the start menu
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(parent, "IO Error: " + ex, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void openPresentationFile() {
        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); // Modern UI
            UIManager.setLookAndFeel(new FlatNordIJTheme());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Open Presentation File");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Presentation Files (*.xml)", "xml"));

        int userSelection = fileChooser.showOpenDialog(parent);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            presentation.clear();
            Accessor xmlAccessor = new XMLAccessor();

            try {
                // Load the selected file
                xmlAccessor.loadFile(presentation, selectedFile.getAbsolutePath());
                presentation.setSlideNumber(0);  // Reset the slide number

                // Create a new SlideViewerFrame with the correct title
                SlideViewerFrame viewerFrame = new SlideViewerFrame("JabberPoint - " + selectedFile.getName(), presentation);

                // Revalidate and repaint the SlideViewerComponent to ensure it updates correctly
                viewerFrame.getSlideViewerComponent().revalidate();
                viewerFrame.getSlideViewerComponent().repaint();

                // If needed, you can repaint the parent or other components as well
                parent.repaint();

                dispose(); // Close the start menu frame

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(parent, "IO Error: " + ex, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}
