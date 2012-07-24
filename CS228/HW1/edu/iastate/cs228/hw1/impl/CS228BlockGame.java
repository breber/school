package edu.iastate.cs228.hw1.impl;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import edu.iastate.cs228.hw1.IPolyominoGenerator;

/**
 * A specific type of BlockGame for the CS228 course.
 * 
 * This game is similar to Tetris.  Unlike Tetris, in this game
 * the blocks collapse when there are 3 or more of the same color blocks in a row.
 * 
 * @author Brian Reber
 *
 */
public class CS228BlockGame extends AbstractBlockGame 
{

	/**
	 * A count containing the number of cells collapsed 
	 */
	private int numCellsCollapsed;
	
	/**
	 * Creates a new instance of <code>CS228BlockGame</code> with the given generator.
	 * 
	 * @param generator
	 * The generator this game uses to generate shapes
	 */
	public CS228BlockGame(IPolyominoGenerator generator) 
	{
		super(generator);
		numCellsCollapsed = 0;
		
		for (int row = HEIGHT - 8; row < HEIGHT; row++) {
			for (int col = (row % 2 == 0) ? 0 : 1; col < WIDTH; col += 2) {
				grid[row][col] = new Block(getRandomColor());
			}
		}
	}
	
	/**
	 * Gets a random Color from the <code>AbstractBlockGame.COLORS</code> array
	 * 
	 * @return
	 * Random color from the <code>AbstractBlockGame.COLORS</code> array
	 */
	private Color getRandomColor() 
	{
		return AbstractBlockGame.COLORS[(int) (Math.random() * AbstractBlockGame.COLORS.length)];
	}

	/* (non-Javadoc)
	 * @see edu.iastate.cs228.hw1.impl.AbstractBlockGame#determineCellsToCollapse()
	 */
	@Override
	public List<Point> determineCellsToCollapse() 
	{
		List<Point> cells = new ArrayList<Point>();
		
	    for (int i = 0; i < WIDTH; i++)
	    {
	    	for (int j = 0; j < HEIGHT; j++)
	    	{
	    		if (getCell(j, i) != null)
	    			cells.addAll(checkCells(new Point(i, j)));
	    	}
	    }

	    numCellsCollapsed += cells.size();
	    return cells;
	}

	/**
	 * Checks the cell at the parameter Point, and the cells surrounding it to the top, left
	 * right, and bottom (4 cells in total)
	 * 
	 * @param p
	 * The starting point to check
	 * @return
	 * A List of 3 or more touching Points that have the same Icon. If there aren't 3 
	 * connecting Points that match, an empty List will be returned. 
	 */
	private List<Point> checkCells(Point p) 
	{
		List<Point> list = new ArrayList<Point>();
		int row = p.y;
		int col = p.x;
		
		// Checking each cell to the top, right, left, and bottom of the current cell to see
		// if we have a match.
		for (int i = 0; i < 4; i++) {
			int offsetRow = 0, offsetCol = 0;
			if (i == 0) { offsetRow = -1; offsetCol = 0; }
			if (i == 1) { offsetRow = 0; offsetCol = -1; }
			if (i == 2) { offsetRow = 0; offsetCol = 1; }
			if (i == 3) { offsetRow = 1; offsetCol = 0; }
			int rowWithModification = row + offsetRow;
			int colWithModification = col + offsetCol;

			// If the cell with our offset is within the bounds of the board, we enter this if statement 
			if (rowWithModification >= 0 && rowWithModification < HEIGHT && colWithModification >= 0 
					&& colWithModification < WIDTH) 
			{
				// If the cell contains an IGameIcon and it matches the current (base) cell we are 
				// checking against, we will add the current (base) position, and the offset position
				// to the array list.
				if (getCell(rowWithModification, colWithModification) != null && 
						getCell(rowWithModification, colWithModification).matches(getCell(row, col))) 
				{
					list.add(p);
					list.add(new Point(colWithModification, rowWithModification));
				}
			}
		}
		
		// If we have more than 2 cells in the arraylist, we will return it.
		// Otherwise, we return an empty arraylist
		if (list.size() > 2) {
			return list;
		} else {
			return new ArrayList<Point>();
		}
	}

	/* (non-Javadoc)
	 * @see edu.iastate.cs228.hw1.impl.AbstractBlockGame#determineScore()
	 */
	@Override
	public int determineScore() {
		return numCellsCollapsed;
	}
}
