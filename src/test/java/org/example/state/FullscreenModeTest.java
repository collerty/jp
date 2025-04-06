package org.example.state;

import org.example.model.Presentation;
import org.example.model.Slide;
import org.example.view.SlideViewerFrame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FullscreenModeTest
{

    private FullscreenMode fullscreenMode;
    private Presentation mockPresentation;
    private SlideViewerFrame mockFrame;
    private ArrayList<Slide> mockSlides;

    @BeforeEach
    void setUp()
    {
        this.fullscreenMode = new FullscreenMode();
        this.mockPresentation = Mockito.mock(Presentation.class);
        this.mockFrame = Mockito.mock(SlideViewerFrame.class);
        this.mockSlides = new ArrayList<>();

        this.mockSlides.add(Mockito.mock(Slide.class));
        this.mockSlides.add(Mockito.mock(Slide.class));
        this.mockSlides.add(Mockito.mock(Slide.class));

        when(this.mockPresentation.getSlideViewerFrame()).thenReturn(this.mockFrame);
        when(this.mockPresentation.getShowList()).thenReturn(this.mockSlides);
        when(this.mockPresentation.getSize()).thenReturn(this.mockSlides.size());
    }

    @Test
    void nextSlide_whenNotAtEndOfPresentation_incrementsSlideNumber()
    {
        when(this.mockPresentation.getSlideNumber()).thenReturn(0);

        this.fullscreenMode.nextSlide(this.mockPresentation);

        verify(this.mockPresentation).setSlideNumber(1);

        when(this.mockPresentation.getSlideNumber()).thenReturn(this.mockSlides.size() - 1);

        this.fullscreenMode.nextSlide(this.mockPresentation);

        verify(this.mockPresentation, never()).setSlideNumber(this.mockSlides.size());
    }

    @Test
    void prevSlide_whenNotAtBeginningOfPresentation_decrementsSlideNumber()
    {
        when(this.mockPresentation.getSlideNumber()).thenReturn(1);

        this.fullscreenMode.prevSlide(this.mockPresentation);

        verify(this.mockPresentation).setSlideNumber(0);

        when(this.mockPresentation.getSlideNumber()).thenReturn(0);

        this.fullscreenMode.prevSlide(this.mockPresentation);

        verify(this.mockPresentation, never()).setSlideNumber(-1);
    }

    @Test
    void editSlide_whenCalled_doesNothing()
    {
        this.fullscreenMode.editSlide(this.mockPresentation);

        verify(this.mockPresentation, never()).setCurrentState(any());
        verify(this.mockFrame, never()).enterEditMode();
    }

    @Test
    void enterFullscreen_whenCalled_exitsFullscreenAndChangesToViewingMode()
    {
        this.fullscreenMode.enterFullscreen(this.mockPresentation);

        verify(this.mockFrame).exitFullScreen();

        verify(this.mockPresentation).setCurrentState(any(ViewingMode.class));
    }

    @Test
    void clear_whenCalled_doesNothing()
    {
        this.fullscreenMode.clear(this.mockPresentation);

        verify(this.mockPresentation, never()).setShowList(any());
        verify(this.mockPresentation, never()).setCurrentSlideNumber(anyInt());
        verify(this.mockPresentation, never()).setTitle(anyString());
    }

    @Test
    void addSlide_whenCalled_doesNothing()
    {
        Slide mockSlide = Mockito.mock(Slide.class);

        this.fullscreenMode.addSlide(this.mockPresentation, mockSlide);

        verify(this.mockPresentation, never()).getShowList();

        assertEquals(3, this.mockSlides.size());
    }

}