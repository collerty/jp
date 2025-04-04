package org.example.exception;

/**
 * Exception thrown when there is an error during file operations.
 * This includes opening, saving, and other file-related operations.
 */
public class FileOperationException extends JabberPointException {
    private static final long serialVersionUID = 1L;
    
    public FileOperationException(String message) {
        super(message);
    }
    
    public FileOperationException(String message, Throwable cause) {
        super(message, cause);
    }
} 