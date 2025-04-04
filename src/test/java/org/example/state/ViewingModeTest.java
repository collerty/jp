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

class ViewingModeTest {

    private ViewingMode viewingMode;
    private Presentation mockPresentation;
    private SlideViewerFrame mockFrame;
    private ArrayList<Slide> mockSlides;

    @BeforeEach
    void setUp() {
        viewingMode = new ViewingMode();
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
        
        viewingMode.nextSlide(mockPresentation);
        
        verify(mockPresentation).setSlideNumber(1);
        
        // Test next slide when at the end
        when(mockPresentation.getSlideNumber()).thenReturn(mockSlides.size() - 1);
        
        viewingMode.nextSlide(mockPresentation);
        
        // Should not change slide number when at the end
        verify(mockPresentation, never()).setSlideNumber(mockSlides.size());
    }

    @Test
    void prevSlide_whenNotAtBeginningOfPresentation_decrementsSlideNumber() {
        // Test prev slide when not at the beginning
        when(mockPresentation.getSlideNumber()).thenReturn(1);
        
        viewingMode.prevSlide(mockPresentation);
        
        verify(mockPresentation).setSlideNumber(0);
        
        // Test prev slide when at the beginning
        when(mockPresentation.getSlideNumber()).thenReturn(0);
        
        viewingMode.prevSlide(mockPresentation);
        
        // Should not change slide number when at the beginning
        verify(mockPresentation, never()).setSlideNumber(-1);
    }

    @Test
    void editSlide_whenCalled_changesStateToEditingMode() {
        viewingMode.editSlide(mockPresentation);
        
        // Should change state to EditingMode
        verify(mockPresentation).setCurrentState(any(EditingMode.class));
        
        // Should call enterEditMode on the frame
        verify(mockFrame).enterEditMode();
    }

    @Test
    void enterFullscreen_whenCalled_changesStateToFullscreenMode() {
        viewingMode.enterFullscreen(mockPresentation);
        
        // Should call enterFullScreen on the frame
        verify(mockFrame).enterFullScreen();
        
        // Should change state to FullscreenMode
        verify(mockPresentation).setCurrentState(any(FullscreenMode.class));
    }

    @Test
    void clear_whenCalled_resetsPresentation() {
        viewingMode.clear(mockPresentation);
        
        // Should clear the presentation
        verify(mockPresentation).setShowList(any(ArrayList.class));
        verify(mockPresentation).setCurrentSlideNumber(0);
        verify(mockPresentation).setTitle("New jabbepoint presentation");
    }

    @Test
    void addSlide_withValidSlide_addsSlideToPresentation() {
        Slide mockSlide = Mockito.mock(Slide.class);
        
        viewingMode.addSlide(mockPresentation, mockSlide);
        
        // Verify the show list is retrieved
        verify(mockPresentation).getShowList();
        
        // Since we can't mock the ArrayList's add method directly, we verify indirectly
        // by checking that the mockSlide was added to our mockSlides list
        assertTrue(mockSlides.contains(mockSlide));
    }
} 