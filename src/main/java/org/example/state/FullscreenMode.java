package org.example.state;

import org.example.model.Presentation;
import org.example.model.Slide;

import javax.swing.*;
import java.util.ArrayList;

public class FullscreenMode implements PresentationState
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
        System.out.println("This action is not allowed");
    }

    @Override
    public void enterFullscreen(Presentation presentation)
    {
        presentation.getSlideViewerFrame().exitFullScreen();
        presentation.setCurrentState(new ViewingMode());
    }

    @Override
    public void exit(Presentation presentation, int statusCode)
    {
        System.exit(statusCode);
    }

    @Override
    public void addSlide(Presentation presentation, Slide slide)
    {
        // Not allowed in fullscreen mode
    }
    
    @Override
    public void clear(Presentation presentation) {
        // Not allowed in fullscreen mode
        System.out.println("Cannot clear presentation in fullscreen mode");
    }
}
