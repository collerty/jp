package org.example.filehandler;

import org.example.Presentation;

import java.io.File;
import java.io.IOException;

public interface FileHandlerStrategy {
    boolean openFile(Presentation presentation);
    boolean saveFile(Presentation presentation, File file);
    boolean saveAs(Presentation presentation);
}
