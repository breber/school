package gui;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Represents a Pillar in the simulation
 * 
 * @author breber
 */
public class Pillar implements Obstacle {

	private int xLoc;
	private int yLoc;
	private int diameter;
	
	/**
	 * Creates a new Pillar at the given location with the given diameter
	 * 
	 * @param xLoc
	 * @param yLoc
	 * @param diameter
	 */
	public Pillar(int xLoc, int yLoc, int diameter) {
		this.xLoc = xLoc;
		this.yLoc = yLoc;
		this.diameter = diameter;
	}
	
	/* (non-Javadoc)
	 * @see gui.Obstacles#drawYourself(java.awt.Graphics)
	 */
	@Override
	public void drawYourself(Graphics g) {
		g.setColor(Color.RED);
		g.fillOval(xLoc - diameter / 2, yLoc - diameter / 2, diameter, diameter);
	}

}
