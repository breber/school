import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * XText - A Text Editor
 * 
 * Java Version: 6
 * 
 * @author Brian Reber (breber)
 */
public class XTextbreber extends JFrame {
	private static final long serialVersionUID = 1L;

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
		JTextArea textArea = new JTextArea();
		textArea.setEditable(true);
		textArea.setLineWrap(true);
		//		textArea.setBackground(Color.GREEN);

		getContentPane().add(new JScrollPane(textArea));
	}

	private JMenuBar buildMenuBar() {
		// Setup the Menu Bar
		JMenuBar menuBar = new JMenuBar();

		// Setup the File Menu
		JMenu fileMenu = new JMenu("File");

		JMenuItem newMenuItem = new JMenuItem("New");
		newMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
			}
		});
		fileMenu.add(newMenuItem);
		fileMenu.add(new JMenuItem("Open"));
		fileMenu.add(new JMenuItem("Save"));
		fileMenu.add(new JMenuItem("Exit"));
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

}
