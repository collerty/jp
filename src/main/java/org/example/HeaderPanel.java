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
