package org.example.util;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileChooserUtils {
    private static volatile JFileChooser xmlFileChooser;
    private static volatile JFileChooser imageFileChooser;
    private static final ExecutorService executor = Executors.newFixedThreadPool(2);
    private static volatile boolean initialized = false;

    public static void initialize() {
        if (!initialized) {
            synchronized (FileChooserUtils.class) {
                if (!initialized) {
                    CompletableFuture<Void> xmlFuture = CompletableFuture.runAsync(() -> {
                        xmlFileChooser = createXMLFileChooser();
                    }, executor);

                    CompletableFuture<Void> imageFuture = CompletableFuture.runAsync(() -> {
                        imageFileChooser = createImageFileChooser();
                    }, executor);

                    CompletableFuture.allOf(xmlFuture, imageFuture).join();
                    initialized = true;
                }
            }
        }
    }

    private static JFileChooser createXMLFileChooser() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Open Presentation File");
        chooser.setFileFilter(new FileNameExtensionFilter("Presentation Files (*.xml)", "xml"));
        return chooser;
    }

    private static JFileChooser createImageFileChooser() {
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

    private static JFileChooser getXMLFileChooser() {
        if (!initialized) {
            initialize();
        }
        return xmlFileChooser;
    }

    private static JFileChooser getImageFileChooser() {
        if (!initialized) {
            initialize();
        }
        return imageFileChooser;
    }

    public static File selectXMLFile(java.awt.Component parent) {
        JFileChooser chooser = getXMLFileChooser();
        int result = chooser.showOpenDialog(parent);
        return result == JFileChooser.APPROVE_OPTION ? chooser.getSelectedFile() : null;
    }

    public static File selectImageFile(java.awt.Component parent) {
        JFileChooser chooser = getImageFileChooser();
        int result = chooser.showOpenDialog(parent);
        return result == JFileChooser.APPROVE_OPTION ? chooser.getSelectedFile() : null;
    }

    public static File selectSaveXMLFile(java.awt.Component parent) {
        JFileChooser chooser = getXMLFileChooser();
        int result = chooser.showSaveDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            String path = file.getAbsolutePath();
            if (!path.toLowerCase().endsWith(".xml")) {
                path += ".xml";
            }
            return new File(path);
        }
        return null;
    }

    public static void shutdown() {
        executor.shutdown();
    }
} 