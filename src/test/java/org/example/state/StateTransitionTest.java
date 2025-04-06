package org.example.state;

import org.example.model.Presentation;
import org.example.view.SlideViewerComponent;
import org.example.view.SlideViewerFrame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StateTransitionTest
{

    private Presentation presentation;
    private SlideViewerComponent mockSlideViewerComponent;
    private SlideViewerFrame mockSlideViewerFrame;

    @BeforeEach
    void setUp()
    {
        this.mockSlideViewerComponent = Mockito.mock(SlideViewerComponent.class);
        this.mockSlideViewerFrame = Mockito.mock(SlideViewerFrame.class);

        this.presentation = new Presentation(this.mockSlideViewerComponent);
        this.presentation.setSlideViewerFrame(this.mockSlideViewerFrame);
    }

    @Test
    void initialState_afterCreation_isViewingMode()
    {
        assertTrue(this.presentation.getCurrentState() instanceof ViewingMode);
    }

    @Test
    void transition_fromViewingToEditing_changesStateAndUpdatesUI()
    {
        assertTrue(this.presentation.getCurrentState() instanceof ViewingMode);

        this.presentation.editSlide();

        assertTrue(this.presentation.getCurrentState() instanceof EditingMode);

        verify(this.mockSlideViewerFrame).enterEditMode();
    }

    @Test
    void transition_fromEditingToViewing_changesStateAndUpdatesUI()
    {
        this.presentation.setCurrentState(new EditingMode());

        assertTrue(this.presentation.getCurrentState() instanceof EditingMode);

        this.presentation.editSlide();

        assertTrue(this.presentation.getCurrentState() instanceof ViewingMode);

        verify(this.mockSlideViewerFrame).exitEditMode();
    }

    @Test
    void transition_fromViewingToFullscreen_changesStateAndUpdatesUI()
    {
        assertTrue(this.presentation.getCurrentState() instanceof ViewingMode);

        this.presentation.enterFullscreen();

        assertTrue(this.presentation.getCurrentState() instanceof FullscreenMode);

        verify(this.mockSlideViewerFrame).enterFullScreen();
    }

    @Test
    void transition_fromFullscreenToViewing_changesStateAndUpdatesUI()
    {
        this.presentation.setCurrentState(new FullscreenMode());

        assertTrue(this.presentation.getCurrentState() instanceof FullscreenMode);

        // Transition back to ViewingMode
        this.presentation.enterFullscreen();

        // Verify state has changed to ViewingMode
        assertTrue(this.presentation.getCurrentState() instanceof ViewingMode);

        // Verify UI update method was called
        verify(this.mockSlideViewerFrame).exitFullScreen();
    }

    @Test
    void transition_fromEditingToFullscreen_doesNotChangeState()
    {
        // First transition to EditingMode
        this.presentation.setCurrentState(new EditingMode());

        // Try to transition to FullscreenMode
        this.presentation.enterFullscreen();

        // Verify state has not changed (still in EditingMode)
        assertTrue(this.presentation.getCurrentState() instanceof EditingMode);

        // Verify fullscreen methods were not called
        verify(this.mockSlideViewerFrame, never()).enterFullScreen();
        verify(this.mockSlideViewerFrame, never()).exitFullScreen();
    }

    @Test
    void transition_fromFullscreenToEditing_doesNotChangeState()
    {
        // First transition to FullscreenMode
        this.presentation.setCurrentState(new FullscreenMode());

        // Try to transition to EditingMode
        this.presentation.editSlide();

        // Verify state has not changed (still in FullscreenMode)
        assertTrue(this.presentation.getCurrentState() instanceof FullscreenMode);

        // Verify edit mode methods were not called
        verify(this.mockSlideViewerFrame, never()).enterEditMode();
        verify(this.mockSlideViewerFrame, never()).exitEditMode();
    }

    @Test
    void clear_inViewingMode_resetsToEmptyPresentation()
    {
        // Ensure we're in ViewingMode
        assertTrue(this.presentation.getCurrentState() instanceof ViewingMode);

        // Call clear
        this.presentation.clear();

        // Verify we're still in ViewingMode
        assertTrue(this.presentation.getCurrentState() instanceof ViewingMode);

        // Verify presentation is reset
        assertEquals(0, this.presentation.getSize());
        assertEquals(0, this.presentation.getSlideNumber());
        assertEquals("New jabbepoint presentation", this.presentation.getTitle());
    }

    @Test
    void clear_inEditingMode_stillResetsPresentation()
    {
        // First transition to EditingMode
        EditingMode editingMode = new EditingMode();
        this.presentation.setCurrentState(editingMode);

        // Set some title to verify it changes
        this.presentation.setTitle("Test Presentation");

        // Create a spy to track the real method call behavior
        Presentation spy = Mockito.spy(this.presentation);

        // Call clear
        spy.clear();

        // Verify the title has been reset (clear takes effect)
        assertEquals("New jabbepoint presentation", spy.getTitle());

        // Verify we're back in ViewingMode after clear
        assertTrue(spy.getCurrentState() instanceof ViewingMode);
    }

    @Test
    void clear_inFullscreenMode_stillResetsPresentation()
    {
        // First transition to FullscreenMode
        FullscreenMode fullscreenMode = new FullscreenMode();
        this.presentation.setCurrentState(fullscreenMode);

        // Set some title to verify it changes
        this.presentation.setTitle("Test Presentation");

        // Create a spy to track the real method call behavior
        Presentation spy = Mockito.spy(this.presentation);

        // Call clear
        spy.clear();

        // Verify the title has been reset (clear takes effect)
        assertEquals("New jabbepoint presentation", spy.getTitle());

        // Verify we're back in ViewingMode after clear
        assertTrue(spy.getCurrentState() instanceof ViewingMode);
    }
} 