package org.example.filehandler;

import org.example.exception.FileOperationException;
import org.example.exception.PresentationException;
import org.example.model.Presentation;
import org.example.util.FileChooserUtils;
import org.example.view.SlideViewerFrame;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class XMLFileHandler implements FileHandlerStrategy
{
    private final Component parent;

    public XMLFileHandler(Component parent)
    {
        this.parent = parent;
    }

    @Override
    public boolean openFile(Presentation presentation) throws FileOperationException
    {
        File selectedFile = FileChooserUtils.selectXMLFile(this.parent);
        if (selectedFile != null)
        {
            Accessor xmlAccessor = new XMLAccessor();
            try
            {
                // Clear the presentation before loading new content
                presentation.clear();
                xmlAccessor.loadFile(presentation, selectedFile.getAbsolutePath());
                presentation.setSlideNumber(0);
                this.parent.repaint();

                return true;
            } catch (IOException exc)
            {
                throw new FileOperationException("Failed to open file: " + selectedFile.getName(), exc);
            }
        }
        return false;
    }

    @Override
    public boolean saveFile(Presentation presentation, File file) throws FileOperationException
    {
        Accessor xmlAccessor = new XMLAccessor();
        try
        {
            xmlAccessor.saveFile(presentation, file.getAbsolutePath());
            return true;
        } catch (IOException exc)
        {
            throw new FileOperationException("Failed to save file: " + file.getName(), exc);
        }
    }

    @Override
    public boolean saveAs(Presentation presentation) throws FileOperationException
    {
        File selectedFile = FileChooserUtils.selectSaveXMLFile(this.parent);
        if (selectedFile != null)
        {
            return this.saveFile(presentation, selectedFile);
        }
        return false;
    }

    @Override
    public boolean newFile(Presentation presentation) throws PresentationException
    {
        try {
            presentation.clear();
            if (this.parent instanceof SlideViewerFrame)
            {
                presentation.setSlideViewerFrame((SlideViewerFrame) this.parent);
            }
            this.parent.repaint();
            return true;
        } catch (Exception e) {
            throw new PresentationException("Failed to create new presentation", e);
        }
    }
}
