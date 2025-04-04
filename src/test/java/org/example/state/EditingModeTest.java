package org.example.state;

import org.example.model.Presentation;
import org.example.model.Slide;
import org.example.view.SlideViewerFrame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import org.mockito.Mockito;

import javax.swing.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EditingModeTest {

    private EditingMode editingMode;
    private Presentation mockPresentation;
    private SlideViewerFrame mockFrame;
    private ArrayList<Slide> mockSlides;

    @BeforeEach
    void setUp() {
        editingMode = new EditingMode();
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
        
        editingMode.nextSlide(mockPresentation);
        
        verify(mockPresentation).setSlideNumber(1);
        
        // Test next slide when at the end
        when(mockPresentation.getSlideNumber()).thenReturn(mockSlides.size() - 1);
        
        editingMode.nextSlide(mockPresentation);
        
        // Should not change slide number when at the end
        verify(mockPresentation, never()).setSlideNumber(mockSlides.size());
    }

    @Test
    void prevSlide_whenNotAtBeginningOfPresentation_decrementsSlideNumber() {
        // Test prev slide when not at the beginning
        when(mockPresentation.getSlideNumber()).thenReturn(1);
        
        editingMode.prevSlide(mockPresentation);
        
        verify(mockPresentation).setSlideNumber(0);
        
        // Test prev slide when at the beginning
        when(mockPresentation.getSlideNumber()).thenReturn(0);
        
        editingMode.prevSlide(mockPresentation);
        
        // Should not change slide number when at the beginning
        verify(mockPresentation, never()).setSlideNumber(-1);
    }

    @Test
    void editSlide_whenCalled_changesStateToViewingMode() {
        editingMode.editSlide(mockPresentation);
        
        // Should change state to ViewingMode
        verify(mockPresentation).setCurrentState(any(ViewingMode.class));
        
        // Should call exitEditMode on the frame
        verify(mockFrame).exitEditMode();
    }

    @Test
    void enterFullscreen_whenCalled_doesNothing() {
        editingMode.enterFullscreen(mockPresentation);
        
        // Should not interact with presentation or frame
        verify(mockPresentation, never()).setCurrentState(any());
        verify(mockFrame, never()).enterFullScreen();
        verify(mockFrame, never()).exitFullScreen();
    }

    @Test
    void clear_whenCalled_doesNothing() {
        editingMode.clear(mockPresentation);
        
        // Should not interact with presentation
        verify(mockPresentation, never()).setShowList(any());
        verify(mockPresentation, never()).setCurrentSlideNumber(anyInt());
        verify(mockPresentation, never()).setTitle(anyString());
    }

    @Test
    void addSlide_whenCalled_addsSlideAndMovesToIt() {
        Slide mockSlide = Mockito.mock(Slide.class);
        
        // Initial size is 3
        int initialSize = mockSlides.size();
        when(mockPresentation.getSize()).thenReturn(initialSize + 1);  // Size after adding the slide
        
        editingMode.addSlide(mockPresentation, mockSlide);
        
        // Verify showList is retrieved to add the slide
        verify(mockPresentation).getShowList();
        
        // Verify slide is added to the list
        assertEquals(initialSize + 1, mockSlides.size());
        assertTrue(mockSlides.contains(mockSlide));
        
        // Verify slide number is updated to the new slide
        verify(mockPresentation).setSlideNumber(initialSize);
        
        // Verify frame focus is maintained
        verify(mockFrame).requestFocus();
        verify(mockFrame).requestFocusInWindow();
        verify(mockFrame).toFront();
    }

    @Test
    @Disabled("Cannot test System.exit without SecurityManager")
    void exit_whenUserConfirms_callsSystemExit() {
        // This test is disabled because we can't easily test System.exit calls
        // without a SecurityManager, which requires more setup
    }

    @Test
    void exit_whenUserCancels_doesNotExit() {
        // Using a simpler approach that doesn't require mocking static methods
        // Just verify the dialog shows up by mocking JOptionPane
        try (var mockedStatic = mockStatic(JOptionPane.class)) {
            // Configure the mock to return NO_OPTION (user cancels)
            mockedStatic.when(() -> JOptionPane.showConfirmDialog(
                    isNull(),
                    anyString(),
                    anyString(),
                    anyInt(),
                    anyInt()
            )).thenReturn(JOptionPane.NO_OPTION);
            
            // Execute the method (should not call System.exit)
            editingMode.exit(mockPresentation, 0);
            
            // Verify the dialog was shown
            mockedStatic.verify(() -> JOptionPane.showConfirmDialog(
                    isNull(),
                    anyString(),
                    anyString(),
                    anyInt(),
                    anyInt()
            ));
        }
    }
} 