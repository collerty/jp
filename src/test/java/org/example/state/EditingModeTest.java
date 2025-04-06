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

class EditingModeTest
{

    private EditingMode editingMode;
    private Presentation mockPresentation;
    private SlideViewerFrame mockFrame;
    private ArrayList<Slide> mockSlides;

    @BeforeEach
    void setUp()
    {
        this.editingMode = new EditingMode();
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

        this.editingMode.nextSlide(this.mockPresentation);

        verify(this.mockPresentation).setSlideNumber(1);

        when(this.mockPresentation.getSlideNumber()).thenReturn(this.mockSlides.size() - 1);

        this.editingMode.nextSlide(this.mockPresentation);

        verify(this.mockPresentation, never()).setSlideNumber(this.mockSlides.size());
    }

    @Test
    void prevSlide_whenNotAtBeginningOfPresentation_decrementsSlideNumber()
    {
        when(this.mockPresentation.getSlideNumber()).thenReturn(1);

        this.editingMode.prevSlide(this.mockPresentation);

        verify(this.mockPresentation).setSlideNumber(0);

        when(this.mockPresentation.getSlideNumber()).thenReturn(0);

        this.editingMode.prevSlide(this.mockPresentation);

        verify(this.mockPresentation, never()).setSlideNumber(-1);
    }

    @Test
    void editSlide_whenCalled_changesStateToViewingMode()
    {
        this.editingMode.editSlide(this.mockPresentation);

        verify(this.mockPresentation).setCurrentState(any(ViewingMode.class));

        verify(this.mockFrame).exitEditMode();
    }

    @Test
    void enterFullscreen_whenCalled_doesNothing()
    {
        this.editingMode.enterFullscreen(this.mockPresentation);

        verify(this.mockPresentation, never()).setCurrentState(any());
        verify(this.mockFrame, never()).enterFullScreen();
        verify(this.mockFrame, never()).exitFullScreen();
    }

    @Test
    void clear_whenCalled_doesNothing()
    {
        this.editingMode.clear(this.mockPresentation);

        verify(this.mockPresentation, never()).setShowList(any());
        verify(this.mockPresentation, never()).setCurrentSlideNumber(anyInt());
        verify(this.mockPresentation, never()).setTitle(anyString());
    }

    @Test
    void addSlide_whenCalled_addsSlideAndMovesToIt()
    {
        Slide mockSlide = Mockito.mock(Slide.class);

        int initialSize = this.mockSlides.size();
        when(this.mockPresentation.getSize()).thenReturn(initialSize + 1);

        this.editingMode.addSlide(this.mockPresentation, mockSlide);

        verify(this.mockPresentation).getShowList();

        assertEquals(initialSize + 1, this.mockSlides.size());
        assertTrue(this.mockSlides.contains(mockSlide));

        verify(this.mockPresentation).setSlideNumber(initialSize);

        verify(this.mockFrame).requestFocus();
        verify(this.mockFrame).requestFocusInWindow();
        verify(this.mockFrame).toFront();
    }


    @Test
    void exit_whenUserCancels_doesNotExit()
    {
        try (var mockedStatic = mockStatic(JOptionPane.class))
        {
            mockedStatic.when(() -> JOptionPane.showConfirmDialog(isNull(), anyString(), anyString(), anyInt(), anyInt())).thenReturn(JOptionPane.NO_OPTION);

            this.editingMode.exit(this.mockPresentation, 0);

            mockedStatic.verify(() -> JOptionPane.showConfirmDialog(isNull(), anyString(), anyString(), anyInt(), anyInt()));
        }
    }
} 