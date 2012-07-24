package edu.iastate.cs228.hw1.impl;

import java.awt.Color;
import java.awt.Point;

import edu.iastate.cs228.hw1.Cell;
import edu.iastate.cs228.hw1.IPolyomino;

/**
 * Sample implementation of IPolyomino with 
 * three cells in a 2 x 2 bounding square.
 */
public class Sampleomino implements IPolyomino
{
	private Point position;
	private Cell[] cells;

	public Sampleomino(Point p, Color[] c)
	{
		position = p;
		cells = new Cell[3];

		// make a 2 x 2, L-shaped triomino
		cells[0] = new Cell(new Block(c[0]), new Point(p.x, p.y));
		cells[1] = new Cell(new Block(c[1]), new Point(p.x, p.y + 1));
		cells[2] = new Cell(new Block(c[2]), new Point(p.x + 1, p.y + 1));
	}

	@Override
	public Cell[] getCells()
	{
		return cells;
	}

	@Override
	public void shiftDown()
	{
		position.y += 1;
		for (Cell c: cells)
		{
			c.getPosition().y += 1;
		}
	}

	@Override
	public void shiftLeft()
	{
		position.x--;
		for (Cell c: cells)
		{
			c.getPosition().x--;
		}
	}

	@Override
	public void shiftRight()
	{
		position.x++;
		for (Cell c: cells)
		{
			c.getPosition().x++;
		}
	}

	@Override
	public void transform()
	{
		// TODO
	}

	@Override
	public void cycle()
	{
		// TODO
	}

	@Override
	public Object clone()
	{
		try
		{
			Sampleomino s = (Sampleomino) super.clone();

			// make it into a deep copy
			s.position = new Point(position);
			s.cells = new Cell[3];
			for (int i = 0; i < 3; ++i)
			{
				s.cells[i] = new Cell(cells[i]);
			}
			return s;
		}
		catch (CloneNotSupportedException e)
		{
			return null;
		}
	}
}
