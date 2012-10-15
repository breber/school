/********************************************************************************
 * Homework on Java Swing: A simple text editor
 *
 * Brian Reber (breber) - Compiles with Java 6
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
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
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
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.BadLocationException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;


@SuppressWarnings("serial")
public class XText1breber extends JFrame implements ActionListener,
                                                   TreeWillExpandListener,
                                                   TreeSelectionListener,
                                                   TreeExpansionListener
{

	/**********************
	 *  Member Attributes
	 *********************/

	// main text area holder: a scrolling pane
	private final JSplitPane splitPane;
	private final JTabbedPane tabbedPane;
	private final JScrollPane filePane;
	private final JTree tree;

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


	/**********************************
	 * AbstractAction Declarations
	 * - function handlers are separately defined
	 **********************************/
	// abstract action: to make sure the popup menu and menuitems work identically
	final AbstractAction replace = new AbstractAction("Replace")
	{
		@Override
		public void actionPerformed(ActionEvent event)
		{
			replaceHandler();
		}
	};

	final AbstractAction replaceAll = new AbstractAction("ReplaceAll")
	{
		@Override
		public void actionPerformed(ActionEvent event)
		{
			replaceAllHandler();
		}
	};

	final AbstractAction find = new AbstractAction("Find")
	{
		@Override
		public void actionPerformed(ActionEvent event)
		{
			findHandler();
		}
	};

	final AbstractAction small = new AbstractAction("Small")
	{
		@Override
		public void actionPerformed(ActionEvent event)
		{
			TextPanebreber current = (TextPanebreber) tabbedPane.getSelectedComponent();
			if (current != null) {
				current.smallHandler();
			}
		}
	};

	final AbstractAction medium = new AbstractAction("Medium")
	{
		@Override
		public void actionPerformed(ActionEvent event)
		{
			TextPanebreber current = (TextPanebreber) tabbedPane.getSelectedComponent();
			if (current != null) {
				current.mediumHandler();
			}
		}
	};

	final AbstractAction large = new AbstractAction("Large")
	{
		@Override
		public void actionPerformed(ActionEvent event)
		{
			TextPanebreber current = (TextPanebreber) tabbedPane.getSelectedComponent();
			if (current != null) {
				current.largeHandler();
			}
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
		tabbedPane = new TabPaneWithIconsbreber(this);

		// Build first level of tree
		File homeDirectory = new File(System.getProperty("user.home"));
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(homeDirectory.getName());

		for (File f : homeDirectory.listFiles()) {
			root.add(new DefaultMutableTreeNode(f.getName(), f.isDirectory()));
		}

		tree = new JTree(root, true);
		tree.setShowsRootHandles(true);
		tree.addTreeWillExpandListener(this);
		tree.addTreeSelectionListener(this);
		tree.addTreeExpansionListener(this);
		filePane = new JScrollPane(tree);

		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, filePane, tabbedPane);

		getContentPane().add(splitPane, BorderLayout.CENTER);

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
			TextPanebreber currentPane = (TextPanebreber) tabbedPane.getSelectedComponent();
			saveHandler(currentPane);
		} else if (source == exit) {
			exitHandler();
		}
	}


	/************************************
	 *  Function to create new text area
	 ***********************************/
	public void xnewHandler()
	{
		TextPanebreber newTextPane = new TextPanebreber(this);
		tabbedPane.addTab("XText:New", new CloseTabIconbreber(null), newTextPane);
		tabbedPane.setSelectedComponent(newTextPane);
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
		fchooser.addChoosableFileFilter(new MyFileFilterbreber());

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
		boolean needToAdd = true;
		for (int i = 0; i < tabbedPane.getTabCount(); i++) {
			TextPanebreber textPane = (TextPanebreber) tabbedPane.getComponentAt(i);
			String filePath = textPane.getFilePath();
			if (filePath != null && filePath.equals(file.getAbsolutePath())) {
				tabbedPane.setSelectedIndex(i);
				needToAdd = false;
				break;
			}
		}

		if (needToAdd) {
			TextPanebreber newTextPane = new TextPanebreber(this);
			tabbedPane.addTab("XText: " + file.getName(), new CloseTabIconbreber(null), newTextPane);
			tabbedPane.setSelectedComponent(newTextPane);

		    // file reading function is a helper to read file-data one line at a time
		    fileReading(file, newTextPane);

		    // Set the file path so we can compare later
		    newTextPane.setFilePath(file.getAbsolutePath());

		    newTextPane.setDocumentChanged(false);
		}
	}

	// file reading helper
	public void fileReading(File file, TextPanebreber textPane)
	{
		try {
			JTextArea currentTextArea = textPane.getTextArea();
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = reader.readLine()) != null) {
				currentTextArea.append(line);
				currentTextArea.append("\n");
			}
			reader.close();
		} catch (IOException excep) {
			excep.printStackTrace();
		}
	}


	/****************************
	 * Saving files
	 *****************************/
	public void saveHandler(TextPanebreber textPane)
	{
		JFileChooser fchooser = new JFileChooser(System.getProperty("user.home"));

		// to make sure that file filters are used appropriately
		fchooser.setAcceptAllFileFilterUsed(false);
		fchooser.addChoosableFileFilter(new MyFileFilterbreber());
		fchooser.setFileHidingEnabled(false);

		int returnVal = fchooser.showSaveDialog(XText1breber.this);

		// Some file has been selected
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fchooser.getSelectedFile();

			if (file.exists()) {
				// If file already exists, ask before replacing it.
				returnVal = JOptionPane.showConfirmDialog(null,	"Replace existing file?", "Confirmation Window", returnVal);
				if (returnVal != JOptionPane.YES_OPTION) {
					return;
				}
			}
			try {
				if (textPane != null) {
					PrintWriter out = new PrintWriter(new FileWriter(file));
					String contents = textPane.getTextArea().getText();
					out.print(contents);
					if (out.checkError()) {
						out.close();
						throw new IOException("Error while writing to file.");
					}
					out.close();
				}
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
		while (tabbedPane.getTabCount() > 0) {
			TextPanebreber textPane = (TextPanebreber) tabbedPane.getSelectedComponent();
			if (textPane != null && textPane.isDocumentChanged())
			{
				// open the dialog asking whether the user wants to save the file or not
				returnVal = JOptionPane.showConfirmDialog(this,
						"Want to save the file in XText editor:" + tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()),
						null, JOptionPane.YES_NO_OPTION);
				if (returnVal == JOptionPane.YES_OPTION) {
					saveHandler(textPane);
				}
			}

			tabbedPane.remove(textPane);
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
		TextPanebreber currentPane = (TextPanebreber) tabbedPane.getSelectedComponent();
		if (currentPane == null) {
			return;
		}
		JTextArea text = currentPane.getTextArea();

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
		TextPanebreber currentPane = (TextPanebreber) tabbedPane.getSelectedComponent();
		if (currentPane == null) {
			return;
		}
		JTextArea text = currentPane.getTextArea();

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
					text.replaceRange(to, fromIndex, fromIndex + from.length());
					text.setCaretPosition(fromIndex + to.length());
					textLength = text.getText().length();
					caretPosition = text.getCaretPosition();
					try {
						fromIndex = text.getText(caretPosition,	textLength - caretPosition).indexOf(from);
					} catch (BadLocationException excep) {
						excep.printStackTrace();
					}
				}
			}
		}
	}

	/*
	 * finding the string - once again use the find2Handler
	 */
	public void findHandler()
	{
		TextPanebreber currentPane = (TextPanebreber) tabbedPane.getSelectedComponent();
		if (currentPane == null) {
			return;
		}
		JTextArea text = currentPane.getTextArea();

		String search = null;

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
		TextPanebreber currentPane = (TextPanebreber) tabbedPane.getSelectedComponent();
		if (currentPane == null) {
			return -1;
		}
		JTextArea text = currentPane.getTextArea();

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

	@Override
	public void treeExpanded(TreeExpansionEvent event) {}

	/* (non-Javadoc)
	 * @see javax.swing.event.TreeExpansionListener#treeCollapsed(javax.swing.event.TreeExpansionEvent)
	 *
	 * When the tree collapses, remove the children of the collapsed
	 * node, and then notify the tree model that the structure
	 * has changed for the given node (so it knows to update the
	 * display of the tree).
	 */
	@Override
	public void treeCollapsed(TreeExpansionEvent event) {
		TreePath path = event.getPath();
		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
		selectedNode.removeAllChildren();
		((DefaultTreeModel) tree.getModel()).nodeStructureChanged(selectedNode);
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.TreeWillExpandListener#treeWillCollapse(javax.swing.event.TreeExpansionEvent)
	 */
	@Override
	public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {	}


	/* (non-Javadoc)
	 * @see javax.swing.event.TreeWillExpandListener#treeWillExpand(javax.swing.event.TreeExpansionEvent)
	 *
	 * When the tree is about to expand, get the list of files
	 * that should be displayed in the node that is expanding.
	 * Build the tree based on this information.
	 */
	@Override
	public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
		TreePath path = event.getPath();

		StringBuilder sb = new StringBuilder(System.getProperty("user.home") + "/");
		for (int i = 1; i < path.getPathCount(); i++) {
			sb.append(path.getPathComponent(i));
			sb.append("/");
		}

		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)path.getLastPathComponent();
		File filePath = new File(sb.toString());

		for (File f : filePath.listFiles()) {
			selectedNode.add(new DefaultMutableTreeNode(f.getName(), f.isDirectory()));
		}
	}


	/* (non-Javadoc)
	 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
	 *
	 * When a tree element is selected, build the path to the file,
	 * and check to see if it is able to be opened (based on rules from
	 * the assignment document). If it is, perform an open operation. If
	 * it isn't, show a message dialog indicating the file isn't able to
	 * be opened.
	 */
	@Override
	public void valueChanged(TreeSelectionEvent e) {
		TreePath path = e.getPath();

		StringBuilder sb = new StringBuilder(System.getProperty("user.home") + File.separator);
		for (int i = 1; i < path.getPathCount(); i++) {
			sb.append(path.getPathComponent(i));
			sb.append(File.separator);
		}

		File filePath = new File(sb.toString());
		if (!filePath.isDirectory()) {
			String fileName = filePath.getName();
			if (fileName.endsWith(".text") || fileName.endsWith(".txt")) {
				openHandlerHelper(filePath);
			} else {
				JOptionPane.showMessageDialog(this, "Cannot open selected file: " + fileName);
			}
		}
	}
}

/**
 * A wrapper for a pane in the JTabbedPane. Contains a scrollview
 * and the text editor. Contains the logic needed to check if the
 * document has changed or now.
 *
 * @author breber
 */
class TextPanebreber extends JScrollPane implements DocumentListener, MouseListener
{
	/**
	 * The XText1 instance that is holding this TextPane
	 */
	private final XText1breber parent;

	/**
	 * The path of the file currently being edited
	 */
	private String filePath;

	/**
	 * main text area
	 */
	private final JTextArea text;

	/**
	 * keeps track of whether the text has changed in text area
	 */
	private boolean documentChanged = false;

	/**
	 * Font size
	 */
	private int size = 15;

	/**
	 * The Font Face
	 */
	private final String face = "SansSerif";

	/**
	 * The Font Type
	 */
	private final int fontType = Font.PLAIN;

	/**
	 * Create a TextPanebrber with the given parent element
	 *
	 * @param parent the parent of this text pane
	 */
	public TextPanebreber(XText1breber parent) {
		super();
		this.parent = parent;
		text = new JTextArea();
		settingTextAreaProperty(text);

		documentChanged = false;

		setViewportView(text);
	}

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

	/**
	 * Get the JTextArea for this tab
	 *
	 * @return the JTextArea for this tab
	 */
	public JTextArea getTextArea() {
		return text;
	}

	/**
	 * Get the path of the file in the editor
	 *
	 * @return the absolute path of the file being edited
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * Set the path of the file currently being edited
	 *
	 * @param filePath
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * Manually set whether the document has been changed or not
	 *
	 * @param documentChanged
	 */
	public void setDocumentChanged(boolean documentChanged) {
		this.documentChanged = documentChanged;
	}

	/**
	 * @return whether the document has changed
	 */
	public boolean isDocumentChanged() {
		return documentChanged;
	}

	/*
	 * Handler for changing font size to small
	 */
	public void smallHandler()
	{
		size = 10;
		text.setFont(new Font(face, fontType, size));
	}

	/*
	 * Handler for changing font size to medium
	 */
	public void mediumHandler()
	{
		size = 15;
		text.setFont(new Font(face, fontType, size));
	}

	/*
	 * Handler for changing font size to large
	 */
	public void largeHandler()
	{
		size = 20;
		text.setFont(new Font(face, fontType, size));
	}


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
			popup.add(parent.replace);
			popup.add(parent.replaceAll);
			popup.add(parent.find);
			popup.add(parent.small);
			popup.add(parent.medium);
			popup.add(parent.large);
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

/**
 * A JTabbedPane that will close a tab if the Icon in that tab is clicked.
 *
 * @author Brian Reber (breber)
 */
class TabPaneWithIconsbreber extends JTabbedPane implements MouseListener {

	/**
	 * The parent of this tab - used to perform save operations
	 */
	private final XText1breber parent;

	/**
	 * Create a new TabPane with the given parent
	 *
	 * @param parent the parent element of this JTabbedPane
	 */
	public TabPaneWithIconsbreber(XText1breber parent) {
		super();
		this.parent = parent;
		addMouseListener(this);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		Point mousePoint = e.getPoint();
		for (int i = 0; i < getTabCount(); i++) {
			CloseTabIconbreber icon = (CloseTabIconbreber) getIconAt(i);
			Rectangle bounds = icon.getBounds();
			if (bounds.contains(mousePoint)) {
				TextPanebreber textPane = (TextPanebreber) getComponentAt(i);
				if (textPane != null && textPane.isDocumentChanged())
				{
					// open the dialog asking whether the user wants to save the file or not
					int returnVal = JOptionPane.showConfirmDialog(this,
							"Want to save the file in XText editor:" + getTitleAt(i),
							null, JOptionPane.YES_NO_OPTION);
					if (returnVal == JOptionPane.YES_OPTION) {
						parent.saveHandler(textPane);
					}
				}

				removeTabAt(i);
				break;
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) { }

	@Override
	public void mouseExited(MouseEvent e) { }

	@Override
	public void mousePressed(MouseEvent e) { }

	@Override
	public void mouseReleased(MouseEvent e) { }
}

/**
 * An Icon that displays a close icon
 *
 * Source from the assignment PDF
 *
 * @author Brian Reber (breber)
 */
class CloseTabIconbreber implements Icon {
	private int x_pos;
	private int y_pos;
	private final int width;
	private final int height;
	private final Icon fileIcon;

	public CloseTabIconbreber(Icon fileIcon) {
		this.fileIcon = fileIcon;
		width = 16;
		height = 16;
	}

	/* (non-Javadoc)
	 * @see javax.swing.Icon#paintIcon(java.awt.Component, java.awt.Graphics, int, int)
	 */
	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		this.x_pos = x;
		this.y_pos = y;
		Color col = g.getColor();
		g.setColor(Color.black);
		int y_p = y + 2;
		g.drawLine(x + 1, y_p, x + 12, y_p);
		g.drawLine(x + 1, y_p + 13, x + 12, y_p + 13);
		g.drawLine(x, y_p + 1, x, y_p + 12);
		g.drawLine(x + 13, y_p + 1, x + 13, y_p + 12);
		g.drawLine(x + 3, y_p + 3, x + 10, y_p + 10);
		g.drawLine(x + 3, y_p + 4, x + 9, y_p + 10);
		g.drawLine(x + 4, y_p + 3, x + 10, y_p + 9);
		g.drawLine(x + 10, y_p + 3, x + 3, y_p + 10);
		g.drawLine(x + 10, y_p + 4, x + 4, y_p + 10);
		g.drawLine(x + 9, y_p + 3, x + 3, y_p + 9);
		g.setColor(col);
		if (fileIcon != null) {
			fileIcon.paintIcon(c, g, x + width, y_p);
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.Icon#getIconWidth()
	 */
	@Override
	public int getIconWidth() {
		return width + (fileIcon != null ? fileIcon.getIconWidth() : 0);
	}

	/* (non-Javadoc)
	 * @see javax.swing.Icon#getIconHeight()
	 */
	@Override
	public int getIconHeight() {
		return height;
	}

	/**
	 * @return the bounds of the icon
	 */
	public Rectangle getBounds() {
		return new Rectangle(x_pos, y_pos, width, height);
	}
}

/*******************************************************************/
/* SIMPLE FILE FILTER */

/*
 * File filter for text documents.
 */
class MyFileFilterbreber extends FileFilter {
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