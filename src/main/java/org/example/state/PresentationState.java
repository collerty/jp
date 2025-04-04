package org.example.state;

import org.example.model.Presentation;
import org.example.model.Slide;

public interface PresentationState
{
    void nextSlide(Presentation presentation);

    void prevSlide(Presentation presentation);

    void clear(Presentation presentation);

    void addSlide(Presentation presentation, Slide slide);

    void editSlide(Presentation presentation);

    void enterFullscreen(Presentation presentation);

    void exit(Presentation presentation, int statusCode);

}
