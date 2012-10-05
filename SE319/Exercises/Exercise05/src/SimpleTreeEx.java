import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;


/**
 * 
 * @author sbasu
 *   Main window of the UI
 *   - mainFrame of type JFrame contains ErdosStructurePanel of type JPanel
 */
public class SimpleTreeEx extends JFrame
{	
	public static void main(String[] args)
	{
		/**
		 * erdosStruct contains a tree where node of the tree represents a computer scientist
		 * - there is an edge from one node to another if the computer scientists associated
		 *   to these nodes have co-authored a scientific article 
		 *   (See createStruct method in ErdosStruct class for details)
		 */
		ErdosStruct erdosStruct = new ErdosStruct();
		erdosStruct.createStruct();
		
		SimpleTreeEx mainFrame = new SimpleTreeEx();
		/**
		 * ErdosStructPanel constructor takes a parameter of type ErdosStruct
		 */
		mainFrame.getContentPane().add(new ErdosStructPanel(erdosStruct));
		
		mainFrame.setSize(500, 500);
		mainFrame.setVisible(true);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
}

class ErdosStructPanel extends JPanel
{
	
	/**
	 * 
	 * @param erdosStruct contains the tree data structure with information on co-authorship relations
	 * You are required to implement this constructor such that the tree data from erdosStruct is used
	 * to create a corresponding Swing tree UI component
	 * Once you have created the Swing tree component, place it in a scroll pane and add the scrollpane
	 * to "this" panel
	 */
	public ErdosStructPanel(ErdosStruct erdosStruct)
	{

	}	
}