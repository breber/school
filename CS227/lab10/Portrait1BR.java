package lab10;

import java.awt.Color;
import java.awt.Graphics;

/**
 * @author brianreber
 *
 */
@SuppressWarnings("serial")
public class Portrait1BR extends Portrait{

	public Portrait1BR() {
		super(.25, 0.13, 0.25, 0.4, -0.15, 0.3);
	}

	/**
	 * Flips the person upside-down and adds a smile, two blue eyes, and a couple eyebrows
	 */
	@Override
	public void paintComponent(Graphics g) {

		g.setColor(Color.BLACK);

		int midX = SIZE / 2;

		int neckTop = headRadius * 2;
		int neckBottom = neckTop + neckHeight;
		int crotch = getHeight() - (neckTop + neckHeight + bodyHeight);

		// Draw legs.
		g.drawLine(midX - spreadEagleness, 0, midX, crotch);
		g.drawLine(midX + spreadEagleness, 0, midX, crotch);

		// Draw neck.
		g.drawLine(midX, neckTop, midX, crotch);

		// Draw head.
		g.setColor(new Color(255,228,181));
		g.fillOval(midX - headRadius, headRadius * 2, headRadius * 2, headRadius * 2);
		g.setColor(Color.BLACK);
		
		// Draw arms.
		g.drawLine(midX, neckBottom + (int)(armRaise - .2 * headRadius), midX - armSpan, (int)(neckBottom - .3 * headRadius));
		g.drawLine(midX, neckBottom + (int)(armRaise - .2 * headRadius), midX + armSpan, (int)(neckBottom - .3 * headRadius));

		// Draw two eyes
		int eyeRadius = (int) (0.02 * SIZE);
		g.setColor(new Color(30,144,255));
		g.fillOval(midX - 2 * eyeRadius, getHeight() - (headRadius - eyeRadius * 3), 2 * eyeRadius, 2 * eyeRadius);
		g.fillOval(midX + 2 * eyeRadius, getHeight() - (headRadius - eyeRadius * 3), 2 * eyeRadius, 2 * eyeRadius);
		g.setColor(Color.BLACK);
		
		// Give him a smile.
		int smileRadius = (int) (0.4 * headRadius);
		g.drawArc(midX - smileRadius, (int)(getHeight() - (1.6 * headRadius)), smileRadius * 2, (int)(smileRadius*1.1), 0, 180);
		g.setColor(new Color(139,69,19));
		
		//Give him eyebrows
		int eyebrowLength = (eyeRadius * 2);
		int yPosition = (int)(getHeight() - (headRadius - eyebrowLength * 2.8));
		g.drawLine(midX - eyebrowLength, (int) (yPosition + .05 * headRadius), midX, yPosition);
		g.drawLine(midX + 2 * eyebrowLength, (int) (yPosition + .05 * headRadius), (int)(midX + .15 * headRadius), yPosition);
	}
}
