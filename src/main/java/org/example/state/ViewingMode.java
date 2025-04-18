package org.example.state;

import org.example.model.Presentation;
import org.example.model.Slide;

import java.util.ArrayList;

public class ViewingMode implements PresentationState
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
        presentation.setCurrentState(new EditingMode());
        presentation.getSlideViewerFrame().enterEditMode();
    }

    @Override
    public void enterFullscreen(Presentation presentation)
    {
        presentation.getSlideViewerFrame().enterFullScreen();
        System.out.println("Entering fullscreen mode");
        presentation.setCurrentState(new FullscreenMode());
    }

    @Override
    public void exit(Presentation presentation, int statusCode)
    {
        System.exit(statusCode);
    }

    @Override
    public void addSlide(Presentation presentation, Slide slide)
    {
        presentation.getShowList().add(slide);
    }

    @Override
    public void clear(Presentation presentation) {
        presentation.setShowList(new ArrayList<>());
        presentation.setCurrentSlideNumber(0);
        presentation.setTitle("New jabbepoint presentation");
    }
}
