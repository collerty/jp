package org.example.state;

import org.example.Presentation;
import org.example.Slide;

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

    }

    @Override
    public void enterFullscreen(Presentation presentation)
    {
        presentation.getSlideViewerFrame().exitFullScreen();
        System.out.println("Exit fullscreen mode");
        presentation.setCurrentState(new ViewingMode());
    }

    @Override
    public void exit(Presentation presentation, int statusCode)
    {
        System.exit(statusCode);
    }

    @Override
    public void clear(Presentation presentation)
    {

    }

    @Override
    public void addSlide(Presentation presentation, Slide slide)
    {

    }
}
