package org.example.model;

import org.example.state.EditingMode;
import org.example.state.ViewingMode;
import org.example.view.SlideViewerComponent;
import org.example.view.SlideViewerFrame;
import org.example.view.SlideThumbnailPanel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PresentationTest {

    private Presentation presentation;
    private SlideViewerComponent mockSlideViewerComponent;
    private SlideViewerFrame mockSlideViewerFrame;
    private SlideThumbnailPanel mockThumbnailPanel;

    @BeforeEach
    void setUp() {
        // Create mocks
        mockSlideViewerComponent = Mockito.mock(SlideViewerComponent.class);
        mockSlideViewerFrame = Mockito.mock(SlideViewerFrame.class);
        mockThumbnailPanel = Mockito.mock(SlideThumbnailPanel.class);
        
        // Create presentation with mock SlideViewerComponent
        presentation = new Presentation(mockSlideViewerComponent);
        presentation.setSlideViewerFrame(mockSlideViewerFrame);
        presentation.setThumbnailPanel(mockThumbnailPanel);
    }

    @Test
    void constructor_withNoParameters_createsEmptyPresentationInViewingMode() {
        Presentation p = new Presentation();
        assertEquals("New jabbepoint presentation", p.getTitle());
        assertEquals(0, p.getSize());
        assertEquals(0, p.getSlideNumber());
        assertTrue(p.getCurrentState() instanceof ViewingMode);
    }

    @Test
    void constructor_withComponent_createsEmptyPresentationWithComponent() {
        assertEquals("New jabbepoint presentation", presentation.getTitle());
        assertEquals(0, presentation.getSize());
        assertEquals(0, presentation.getSlideNumber());
        assertTrue(presentation.getCurrentState() instanceof ViewingMode);
    }

    @Test
    void setTitle_withValidString_updatesTitleCorrectly() {
        presentation.setTitle("Test Presentation");
        assertEquals("Test Presentation", presentation.getTitle());
    }

    @Test
    void setSlideNumber_withValidIndex_updatesCurrentSlideNumber() {
        // Create mock slides
        Slide mockSlide1 = Mockito.mock(Slide.class);
        Slide mockSlide2 = Mockito.mock(Slide.class);
        
        // Add slides to presentation
        presentation.append(mockSlide1);
        presentation.append(mockSlide2);
        
        // Set slide number
        presentation.setSlideNumber(1);
        
        // Verify slide number was set correctly
        assertEquals(1, presentation.getSlideNumber());
    }

    @Test
    void setShowList_withValidList_updatesSlideListCorrectly() {
        ArrayList<Slide> slides = new ArrayList<>();
        slides.add(new Slide());
        slides.add(new Slide());
        
        presentation.setShowList(slides);
        
        assertSame(slides, presentation.getShowList());
        assertEquals(2, presentation.getSize());
    }

    @Test
    void getSlide_withValidAndInvalidIndices_returnsCorrectSlides() {
        Slide slide1 = new Slide();
        Slide slide2 = new Slide();
        
        presentation.append(slide1);
        presentation.append(slide2);
        
        assertSame(slide1, presentation.getSlide(0));
        assertSame(slide2, presentation.getSlide(1));
        
        // Test with invalid index
        assertNull(presentation.getSlide(-1));
        assertNull(presentation.getSlide(2));
    }

    @Test
    void nextSlide_whenNotAtLastSlide_incrementsSlideNumber() {
        // Setup slides
        presentation.append(new Slide());
        presentation.append(new Slide());
        
        // Initially at slide 0
        assertEquals(0, presentation.getSlideNumber());
        
        // Move to next slide
        presentation.nextSlide();
        assertEquals(1, presentation.getSlideNumber());
        
        // Try to move beyond last slide
        presentation.nextSlide();
        assertEquals(1, presentation.getSlideNumber()); // Should still be at slide 1
    }

    @Test
    void prevSlide_whenNotAtFirstSlide_decrementsSlideNumber() {
        // Setup slides
        presentation.append(new Slide());
        presentation.append(new Slide());
        
        // Start at slide 1
        presentation.setSlideNumber(1);
        
        // Move to previous slide
        presentation.prevSlide();
        assertEquals(0, presentation.getSlideNumber());
        
        // Try to move before first slide
        presentation.prevSlide();
        assertEquals(0, presentation.getSlideNumber()); // Should still be at slide 0
    }

    @Test
    void clear_afterAddingSlides_resetsToEmptyPresentation() {
        // Add some slides and set current slide
        presentation.append(new Slide());
        presentation.append(new Slide());
        presentation.setSlideNumber(1);
        
        // Clear presentation
        presentation.clear();
        
        // Verify state after clearing
        assertEquals(0, presentation.getSize());
        assertEquals(0, presentation.getSlideNumber());
        assertEquals("New jabbepoint presentation", presentation.getTitle());
        assertTrue(presentation.getCurrentState() instanceof ViewingMode);
    }

    @Test
    void append_withValidSlide_addsSlideToPresentation() {
        Slide slide = new Slide();
        
        // Append slide
        presentation.append(slide);
        
        // Verify slide was added
        assertEquals(1, presentation.getSize());
        assertSame(slide, presentation.getSlide(0));
    }

    @Test
    void editSlide_whenCalled_changesStateToEditingMode() {
        // Setup
        presentation.append(new Slide());
        
        // Call editSlide
        presentation.editSlide();
        
        // Verify state change
        assertTrue(presentation.getCurrentState() instanceof EditingMode);
    }
} 