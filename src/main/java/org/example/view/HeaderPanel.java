package org.example.view;

import org.example.model.Presentation;
import org.example.model.Slide;
import org.example.model.slideComponents.SlideItem;
import org.example.model.slideComponents.factory.SlideItemFactory;
import org.example.style.StyleConstants;
import org.example.util.FileChooserUtils;

import javax.swing.*;
import java.awt.*;

public class HeaderPanel extends JPanel
{
    private final Presentation presentation;
    private JButton addSlideButton;
    private JButton addTextSlideButton;
    private JButton addTextButton;
    private JButton addImageButton;

    public HeaderPanel(Presentation presentation)
    {
        this.presentation = presentation;
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(Slide.WIDTH, StyleConstants.PANEL_HEIGHT));
        this.setOpaque(false); // Important for rounded corners to work

        // Create a container panel for buttons with vertical centering
        JPanel buttonContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw rounded background
                g2.setColor(StyleConstants.SECONDARY_BACKGROUND);
                g2.fillRoundRect(0, 0, this.getWidth(), this.getHeight(),
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
        this.addSlideButton = this.createStyledButton("Add New Slide");
        this.addSlideButton.addActionListener(e ->
        {
            Slide newSlide = new Slide();
            presentation.append(newSlide);
            presentation.setSlideNumber(presentation.getSize() - 1);
        });

        // Create and style the "Add Text Slide" button
        this.addTextSlideButton = this.createStyledButton("Add Text Slide");
        this.addTextSlideButton.addActionListener(e ->
        {
            Slide newSlide = new Slide();
            // Use factory to create TextItems
            SlideItem titleItem = SlideItemFactory.createTextItem(1, "New Text Slide");
            SlideItem textItem = SlideItemFactory.createTextItem(2, "Enter your text here");
            newSlide.append(titleItem);
            newSlide.append(textItem);
            presentation.append(newSlide);
            presentation.setSlideNumber(presentation.getSize() - 1);
        });

        // Create and style the "Add Text" button
        this.addTextButton = this.createStyledButton("Add Text");
        this.addTextButton.addActionListener(e ->
        {
            if (presentation.getCurrentSlide() != null)
            {
                String text = JOptionPane.showInputDialog("Enter text to add:");
                if (text != null && !text.trim().isEmpty())
                {
                    int level = presentation.getCurrentSlide().getSize() + 1;
                    
                    int boldChoice = JOptionPane.showConfirmDialog(null, "Do you want the text to be bold?", "Bold", JOptionPane.YES_NO_OPTION);
                    boolean isBold = (boldChoice == JOptionPane.YES_OPTION);
                    
                    int italicChoice = JOptionPane.showConfirmDialog(null, "Do you want the text to be italic?", "Italic", JOptionPane.YES_NO_OPTION);
                    boolean isItalic = (italicChoice == JOptionPane.YES_OPTION);
                    
                    SlideItem item = SlideItemFactory.createFormattedTextItem(level, text, isBold, isItalic);
                    

                    presentation.getCurrentSlide().append(item);
                    
                    presentation.getSlideViewComponent().update(presentation, presentation.getCurrentSlide());
                    
                    SwingUtilities.invokeLater(() -> {
                        presentation.getSlideViewComponent().revalidate();
                        presentation.getSlideViewComponent().repaint();
                        
                        SwingUtilities.invokeLater(() -> {
                            presentation.getSlideViewComponent().repaint();
                        });
                    });

                    this.maintainFrameFocus();
                }
            }
        });


        // Create and style the "Add Image" button
        this.addImageButton = this.createStyledButton("Add Image");
        this.addImageButton.addActionListener(e ->
        {
            if (presentation.getCurrentSlide() != null)
            {
                this.handleImageAddition();
            }
        });

        buttonsPanel.add(this.addSlideButton);
        buttonsPanel.add(this.addTextSlideButton);
        buttonsPanel.add(this.addTextButton);
        buttonsPanel.add(this.addImageButton);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        buttonContainer.add(buttonsPanel, gbc);

        JPanel containerWrapper = new JPanel(new BorderLayout());
        containerWrapper.setOpaque(false);
        containerWrapper.setBorder(BorderFactory.createEmptyBorder(
            StyleConstants.MARGIN_MEDIUM, 
            StyleConstants.MARGIN_LARGE, 
            StyleConstants.MARGIN_MEDIUM, 
            StyleConstants.MARGIN_LARGE
        ));
        containerWrapper.add(buttonContainer, BorderLayout.CENTER);

        this.add(containerWrapper, BorderLayout.CENTER);
    }

    private void handleImageAddition() {
        java.io.File selectedFile = FileChooserUtils.selectImageFile(this);
        if (selectedFile != null) {
            int level = this.presentation.getCurrentSlide().getSize() + 1;
            SlideItem bitmapItem = SlideItemFactory.createBitmapItem(level, selectedFile.getAbsolutePath());

            this.presentation.getCurrentSlide().append(bitmapItem);
            this.presentation.getSlideViewComponent().update(this.presentation, this.presentation.getCurrentSlide());
            this.maintainFrameFocus();
        }
    }

    private void maintainFrameFocus() {
        if (this.presentation.getSlideViewerFrame() != null) {
            this.presentation.getSlideViewerFrame().requestFocus();
            this.presentation.getSlideViewerFrame().requestFocusInWindow();
            this.presentation.getSlideViewerFrame().toFront();
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
                
                if (this.getModel().isPressed() || this.getModel().isRollover())
                {
                    g2.setColor(StyleConstants.PRIMARY_HOVER);
                }
                else
                {
                    g2.setColor(StyleConstants.PRIMARY);
                }
                
                g2.fillRoundRect(0, 0, this.getWidth() - 1, this.getHeight() - 1,
                    StyleConstants.BORDER_RADIUS, StyleConstants.BORDER_RADIUS);
                
                super.paintComponent(g);
            }
            
            @Override
            public Dimension getMinimumSize() {
                return this.getPreferredSize();
            }
            
            @Override
            public Dimension getMaximumSize() {
                return this.getPreferredSize();
            }
        };

        button.setForeground(StyleConstants.TEXT_ON_DARK);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFont(StyleConstants.BODY_FONT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.setPreferredSize(new Dimension(StyleConstants.BUTTON_WIDTH, StyleConstants.BUTTON_HEIGHT));

        return button;
    }
}
