package org.example.filehandler;

import org.example.exception.FileOperationException;
import org.example.exception.PresentationException;
import org.example.model.Presentation;

import java.io.File;

/**
 * Strategy interface for handling file operations in the presentation application.
 */
public interface FileHandlerStrategy
{
    /**
     * Opens a presentation file.
     * @param presentation The presentation to load the file into
     * @return true if the file was successfully opened, false otherwise
     * @throws FileOperationException if there is an error during file operations
     */
    boolean openFile(Presentation presentation) throws FileOperationException;

    /**
     * Saves the presentation to a file.
     * @param presentation The presentation to save
     * @param file The file to save to
     * @return true if the file was successfully saved, false otherwise
     * @throws FileOperationException if there is an error during file operations
     */
    boolean saveFile(Presentation presentation, File file) throws FileOperationException;

    /**
     * Saves the presentation with a new filename.
     * @param presentation The presentation to save
     * @return true if the file was successfully saved, false otherwise
     * @throws FileOperationException if there is an error during file operations
     */
    boolean saveAs(Presentation presentation) throws FileOperationException;

    /**
     * Creates a new presentation.
     * @param presentation The presentation to initialize
     * @return true if the new presentation was successfully created, false otherwise
     * @throws PresentationException if there is an error during presentation creation
     */
    boolean newFile(Presentation presentation) throws PresentationException;
}
