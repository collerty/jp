package org.example.state;

import org.example.model.Presentation;
import org.example.view.SlideViewerComponent;
import org.example.view.SlideViewerFrame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StateTransitionTest {

    private Presentation presentation;
    private SlideViewerComponent mockSlideViewerComponent;
    private SlideViewerFrame mockSlideViewerFrame;

    @BeforeEach
    void setUp() {
        // Create mocks
        mockSlideViewerComponent = Mockito.mock(SlideViewerComponent.class);
        mockSlideViewerFrame = Mockito.mock(SlideViewerFrame.class);
        
        // Create presentation with mock components
        presentation = new Presentation(mockSlideViewerComponent);
        presentation.setSlideViewerFrame(mockSlideViewerFrame);
    }

    @Test
    void initialState_afterCreation_isViewingMode() {
        // Verify initial state is ViewingMode
        assertTrue(presentation.getCurrentState() instanceof ViewingMode);
    }

    @Test
    void transition_fromViewingToEditing_changesStateAndUpdatesUI() {
        // Ensure we start in ViewingMode
        assertTrue(presentation.getCurrentState() instanceof ViewingMode);
        
        // Transition to EditingMode
        presentation.editSlide();
        
        // Verify state has changed to EditingMode
        assertTrue(presentation.getCurrentState() instanceof EditingMode);
        
        // Verify UI update method was called
        verify(mockSlideViewerFrame).enterEditMode();
    }

    @Test
    void transition_fromEditingToViewing_changesStateAndUpdatesUI() {
        // First transition to EditingMode
        presentation.setCurrentState(new EditingMode());
        
        // Verify we're in EditingMode
        assertTrue(presentation.getCurrentState() instanceof EditingMode);
        
        // Transition back to ViewingMode
        presentation.editSlide();
        
        // Verify state has changed to ViewingMode
        assertTrue(presentation.getCurrentState() instanceof ViewingMode);
        
        // Verify UI update method was called
        verify(mockSlideViewerFrame).exitEditMode();
    }

    @Test
    void transition_fromViewingToFullscreen_changesStateAndUpdatesUI() {
        // Ensure we start in ViewingMode
        assertTrue(presentation.getCurrentState() instanceof ViewingMode);
        
        // Transition to FullscreenMode
        presentation.enterFullscreen();
        
        // Verify state has changed to FullscreenMode
        assertTrue(presentation.getCurrentState() instanceof FullscreenMode);
        
        // Verify UI update method was called
        verify(mockSlideViewerFrame).enterFullScreen();
    }

    @Test
    void transition_fromFullscreenToViewing_changesStateAndUpdatesUI() {
        // First transition to FullscreenMode
        presentation.setCurrentState(new FullscreenMode());
        
        // Verify we're in FullscreenMode
        assertTrue(presentation.getCurrentState() instanceof FullscreenMode);
        
        // Transition back to ViewingMode
        presentation.enterFullscreen();
        
        // Verify state has changed to ViewingMode
        assertTrue(presentation.getCurrentState() instanceof ViewingMode);
        
        // Verify UI update method was called
        verify(mockSlideViewerFrame).exitFullScreen();
    }

    @Test
    void transition_fromEditingToFullscreen_doesNotChangeState() {
        // First transition to EditingMode
        presentation.setCurrentState(new EditingMode());
        
        // Try to transition to FullscreenMode
        presentation.enterFullscreen();
        
        // Verify state has not changed (still in EditingMode)
        assertTrue(presentation.getCurrentState() instanceof EditingMode);
        
        // Verify fullscreen methods were not called
        verify(mockSlideViewerFrame, never()).enterFullScreen();
        verify(mockSlideViewerFrame, never()).exitFullScreen();
    }

    @Test
    void transition_fromFullscreenToEditing_doesNotChangeState() {
        // First transition to FullscreenMode
        presentation.setCurrentState(new FullscreenMode());
        
        // Try to transition to EditingMode
        presentation.editSlide();
        
        // Verify state has not changed (still in FullscreenMode)
        assertTrue(presentation.getCurrentState() instanceof FullscreenMode);
        
        // Verify edit mode methods were not called
        verify(mockSlideViewerFrame, never()).enterEditMode();
        verify(mockSlideViewerFrame, never()).exitEditMode();
    }

    @Test
    void clear_inViewingMode_resetsToEmptyPresentation() {
        // Ensure we're in ViewingMode
        assertTrue(presentation.getCurrentState() instanceof ViewingMode);
        
        // Call clear
        presentation.clear();
        
        // Verify we're still in ViewingMode
        assertTrue(presentation.getCurrentState() instanceof ViewingMode);
        
        // Verify presentation is reset
        assertEquals(0, presentation.getSize());
        assertEquals(0, presentation.getSlideNumber());
        assertEquals("New jabbepoint presentation", presentation.getTitle());
    }

    @Test
    void clear_inEditingMode_stillResetsPresentation() {
        // First transition to EditingMode
        EditingMode editingMode = new EditingMode();
        presentation.setCurrentState(editingMode);
        
        // Set some title to verify it changes
        presentation.setTitle("Test Presentation");
        
        // Create a spy to track the real method call behavior
        Presentation spy = Mockito.spy(presentation);
        
        // Call clear
        spy.clear();
        
        // Verify the title has been reset (clear takes effect)
        assertEquals("New jabbepoint presentation", spy.getTitle());
        
        // Verify we're back in ViewingMode after clear
        assertTrue(spy.getCurrentState() instanceof ViewingMode);
    }

    @Test
    void clear_inFullscreenMode_stillResetsPresentation() {
        // First transition to FullscreenMode
        FullscreenMode fullscreenMode = new FullscreenMode();
        presentation.setCurrentState(fullscreenMode);
        
        // Set some title to verify it changes
        presentation.setTitle("Test Presentation");
        
        // Create a spy to track the real method call behavior
        Presentation spy = Mockito.spy(presentation);
        
        // Call clear
        spy.clear();
        
        // Verify the title has been reset (clear takes effect)
        assertEquals("New jabbepoint presentation", spy.getTitle());
        
        // Verify we're back in ViewingMode after clear
        assertTrue(spy.getCurrentState() instanceof ViewingMode);
    }
} 