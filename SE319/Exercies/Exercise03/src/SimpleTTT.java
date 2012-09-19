import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Author: Brian Reber (breber)
 * 
 * SimpleTTT is an interface for playing tictactoe
 * It extends JFrame, implements action listener interface
 * - JFrame: forms the UI window
 * - listener reacts to user selection
 * 
 * SimpleTTT contains a panel
 * - panel is set up in GridLayout
 * 
 * panel contains 9 buttons
 * - whenever an user clicks on a button, its text-value changes
 * to either "X" or "O" depending on the turn (turn toggles between
 * player 1 and player 2)
 * 
 * Whenever a player wins, a dialog opens stating the winner of the game
 * and exits the game.
 */
public class SimpleTTT extends JFrame implements ActionListener {
	// turn keeps track of player's turn: true => player 1 and false => player 2
	private boolean turn = true;

	// array of buttons put in the panel to form the game board
	private JButton gridElement[] = new JButton[9];

	JPanel mainPanel = new JPanel();

	/*
	 * creating the main window
	 */
	public static void main(String[] args) {
		SimpleTTT mainFrame = new SimpleTTT("TicTacToe");
		mainFrame.setSize(200, 200);
		mainFrame.setVisible(true);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/*
	 * creating the game board in the constructor 1. set the title 2. - set the
	 * layout of the panel - add the buttons to the panel - add action listener
	 * to the buttons 3. add the panel to the frame
	 */
	public SimpleTTT(String title) {
		super(title);
		setGrid(mainPanel);
		getContentPane().add(mainPanel);
	}

	public void setGrid(JPanel mainPanel) {
		mainPanel.setLayout(new GridLayout(3, 3));

		// adding empty valued buttons to form the elements in the grid
		for (int i = 0; i < 9; i++) {
			// create
			gridElement[i] = new JButton("");
			// register a listener
			gridElement[i].addActionListener(this);
			// add to the panel
			mainPanel.add(gridElement[i]);
		}
	}

	/*
	 * Implementation of action listener interface.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton b = (JButton) e.getSource();

		if ("".equals(b.getText())) {
			if (turn) {
				b.setText("X");
			} else {
				b.setText("O");
			}

			turn = !turn;
		}
	}
}