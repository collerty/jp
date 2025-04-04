package org.example.filehandler;

import org.example.model.Presentation;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class XMLFileHandler implements FileHandlerStrategy {
    private Frame parent;

    public XMLFileHandler(Frame parent) {
        this.parent = parent;
    }

    @Override
    public boolean openFile(Presentation presentation) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Open Presentation File");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Presentation Files (*.xml)", "xml"));

        int userSelection = fileChooser.showOpenDialog(parent);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            Accessor xmlAccessor = new XMLAccessor();
            try {
                xmlAccessor.loadFile(presentation, selectedFile.getAbsolutePath());
                presentation.setSlideNumber(2);
                parent.repaint();

                return true;
            } catch (IOException exc) {
                JOptionPane.showMessageDialog(parent, "IO Exception: " + exc, "Load Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return false; // If no file was selected
    }



    @Override
    public boolean saveFile(Presentation presentation, File file) {
        Accessor xmlAccessor = new XMLAccessor();
        try {
            xmlAccessor.saveFile(presentation, file.getAbsolutePath());
            return true;
        } catch (IOException exc) {
            JOptionPane.showMessageDialog(parent, "IO Exception: " + exc, "Save Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    @Override
    public boolean saveAs(Presentation presentation) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Presentation As...");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Presentation Files (*.xml)", "xml"));

        int userSelection = fileChooser.showSaveDialog(parent);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            // Ensure the file has .xml extension
            String filePath = selectedFile.getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".xml")) {
                filePath += ".xml";
            }

            return saveFile(presentation, new File(filePath));
        }
        return false; // If user cancels the save operation
    }
}
