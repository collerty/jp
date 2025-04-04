package org.example.filehandler;

import org.example.model.demo.DemoPresentation;
import org.example.model.Presentation;

import java.io.IOException;

public abstract class Accessor
{
    public static final String DEMO_NAME = "Demonstration presentation";
    public static final String DEFAULT_EXTENSION = ".xml";

    public static Accessor getDemoAccessor()
    {
        return new DemoPresentation();
    }

    public Accessor()
    {
    }

    abstract public void loadFile(Presentation p, String fn) throws IOException;

    abstract public void saveFile(Presentation p, String fn) throws IOException;

}
