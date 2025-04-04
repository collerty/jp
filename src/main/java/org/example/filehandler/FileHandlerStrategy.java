package org.example.filehandler;

import org.example.model.Presentation;

import java.io.File;

public interface FileHandlerStrategy
{
    boolean openFile(Presentation presentation);

    boolean saveFile(Presentation presentation, File file);

    boolean saveAs(Presentation presentation);
}
