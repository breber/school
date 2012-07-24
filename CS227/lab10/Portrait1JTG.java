package lab10;

import java.awt.Color;
import java.awt.Graphics;

@SuppressWarnings("serial")
public class Portrait1JTG extends Portrait {

	public Portrait1JTG() {
		super(0.2);
		setBodyHeight(0.5);
		setArmSpan(0.5);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		int midX = getWidth() / 2;
		g.setColor(Color.RED);

		int eyeRadius = (int) (0.05 * SIZE);
		g.fillOval(midX - 8, headRadius - eyeRadius * 2, 1 * eyeRadius,
				2 * eyeRadius);
		g.fillOval(midX + 3, headRadius - eyeRadius * 2, 1 * eyeRadius,
				2 * eyeRadius);

		int smileRadius = (int) (0.5 * headRadius);
		g.drawArc(midX - smileRadius, (int) (0.7 * headRadius),
				smileRadius * 2, smileRadius * 2, 0, -180);
	}
}