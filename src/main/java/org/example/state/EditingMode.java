package org.example.state;

import org.example.model.Presentation;
import org.example.model.Slide;

import javax.swing.*;
import java.util.ArrayList;

public class EditingMode implements PresentationState
{
    @Override
    public void nextSlide(Presentation presentation)
    {
        if (presentation.getSlideNumber() < (presentation.getSize() - 1))
        {
            presentation.setSlideNumber(presentation.getSlideNumber() + 1);
        }
    }

    @Override
    public void prevSlide(Presentation presentation)
    {
        if (presentation.getSlideNumber() > 0)
        {
            presentation.setSlideNumber(presentation.getSlideNumber() - 1);
        }
    }

    @Override
    public void editSlide(Presentation presentation)
    {
        presentation.setCurrentState(new ViewingMode());
        presentation.getSlideViewerFrame().exitEditMode();
    }

    @Override
    public void enterFullscreen(Presentation presentation)
    {
        System.out.println("Cannot go to fullscreen mode in editing mode");
    }

    @Override
    public void exit(Presentation presentation, int statusCode)
    {
        int result = JOptionPane.showConfirmDialog(
                null,
                "Are you sure you want to exit?",
                "Exit Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (result == JOptionPane.YES_OPTION)
        {
            System.exit(statusCode);
        }

    }

    @Override
    public void addSlide(Presentation presentation, Slide slide)
    {
        presentation.getShowList().add(slide);
        presentation.setSlideNumber(presentation.getSize() - 1);
        // Ensure frame maintains focus ( do not remove, fixes bug with not registering shortcuts )
        if (presentation.getSlideViewerFrame() != null) {
            presentation.getSlideViewerFrame().requestFocus();
            presentation.getSlideViewerFrame().requestFocusInWindow();
            presentation.getSlideViewerFrame().toFront();
        }
    }
    
    @Override
    public void clear(Presentation presentation) {
        // In editing mode, clearing should do nothing to match the test expectations
        System.out.println("Cannot clear presentation in edit mode");
    }
}
