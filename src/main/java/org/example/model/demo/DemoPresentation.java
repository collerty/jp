package org.example.model.demo;

import org.example.filehandler.Accessor;
import org.example.model.BitmapItem;
import org.example.model.Presentation;
import org.example.model.Slide;


public class DemoPresentation extends Accessor
{

	public void loadFile(Presentation presentation, String unusedFilename) {
		presentation.setTitle("Demo java.com.Presentation");
		Slide slide;
		slide = new Slide();
		slide.setTitle("java.com.JabberPoint");
		slide.append(1, "The Java java.com.Presentation Tool");
		slide.append(2, "Copyright (c) 1996-2000: Ian Darwin");
		slide.append(2, "Copyright (c) 2000-now:");
		slide.append(2, "Gert Florijn andn Sylvia Stuurman");
		slide.append(4, "Starting java.com.JabberPoint without a filename");
		slide.append(4, "shows this presentation");
		slide.append(1, "Navigate:");
		slide.append(3, "Next slide: PgDn or Enter");
		slide.append(3, "Previous slide: PgUp or up-arrow");
		slide.append(3, "Quit: q or Q");
		presentation.append(slide);

		slide = new Slide();
		slide.setTitle("Demonstration of levels and stijlen");
		slide.append(1, "Level 1");
		slide.append(2, "Level 2");
		slide.append(1, "Again level 1");
		slide.append(1, "Level 1 has style number 1");
		slide.append(2, "Level 2 has style number  2");
		slide.append(3, "This is how level 3 looks like");
		slide.append(4, "And this is level 4");
		presentation.append(slide);

		slide = new Slide();
		slide.setTitle("The third slide");
		slide.append(1, "To open a new presentation,");
		slide.append(2, "use File->Open from the menu.");
		slide.append(1, " ");
		slide.append(1, "This is the end of the presentation.");
		slide.append(new BitmapItem(1, "JabberPoint.jpg"));
		presentation.append(slide);
	}

	public void saveFile(Presentation presentation, String unusedFilename) {
		throw new IllegalStateException("Save As->Demo! called");
	}
}
