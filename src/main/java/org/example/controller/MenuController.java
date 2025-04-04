package org.example.controller;

import org.example.model.Presentation;
import org.example.view.AboutBox;
import org.example.filehandler.FileHandlerStrategy;
import org.example.filehandler.XMLFileHandler;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.io.File;

public class MenuController extends JMenuBar
{

    private JFrame parent; // the frame, only used as parent for the Dialogs
    private Presentation presentation; // Commands are given to the presentation
    private File currentFile;  // tracks current file to chnage on save
    private FileHandlerStrategy fileHandler;

    private static final long serialVersionUID = 227L;

    protected static final String ABOUT = "About";
    protected static final String FILE = "File";
    protected static final String EXIT = "Exit";
    protected static final String GOTO = "Go to";
    protected static final String HELP = "Help";
    protected static final String NEW = "New";
    protected static final String NEXT = "Next";
    protected static final String OPEN = "Open";
    protected static final String PAGENR = "Page number?";
    protected static final String PREV = "Prev";
    protected static final String SAVE = "Save";
    protected static final String SAVE_AS = "Save As...";
    protected static final String VIEW = "View";
    protected static final String FULL_SCREEN = "Full Screen";

    protected static final String IOEX = "IO Exception: ";
    protected static final String LOADERR = "Load Error";
    protected static final String SAVEERR = "Save Error";

    public MenuController(JFrame frame, Presentation pres)
    {
        parent = frame;
        presentation = pres;
        this.fileHandler = new XMLFileHandler(frame);
        JMenuItem menuItem;
        JMenu fileMenu = new JMenu(FILE);

        fileMenu.add(menuItem = mkMenuItem(NEW));
        menuItem.addActionListener(e -> newFile());

        fileMenu.add(menuItem = mkMenuItem(OPEN));
        menuItem.addActionListener(e -> openFile());

        fileMenu.add(menuItem = mkMenuItem(FULL_SCREEN));
        menuItem.addActionListener(e ->
        {
            presentation.enterFullscreen();
        });

        fileMenu.add(menuItem = mkMenuItem(SAVE));
        menuItem.addActionListener(e -> saveFile());

        fileMenu.add(menuItem = mkMenuItem(SAVE_AS, KeyEvent.VK_S, true));
        menuItem.addActionListener(e -> saveAs());

        fileMenu.addSeparator();
        fileMenu.add(menuItem = mkMenuItem(EXIT));
        menuItem.addActionListener(e -> presentation.exit(0));
        add(fileMenu);

        JMenu viewMenu = new JMenu(VIEW);
        viewMenu.add(menuItem = mkMenuItem(NEXT));
        menuItem.addActionListener(e -> presentation.nextSlide());
        viewMenu.add(menuItem = mkMenuItem(PREV));
        menuItem.addActionListener(e -> presentation.prevSlide());
        viewMenu.add(menuItem = mkMenuItem(GOTO));
        menuItem.addActionListener(e ->
        {
            String pageNumberStr = JOptionPane.showInputDialog((Object) PAGENR);
            int pageNumber = Integer.parseInt(pageNumberStr);
            presentation.setSlideNumber(pageNumber - 1);
        });
        add(viewMenu);

        JMenu helpMenu = new JMenu(HELP);
        helpMenu.add(menuItem = mkMenuItem(ABOUT));
        menuItem.addActionListener(e -> AboutBox.show(parent));
        add(helpMenu);
    }

    private void newFile()
    {
        fileHandler.newFile(presentation);
    }

    private void openFile()
    {
        fileHandler.openFile(presentation);
    }

    private void saveFile()
    {
        fileHandler.saveFile(presentation, currentFile);
    }

    private void saveAs()
    {
        fileHandler.saveAs(presentation);
    }

    // Create a menu item with specific shortcut key
    public JMenuItem mkMenuItem(String name, int keyCode, boolean useShiftModifier)
    {
        JMenuItem item = new JMenuItem(name);
        item.setAccelerator(KeyStroke.getKeyStroke(keyCode, useShiftModifier ? KeyEvent.SHIFT_DOWN_MASK : 0));

        return item;
    }

    // Create a menu item with default shortcut (first letter)
    public JMenuItem mkMenuItem(String name)
    {
        return new JMenuItem(name);
    }
}