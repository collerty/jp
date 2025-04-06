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
class FileChooserUtilsTest
{

    private JFileChooser mockXmlChooser;
    private JFileChooser mockImageChooser;
    private Component testComponent;

    @BeforeEach
    void setUp() throws Exception
    {
        this.mockXmlChooser = mock(JFileChooser.class);
        this.mockImageChooser = mock(JFileChooser.class);

        this.injectMockFileChoosers(this.mockXmlChooser, this.mockImageChooser);

        this.setInitializedField(true);

        this.testComponent = new JPanel();
    }

    @AfterEach
    void tearDown() throws Exception
    {
        this.setInitializedField(false);
    }

    @Test
    void initialize_whenCalled_createsFileChoosersAndSetsInitialized()
    {
        this.setInitializedField(false);

        try (MockedStatic<Executors> mockExecutors = mockStatic(Executors.class);
             MockedStatic<CompletableFuture> mockCompletableFuture = mockStatic(CompletableFuture.class))
        {

            ExecutorService mockExecutor = mock(ExecutorService.class);

            mockExecutors.when(() -> Executors.newFixedThreadPool(anyInt())).thenReturn(mockExecutor);
            CompletableFuture<Void> mockFuture = mock(CompletableFuture.class);
            mockCompletableFuture.when(() -> CompletableFuture.runAsync(any(Runnable.class), any(ExecutorService.class)))
                    .thenReturn(mockFuture);
            mockCompletableFuture.when(() -> CompletableFuture.allOf(any())).thenReturn(mockFuture);

            FileChooserUtils.initialize();

            assertTrue(this.getInitializedField());
        }
    }

    @Test
    void selectXMLFile_whenUserApproves_returnsSelectedFile()
    {
        File expectedFile = new File("test.xml");
        when(this.mockXmlChooser.showOpenDialog(this.testComponent)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(this.mockXmlChooser.getSelectedFile()).thenReturn(expectedFile);

        File result = FileChooserUtils.selectXMLFile(this.testComponent);

        assertEquals("test.xml", result.getName());
        verify(this.mockXmlChooser).showOpenDialog(this.testComponent);
    }

    @Test
    void selectXMLFile_whenUserCancels_returnsNull()
    {
        when(this.mockXmlChooser.showOpenDialog(this.testComponent)).thenReturn(JFileChooser.CANCEL_OPTION);

        File result = FileChooserUtils.selectXMLFile(this.testComponent);

        assertNull(result);
        verify(this.mockXmlChooser).showOpenDialog(this.testComponent);
    }

    @Test
    void selectImageFile_whenUserApproves_returnsSelectedFile()
    {
        File expectedFile = new File("test.jpg");
        when(this.mockImageChooser.showOpenDialog(this.testComponent)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(this.mockImageChooser.getSelectedFile()).thenReturn(expectedFile);

        File result = FileChooserUtils.selectImageFile(this.testComponent);

        assertEquals("test.jpg", result.getName());
        verify(this.mockImageChooser).showOpenDialog(this.testComponent);
    }

    @Test
    void selectSaveXMLFile_withFileWithoutExtension_returnsFileWithXmlExtension()
    {
        File selectedFile = new File("test");
        when(this.mockXmlChooser.showSaveDialog(this.testComponent)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(this.mockXmlChooser.getSelectedFile()).thenReturn(selectedFile);

        File result = FileChooserUtils.selectSaveXMLFile(this.testComponent);

        assertNotNull(result);
        assertTrue(result.getPath().endsWith(".xml"));
        verify(this.mockXmlChooser).showSaveDialog(this.testComponent);
    }

    private void setInitializedField(boolean value)
    {
        try
        {
            Field field = FileChooserUtils.class.getDeclaredField("initialized");
            field.setAccessible(true);
            field.set(null, value);
        }
        catch (Exception e)
        {
            fail("Failed to set initialized field: " + e.getMessage());
        }
    }

    private boolean getInitializedField()
    {
        try
        {
            Field field = FileChooserUtils.class.getDeclaredField("initialized");
            field.setAccessible(true);
            return (boolean) field.get(null);
        }
        catch (Exception e)
        {
            fail("Failed to get initialized field: " + e.getMessage());
            return false;
        }
    }

    private void injectMockFileChoosers(JFileChooser xmlChooser, JFileChooser imageChooser)
    {
        try
        {
            Field xmlField = FileChooserUtils.class.getDeclaredField("xmlFileChooser");
            xmlField.setAccessible(true);
            xmlField.set(null, xmlChooser);

            Field imageField = FileChooserUtils.class.getDeclaredField("imageFileChooser");
            imageField.setAccessible(true);
            imageField.set(null, imageChooser);
        }
        catch (Exception e)
        {
            fail("Failed to inject mock file choosers: " + e.getMessage());
        }
    }
} 