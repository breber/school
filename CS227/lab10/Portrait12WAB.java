package lab10;

import java.awt.Font;
import java.awt.Graphics;

/**
 * Bunch of clock-heads.
 * 
 * @author William A. Bryan
 *
 */

@SuppressWarnings("serial")
public class Portrait12WAB extends Portrait {

	public Portrait12WAB() {
		super(0.25);
	}

	@Override

	public void paintComponent(Graphics g) {
		Font f = new Font(null, Font.PLAIN, 9);
		g.setFont(f);

		super.paintComponent(g);

		int midX = getWidth() / 2;

		//Draw the numbers on their faces
		g.drawString("12", getWidth() / 2 - 4, 10);
		g.drawString("1", getWidth() / 2 + 9, 13);
		g.drawString("2", getWidth() / 2 + 16, 19);
		g.drawString("3", getWidth() / 2 + 20, 28);
		g.drawString("4", getWidth() / 2 + 16, 37);
		g.drawString("5",getWidth() / 2 + 9 ,46);
		g.drawString("6", getWidth() / 2 - 2, 48);
		g.drawString("7",getWidth() / 2 - 12,46);
		g.drawString("8",getWidth() / 2 - 19,37);
		g.drawString("9",getWidth() / 2 - 23,28);
		g.drawString("10",getWidth() / 2 - 21,19);
		g.drawString("11",getWidth() / 2 - 12,13);

		g.fillOval(midX - 2, 25, 4,4);

		//Draw the hands of the clock
		g.drawLine(midX, 26, 58, 33);
		g.drawLine(midX, 26, 69, 33);
	}

}
