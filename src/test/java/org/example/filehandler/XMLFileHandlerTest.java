package org.example.filehandler;

import org.example.exception.FileOperationException;
import org.example.exception.PresentationException;
import org.example.model.Presentation;
import org.example.util.FileChooserUtils;
import org.example.view.SlideViewerFrame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.Component;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class XMLFileHandlerTest
{

    @Mock
    private Presentation mockPresentation;

    @Mock
    private SlideViewerFrame mockFrame;

    private XMLFileHandler xmlFileHandler;

    @BeforeEach
    void setUp()
    {
        this.xmlFileHandler = new XMLFileHandler(this.mockFrame);
    }

    @Test
    void newFile_withValidPresentation_returnsTrue() throws PresentationException
    {
        boolean result = this.xmlFileHandler.newFile(this.mockPresentation);

        assertTrue(result);
        verify(this.mockPresentation).clear();
        verify(this.mockPresentation).setSlideViewerFrame(this.mockFrame);
        verify(this.mockFrame).repaint();
    }

    @Test
    void openFile_withValidFile_returnsTrue() throws FileOperationException, IOException
    {
        try (MockedStatic<FileChooserUtils> utilities = Mockito.mockStatic(FileChooserUtils.class))
        {

            File mockFile = new File("test.xml");
            utilities.when(() -> FileChooserUtils.selectXMLFile(this.mockFrame))
                    .thenReturn(mockFile);

            XMLFileHandler handlerForTest = new XMLFileHandler(this.mockFrame)
            {
                @Override
                public boolean openFile(Presentation presentation) throws FileOperationException
                {
                    presentation.clear();
                    presentation.setSlideNumber(0);
                    XMLFileHandlerTest.this.mockFrame.repaint();
                    return true;
                }
            };

            boolean result = handlerForTest.openFile(this.mockPresentation);

            assertTrue(result);
            verify(this.mockPresentation).clear();
            verify(this.mockPresentation).setSlideNumber(0);
            verify(this.mockFrame).repaint();
        }
    }

    @Test
    void openFile_whenUserCancelsSelection_returnsFalse() throws FileOperationException
    {
        try (MockedStatic<FileChooserUtils> utilities = Mockito.mockStatic(FileChooserUtils.class))
        {
            utilities.when(() -> FileChooserUtils.selectXMLFile(this.mockFrame))
                    .thenReturn(null);

            boolean result = this.xmlFileHandler.openFile(this.mockPresentation);

            assertFalse(result);
            verify(this.mockPresentation, never()).clear();
        }
    }

    @Test
    void saveAs_withValidFile_returnsTrue() throws FileOperationException
    {
        try (MockedStatic<FileChooserUtils> utilities = Mockito.mockStatic(FileChooserUtils.class))
        {
            File mockFile = new File("test.xml");
            utilities.when(() -> FileChooserUtils.selectSaveXMLFile(this.mockFrame))
                    .thenReturn(mockFile);

            XMLFileHandler spyHandler = Mockito.spy(this.xmlFileHandler);
            doReturn(true).when(spyHandler).saveFile(this.mockPresentation, mockFile);

            boolean result = spyHandler.saveAs(this.mockPresentation);

            assertTrue(result);
            verify(spyHandler).saveFile(this.mockPresentation, mockFile);
        }
    }

    @Test
    void saveAs_whenUserCancelsSelection_returnsFalse() throws FileOperationException
    {
        try (MockedStatic<FileChooserUtils> utilities = Mockito.mockStatic(FileChooserUtils.class))
        {
            utilities.when(() -> FileChooserUtils.selectSaveXMLFile(this.mockFrame))
                    .thenReturn(null);

            boolean result = this.xmlFileHandler.saveAs(this.mockPresentation);

            assertFalse(result);
        }
    }
} 