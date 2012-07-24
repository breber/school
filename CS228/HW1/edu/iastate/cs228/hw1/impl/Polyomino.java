
package edu.iastate.cs228.hw1.impl;

import java.awt.Point;

import edu.iastate.cs228.hw1.Cell;
import edu.iastate.cs228.hw1.IGameIcon;
import edu.iastate.cs228.hw1.IPolyomino;

/**
 * An abstraction of a Polyomino.
 * 
 * @author Original abstract class part of supplied code. Modified by Brian Reber
 */
public abstract class Polyomino implements IPolyomino
{
	/**
	 * An array of all the cells in this Polyomino's bounding box
	 */
	protected Cell[] cells;
	
	/**
	 * The width of this Polyomino's bounding box
	 */
	protected int width;
	
	/**
	 * The number of <code>IGameIcon</code>s in this Polyomino
	 */
	private int count;
	
	/**
	 * The position of the top left corner of this Polyomino's bounding box
	 */
	private Point position;

	/**
	 * Creates a new <code>Polyomino</code> with the beginning position
	 * for the top left corner of the bounding box at the given <code>Point</code>, 
	 * the given width of the bounding box and a count of the <code>IGameIcon</code>s
	 * this <cod>Polyomino</code> contains.
	 * 
	 * @param position
	 * The location of the top left corner of the bounding box for this <code>Polyomino</code>
	 * @param width
	 * The width of the bounding box surrounding this <code>Polyomino</code>
	 * @param count
	 * The number of <code>IGameIcon</code>s this <code>Polyomino</code> contains
	 */
	protected Polyomino(Point position, int width, int count)
	{
		this.cells = new Cell[count];
		this.count = count;
		this.width = width;
		this.position = new Point(position);
	}
	
	@Override
	public Cell[] getCells()
	{
		Cell[] absCells = new Cell[count];
		for (int i = 0; i < count; i++)
		{
			Point p = cells[i].getPosition();
			Point newP = new Point(p.x + position.x, p.y + position.y);
			IGameIcon newB = (IGameIcon)cells[i].getIcon().clone();
			absCells[i] = new Cell(newB, newP);
		}
		return absCells;
	}

	@Override
	public void shiftDown()
	{
		position.y++;
	}

	@Override
	public void shiftLeft()
	{
		position.x--;
	}

	@Override
	public void shiftRight()
	{
		position.x++;
	}

	@Override
	public void transform()
	{
		flip();
	}

	/**
	 * Flips the <code>IGameIcon</code>s across the x-axis of the bounding box
	 */
	private void flip()
	{
		for (Cell c : cells)
		{
			Point p = c.getPosition();
			int x = p.x;
			p.x = width - (x + 1);
		}
	}

	@Override
	public void cycle()
	{
		IGameIcon init = cells[cells.length - 1].getIcon();
		System.out.println("last cell - " + init.getColorHint());
		for (int i = cells.length - 1; i > 0; i--)
		{
			cells[i].setIcon(cells[i - 1].getIcon());
		}
		cells[0].setIcon(init);
	}

	@Override
	public Polyomino clone()
	{
		Polyomino cloned = null;
		try
		{
			cloned = (Polyomino) super.clone();
			cloned.position = new Point(this.position);
			cloned.cells = new Cell[count];
			for (int i = 0; i < count; i++)
			{
				cloned.cells[i] = new Cell(this.cells[i]);
			}
		}
		catch (CloneNotSupportedException e)
		{
			e.printStackTrace();
		}
		return cloned;
	}
	
	
	/**
	 * Compares this Polyomino with the given <code>Object</code> to determine if they are equal.
	 * 
	 * @param o
	 * The <code>Object</code> to compare to
	 * @return
	 * -True if the given <code>Object</code> is at the same location, and has <code>Cell</code>s
	 * that match this <code>Polyomino</code>'s cells (including the same order).<br />
	 * -False if they are not equal
	 */
	@Override
	public boolean equals(Object o)
	{
		if (o == null || o.getClass() != this.getClass())
		{
			return false;
		}
		
		Polyomino other = (Polyomino) o;
		
		// Check the position
		if (!other.position.equals(position))
		{
			return false;
		}
		
		// Check the count
		if (other.count != count)
		{
			return false;
		}
		
		// Check each cell
		for (int i = 0; i < count; i++)
		{
			//Check each cell's position
			if (!other.cells[i].getPosition().equals(cells[i].getPosition()))
			{
				return false;
			}
			//Check each cell's icon
			if (!other.cells[i].getIcon().matches(cells[i].getIcon()))
			{
				return false;
			}
		}
		
		return true;
	}
}
