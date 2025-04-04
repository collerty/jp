package org.example.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.lang.reflect.Field;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileChooserUtilsTest {
    
    private JFileChooser mockXmlChooser;
    private JFileChooser mockImageChooser;
    private Component testComponent;
    
    @BeforeEach
    void setUp() throws Exception {
        // Create mock file choosers
        mockXmlChooser = mock(JFileChooser.class);
        mockImageChooser = mock(JFileChooser.class);
        
        // Inject the mocks into the FileChooserUtils class
        injectMockFileChoosers(mockXmlChooser, mockImageChooser);
        
        // Set initialized to true
        setInitializedField(true);
        
        // Create a test component for parent
        testComponent = new JPanel();
    }
    
    @AfterEach
    void tearDown() throws Exception {
        // Reset initialized state
        setInitializedField(false);
    }

    @Test
    void initialize_whenCalled_createsFileChoosersAndSetsInitialized() {
        // Reset the initialized state before testing
        setInitializedField(false);

        // We need to use mockStatic for ExecutorService and CompletableFuture to avoid real thread creation
        try (MockedStatic<Executors> mockExecutors = mockStatic(Executors.class);
             MockedStatic<CompletableFuture> mockCompletableFuture = mockStatic(CompletableFuture.class)) {
            
            // Mock the executor
            ExecutorService mockExecutor = mock(ExecutorService.class);
            mockExecutors.when(() -> Executors.newFixedThreadPool(anyInt())).thenReturn(mockExecutor);
            
            // Mock CompletableFuture behavior
            CompletableFuture<Void> mockFuture = mock(CompletableFuture.class);
            mockCompletableFuture.when(() -> CompletableFuture.runAsync(any(Runnable.class), any(ExecutorService.class)))
                                 .thenReturn(mockFuture);
            mockCompletableFuture.when(() -> CompletableFuture.allOf(any())).thenReturn(mockFuture);
            
            // Call initialize method
            FileChooserUtils.initialize();
            
            // Verify the initialized state was set to true
            assertTrue(getInitializedField());
        }
    }

    @Test
    void selectXMLFile_whenUserApproves_returnsSelectedFile() {
        // Set up the mock behavior
        File expectedFile = new File("test.xml");
        when(mockXmlChooser.showOpenDialog(testComponent)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(mockXmlChooser.getSelectedFile()).thenReturn(expectedFile);
        
        // Test the method
        File result = FileChooserUtils.selectXMLFile(testComponent);
        
        // Verify the result
        assertEquals("test.xml", result.getName());
        verify(mockXmlChooser).showOpenDialog(testComponent);
    }

    @Test
    void selectXMLFile_whenUserCancels_returnsNull() {
        // Set up the mock behavior
        when(mockXmlChooser.showOpenDialog(testComponent)).thenReturn(JFileChooser.CANCEL_OPTION);
        
        // Test the method
        File result = FileChooserUtils.selectXMLFile(testComponent);
        
        // Verify the result
        assertNull(result);
        verify(mockXmlChooser).showOpenDialog(testComponent);
    }

    @Test
    void selectImageFile_whenUserApproves_returnsSelectedFile() {
        // Set up the mock behavior
        File expectedFile = new File("test.jpg");
        when(mockImageChooser.showOpenDialog(testComponent)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(mockImageChooser.getSelectedFile()).thenReturn(expectedFile);
        
        // Test the method
        File result = FileChooserUtils.selectImageFile(testComponent);
        
        // Verify the result
        assertEquals("test.jpg", result.getName());
        verify(mockImageChooser).showOpenDialog(testComponent);
    }

    @Test
    void selectSaveXMLFile_withFileWithoutExtension_returnsFileWithXmlExtension() {
        // Set up the mock behavior
        File selectedFile = new File("test");  // No extension
        when(mockXmlChooser.showSaveDialog(testComponent)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(mockXmlChooser.getSelectedFile()).thenReturn(selectedFile);
        
        // Test the method
        File result = FileChooserUtils.selectSaveXMLFile(testComponent);
        
        // Verify the result
        assertNotNull(result);
        assertTrue(result.getPath().endsWith(".xml"));  // Should add extension
        verify(mockXmlChooser).showSaveDialog(testComponent);
    }

    // Helper methods to access and modify private fields for testing
    private void setInitializedField(boolean value) {
        try {
            Field field = FileChooserUtils.class.getDeclaredField("initialized");
            field.setAccessible(true);
            field.set(null, value);
        } catch (Exception e) {
            fail("Failed to set initialized field: " + e.getMessage());
        }
    }

    private boolean getInitializedField() {
        try {
            Field field = FileChooserUtils.class.getDeclaredField("initialized");
            field.setAccessible(true);
            return (boolean) field.get(null);
        } catch (Exception e) {
            fail("Failed to get initialized field: " + e.getMessage());
            return false;
        }
    }
    
    private void injectMockFileChoosers(JFileChooser xmlChooser, JFileChooser imageChooser) {
        try {
            Field xmlField = FileChooserUtils.class.getDeclaredField("xmlFileChooser");
            xmlField.setAccessible(true);
            xmlField.set(null, xmlChooser);
            
            Field imageField = FileChooserUtils.class.getDeclaredField("imageFileChooser");
            imageField.setAccessible(true);
            imageField.set(null, imageChooser);
        } catch (Exception e) {
            fail("Failed to inject mock file choosers: " + e.getMessage());
        }
    }
} 