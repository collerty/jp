package org.example.filehandler;

import org.example.exception.FileOperationException;
import org.example.exception.PresentationException;
import org.example.model.Presentation;

import java.io.File;

public interface FileHandlerStrategy
{
    boolean openFile(Presentation presentation) throws FileOperationException;

    boolean saveFile(Presentation presentation, File file) throws FileOperationException;

    boolean saveAs(Presentation presentation) throws FileOperationException;

    boolean newFile(Presentation presentation) throws PresentationException;
}
