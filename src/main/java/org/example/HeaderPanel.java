package org.example;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import java.awt.geom.*;

public class HeaderPanel extends JPanel {
    private static final Color BACKGROUND_COLOR = new Color(76, 86, 106);
    private static final Color BUTTON_COLOR = new Color(67, 76, 94);
    private static final Color BUTTON_HOVER_COLOR = new Color(88, 101, 126);
    private static final Color TEXT_COLOR = Color.WHITE;
    
    private static final int PANEL_HEIGHT = 96;  // Fixed height for the header
    private static final int BUTTON_WIDTH = 160;  // Fixed width for buttons
    private static final int BUTTON_HEIGHT = 96;  // Fixed height for buttons
    private static final int HORIZONTAL_MARGIN = 40;  // Margin from left/right edges
    private static final int VERTICAL_MARGIN = 20;    // Margin from top/bottom edges
    private static final int BUTTON_SPACING = 16;     // Space between buttons
    private static final int PANEL_PADDING = 40;      // Padding for the panel
    
    private Presentation presentation;
    private JButton addSlideButton;
    private JButton addTextSlideButton;

    public HeaderPanel(Presentation presentation) {
        this.presentation = presentation;
        setBackground(BACKGROUND_COLOR);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(new TextBubbleBorder(BACKGROUND_COLOR, 2, 15, 0)); // Using TextBubbleBorder with no pointer
        setPreferredSize(new Dimension(Slide.WIDTH, PANEL_HEIGHT));

        // Create a container panel for buttons with vertical centering
        JPanel buttonContainer = new JPanel();
        buttonContainer.setLayout(new BoxLayout(buttonContainer, BoxLayout.X_AXIS));
        buttonContainer.setBackground(BACKGROUND_COLOR);

        // Create and style the "Add New Slide" button
        addSlideButton = createStyledButton("Add New Slide");
        addSlideButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        addSlideButton.addActionListener(e -> {
            Slide newSlide = new Slide();
            presentation.append(newSlide);
            presentation.setSlideNumber(presentation.getSize() - 1);
        });

        // Create and style the "Add Text Slide" button
        addTextSlideButton = createStyledButton("Add Text Slide");
        addTextSlideButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        addTextSlideButton.addActionListener(e -> {
            Slide newSlide = new Slide();
            TextItem titleItem = new TextItem(1, "New Text Slide");
            TextItem textItem = new TextItem(2, "Enter your text here");
            newSlide.append(titleItem);
            newSlide.append(textItem);
            presentation.append(newSlide);
            presentation.setSlideNumber(presentation.getSize() - 1);
        });

        // Add buttons to the container with spacing
        buttonContainer.add(Box.createHorizontalGlue()); // Push buttons to center
        buttonContainer.add(addSlideButton);
        buttonContainer.add(Box.createHorizontalStrut(BUTTON_SPACING));
        buttonContainer.add(addTextSlideButton);
        buttonContainer.add(Box.createHorizontalGlue()); // Push buttons to center

        // Add the button container to the main panel
        add(buttonContainer);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                if (getModel().isPressed()) {
                    g.setColor(BUTTON_HOVER_COLOR);
                } else if (getModel().isRollover()) {
                    g.setColor(BUTTON_HOVER_COLOR);
                } else {
                    g.setColor(BUTTON_COLOR);
                }
                g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 5, 5);
                super.paintComponent(g);
            }
        };
        
        button.setForeground(TEXT_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Set both preferred and maximum size
        Dimension buttonSize = new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT);
        button.setPreferredSize(buttonSize);
        button.setMaximumSize(buttonSize);
        button.setMinimumSize(buttonSize);
        
        return button;
    }
}

class TextBubbleBorder extends AbstractBorder {
    private Color color;
    private int thickness = 4;
    private int radii = 8;
    private int pointerSize = 7;
    private Insets insets = null;
    private BasicStroke stroke = null;
    private int strokePad;
    private int pointerPad = 4;
    private boolean left = true;
    RenderingHints hints;

    TextBubbleBorder(Color color) {
        this(color, 4, 8, 7);
    }

    TextBubbleBorder(Color color, int thickness, int radii, int pointerSize) {
        this.thickness = thickness;
        this.radii = radii;
        this.pointerSize = pointerSize;
        this.color = color;

        stroke = new BasicStroke(thickness);
        strokePad = thickness / 2;

        hints = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int pad = radii + strokePad;
        int bottomPad = pad + pointerSize + strokePad;
        insets = new Insets(pad, pad, bottomPad, pad);
    }

    TextBubbleBorder(Color color, int thickness, int radii, int pointerSize, boolean left) {
        this(color, thickness, radii, pointerSize);
        this.left = left;
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return insets;
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        return getBorderInsets(c);
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g;

        int bottomLineY = height - thickness - pointerSize;

        RoundRectangle2D.Double bubble = new RoundRectangle2D.Double(
                0 + strokePad,
                0 + strokePad,
                width - thickness,
                bottomLineY,
                radii,
                radii);

        Polygon pointer = new Polygon();

        if (left) {
            // left point
            pointer.addPoint(
                    strokePad + radii + pointerPad,
                    bottomLineY);
            // right point
            pointer.addPoint(
                    strokePad + radii + pointerPad + pointerSize,
                    bottomLineY);
            // bottom point
            pointer.addPoint(
                    strokePad + radii + pointerPad + (pointerSize / 2),
                    height - strokePad);
        } else {
            // left point
            pointer.addPoint(
                    width - (strokePad + radii + pointerPad),
                    bottomLineY);
            // right point
            pointer.addPoint(
                    width - (strokePad + radii + pointerPad + pointerSize),
                    bottomLineY);
            // bottom point
            pointer.addPoint(
                    width - (strokePad + radii + pointerPad + (pointerSize / 2)),
                    height - strokePad);
        }

        Area area = new Area(bubble);
        area.add(new Area(pointer));

        g2.setRenderingHints(hints);

        // Paint the BG color of the parent, everywhere outside the clip
        // of the text bubble.
        Component parent = c.getParent();
        if (parent != null) {
            Color bg = parent.getBackground();
            Rectangle rect = new Rectangle(0, 0, width, height);
            Area borderRegion = new Area(rect);
            borderRegion.subtract(area);
            g2.setClip(borderRegion);
            g2.setColor(bg);
            g2.fillRect(0, 0, width, height);
            g2.setClip(null);
        }

        g2.setColor(color);
        g2.setStroke(stroke);
        g2.draw(area);
    }
}