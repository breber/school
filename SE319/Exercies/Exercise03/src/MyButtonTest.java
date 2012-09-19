import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * MyButtonTest
 * 
 * Performs same functionality of ButtonTest, but without
 * using a ColorController.
 * 
 * @author Brian Reber (breber)
 */
public class MyButtonTest
{
	public static void main(String[] args)
	{
		ButtonFrame frame = new ButtonFrame();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}

/*********************************
   A frame with a button panel
 */
class ButtonFrame extends JFrame
{
	public ButtonFrame()
	{
		setTitle("ButtonTest");
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

		// Frame composed of Panel
		ButtonPanel panel = new ButtonPanel();
		getContentPane().add(panel);
	}

	public static final int DEFAULT_WIDTH = 500;
	public static final int DEFAULT_HEIGHT = 500;
}

/*********************************
   A panel with three buttons.
 */
class ButtonPanel extends JPanel implements ActionListener
{
	private JButton yellowButton = new JButton("Yellow");
	private JButton blueButton = new JButton("Blue");
	private JButton redButton = new JButton("Red");

	public ButtonPanel()
	{
		// Panel composed of three buttons
		add(yellowButton);
		add(blueButton);
		add(redButton);

		blueButton.addActionListener(this);
		yellowButton.addActionListener(this);
		redButton.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton b = (JButton) e.getSource();

		if (yellowButton.equals(b)) {
			b.getParent().setBackground(Color.YELLOW);
		} else if (blueButton.equals(b)) {
			b.getParent().setBackground(Color.BLUE);
		} else if (redButton.equals(b)) {
			b.getParent().setBackground(Color.RED);
		}
	}
}