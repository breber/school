import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileFilter;

/**
 * XText - A Text Editor
 * 
 * Java Version: 6
 * 
 * @author Brian Reber (breber)
 */
public class XTextbreber extends JFrame implements KeyListener {
	private static final long serialVersionUID = 1L;

	/**
	 * The JTextArea we use
	 */
	private JTextArea mTextArea = new JTextArea();

	/**
	 * Indicates whether the document has been modified since the last save
	 */
	private boolean mDocumentModified = false;

	/**
	 * The ActionListener for the replace button
	 */
	private ActionListener mReplaceActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			performReplaceOperation(false);
		}
	};

	/**
	 * The ActionListener for the replace all button
	 */
	private ActionListener mReplaceAllActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			performReplaceOperation(true);
		}
	};

	/**
	 * The ActionListener for the find button
	 */
	private ActionListener mFindActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			String result = JOptionPane.showInputDialog("What would you like to find?");

			if (!"".equals(result)) {
				int cursorPosition = mTextArea.getCaretPosition();
				String textFieldString = mTextArea.getText();
				int index = textFieldString.indexOf(result, cursorPosition);

				if (index >= 0) {
					mTextArea.select(index, index + result.length());
				}
			}
		}
	};

	/**
	 * The ActionListener for the small button
	 */
	private ActionListener mSmallActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			Font current = mTextArea.getFont();
			Font updated = new Font(current.getName(), current.getStyle(), 10);
			mTextArea.setFont(updated);
		}
	};

	/**
	 * The ActionListener for the medium button
	 */
	private ActionListener mMediumActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			Font current = mTextArea.getFont();
			Font updated = new Font(current.getName(), current.getStyle(), 15);
			mTextArea.setFont(updated);
		}
	};

	/**
	 * The ActionListener for the large button
	 */
	private ActionListener mLargeActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			Font current = mTextArea.getFont();
			Font updated = new Font(current.getName(), current.getStyle(), 20);
			mTextArea.setFont(updated);
		}
	};

	/**
	 * Main entry point
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		XTextbreber xtext = new XTextbreber();
		xtext.setVisible(true);
		xtext.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public XTextbreber() {
		setJMenuBar(buildMenuBar());
		setSize(800, 600);

		// Build the text area
		mTextArea.setEditable(true);
		mTextArea.setLineWrap(true);
		mTextArea.addKeyListener(this);

		// Build the context menu and add it to the textarea
		buildContextMenu();

		// Perform actions necessary to create a new document
		newDocument();

		getContentPane().add(new JScrollPane(mTextArea));
	}

	/**
	 * Build the menu bar with the File and Edit menu
	 * 
	 * @return the menubar to add to the JFrame
	 */
	private JMenuBar buildMenuBar() {
		// Setup the Menu Bar
		JMenuBar menuBar = new JMenuBar();

		// Setup the File Menu
		JMenu fileMenu = new JMenu("File");

		// New Menu Option
		JMenuItem newMenuItem = new JMenuItem("New");
		newMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				saveIfFileModified();

				newDocument();
			}
		});
		fileMenu.add(newMenuItem);

		// Open Menu Item
		JMenuItem openMenuItem = new JMenuItem("Open");
		openMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				saveIfFileModified();

				openDocument();
			}
		});
		fileMenu.add(openMenuItem);

		// Save Menu Item
		JMenuItem saveMenuItem = new JMenuItem("Save");
		saveMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				saveDocument();
			}
		});
		fileMenu.add(saveMenuItem);

		// Exit Menu Item
		JMenuItem exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				saveIfFileModified();

				System.exit(0);
			}
		});
		fileMenu.add(exitMenuItem);
		menuBar.add(fileMenu);

		// Setup the Edit Menu
		JMenu editMenu = new JMenu("Edit");

		JMenuItem replaceMenuItem = new JMenuItem("Replace");
		replaceMenuItem.addActionListener(mReplaceActionListener);
		editMenu.add(replaceMenuItem);

		JMenuItem replaceAllMenuItem = new JMenuItem("Replace All");
		replaceAllMenuItem.addActionListener(mReplaceAllActionListener);
		editMenu.add(replaceAllMenuItem);

		JMenuItem findMenuItem = new JMenuItem("Find");
		findMenuItem.addActionListener(mFindActionListener);
		editMenu.add(findMenuItem);

		JMenu fontSizeSubmenu = new JMenu("Font Size");
		JMenuItem smallMenuItem = new JMenuItem("Small");
		smallMenuItem.addActionListener(mSmallActionListener);
		fontSizeSubmenu.add(smallMenuItem);

		JMenuItem mediumMenuItem = new JMenuItem("Medium");
		mediumMenuItem.addActionListener(mMediumActionListener);
		fontSizeSubmenu.add(mediumMenuItem);

		JMenuItem largeMenuItem = new JMenuItem("Large");
		largeMenuItem.addActionListener(mLargeActionListener);
		fontSizeSubmenu.add(largeMenuItem);
		editMenu.add(fontSizeSubmenu);

		menuBar.add(editMenu);

		return menuBar;
	}

	/**
	 * Build the context menu for when a user right clicks
	 */
	private void buildContextMenu() {
		final JPopupMenu menu = new JPopupMenu();

		JMenuItem replaceMenuItem = new JMenuItem("Replace");
		replaceMenuItem.addActionListener(mReplaceActionListener);
		menu.add(replaceMenuItem);

		JMenuItem replaceAllMenuItem = new JMenuItem("Replace All");
		replaceAllMenuItem.addActionListener(mReplaceAllActionListener);
		menu.add(replaceAllMenuItem);

		JMenuItem findMenuItem = new JMenuItem("Find");
		findMenuItem.addActionListener(mFindActionListener);
		menu.add(findMenuItem);

		JMenuItem smallMenuItem = new JMenuItem("Small");
		smallMenuItem.addActionListener(mSmallActionListener);
		menu.add(smallMenuItem);

		JMenuItem mediumMenuItem = new JMenuItem("Medium");
		mediumMenuItem.addActionListener(mMediumActionListener);
		menu.add(mediumMenuItem);

		JMenuItem largeMenuItem = new JMenuItem("Large");
		largeMenuItem.addActionListener(mLargeActionListener);
		menu.add(largeMenuItem);

		mTextArea.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				maybeShowPopup(arg0);
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				maybeShowPopup(arg0);
			}

			/**
			 * Show the popup menu if this is a right click
			 * 
			 * @param e
			 */
			private void maybeShowPopup(MouseEvent e) {
				if (e.isPopupTrigger()) {
					menu.show(e.getComponent(), e.getX(), e.getY());
				}
			}

			@Override
			public void mouseExited(MouseEvent arg0) { }

			@Override
			public void mouseEntered(MouseEvent arg0) { }

			@Override
			public void mouseClicked(MouseEvent arg0) { }
		});
	}

	/**
	 * Create a new Document
	 * 
	 * -Sets the title
	 * -Clears the text area
	 */
	public void newDocument() {
		// Update the window title
		setTitle("XText:New");

		// Clear the text area
		mTextArea.setText("");

		// The document hasn't been modified yet
		mDocumentModified = false;
	}

	/**
	 * Check to see if the current file needs saving, prompt the user
	 * to see if they want to save it, and then save it if they do.
	 */
	private void saveIfFileModified() {
		if (mDocumentModified) {
			// If the document content has changed, prompt the user to see if they
			// want to save their work
			int saveResult = JOptionPane.showConfirmDialog(XTextbreber.this, "Would you like to save your work?", "", JOptionPane.YES_NO_OPTION);

			// If the user responded yes, perform the save operation
			if (JOptionPane.YES_OPTION == saveResult) {
				saveDocument();
			}
		}
	}

	/**
	 * Perform a save operation by prompting the user to choose where to
	 * save the document
	 */
	public void saveDocument() {
		JFileChooser saveFileChooser = new JFileChooser();
		saveFileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
		saveFileChooser.setFileFilter(new FileFilter() {
			@Override
			public String getDescription() {
				return "Text documents";
			}

			@Override
			public boolean accept(File arg0) {
				return arg0.getName().contains(".text") || arg0.getName().contains(".txt");
			}
		});

		int saveResult = saveFileChooser.showSaveDialog(this);

		if (JFileChooser.APPROVE_OPTION == saveResult) {
			File f = saveFileChooser.getSelectedFile();

			setTitle("XText:" + f.getName());

			FileOutputStream fos;
			try {
				fos = new FileOutputStream(f);
				fos.write(mTextArea.getText().getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}

			mDocumentModified = false;
		}
	}

	/**
	 * Prompts the user to open a file, then replaces the text in the JTextArea
	 * with the contents of that file
	 */
	public void openDocument() {
		JFileChooser openFileChooser = new JFileChooser();
		openFileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
		openFileChooser.setFileFilter(new FileFilter() {
			@Override
			public String getDescription() {
				return "Text documents";
			}

			@Override
			public boolean accept(File arg0) {
				return arg0.getName().contains(".text") || arg0.getName().contains(".txt");
			}
		});

		int saveResult = openFileChooser.showOpenDialog(this);

		if (JFileChooser.APPROVE_OPTION == saveResult) {
			File f = openFileChooser.getSelectedFile();

			setTitle("XText:" + f.getName());

			mDocumentModified = false;

			try {
				Scanner scanner = new Scanner(f);
				StringBuilder temp = new StringBuilder();
				while (scanner.hasNextLine()) {
					temp.append(scanner.nextLine());
					temp.append("\n");
				}

				mTextArea.setText(temp.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * The replace operation
	 * 
	 * @param replaceAll whether to replace all instances or just first
	 */
	private void performReplaceOperation(boolean replaceAll) {
		String fromString = null;
		String fullTextAreaText = mTextArea.getText();
		String preTextAreaText = null;
		String postTextAreaText = null;
		String selectedText = mTextArea.getSelectedText();

		// If the user hasn't selected any text, prompt them for the string to replace
		if (selectedText == null) {
			fromString = JOptionPane.showInputDialog("What string would you like to replace?");

			// Get the text from the beginning to the cursor
			preTextAreaText = fullTextAreaText.substring(0, mTextArea.getCaretPosition());

			// Get only the string from the current position
			postTextAreaText = fullTextAreaText.substring(mTextArea.getCaretPosition());
		} else {
			// If the user has selected text, use that as the from string
			fromString = selectedText;

			// Get the text from the beginning to the cursor
			preTextAreaText = fullTextAreaText.substring(0, mTextArea.getSelectionStart());

			// Get only the string from the current position
			postTextAreaText = fullTextAreaText.substring(mTextArea.getSelectionStart());
		}

		// If the text from the current location contains the from string, prompt the user for the replacement string
		if (fromString != null && postTextAreaText.contains(fromString)) {
			String toString = JOptionPane.showInputDialog("What would you like to replace it with?");

			// Perform a replace all or just a replace depending on what option they chose
			if (replaceAll) {
				postTextAreaText = postTextAreaText.replaceAll(fromString, toString);
			} else {
				postTextAreaText = postTextAreaText.replaceFirst(fromString, toString);
			}

			// Update the text with the updated string
			mTextArea.setText(preTextAreaText + postTextAreaText);
		} else {
			JOptionPane.showConfirmDialog(this, "The text \"" + fromString + "\" was not found after the current position", "", JOptionPane.OK_CANCEL_OPTION);
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) { }

	@Override
	public void keyReleased(KeyEvent arg0) { }

	@Override
	public void keyTyped(KeyEvent arg0) {
		mDocumentModified = true;
	}
}
