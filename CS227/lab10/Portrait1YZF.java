package lab10;

import java.awt.Graphics;


@SuppressWarnings("serial")
public class Portrait1YZF extends Portrait {

	public Portrait1YZF() {
		super(0.3);

		setBodyHeight(0.1);
		setArmSpan(0.30);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		int leftEX = getWidth() / 3;
		int rightEX = getWidth() - getWidth() / 3;
		int midX = getWidth() / 2;

		// Draw eyes
		int eyeRadius = (int) (0.05 * SIZE);
		g.fillOval(leftEX - eyeRadius, headRadius - eyeRadius * 2, 2 * eyeRadius, 2 * eyeRadius);
		g.fillOval(rightEX - eyeRadius, headRadius - eyeRadius * 2, 2 * eyeRadius, 2 * eyeRadius);
		// And give him a creepy smile.
		int smileRadius = (int) (0.5 * headRadius);
		g.drawArc(midX - smileRadius, (int) (0.8 * headRadius), smileRadius * 2, smileRadius * 2, 0, -180);
		// Draw ears
		int earRadius = (int) (0.05 * SIZE);
		g.drawArc(midX - earRadius - 35, (int) (0.8 * headRadius), earRadius * 2, earRadius * 2, 0, 360);
		g.drawArc(midX + earRadius + 25, (int) (0.8 * headRadius), earRadius * 2, earRadius * 2, 0, 360);
	}
}