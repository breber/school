package edu.iastate.cs228.hw1.impl;

import java.awt.Color;
import java.awt.Point;

import edu.iastate.cs228.hw1.Cell;

/**
 * A Polyomino that contains 4 blocks in the shape of an upside-down L
 * <br />
 * Example:
 * <br />
 * <code>
 * [ X , X , &nbsp; ]<br />
 * [ &nbsp; , X , &nbsp; ]<br />
 * [ &nbsp; , X , &nbsp; ]<br />
 * </code>
 * @author Brian Reber
 */
public class LTetromino extends Polyomino 
{
	/**
	 * Creates a new <code>LTetromino</code> with the bounding square at the given position, and
	 * with <code>IGameIcon</code>s the given colors in the <code>Color</code> array.
	 * 
	 * @param position
	 * Initial position of the bounding square
	 * @param c
	 * An array with no fewer than 4 entries in it. These colors will be the colors of the <code>IGameIcon</code>s 
	 */
	public LTetromino(Point position, Color[] c) 
	{
		super(position, 3, 4);
		
		cells[0] = new Cell(new Block(c[0]), new Point(0, 0));
		cells[1] = new Cell(new Block(c[1]), new Point(1, 0));
		cells[2] = new Cell(new Block(c[2]), new Point(1, 1));
		cells[3] = new Cell(new Block(c[3]), new Point(1, 2));
	}
}
