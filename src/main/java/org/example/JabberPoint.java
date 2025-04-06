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
            UIManager.setLookAndFeel(new FlatNordIJTheme());
        } catch (UnsupportedLookAndFeelException e)
        {
            e.printStackTrace();
        }

        FileChooserUtils.initialize();

        Style.createStyles();

        Presentation presentation = new Presentation();

        StartMenu startMenu = new StartMenu();

    }
}
