package org.example.exception;

/**
 * Exception thrown when there is an error during presentation operations.
 * This includes creating new presentations, modifying slides, and other presentation-related operations.
 */
public class PresentationException extends JabberPointException {
    private static final long serialVersionUID = 1L;
    
    public PresentationException(String message) {
        super(message);
    }
    
    public PresentationException(String message, Throwable cause) {
        super(message, cause);
    }
} 