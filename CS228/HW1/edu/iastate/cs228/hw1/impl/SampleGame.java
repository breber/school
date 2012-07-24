package edu.iastate.cs228.hw1.impl;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import edu.iastate.cs228.hw1.IGameIcon;
import edu.iastate.cs228.hw1.Cell;

/**
 * A basic concrete implementation of the IGame interface 
 * for a simple version of Tetris.
 */
public class SampleGame extends AbstractBlockGame
{
  private int rowsCompleted;
  
  public SampleGame()
  {
	  super(new SampleGenerator());
  }
  
  /**
   * Returns all positions within completed rows in the grid.
   */
  @Override
  protected List<Point> determineCellsToCollapse()
  {
    List<Point> result = new ArrayList<Point>();
    List<Integer> rows = new ArrayList<Integer>();
    
    // we only need to look at rows affected by the current 
    // polyomino
    for (Cell c : current.getCells())
    {
      int row = c.getPosition().y;
      if (isRowComplete(row) && !rows.contains(row) )
      {
        rows.add(row);
      }
    }
    for (int row : rows)
    {
      // add all cells in the row to the result list
      for (int i = 0; i < WIDTH; ++i)
      {
        result.add(new Point(i, row));
      }
    }
    rowsCompleted += result.size();
    return result;
  }
  
  /**
   * Determines whether all cells in the given row are occupied.
   *
   * @param row index of the row to check
   * @return true if all cells in the row are occupied, false otherwise
   */
	private boolean isRowComplete(int row) {
		boolean ret = true;
		if(0 <= row && row < getHeight()) {
			for(IGameIcon p : grid[row]) {
				if(p == null) {
					ret = false;
					break;
				}
			}
		} else {
			ret = false;
		}
		return ret;
	}

  @Override
  protected int determineScore()
  {
    return rowsCompleted;
  }
}
