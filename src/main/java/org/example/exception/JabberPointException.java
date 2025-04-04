package org.example.exception;

/**
 * Base exception class for JabberPoint application.
 * All custom exceptions should extend this class.
 */
public class JabberPointException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    public JabberPointException(String message) {
        super(message);
    }
    
    public JabberPointException(String message, Throwable cause) {
        super(message, cause);
    }
} 