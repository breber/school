package edu.iastate.cs228.hw1.impl;

import java.awt.Color;
import java.awt.Point;

import edu.iastate.cs228.hw1.PolyominoFactory;

/**
 * Implementation of the PolyominoFactory interface for creating polyominos of a specific kind.
 *
 * @author Brian Reber
 */
public class CS228PolyominoFactory implements PolyominoFactory {

	/* (non-Javadoc)
	 * @see edu.iastate.cs228.hw1.PolyominoFactory#getITriomino(java.awt.Point, java.awt.Color[])
	 */
	@Override
	public ITriomino getITriomino(Point position, Color[] colors) {
		return new ITriomino(position, colors);
	}

	/* (non-Javadoc)
	 * @see edu.iastate.cs228.hw1.PolyominoFactory#getLTetromino(java.awt.Point, java.awt.Color[])
	 */
	@Override
	public LTetromino getLTetromino(Point position, Color[] colors) {
		return new LTetromino(position, colors);
	}

	/* (non-Javadoc)
	 * @see edu.iastate.cs228.hw1.PolyominoFactory#getSlashDomino(java.awt.Point, java.awt.Color[])
	 */
	@Override
	public SlashDomino getSlashDomino(Point position, Color[] colors) {
		return new SlashDomino(position, colors);
	}

}
