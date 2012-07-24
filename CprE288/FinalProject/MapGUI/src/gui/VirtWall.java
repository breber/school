package gui;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Represents a virtual wall in the simulation
 * 
 * @author breber
 */
public class VirtWall implements Obstacle {

	private int xS;
	private int yS;
	private int xE;
	private int yE;
	
	/**
	 * Creates a new Virtual Wall with the given coordinates
	 * 
	 * @param xS the starting X position
	 * @param yS the starting Y position
	 * @param xE the ending X position
	 * @param yE the ending Y position
	 */
	public VirtWall(int xS, int yS, int xE, int yE) {
		this.xS = xS;
		this.yS = yS;
		this.xE = xE;
		this.yE = yE;
	}
	
	/* (non-Javadoc)
	 * @see gui.Obstacles#drawYourself(java.awt.Graphics)
	 */
	@Override
	public void drawYourself(Graphics g) {
		g.setColor(Color.RED);
		g.drawLine(xS, yS, xE, yE);
	}

}
