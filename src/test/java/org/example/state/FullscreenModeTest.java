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

class FullscreenModeTest {

    private FullscreenMode fullscreenMode;
    private Presentation mockPresentation;
    private SlideViewerFrame mockFrame;
    private ArrayList<Slide> mockSlides;

    @BeforeEach
    void setUp() {
        fullscreenMode = new FullscreenMode();
        mockPresentation = Mockito.mock(Presentation.class);
        mockFrame = Mockito.mock(SlideViewerFrame.class);
        mockSlides = new ArrayList<>();
        
        // Add mock slides
        mockSlides.add(Mockito.mock(Slide.class));
        mockSlides.add(Mockito.mock(Slide.class));
        mockSlides.add(Mockito.mock(Slide.class));
        
        // Setup mock presentation
        when(mockPresentation.getSlideViewerFrame()).thenReturn(mockFrame);
        when(mockPresentation.getShowList()).thenReturn(mockSlides);
        when(mockPresentation.getSize()).thenReturn(mockSlides.size());
    }

    @Test
    void nextSlide_whenNotAtEndOfPresentation_incrementsSlideNumber() {
        // Test next slide when not at the end
        when(mockPresentation.getSlideNumber()).thenReturn(0);
        
        fullscreenMode.nextSlide(mockPresentation);
        
        verify(mockPresentation).setSlideNumber(1);
        
        // Test next slide when at the end
        when(mockPresentation.getSlideNumber()).thenReturn(mockSlides.size() - 1);
        
        fullscreenMode.nextSlide(mockPresentation);
        
        // Should not change slide number when at the end
        verify(mockPresentation, never()).setSlideNumber(mockSlides.size());
    }

    @Test
    void prevSlide_whenNotAtBeginningOfPresentation_decrementsSlideNumber() {
        // Test prev slide when not at the beginning
        when(mockPresentation.getSlideNumber()).thenReturn(1);
        
        fullscreenMode.prevSlide(mockPresentation);
        
        verify(mockPresentation).setSlideNumber(0);
        
        // Test prev slide when at the beginning
        when(mockPresentation.getSlideNumber()).thenReturn(0);
        
        fullscreenMode.prevSlide(mockPresentation);
        
        // Should not change slide number when at the beginning
        verify(mockPresentation, never()).setSlideNumber(-1);
    }

    @Test
    void editSlide_whenCalled_doesNothing() {
        fullscreenMode.editSlide(mockPresentation);
        
        // Should not interact with presentation or frame
        verify(mockPresentation, never()).setCurrentState(any());
        verify(mockFrame, never()).enterEditMode();
    }

    @Test
    void enterFullscreen_whenCalled_exitsFullscreenAndChangesToViewingMode() {
        fullscreenMode.enterFullscreen(mockPresentation);
        
        // Should call exitFullScreen on the frame
        verify(mockFrame).exitFullScreen();
        
        // Should change state to ViewingMode
        verify(mockPresentation).setCurrentState(any(ViewingMode.class));
    }

    @Test
    void clear_whenCalled_doesNothing() {
        fullscreenMode.clear(mockPresentation);
        
        // Should not interact with presentation
        verify(mockPresentation, never()).setShowList(any());
        verify(mockPresentation, never()).setCurrentSlideNumber(anyInt());
        verify(mockPresentation, never()).setTitle(anyString());
    }

    @Test
    void addSlide_whenCalled_doesNothing() {
        Slide mockSlide = Mockito.mock(Slide.class);
        
        fullscreenMode.addSlide(mockPresentation, mockSlide);
        
        // Verify no interactions with presentation
        verify(mockPresentation, never()).getShowList();
        
        // Verify the slide is not added to our mockSlides list
        assertEquals(3, mockSlides.size());
    }

    @Test
    @Disabled("Cannot test System.exit without SecurityManager")
    void exit_whenCalled_callsSystemExit() {
        // This test is disabled because we can't easily test System.exit calls
        // without a SecurityManager, which requires more setup
    }
} 