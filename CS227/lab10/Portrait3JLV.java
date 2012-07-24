package lab10;

import java.awt.Color;
import java.awt.Graphics;
/**
 * 
 * @author Jeramie Vens
 *
 */
@SuppressWarnings("serial")
public class Portrait3JLV extends Portrait
{

	public Portrait3JLV()
	{
		super(.22);

	}

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		int centerX = getWidth()/2;
		int leftX = centerX-(headRadius/2);
		int rightX= centerX+(headRadius/2);

		int eyeRadius = (int) (0.05 * SIZE);
		int eye2Radius = (int) (.02*SIZE);
		g.setColor(Color.blue);
		g.fillOval(leftX - eyeRadius, headRadius - eyeRadius * 2, 2 * eyeRadius,2 * eyeRadius);
		g.fillOval(rightX - eyeRadius, headRadius - eyeRadius * 2, 2*eyeRadius, 2*eyeRadius);

		g.setColor(Color.red);
		g.fillOval(leftX, headRadius - eye2Radius * 3, 2 * eye2Radius,2 * eye2Radius);
		g.fillOval(rightX - 2*eye2Radius, headRadius - eye2Radius * 3, 2*eye2Radius, 2*eye2Radius);

		g.setColor(Color.GREEN);
		g.fill3DRect(centerX-(int)(.025*SIZE), headRadius, (int)(.05*SIZE), headRadius/2, true);

		g.setColor(Color.red);
		int smileRadius = (int) (0.5 * headRadius);
		g.drawArc(centerX - smileRadius, (int) (0.8 * headRadius),
				smileRadius * 2, smileRadius * 2, 0, -180);
	}
}