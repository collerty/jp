package org.example;

import com.formdev.flatlaf.intellijthemes.FlatNordIJTheme;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.filechooser.FileNameExtensionFilter;

/** <p>The controller for the menu</p>
 * @author Ian F. Darwin, ian@darwinsys.com, Gert Florijn, Sylvia Stuurman
 * @version 1.1 2002/12/17 Gert Florijn
 * @version 1.2 2003/11/19 Sylvia Stuurman
 * @version 1.3 2004/08/17 Sylvia Stuurman
 * @version 1.4 2007/07/16 Sylvia Stuurman
 * @version 1.5 2010/03/03 Sylvia Stuurman
 * @version 1.6 2014/05/16 Sylvia Stuurman
 */
public class MenuController extends JMenuBar {

	private JFrame parent; // the frame, only used as parent for the Dialogs
	private Presentation presentation; // Commands are given to the presentation
	private File currentFile;  // tracks current file to chnage on save

	private static final long serialVersionUID = 227L;

	protected static final String ABOUT = "About";
	protected static final String FILE = "File";
	protected static final String EXIT = "Exit";
	protected static final String GOTO = "Go to";
	protected static final String HELP = "Help";
	protected static final String NEW = "New";
	protected static final String NEXT = "Next";
	protected static final String OPEN = "Open";
	protected static final String PAGENR = "Page number?";
	protected static final String PREV = "Prev";
	protected static final String SAVE = "Save";
	protected static final String SAVE_AS = "Save As...";
	protected static final String VIEW = "View";

	protected static final String SAVEFILE = "dump.xml";

	protected static final String IOEX = "IO Exception: ";
	protected static final String LOADERR = "Load Error";
	protected static final String SAVEERR = "Save Error";

	public MenuController(JFrame frame, Presentation pres) {
		parent = frame;
		presentation = pres;
		JMenuItem menuItem;
		JMenu fileMenu = new JMenu(FILE);

		fileMenu.add(menuItem = mkMenuItem(NEW));
		menuItem.addActionListener(e -> {
			presentation.clear();
			parent.repaint();
		});

		fileMenu.add(menuItem = mkMenuItem(OPEN));
		menuItem.addActionListener(e -> {
			try {
				UIManager.setLookAndFeel(new FlatNordIJTheme());
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Open Presentation File");
			fileChooser.setFileFilter(new FileNameExtensionFilter("Presentation Files (*.xml)", "xml"));

			int userSelection = fileChooser.showOpenDialog(parent);

			if (userSelection == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fileChooser.getSelectedFile();
				currentFile = selectedFile;
				presentation.clear();
				Accessor xmlAccessor = new XMLAccessor();

				try {
					xmlAccessor.loadFile(presentation, selectedFile.getAbsolutePath());
					presentation.setSlideNumber(0);
				} catch (IOException exc) {
					JOptionPane.showMessageDialog(parent, IOEX + exc,
							LOADERR, JOptionPane.ERROR_MESSAGE);
				}
				parent.repaint();
			}
		});

		fileMenu.add(menuItem = mkMenuItem(SAVE));
		menuItem.addActionListener(e -> {
			if (currentFile != null) {
				Accessor xmlAccessor = new XMLAccessor();
				try {
					xmlAccessor.saveFile(presentation, currentFile.getAbsolutePath());
				} catch (IOException exc) {
					JOptionPane.showMessageDialog(parent, IOEX + exc,
							SAVEERR, JOptionPane.ERROR_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(parent, "No file opened. Please use 'Save As...'",
						SAVEERR, JOptionPane.WARNING_MESSAGE);
			}
		});

		fileMenu.add(menuItem = mkMenuItem(SAVE_AS, KeyEvent.VK_S, true));
		menuItem.addActionListener(e -> {
			try {
				UIManager.setLookAndFeel(new FlatNordIJTheme());
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Save Presentation As...");
			fileChooser.setFileFilter(new FileNameExtensionFilter("Presentation Files (*.xml)", "xml"));

			int userSelection = fileChooser.showSaveDialog(parent);

			if (userSelection == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fileChooser.getSelectedFile();
				currentFile = selectedFile;
				String filePath = selectedFile.getAbsolutePath();
				if (!filePath.toLowerCase().endsWith(".xml")) {
					filePath += ".xml";
				}

				Accessor xmlAccessor = new XMLAccessor();
				try {
					xmlAccessor.saveFile(presentation, filePath);
				} catch (IOException exc) {
					JOptionPane.showMessageDialog(parent, IOEX + exc,
							SAVEERR, JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		fileMenu.addSeparator();
		fileMenu.add(menuItem = mkMenuItem(EXIT));
		menuItem.addActionListener(e -> presentation.exit(0));
		add(fileMenu);

		JMenu viewMenu = new JMenu(VIEW);
		viewMenu.add(menuItem = mkMenuItem(NEXT));
		menuItem.addActionListener(e -> presentation.nextSlide());
		viewMenu.add(menuItem = mkMenuItem(PREV));
		menuItem.addActionListener(e -> presentation.prevSlide());
		viewMenu.add(menuItem = mkMenuItem(GOTO));
		menuItem.addActionListener(e -> {
			String pageNumberStr = JOptionPane.showInputDialog((Object)PAGENR);
			int pageNumber = Integer.parseInt(pageNumberStr);
			presentation.setSlideNumber(pageNumber - 1);
		});
		add(viewMenu);

		JMenu helpMenu = new JMenu(HELP);
		helpMenu.add(menuItem = mkMenuItem(ABOUT));
		menuItem.addActionListener(e -> AboutBox.show(parent));
		add(helpMenu);
	}

	// Create a menu item with specific shortcut key
	public JMenuItem mkMenuItem(String name, int keyCode, boolean useShiftModifier) {
		JMenuItem item = new JMenuItem(name);
		item.setAccelerator(KeyStroke.getKeyStroke(keyCode, useShiftModifier ? KeyEvent.SHIFT_DOWN_MASK : 0));
		return item;
	}

	// Create a menu item with default shortcut (first letter)
	public JMenuItem mkMenuItem(String name) {
		return new JMenuItem(name);
	}
}
