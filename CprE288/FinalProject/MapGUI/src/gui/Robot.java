package gui;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Represents the robot in the simulation
 * 
 * @author breber
 */
public class Robot implements Obstacle {

	private double xCord;
	private double yCord;
	private int direction;

	private static final int DIAMETER = 35;
	
	/**
	 * Creates a new robot with the given coordinates
	 * 
	 * @param xCord
	 * @param yCord
	 */
	public Robot(int xCord, int yCord) {
		this.xCord = xCord;
		this.yCord = yCord;
		this.direction = 0;
	}
	
	/**
	 * Moves the robot forward 1 cm
	 */
	public void moveForward() {
		for (int i = 0; i < 2; i++) {
			xCord += Math.cos(Math.toRadians(direction));
			yCord += Math.sin(Math.toRadians(direction));
		}
	}
	
	/**
	 * Moves the robot backward 1 cm
	 */
	public void moveBackward() {
		for (int i = 0; i < 2; i++) {
			xCord -= Math.cos(Math.toRadians(direction));
			yCord -= Math.sin(Math.toRadians(direction));
		}
	}
	
	/**
	 * Rotates the robot the given number of degrees
	 * 
	 * @param degrees
	 * Degrees to rotate (can be negative or positive)
	 */
	public void rotate(int degrees) {
		direction += degrees;
		direction %= 360;
	}
	
	/**
	 * Gets the x coordinate of the robot
	 * 
	 * @return the x coordinate of the robot
	 */
	public double getXCord() {
		return xCord;
	}
	
	/**
	 * Gets the y coordinate of the robot
	 * 
	 * @return the y coordinate of the robot
	 */
	public double getYCord() {
		return yCord;
	}

	/* (non-Javadoc)
	 * @see gui.Obstacle#drawYourself(java.awt.Graphics)
	 */
	@Override
	public void drawYourself(Graphics g) {
		g.setColor(Color.BLUE);
		g.fillOval((int) (getXCord() + DIAMETER / 2), (int) (getYCord() + DIAMETER / 2), DIAMETER, DIAMETER);
		
		int xFirst = (int) ((getXCord() + DIAMETER) + 4 * Math.cos(Math.toRadians(direction)));
		int yFirst = (int) ((getYCord() + DIAMETER) + 4 * Math.sin(Math.toRadians(direction)));
		int xSecond = (int) ((getXCord() + DIAMETER) - 4 * Math.cos(Math.toRadians(direction)));
		int ySecond = (int) ((getYCord() + DIAMETER) - 4 * Math.sin(Math.toRadians(direction)));
	
		g.setColor(Color.RED);
		g.drawLine(xFirst, yFirst, xSecond, ySecond);
		xFirst = (int) ((getXCord() + DIAMETER) + 4 * Math.cos(Math.toRadians(direction+45)));
		yFirst = (int) ((getYCord() + DIAMETER) + 4 * Math.sin(Math.toRadians(direction+45)));
		xSecond = (int) ((getXCord() + DIAMETER) + 4 * Math.cos(Math.toRadians(direction-45)));
		ySecond = (int) ((getYCord() + DIAMETER) + 4 * Math.sin(Math.toRadians(direction-45)));
	
		g.setColor(Color.RED);
		g.drawLine(xFirst, yFirst, xSecond, ySecond);
	}
}
