package org.example.state;

import org.example.model.Presentation;
import org.example.model.Slide;
import org.example.view.SlideViewerFrame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ViewingModeTest
{

    private ViewingMode viewingMode;
    private Presentation mockPresentation;
    private SlideViewerFrame mockFrame;
    private ArrayList<Slide> mockSlides;

    @BeforeEach
    void setUp()
    {
        this.viewingMode = new ViewingMode();
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

        this.viewingMode.nextSlide(this.mockPresentation);

        verify(this.mockPresentation).setSlideNumber(1);

        when(this.mockPresentation.getSlideNumber()).thenReturn(this.mockSlides.size() - 1);

        this.viewingMode.nextSlide(this.mockPresentation);

        verify(this.mockPresentation, never()).setSlideNumber(this.mockSlides.size());
    }

    @Test
    void prevSlide_whenNotAtBeginningOfPresentation_decrementsSlideNumber()
    {
        when(this.mockPresentation.getSlideNumber()).thenReturn(1);

        this.viewingMode.prevSlide(this.mockPresentation);

        verify(this.mockPresentation).setSlideNumber(0);

        when(this.mockPresentation.getSlideNumber()).thenReturn(0);

        this.viewingMode.prevSlide(this.mockPresentation);

        verify(this.mockPresentation, never()).setSlideNumber(-1);
    }

    @Test
    void editSlide_whenCalled_changesStateToEditingMode()
    {
        this.viewingMode.editSlide(this.mockPresentation);

        verify(this.mockPresentation).setCurrentState(any(EditingMode.class));

        verify(this.mockFrame).enterEditMode();
    }

    @Test
    void enterFullscreen_whenCalled_changesStateToFullscreenMode()
    {
        this.viewingMode.enterFullscreen(this.mockPresentation);

        verify(this.mockFrame).enterFullScreen();

        verify(this.mockPresentation).setCurrentState(any(FullscreenMode.class));
    }

    @Test
    void clear_whenCalled_resetsPresentation()
    {
        this.viewingMode.clear(this.mockPresentation);

        verify(this.mockPresentation).setShowList(any(ArrayList.class));
        verify(this.mockPresentation).setCurrentSlideNumber(0);
        verify(this.mockPresentation).setTitle("New jabbepoint presentation");
    }

    @Test
    void addSlide_withValidSlide_addsSlideToPresentation()
    {
        Slide mockSlide = Mockito.mock(Slide.class);

        this.viewingMode.addSlide(this.mockPresentation, mockSlide);

        verify(this.mockPresentation).getShowList();

        assertTrue(this.mockSlides.contains(mockSlide));
    }
} 