package org.example;

import com.formdev.flatlaf.intellijthemes.FlatNordIJTheme;
import org.example.model.Presentation;
import org.example.model.Style;
import org.example.view.StartMenu;
import org.example.view.SlideViewerFrame;
import org.example.util.FileChooserUtils;

import javax.swing.*;


public class JabberPoint
{
    protected static final String IOERR = "IO Error: ";
    protected static final String JABERR = "Jabberpoint Error ";
    protected static final String JABVERSION = "Jabberpoint 1.6 - OU version";

    /**
     * Main Program
     */

    public static void main(String argv[])
    {
        try
        {
            UIManager.setLookAndFeel(new FlatNordIJTheme());  // Set the Look and Feel
        } catch (UnsupportedLookAndFeelException e)
        {
            e.printStackTrace();
        }

        // Initialize file choosers in parallel
        FileChooserUtils.initialize();

        // Create styles
        Style.createStyles();

        Presentation presentation = new Presentation();

        // First show the Start Menu
        StartMenu startMenu = new StartMenu();

    }
}
