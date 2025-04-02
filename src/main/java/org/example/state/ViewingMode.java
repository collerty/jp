package org.example.state;

import org.example.Presentation;
import org.example.Slide;

import javax.swing.*;
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

    }

    @Override
    public void enterFullscreen(Presentation presentation)
    {
        presentation.getThumbnailPanel().setVisible(false);
        presentation.getSlideViewComponent().enterFullScreen();
        System.out.println("Entering fullscreen mode");
        presentation.setCurrentState(new FullscreenMode());
    }

    @Override
    public void exit(Presentation presentation, int statusCode)
    {
        System.exit(statusCode);
    }


    @Override
    public void save(Presentation presentation)
    {

    }

    @Override
    public void clear(Presentation presentation)
    {
        presentation.setShowList(new ArrayList<Slide>());
        presentation.setSlideNumber(-1);
    }

    @Override
    public void addSlide(Presentation presentation, Slide slide)
    {
        presentation.getShowList().add(slide);
        if (presentation.getThumbnailPanel() != null)
        {
            presentation.getThumbnailPanel().updateThumbnails();
        }
    }

}
