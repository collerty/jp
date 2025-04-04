package org.example.exception;

public class JabberPointException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public JabberPointException(String message)
    {
        super(message);
    }

    public JabberPointException(String message, Throwable cause)
    {
        super(message, cause);
    }
} 