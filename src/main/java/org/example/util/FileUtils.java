 package org.example.util;

import javax.swing.*;
import java.io.File;

public class FileUtils {
    private static final JFileChooser fileChooser = createFileChooser();

    private static JFileChooser createFileChooser() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                return f.isDirectory() || 
                       f.getName().toLowerCase().endsWith(".jpg") ||
                       f.getName().toLowerCase().endsWith(".png") ||
                       f.getName().toLowerCase().endsWith(".gif");
            }

            public String getDescription() {
                return "Image files (*.jpg, *.png, *.gif)";
            }
        });
        return chooser;
    }

    public static File selectImageFile(JComponent parent) {
        int result = fileChooser.showOpenDialog(parent);
        return result == JFileChooser.APPROVE_OPTION ? fileChooser.getSelectedFile() : null;
    }
}