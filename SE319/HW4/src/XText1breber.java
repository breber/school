/********************************************************************************
 * Homework on Java Swing: A simple text editor
 *
 * Code Map
 *
 * XText1: type JFrame acts as the main window and implements all listeners
 * 	ActionListener for menuitems and popup menuitem
 *  MouseListener for popping up popup menu
 * 	DocumentListener for keeping track of edits in text area
 *
 * XText1
 * contains a textPane (type: JScrollPane)
 *   - textPane contains text (type: JTextArea)
 * contains menubar (type: JMenuBar)
 *   - menubar contains munus and menuitems
 *
 * XText1 class contains all the methods: will ease the extension homework
 * MyFileFilter class contains the file filter description: will ease grading
 *
 * 1. settingTextAreaProperty: listeners, editing features etc. (should be a hint for next homework)
 * 2. createFileMenu
 * 3. createEditSubMenu
 * 4. createEditMenu: uses abstract action
 * 5. createMenuBar
 * 6. actionPerformed: listener -> xnewHandler, openHandler, saveHandler, exitHandler
 * 7. xnewHandler
 * 8. openHandler
 * 9. saveHandler
 * 10.exitHandler
 * 11.replaceHandler
 * 12.replaceAllHandler
 * 13.smallHandler
 * 14.mediumHandler
 * 15.largeHandler
 * 16.mouse listener methods
 * 17.document listener methods
 *
 * MyFileFiler: extends FileFilter class
 **********************************************************************************/

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.BadLocationException;
import javax.swing.tree.DefaultMutableTreeNode;


@SuppressWarnings("serial")
public class XText1breber extends JFrame implements ActionListener,
                                                   DocumentListener,
                                                   MouseListener
{

	/**********************
	 *  Member Attributes
	 *********************/
	// main text area
	private final JTextArea text;
	// keeps track of whether the text has changed in text area
	private boolean documentChanged = false;

	// main text area holder: a scrolling pane
	private final JSplitPane splitPane;
	private final JScrollPane filePane;
	private final JScrollPane textPane;

	// menubar with menus
	private final JMenuBar menubar = new JMenuBar();
	private final JMenu fileMenu = new JMenu("File");
	private final JMenu optionSubMenu = new JMenu("Font Size");
	private final JMenu editMenu = new JMenu("Edit");

	// file menu items
	private final JMenuItem xnew = new JMenuItem("New", 'N');
	private final JMenuItem open = new JMenuItem("Open", 'O');
	private final JMenuItem exit = new JMenuItem("eXit", 'X');
	private final JMenuItem save = new JMenuItem("Save As", 'S');

	// font specifics: default values
	private int size = 15;
	private final String face = "SansSerif";
	private final int fontType = Font.PLAIN;


	/**********************************
	 * AbstractAction Declarations
	 * - function handlers are separately defined
	 **********************************/
	// abstract action: to make sure the popup menu and menuitems work identically
	private final AbstractAction replace = new AbstractAction("Replace")
	{
		@Override
		public void actionPerformed(ActionEvent event)
		{
			replaceHandler();
		}
	};

	private final AbstractAction replaceAll = new AbstractAction("ReplaceAll")
	{
		@Override
		public void actionPerformed(ActionEvent event)
		{
			replaceAllHandler();
		}
	};

	private final AbstractAction find = new AbstractAction("Find")
	{
		@Override
		public void actionPerformed(ActionEvent event)
		{
			findHandler();
		}
	};

	private final AbstractAction small = new AbstractAction("Small")
	{
		@Override
		public void actionPerformed(ActionEvent event)
		{
			smallHandler();
		}
	};

	private final AbstractAction medium = new AbstractAction("Medium")
	{
		@Override
		public void actionPerformed(ActionEvent event)
		{
			mediumHandler();
		}
	};

	private final AbstractAction large = new AbstractAction("Large")
	{
		@Override
		public void actionPerformed(ActionEvent event)
		{
			largeHandler();
		}
	};


	// creating the main
	public static void main(String[] args)
	{
		final XText1breber frame = new XText1breber("XText");
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e)
			{
				frame.exitHandler();
				System.exit(0);
			}
		});
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}


	/**************
	 * Constructor
	 *************/
	XText1breber(String title)
	{
		super(title);
		setSize(500,500);

		/*****************************************************************************************
		 *  1. create the text area
		 *  2. register the mouse listener: to enable popup whenever right-mouse button clicked
		 *  3. register the document listener: any change should be caught
		 *         For items 2 and 3 look inside settingTextAreaProperty()
		 *  4. put text area in a scrollpane
		 *  5. put the scrollpane in the frames content pane
		 ****************************************************************************************/
		text = new JTextArea();
		settingTextAreaProperty(text);
		textPane = new JScrollPane(text);

		// Build first level of tree
		File homeDirectory = new File(System.getProperty("user.home"));
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(homeDirectory.getName());

		for (File f : homeDirectory.listFiles()) {
			root.add(new DefaultMutableTreeNode(f.getName(), f.isDirectory()));
		}

		JTree tree = new JTree(root, true);
		tree.setShowsRootHandles(true);
		tree.setEditable(true);
		filePane = new JScrollPane(tree);

		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, filePane, textPane);

		getContentPane().add(splitPane, BorderLayout.CENTER);

		// set the documentChanged to false to indicate initially nothing has changed in the text area
		documentChanged = false;
		setTitle("XText:New");

		/*****************************************
		 *  create all the menus and the menubar
		 *****************************************/
		createFileMenu();
		createEditSubMenu();
		createEditMenu();
		createMenuBar();
		setJMenuBar(menubar);
	}

	/*********************
	 * End of constructor
	 * Rest of the functions will be written in Depth-First-Order of usage
	 * 1. settingTextAreaProperty
	 * 2. createFileMenu
	 * 3. createEditSubMenu
	 * 4. createSubMenu
	 * 5. createMenuBar
	 ********************/

	/******************************************************
	 * This method is called just once!!!
	 ******************************************************/
	public void settingTextAreaProperty(JTextArea text)
	{
		text.setLineWrap(true);
		text.addMouseListener(this);
		text.setEditable(true);
		text.setFont(new Font(face, fontType, size));
		text.getDocument().addDocumentListener(this);
	}

	/******************************************************************
	 * Creating the file menu: the registration of ActionListener
	 *****************************************************************/
	public void createFileMenu()
	{
		fileMenu.add(xnew);
		fileMenu.add(open);
		fileMenu.add(save);
		fileMenu.add(exit);
		xnew.addActionListener(this);
		open.addActionListener(this);
		save.addActionListener(this);
		exit.addActionListener(this);
	}

	/***************************************************
	 *  creating the edit submenu for the size options
	 **************************************************/
	public void createEditSubMenu()
	{
		optionSubMenu.add(small);
		optionSubMenu.add(medium);
		optionSubMenu.add(large);
	}

	/***************************************************************
	 * creating the edit menu: note the usage of abstract action class
	 * replace, replaceAll and find
	 ***************************************************************/
	public void createEditMenu()
	{
		editMenu.add(replace);
		editMenu.add(replaceAll);
		editMenu.add(find);
		editMenu.add(optionSubMenu);
	}


	/******************************
	 *  finally create the menu bar
	 ******************************/
	public void createMenuBar()
	{
		menubar.add(fileMenu);
		menubar.add(editMenu);
	}

	/*********************************************
	 * Next comes the event handlers
	 * There are event handlers
	 * 1. open, save, new, exit
	 * 2. replace, replaceall, find, font size
	 ********************************************/

	/****************************************************************************
	 * This class implements ActionListener interface: based on where the
	 * event has been generated, different action handlers are invoked
	 * For example: if the new file menu item is clicked, xnewHandler is invoked.
	 ****************************************************************************/
	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object source = e.getSource();
		if (source == xnew) {
			xnewHandler();
		} else if (source == open) {
			openHandler();
		} else if (source == save) {
			saveHandler();
		} else if (source == exit) {
			exitHandler();
		}
	}


	/************************************
	 *  Function to create new text area
	 ***********************************/
	public void xnewHandler()
	{
		int returnVal;

		// Option to save is only provided when the current text document has been updated
		if (documentChanged)
		{
			// open the dialog asking whether the user wants to save the file or not
			returnVal = JOptionPane.showConfirmDialog(this, "The textarea contents are not saved. Do you want to Save?", null, JOptionPane.YES_NO_OPTION);
			// when the answer is yes, invoke savehandler (reuse)
			if (returnVal == JOptionPane.YES_OPTION) {
				saveHandler();
			}
		}
		// empty the text area
		text.setText("");
		setTitle("XText:New");
	}


	/*******************************************************************************************
	 * Function to open new files
	 *  1. usage of file chooser starting from user home
	 *  2. usage of file filter so that any file with "text" or "txt" extensions can be opened.
     *****************************************************************************************/
	public void openHandler()
	{
		int returnVal;
		JFileChooser fchooser = new JFileChooser(System.getProperty("user.home"));

		fchooser.setAcceptAllFileFilterUsed(false);
		fchooser.addChoosableFileFilter(new MyFileFilter());

		returnVal = fchooser.showOpenDialog(XText1breber.this);

		// a file to be opened is selected
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fchooser.getSelectedFile();
			if (file == null || !file.exists()) {
				JOptionPane.showMessageDialog(this, "File " + file.getName() + " not found!");
				return;
			}

			// writing function is invoked via this helper
			openHandlerHelper(file);
		} else {
			System.out.println("User decided not to open a file");
		}
	}

	// Another function: simple but invoked once: future use
	public void openHandlerHelper(File file)
	{
		String title = "XText: " + file.getName();
		setTitle(title);

	    // file reading function is a helper to read file-data one line at a time
	    fileReading(file);

	    // need to set up the documentChanged to false; otherwise it will remain true incorrectly.
	    documentChanged = false;
	}

	// file reading helper
	public void fileReading(File file)
	{
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = reader.readLine()) != null) {
				text.append(line);
				text.append("\n");
			}
			reader.close();
		} catch (IOException excep) {
			excep.printStackTrace();
		}
	}


	/****************************
	 * Saving files
	 *****************************/

	public void saveHandler()
	{
		JFileChooser fchooser = new JFileChooser(System.getProperty("user.home"));

		// to make sure that file filters are used appropriately
		fchooser.setAcceptAllFileFilterUsed(false);
		fchooser.addChoosableFileFilter(new MyFileFilter());
		fchooser.setFileHidingEnabled(false);

		int returnVal = fchooser.showSaveDialog(XText1breber.this);

		// Some file has been selected
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fchooser.getSelectedFile();

			boolean newFile = true;
			if (file.exists()) {
				newFile = false;
				// If file already exists, ask before replacing it.
				returnVal = JOptionPane.showConfirmDialog(null,	"Replace existing file?", "Confirmation Window", returnVal);
				if (returnVal != JOptionPane.YES_OPTION) {
					return;
				}
			}
			try {
				PrintWriter out = new PrintWriter(new FileWriter(file));
				String contents = text.getText();
				out.print(contents);
				if (out.checkError()) {
					out.close();
					throw new IOException("Error while writing to file.");
				}
				out.close();
			} catch (IOException excep) {
				excep.printStackTrace();
			}
		} else {
			// save is cancelled
		}
	}

	/***************************
	 * Exiting and cleaning up
	***************************/

	public void exitHandler()
	{
		int returnVal;

		// Option to save is only provided when the current text document has been updated
		if (documentChanged)
		{
			// open the dialog asking whether the user wants to save the file or not
			returnVal = JOptionPane.showConfirmDialog(this, "Want to save the file in XText editor:" + this.getTitle(), null, JOptionPane.YES_NO_OPTION);
			if (returnVal == JOptionPane.YES_OPTION) {
				saveHandler();
			}
		}
		System.exit(0);
	}


	/**************************
     * Editing operations
     ***********************/
	/*
	 * Replacing:
	 * 	1. starting from the current caret position
	 *     replace the first occurrence of the from-string with to-string
	 *  2. replace the selected text with the to-string
	 */
	public void replaceHandler()
	{
		String to = null;
		String from = null;

		// check whether any text is selected or not
		from = text.getSelectedText();

		if (from == null) {
			// not selected: open dialog
			from = JOptionPane.showInputDialog(this,"Provide the string to replace?");
		} else {
			// otherwise set the caret to the start of the selected text
			text.setCaretPosition(text.getSelectionStart());
		}

		if (from == null) {
			return;
		}

		// this is to make sure the string to replace exists in the text area
		int caretPosition = text.getCaretPosition();
		int fromIndex = find2Handler(from, caretPosition);
		if (fromIndex == -1) {
			return;
		}

		// Reuse the fromIndex: set it from the beginning of the text
		fromIndex = fromIndex + caretPosition;

		// Now its time to get the replace-to string
		to = JOptionPane.showInputDialog(this, "What do you want to replace with?");
		if (to == null) {
			return;
		} else {
			text.replaceRange(to, fromIndex, fromIndex+from.length());
		}
	}

	/*
	 *  Replace all
	 *  1. Starting from the current caret position, replace all occurrences of from-string
	 *     with the to-string
	 *  2. Starting with the selected from-string, replace all occurrences of selected
	 *     from-string (incl. the selected from-string)
	 */
	public void replaceAllHandler()
	{
		String to = null;
		String from = null;

		from = text.getSelectedText();
		if (from == null) {
			from = JOptionPane.showInputDialog(this,"Provide the string to replace");
		} else {
			text.setCaretPosition(text.getSelectionStart());
		}
		if (from == null) {
			return;
		}

		// this is for making sure that the string to replace exists in text area
		int caretPosition = text.getCaretPosition();
		int fromIndex = find2Handler(from, caretPosition);
		if (fromIndex == -1) {
			return;
		}

		to = JOptionPane.showInputDialog(this, "What do you want to replace with?");
		if (to == null) {
			return;
		} else
		{
			int textLength = text.getText().length();
			while (fromIndex != -1)
			{
				fromIndex = fromIndex + caretPosition;
				if (fromIndex >= 0 && from.length() > 0)
				{
					text.replaceRange(to, fromIndex, fromIndex+from.length());
					text.setCaretPosition(fromIndex+to.length());
					textLength = text.getText().length();
					caretPosition = text.getCaretPosition();
					try {
						fromIndex = text.getText(caretPosition, textLength - caretPosition).indexOf(from);
					} catch (BadLocationException excep) {excep.printStackTrace();}
				}
			}
		}
	}

	/*
	 * finding the string - once again use the find2Handler
	 */
	public void findHandler()
	{
		String search = null;

		String in = text.getText();

		search = JOptionPane.showInputDialog(null, "Enter the search string\n", "Find", JOptionPane.PLAIN_MESSAGE);
		if (search == null) {
			return;
		} else {
			int caretPosition = text.getCaretPosition();
			int fromIndex = find2Handler(search, caretPosition);
			if (fromIndex == -1) {
				return;
			}
			text.select(caretPosition + fromIndex, caretPosition + fromIndex + search.length());
		}
	}

	/*
	 * Level two find handling used by replace, replaceAll and find handers
	 */

	public int find2Handler(String search, int caretPosition)
	{
		int textLength = text.getText().length();
		int fromIndex = -1;
		try {
			fromIndex = text.getText(caretPosition, textLength - caretPosition).indexOf(search);
		} catch (BadLocationException excep) {excep.printStackTrace(); }

		if (fromIndex == -1) {
			JOptionPane.showMessageDialog(this, "Word not found");
		}
		return fromIndex;
	}



	/*
	 * suboption menu handler routines
	 */
	public void smallHandler()
	{
		size = 10;
		text.setFont(new Font(face, fontType, size));
	}

	public void mediumHandler()
	{
		size = 15;
		text.setFont(new Font(face, fontType, size));
	}

	public void largeHandler()
	{
		size = 20;
		text.setFont(new Font(face, fontType, size));
	}


	/*
	 * All the methods are implemented: We are left with the listener interfaces
	 */


	/****************************************************************************************
	 * Implementing the MouseListener interface: This is for right mouse button click event,
	 * when the popup is shown
	 ****************************************************************************************/
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e)
	{
		// reuse the abstract action
		if (e.isPopupTrigger())
		{
			JPopupMenu popup = new JPopupMenu();
			popup.add(replace);
			popup.add(replaceAll);
			popup.add(find);
			popup.add(small);
			popup.add(medium);
			popup.add(large);
			popup.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	/*****************************************************************************************
	 * Implementing the DocumentListener: this is required to keep track of text area updates
	 *****************************************************************************************/
	@Override
	public void insertUpdate(DocumentEvent e)
	{
		documentChanged = true;
	}


	@Override
	public void removeUpdate(DocumentEvent e)
	{
		documentChanged = true;
	}

	@Override
	public void changedUpdate(DocumentEvent e)
	{
		// nothing should go in here because this is nothing to do with the text in the text area
	}

}
/*******************************************************************/
/* SIMPLE FILE FILTER */

/*
 * File filter for text documents.
 */
class MyFileFilter extends FileFilter {
	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}
		if (f.getName().endsWith(".text") || f.getName().endsWith(".txt")) {
			return true;
		}
		return false;
	}

	@Override
	public String getDescription() {
		return "Text Files Only";
	}
}