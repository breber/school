package edu.iastate.cs228.hw1.impl;

import java.awt.Color;
import java.awt.Point;

import edu.iastate.cs228.hw1.Cell;

/**
 * A Polyomino that contains 3 blocks along the center of its 3x3 bounding square
 * <br />
 * Example:
 * <br />
 * <code>
 * [ &nbsp; , X , &nbsp; ] <br />
 * [ &nbsp; , X , &nbsp; ]<br />
 * [ &nbsp; , X , &nbsp; ]<br />
 * </code>
 * @author Brian Reber
 */
public class ITriomino extends Polyomino 
{
	/**
	 * Creates a new <code>ITriomino</code> with the bounding square at the given position, and
	 * with <code>IGameIcon</code>s the given colors in the <code>Color</code> array.
	 * 
	 * @param position
	 * Initial position of the bounding square
	 * @param c
	 * An array with no fewer than 3 entries in it. These colors will be the colors of the <code>IGameIcon</code>s
	 */
	public ITriomino(Point position, Color[] c) 
	{
		super(position, 3, 3);
		
		cells[0] = new Cell(new Block(c[0]), new Point(1, 0));
		cells[1] = new Cell(new Block(c[1]), new Point(1, 1));
		cells[2] = new Cell(new Block(c[2]), new Point(1, 2));
	}
}
