package edu.iastate.cs228.hw5.eightpuzzle;

import java.util.Iterator;

import edu.iastate.cs228.hw5.api.Graph;

/**
 * Implicit graph of PuzzleState nodes.
 */
public class PuzzleGraph implements Graph<PuzzleState>
{
  @Override
  public Iterator<Graph.Edge<PuzzleState>> getNeighbors(PuzzleState vertex)
  {
    return vertex.neighbors().iterator();
  }

  @Override
  public int h(PuzzleState current, PuzzleState goal)
  {
    return current.score();
  }

}
