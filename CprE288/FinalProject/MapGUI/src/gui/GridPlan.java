package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * A grid that contains a robot and obstacles
 * 
 * 20px = 10 cm
 * 
 * @author breber
 */
@SuppressWarnings("serial")
public class GridPlan extends JPanel implements MouseListener {
	/**
	 * The robot currently traversing the course
	 */
	public Robot robot;
	
	/**
	 * A list of obstacles encountered
	 */
	private ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
	
	/**
	 * Booleans representing whether we are adding elements right now.
	 * Used to control mouse clicks
	 */
	private boolean addingPillar = false;
	private boolean addingCliff = false;
	private boolean addingVirtWall = false;
	
	/**
	 * Temporary variables for storing original location
	 * of clicks for adding virtual walls
	 */
	private int tempX = -1;
	private int tempY = -1;
	
	/**
	 * Creates a new Grid
	 */
	public GridPlan() {
		addMouseListener(this);
	}
	
	/**
	 * Paint the grid. Includes lines, robot and all objects currently added
	 */
	protected synchronized void paintComponent(Graphics gr) {
		gr.setColor(Color.BLACK);
		// Draw gridlines
		for (int i = 0; i < getWidth(); i += 20) {
			gr.drawLine(i, 0, i, getHeight());
		}
		// Draw gridlines
		for (int i = 0; i < getHeight(); i += 20) {
			gr.drawLine(0, i, getWidth(), i);
		}
		
		// Draw robot
		robot.drawYourself(gr);
		
		// Draw all obstacles
		for (Obstacle o : obstacles) {
			o.drawYourself(gr);
		}
    }

	/**
	 * Add a pillar to the simulation.
	 * 
	 * Will cause the next click on the grid to pop up a box
	 */
	public void addPillar() {
		addingPillar = true;
	}
	
	/**
	 * Add virtual wall to simulation
	 * 
	 * Will cause next 2 clicks on the grid to control the location
	 */
	public void addVirtWall() {
		addingVirtWall = true;
	}
	
	/**
	 * Add cliff to the simulation
	 * 
	 * WIll cause next click on grid to open a pop up box
	 */
	public void addCliff() {
		addingCliff = true;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		if (addingPillar) {
			String res = JOptionPane.showInputDialog("Width:");
			if (res != null) {
				try {
					int width = Integer.parseInt(res);
					obstacles.add(new Pillar(e.getX(), e.getY(), 2 * width));
				} catch (NumberFormatException ex) {
					System.err.println("Invalid number");
				}
			}
			repaint();
			addingPillar = false;
		}
		if (addingVirtWall) {
			if (tempX != -1 && tempY != -1) {
				obstacles.add(new VirtWall(e.getX(), e.getY(), tempX, tempY));
				tempX = -1;
				tempY = -1;

				repaint();
				addingVirtWall = false;
			} else {
				tempX = e.getX();
				tempY = e.getY();
			}
		}
		
		if (addingCliff) {
			String resX = JOptionPane.showInputDialog("Width:");
			String resY = JOptionPane.showInputDialog("Height:");
			try {
				obstacles.add(new Cliff(e.getX(), e.getY(), 2 * Integer.parseInt(resX), 2 * Integer.parseInt(resY)));
			} catch (NumberFormatException ex) {
				System.err.println("Invalid number");
			}
			repaint();
			addingCliff = false;
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent e) {}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent e) {}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent e) {}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent e) {}
	
}
