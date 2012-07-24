package lab10;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * This class displays a bunch of portraits in a scrollable window.
 */
@SuppressWarnings("serial")
public class GroupPhoto extends JFrame {

	public GroupPhoto(ArrayList<Portrait> portraits) {
		super("Our Family");

		// Create a gridded panel having enough spots for every portrait.
		// Portraits are arranged in a square.
		JPanel panel = new JPanel();
		int root = (int) Math.ceil(Math.sqrt(portraits.size()));
		panel.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		for (Portrait p : portraits) {
			constraints.gridx = (constraints.gridx + 1) % root;
			panel.add(p, constraints);
		}

		// We'll someday have enough portraits that our window won't be big
		// enough, so let's give it scrollbars.
		JScrollPane scroller = new JScrollPane(panel);
		add(scroller);

		// Make the frame fit around its contents. If you're on a netbook,
		// you might want to shrink the height, the second argument.
		setSize(800, 800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	public static void main(String[] args) {
		ArrayList<Portrait> portraits = new ArrayList<Portrait>();
		portraits.add(new Portrait(0.2));

		// Replace these constructor calls with calls to constructors
		// of code you pull off of WebCT.
		portraits.add(new Portrait0CRJ());
		portraits.add(new Portrait1BR());
		portraits.add(new Portrait3JLV());
		portraits.add(new Portrait12WAB());
		portraits.add(new Portrait1YZF());
		portraits.add(new Portrait1JTG());
		portraits.add(new Portrait(0.26));
		portraits.add(new Portrait(0.27));
		portraits.add(new Portrait(0.28));
		portraits.add(new Portrait(0.29));
		portraits.add(new Portrait(0.3));

		new GroupPhoto(portraits);
	}
}

