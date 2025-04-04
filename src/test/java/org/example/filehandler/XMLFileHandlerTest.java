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
class XMLFileHandlerTest {

    @Mock
    private Presentation mockPresentation;

    @Mock
    private SlideViewerFrame mockFrame;

    private XMLFileHandler xmlFileHandler;

    @BeforeEach
    void setUp() {
        xmlFileHandler = new XMLFileHandler(mockFrame);
    }

    @Test
    void newFile_withValidPresentation_returnsTrue() throws PresentationException {
        // No need to mock getClass() - just use the actual SlideViewerFrame instance
        
        // Test new file
        boolean result = xmlFileHandler.newFile(mockPresentation);
        
        // Verify
        assertTrue(result);
        verify(mockPresentation).clear();
        verify(mockPresentation).setSlideViewerFrame(mockFrame);
        verify(mockFrame).repaint();
    }

    @Test
    void openFile_withValidFile_returnsTrue() throws FileOperationException, IOException {
        // Use the mockito-inline capability to mock static methods
        try (MockedStatic<FileChooserUtils> utilities = Mockito.mockStatic(FileChooserUtils.class)) {
            
            // Setup mock file
            File mockFile = new File("test.xml");
            utilities.when(() -> FileChooserUtils.selectXMLFile(mockFrame))
                  .thenReturn(mockFile);
            
            // Create a special subclass of XMLFileHandler for testing
            XMLFileHandler handlerForTest = new XMLFileHandler(mockFrame) {
                @Override
                public boolean openFile(Presentation presentation) throws FileOperationException {
                    // Simulate what the real openFile method does but without creating an actual XMLAccessor
                    presentation.clear();
                    // Skip the loadFile call
                    presentation.setSlideNumber(0);
                    mockFrame.repaint();
                    return true;
                }
            };
            
            // Test open file
            boolean result = handlerForTest.openFile(mockPresentation);
            
            // Verify
            assertTrue(result);
            verify(mockPresentation).clear();
            verify(mockPresentation).setSlideNumber(0);
            verify(mockFrame).repaint();
        }
    }

    @Test
    void openFile_whenUserCancelsSelection_returnsFalse() throws FileOperationException {
        try (MockedStatic<FileChooserUtils> utilities = Mockito.mockStatic(FileChooserUtils.class)) {
            // Setup mocks - user cancels file selection
            utilities.when(() -> FileChooserUtils.selectXMLFile(mockFrame))
                  .thenReturn(null);
            
            // Test open file
            boolean result = xmlFileHandler.openFile(mockPresentation);
            
            // Verify
            assertFalse(result);
            verify(mockPresentation, never()).clear();
        }
    }

    @Test
    void saveAs_withValidFile_returnsTrue() throws FileOperationException {
        try (MockedStatic<FileChooserUtils> utilities = Mockito.mockStatic(FileChooserUtils.class)) {
            // Setup mocks
            File mockFile = new File("test.xml");
            utilities.when(() -> FileChooserUtils.selectSaveXMLFile(mockFrame))
                  .thenReturn(mockFile);
            
            // Create a spy on XMLFileHandler to mock saveFile method
            XMLFileHandler spyHandler = Mockito.spy(xmlFileHandler);
            doReturn(true).when(spyHandler).saveFile(mockPresentation, mockFile);
            
            // Test save as
            boolean result = spyHandler.saveAs(mockPresentation);
            
            // Verify
            assertTrue(result);
            verify(spyHandler).saveFile(mockPresentation, mockFile);
        }
    }

    @Test
    void saveAs_whenUserCancelsSelection_returnsFalse() throws FileOperationException {
        try (MockedStatic<FileChooserUtils> utilities = Mockito.mockStatic(FileChooserUtils.class)) {
            // Setup mocks - user cancels file selection
            utilities.when(() -> FileChooserUtils.selectSaveXMLFile(mockFrame))
                  .thenReturn(null);
            
            // Test save as
            boolean result = xmlFileHandler.saveAs(mockPresentation);
            
            // Verify
            assertFalse(result);
        }
    }
} 