package org.example.exception;

public class FileOperationException extends JabberPointException
{
    private static final long serialVersionUID = 1L;

    public FileOperationException(String message)
    {
        super(message);
    }

    public FileOperationException(String message, Throwable cause)
    {
        super(message, cause);
    }
} 