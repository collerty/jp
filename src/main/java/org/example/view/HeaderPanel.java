package org.example.view;

import org.example.model.BitmapItem;
import org.example.model.Presentation;
import org.example.model.Slide;
import org.example.model.TextItem;
import org.example.style.StyleConstants;
import org.example.util.FileChooserUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class HeaderPanel extends JPanel
{
    private Presentation presentation;
    private JButton addSlideButton;
    private JButton addTextSlideButton;
    private JButton addTextButton;
    private JButton addImageButton;

    public HeaderPanel(Presentation presentation)
    {
        this.presentation = presentation;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(Slide.WIDTH, StyleConstants.PANEL_HEIGHT));
        setOpaque(false); // Important for rounded corners to work

        // Create a container panel for buttons with vertical centering
        JPanel buttonContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw rounded background
                g2.setColor(StyleConstants.SECONDARY_BACKGROUND);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 
                    StyleConstants.BORDER_RADIUS, StyleConstants.BORDER_RADIUS);
            }
        };
        
        // Use GridBagLayout for precise centering
        buttonContainer.setLayout(new GridBagLayout());
        buttonContainer.setOpaque(false);
        
        // Create a panel for the buttons using FlowLayout
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setOpaque(false);
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, StyleConstants.BUTTON_SPACING, 0));

        // Create and style the "Add New Slide" button
        addSlideButton = createStyledButton("Add New Slide");
        addSlideButton.addActionListener(e ->
        {
            Slide newSlide = new Slide();
            presentation.append(newSlide);
            presentation.setSlideNumber(presentation.getSize() - 1);
        });

        // Create and style the "Add Text Slide" button
        addTextSlideButton = createStyledButton("Add Text Slide");
        addTextSlideButton.addActionListener(e ->
        {
            Slide newSlide = new Slide();
            TextItem titleItem = new TextItem(1, "New Text Slide");
            TextItem textItem = new TextItem(2, "Enter your text here");
            newSlide.append(titleItem);
            newSlide.append(textItem);
            presentation.append(newSlide);
            presentation.setSlideNumber(presentation.getSize() - 1);
        });

        // Create and style the "Add Text" button
        addTextButton = createStyledButton("Add Text");
        addTextButton.addActionListener(e ->
        {
            if (presentation.getCurrentSlide() != null)
            {
                String text = JOptionPane.showInputDialog("Enter text to add:");
                if (text != null && !text.trim().isEmpty())
                {
                    TextItem textItem = new TextItem(presentation.getCurrentSlide().getSize() + 1, text);
                    presentation.getCurrentSlide().append(textItem);
                    presentation.getSlideViewComponent().update(presentation, presentation.getCurrentSlide());
                    maintainFrameFocus();
                }
            }
        });

        // Create and style the "Add Image" button
        addImageButton = createStyledButton("Add Image");
        addImageButton.addActionListener(e ->
        {
            if (presentation.getCurrentSlide() != null)
            {
                handleImageAddition();
            }
        });

        // Add buttons to the buttons panel
        buttonsPanel.add(addSlideButton);
        buttonsPanel.add(addTextSlideButton);
        buttonsPanel.add(addTextButton);
        buttonsPanel.add(addImageButton);
        
        // Add the buttons panel to the container with GridBagConstraints for perfect centering
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        buttonContainer.add(buttonsPanel, gbc);

        // Create a wrapper with padding
        JPanel containerWrapper = new JPanel(new BorderLayout());
        containerWrapper.setOpaque(false);
        containerWrapper.setBorder(BorderFactory.createEmptyBorder(
            StyleConstants.MARGIN_MEDIUM, 
            StyleConstants.MARGIN_LARGE, 
            StyleConstants.MARGIN_MEDIUM, 
            StyleConstants.MARGIN_LARGE
        ));
        containerWrapper.add(buttonContainer, BorderLayout.CENTER);

        // Add the wrapper to the main panel
        add(containerWrapper, BorderLayout.CENTER);
    }

    private void handleImageAddition() {
        java.io.File selectedFile = FileChooserUtils.selectImageFile(this);
        if (selectedFile != null) {
            BitmapItem bitmapItem = new BitmapItem(presentation.getCurrentSlide().getSize() + 1, selectedFile.getAbsolutePath());
            presentation.getCurrentSlide().append(bitmapItem);
            presentation.getSlideViewComponent().update(presentation, presentation.getCurrentSlide());
            maintainFrameFocus();
        }
    }

    private void maintainFrameFocus() {
        // Ensure frame maintains focus
        if (presentation.getSlideViewerFrame() != null) {
            presentation.getSlideViewerFrame().requestFocus();
            presentation.getSlideViewerFrame().requestFocusInWindow();
            presentation.getSlideViewerFrame().toFront();
        }
    }

    private JButton createStyledButton(String text)
    {
        JButton button = new JButton(text)
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed() || getModel().isRollover())
                {
                    g2.setColor(StyleConstants.PRIMARY_HOVER);
                }
                else
                {
                    g2.setColor(StyleConstants.PRIMARY);
                }
                
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 
                    StyleConstants.BORDER_RADIUS, StyleConstants.BORDER_RADIUS);
                
                super.paintComponent(g);
            }
            
            // Force the button to respect its preferred size
            @Override
            public Dimension getMinimumSize() {
                return getPreferredSize();
            }
            
            @Override
            public Dimension getMaximumSize() {
                return getPreferredSize();
            }
        };

        button.setForeground(StyleConstants.TEXT_ON_DARK);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFont(StyleConstants.BODY_FONT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Set the preferred size
        button.setPreferredSize(new Dimension(StyleConstants.BUTTON_WIDTH, StyleConstants.BUTTON_HEIGHT));

        return button;
    }
}
