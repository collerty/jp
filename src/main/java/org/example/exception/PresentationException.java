package org.example.exception;

public class PresentationException extends JabberPointException
{
    private static final long serialVersionUID = 1L;

    public PresentationException(String message)
    {
        super(message);
    }

    public PresentationException(String message, Throwable cause)
    {
        super(message, cause);
    }
} 