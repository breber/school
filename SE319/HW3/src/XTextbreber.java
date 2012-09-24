import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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

		// Perform actions necessary to create a new document
		newDocument();

		getContentPane().add(new JScrollPane(mTextArea));
	}

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
		editMenu.add(new JMenuItem("Replace"));
		editMenu.add(new JMenuItem("Replace All"));
		editMenu.add(new JMenuItem("Find"));

		JMenu fontSizeSubmenu = new JMenu("Font Size");
		fontSizeSubmenu.add(new JMenuItem("Small"));
		fontSizeSubmenu.add(new JMenuItem("Medium"));
		fontSizeSubmenu.add(new JMenuItem("Large"));
		editMenu.add(fontSizeSubmenu);

		menuBar.add(editMenu);

		return menuBar;
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

	@Override
	public void keyPressed(KeyEvent arg0) { }

	@Override
	public void keyReleased(KeyEvent arg0) { }

	@Override
	public void keyTyped(KeyEvent arg0) {
		mDocumentModified = true;
	}

}
