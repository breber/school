package edu.iastate.cs228.hw1.impl;

import java.awt.Color;
import java.awt.Point;

import edu.iastate.cs228.hw1.Cell;

/**
 * A Polyomino that contains 2 blocks in the shape on the diagonal of its 2x2 bounding square.
 * <br />
 * Example:
 * <br />
 * <code>
 * [ X , &nbsp;  ]<br />
 * [ &nbsp; , X ]<br />
 * </code>
 * 
 * @author Brian Reber
 */
public class SlashDomino extends Polyomino 
{
	/**
	 * Creates a new <code>SlashDomino</code> with the bounding square at the given position, and
	 * with <code>IGameIcon</code>s the given colors in the <code>Color</code> array.
	 * 
	 * @param position
	 * Initial position of the bounding square
	 * @param c
	 * An array with no fewer than 2 entries in it. These colors will be the colors of the <code>IGameIcon</code>s
	 */
	public SlashDomino(Point position, Color[] c) 
	{
		super(position, 2, 2);

		cells[0] = new Cell(new Block(c[0]), new Point(0, 0));
		cells[1] = new Cell(new Block(c[1]), new Point(1, 1));
	}
}
