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

class PresentationTest
{

    private Presentation presentation;
    private SlideViewerComponent mockSlideViewerComponent;
    private SlideViewerFrame mockSlideViewerFrame;
    private SlideThumbnailPanel mockThumbnailPanel;

    @BeforeEach
    void setUp()
    {
        this.mockSlideViewerComponent = Mockito.mock(SlideViewerComponent.class);
        this.mockSlideViewerFrame = Mockito.mock(SlideViewerFrame.class);
        this.mockThumbnailPanel = Mockito.mock(SlideThumbnailPanel.class);

        this.presentation = new Presentation(this.mockSlideViewerComponent);
        this.presentation.setSlideViewerFrame(this.mockSlideViewerFrame);
        this.presentation.setThumbnailPanel(this.mockThumbnailPanel);
    }

    @Test
    void constructor_withNoParameters_createsEmptyPresentationInViewingMode()
    {
        Presentation p = new Presentation();
        assertEquals("New jabbepoint presentation", p.getTitle());
        assertEquals(0, p.getSize());
        assertEquals(0, p.getSlideNumber());
        assertTrue(p.getCurrentState() instanceof ViewingMode);
    }

    @Test
    void constructor_withComponent_createsEmptyPresentationWithComponent()
    {
        assertEquals("New jabbepoint presentation", this.presentation.getTitle());
        assertEquals(0, this.presentation.getSize());
        assertEquals(0, this.presentation.getSlideNumber());
        assertTrue(this.presentation.getCurrentState() instanceof ViewingMode);
    }

    @Test
    void setTitle_withValidString_updatesTitleCorrectly()
    {
        this.presentation.setTitle("Test Presentation");
        assertEquals("Test Presentation", this.presentation.getTitle());
    }

    @Test
    void setSlideNumber_withValidIndex_updatesCurrentSlideNumber()
    {
        Slide mockSlide1 = Mockito.mock(Slide.class);
        Slide mockSlide2 = Mockito.mock(Slide.class);

        this.presentation.append(mockSlide1);
        this.presentation.append(mockSlide2);

        this.presentation.setSlideNumber(1);

        assertEquals(1, this.presentation.getSlideNumber());
    }

    @Test
    void setShowList_withValidList_updatesSlideListCorrectly()
    {
        ArrayList<Slide> slides = new ArrayList<>();
        slides.add(new Slide());
        slides.add(new Slide());

        this.presentation.setShowList(slides);

        assertSame(slides, this.presentation.getShowList());
        assertEquals(2, this.presentation.getSize());
    }

    @Test
    void getSlide_withValidAndInvalidIndices_returnsCorrectSlides()
    {
        Slide slide1 = new Slide();
        Slide slide2 = new Slide();

        this.presentation.append(slide1);
        this.presentation.append(slide2);

        assertSame(slide1, this.presentation.getSlide(0));
        assertSame(slide2, this.presentation.getSlide(1));

        assertNull(this.presentation.getSlide(-1));
        assertNull(this.presentation.getSlide(2));
    }

    @Test
    void nextSlide_whenNotAtLastSlide_incrementsSlideNumber()
    {
        this.presentation.append(new Slide());
        this.presentation.append(new Slide());

        assertEquals(0, this.presentation.getSlideNumber());

        this.presentation.nextSlide();
        assertEquals(1, this.presentation.getSlideNumber());

        this.presentation.nextSlide();
        assertEquals(1, this.presentation.getSlideNumber());
    }

    @Test
    void prevSlide_whenNotAtFirstSlide_decrementsSlideNumber()
    {
        this.presentation.append(new Slide());
        this.presentation.append(new Slide());

        this.presentation.setSlideNumber(1);

        this.presentation.prevSlide();
        assertEquals(0, this.presentation.getSlideNumber());

        this.presentation.prevSlide();
        assertEquals(0, this.presentation.getSlideNumber());
    }

    @Test
    void clear_afterAddingSlides_resetsToEmptyPresentation()
    {
        this.presentation.append(new Slide());
        this.presentation.append(new Slide());
        this.presentation.setSlideNumber(1);

        this.presentation.clear();

        assertEquals(0, this.presentation.getSize());
        assertEquals(0, this.presentation.getSlideNumber());
        assertEquals("New jabbepoint presentation", this.presentation.getTitle());
        assertTrue(this.presentation.getCurrentState() instanceof ViewingMode);
    }

    @Test
    void append_withValidSlide_addsSlideToPresentation()
    {
        Slide slide = new Slide();

        this.presentation.append(slide);

        assertEquals(1, this.presentation.getSize());
        assertSame(slide, this.presentation.getSlide(0));
    }

    @Test
    void editSlide_whenCalled_changesStateToEditingMode()
    {
        this.presentation.append(new Slide());

        this.presentation.editSlide();

        assertTrue(this.presentation.getCurrentState() instanceof EditingMode);
    }
} 