/*
 * @author Brian Reber (breber)
 * 
 * This implementation satisfies the design specification. Below
 * is a description about how it works.
 * 
 * When a user selects an item from the list, we get the source of
 * the event (the JList), and then get the selected index. Once we
 * have the selected index, we use our PlanetListModel to get the
 * File corresponding to the selected list item. We then use the provided
 * viewModuleFunctionality to display the contents of the file in the
 * planetView.
 */

import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * 
 * @author sbasu
 * Simple list with DefaultListModel
 */
public class SimpleListEx extends JFrame
{
	/**
	 * 
	 * @param args
	 * Setting up the frame
	 */
	public static void main(String[] args)
	{
		SimpleListEx myFrame = new SimpleListEx();
		myFrame.setSize(1000, 500);
		myFrame.setVisible(true);
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * Constructing the entire frame
	 */
	SimpleListEx()
	{
		// add the split panel
		getContentPane().add(splitPane);

		/**
		 * set up the select module: a list made of list model
		 */
		myList.setModel(myListModel);
		myList.setCellRenderer(new SimpleRenderEx());

		/*
		 * set up the view module: a pane containing a label
		 */
		planetPane.add(new JLabel("The images appear here"));

		/**
		 * Controller for list realizes the functionality for list selection
		 */
		myList.addListSelectionListener(new ListSelectionListener() {

			/**
			 * List event: send picture file information to the view module
			 * Interface between the select module and the view module
			 */
			@Override
			public void valueChanged(ListSelectionEvent event)
			{
				// Get the event source
				JList list = (JList) event.getSource();

				// Get the selected index
				int selectedIndex = list.getSelectedIndex();

				// Get the element at the specified index
				File selectedFile = (File) myListModel.getElementAt(selectedIndex);

				// Have the file rendered using the provided functionality
				viewModuleFunctionality(selectedFile);
			}
		});
	}

	/**
	 * 
	 * @param selected: file containing the picture of the item selected in the list
	 */
	public void viewModuleFunctionality(File selected)
	{
		planetPane.removeAll();
		planetPane.repaint();
		try
		{
			planetPane.add(new JLabel(new ImageIcon(ImageIO.read(selected))));
		} catch (IOException ex) {
			System.out.println("Some error");
		}
		planetPane.revalidate();
	}

	private JList myList= new JList();
	private PlanetListModel myListModel = new PlanetListModel();
	private JScrollPane myListScrollPane = new JScrollPane(myList);
	private JPanel planetPane = new JPanel();
	private JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, myListScrollPane, planetPane);
}




/**
 * 
 * @author sbasu
 * Class to create list data dynamically using AbstractListModel
 */
class PlanetListModel extends AbstractListModel
{
	/**
	 *  Returns the i-th element in the list
	 */
	@Override
	public Object getElementAt(int i)
	{
		switch (i)
		{
		case 1: return new File("images/mercury.jpg");
		case 2: return new File("images/venus.jpg");
		case 3: return new File("images/earth.gif");
		case 4: return new File("images/mars.jpg");
		case 5: return new File("images/jupiter.jpg");
		case 6: return new File("images/saturn.jpg");
		case 7: return new File("images/uranus.png");
		case 8: return new File("images/neptune.jpg");
		case 9: return new File("images/pluto.jpeg");
		default: return new File("images/sun.jpg");
		}
	}

	/**
	 * returns the size of the list
	 */
	@Override
	public int getSize()  { return 10; }
}


class SimpleRenderEx implements ListCellRenderer
{

	@Override
	public Component getListCellRendererComponent(JList list, Object item,
			int index, boolean isSelected, boolean isFocus)
	{
		int i = item.toString().lastIndexOf('/');
		int j = item.toString().lastIndexOf('.');
		JLabel label = new JLabel(item.toString().substring(i+1, j).toUpperCase());
		label.setOpaque(true);
		if (isSelected)
		{
			label.setBackground(Color.red);
			label.setForeground(Color.white);

		}
		return label;
	}
}