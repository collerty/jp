package org.example;

import com.formdev.flatlaf.intellijthemes.FlatNordIJTheme;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Dimension;

/**
 * <p>The application window for a slideviewcomponent</p>
 * @author Ian F. Darwin, ian@darwinsys.com, Gert Florijn, Sylvia Stuurman
 * @version 1.1 2002/12/17 Gert Florijn
 * @version 1.2 2003/11/19 Sylvia Stuurman
 * @version 1.3 2004/08/17 Sylvia Stuurman
 * @version 1.4 2007/07/16 Sylvia Stuurman
 * @version 1.5 2010/03/03 Sylvia Stuurman
 * @version 1.6 2014/05/16 Sylvia Stuurman
*/

public class SlideViewerFrame extends JFrame {
	private static final long serialVersionUID = 3227L;
	
	private static final String JABTITLE = "Jabberpoint 1.6 - OU";
	public final static int WIDTH = 1200;
	public final static int HEIGHT = 800;
	private SlideViewerComponent slideViewerComponent; // an instance variable

	public SlideViewerFrame(String title, Presentation presentation) {
		super(title);
		try {
			UIManager.setLookAndFeel(new FlatNordIJTheme());  // Apply the FlatNord theme
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		this.slideViewerComponent = new SlideViewerComponent(presentation, this);
		presentation.setShowView(slideViewerComponent);
		setupWindow(slideViewerComponent, presentation);
	}

// Setup GUI
	public void setupWindow(SlideViewerComponent 
			slideViewerComponent, Presentation presentation) {
		setTitle(JABTITLE);
		addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
			});
		getContentPane().add(slideViewerComponent);
		addKeyListener(new KeyController(presentation)); // add a controller
		setJMenuBar(new MenuController(this, presentation));	// add another controller
		setSize(new Dimension(WIDTH, HEIGHT)); // Same sizes as java.com.Slide has.
		setVisible(true);
		requestFocus(); // Request focus for the window
		requestFocusInWindow(); // Request focus in the window
		toFront(); // Bring window to front
	}
	public SlideViewerComponent getSlideViewerComponent() {
		return slideViewerComponent;
	}
}
