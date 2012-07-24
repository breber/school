package gui;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Represents a cliff in the simulation
 * 
 * @author breber
 */
public class Cliff implements Obstacle {

	private int x;
	private int y;
	private int width;
	private int height;
	
	/**
	 * Creates a new Cliff at the given location, with the 
	 * given dimensions 
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public Cliff(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	/* (non-Javadoc)
	 * @see gui.Obstacles#drawYourself(java.awt.Graphics)
	 */
	@Override
	public void drawYourself(Graphics g) {
		g.setColor(Color.RED);
		g.fillRect(x - width / 2, y - height / 2, width, height);
	}

}
