package org.example.style;

import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

public class StyleConstants {
    // Colors
    public static final Color PRIMARY = new Color(67, 76, 94);
    public static final Color PRIMARY_HOVER = new Color(88, 101, 126);  // slide hover in thumbnail bg
    public static final Color SECONDARY = new Color(46, 52, 64);
    public static final Color BACKGROUND = new Color(46, 52, 64);
    public static final Color SECONDARY_BACKGROUND = new Color(76, 86, 106); // header bg
    public static final Color SURFACE = Color.WHITE;  // Slide surface
    public static final Color TEXT_ON_DARK = new Color(216, 222, 233);
    public static final Color TEXT_ON_LIGHT = Color.BLACK;
    public static final Color BORDER = new Color(200, 200, 200);
    
    // Fonts
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font LABEL_FONT = new Font("Dialog", Font.BOLD, 10);
    
    // Dimensions
    public static final int WINDOW_WIDTH = 1200;
    public static final int WINDOW_HEIGHT = 800;
    public static final int BUTTON_WIDTH = 160;
    public static final int BUTTON_HEIGHT = 60;
    public static final int THUMBNAIL_WIDTH = 160;
    public static final int THUMBNAIL_HEIGHT = 120;
    public static final int SLIDE_MARGIN = 40;
    public static final int BUTTON_SPACING = 16;
    public static final int PANEL_HEIGHT = 120;

    // Borders and padding
    public static final int BORDER_RADIUS = 15;
    public static final int BORDER_WIDTH = 2;
    public static final Border EMPTY_BORDER = BorderFactory.createEmptyBorder(5, 10, 5, 10);
    
    // Common margins
    public static final int MARGIN_SMALL = 8;
    public static final int MARGIN_MEDIUM = 16;
    public static final int MARGIN_LARGE = 24;
}