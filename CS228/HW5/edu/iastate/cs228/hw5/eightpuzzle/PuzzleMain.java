package edu.iastate.cs228.hw5.eightpuzzle;

import java.util.List;

import edu.iastate.cs228.hw5.PathFinderFactoryImpl;
import edu.iastate.cs228.hw5.api.BidirectionalGraphAnimation;
import edu.iastate.cs228.hw5.api.GraphAnimation;
import edu.iastate.cs228.hw5.api.PathFinderFactory;

/**
 * Demo using the shortest path algorithms to solve an instance of 
 * the "sliding square" puzzle.  
 * 
 * A specific initial configuration can be specified like this:
 *    int[] a = {1, 7, 2, 0, 4, 5, 8, 6, 3};
 *    PuzzleState start = new PuzzleState(a);
 * 
 * The goal state is always {1, 2, 3, 4, 5, 6, 7, 8, 0}.
 * 
 * Note that not all initial states are solvable; you can check
 * by calling PuzzleState.isSolvable(a)
 * 
 */
public class PuzzleMain
{
  /**
   * @param args
   */
  public static void main(String[] args)
  {
    // generate a random, solvable state
    PuzzleState start = PuzzleState.randomState();
       
    PuzzleGraph g = new PuzzleGraph();
    PuzzleState goal = PuzzleState.goalState();
    System.out.println("Start: " + start);
    
    PathFinderFactory factory = new PathFinderFactoryImpl();    

    GraphAnimation<PuzzleState> finder = factory.createDijkstraFinder(g, start, goal);
    System.out.println("Using Dijkstra's algorithm");
    while (!finder.done())
    {
      finder.step();
    }
    System.out.println("Path length: " + finder.getDistance(goal));
    System.out.println("Closed set size: " + finder.closedSet().size());
    List<PuzzleState> path = finder.getPath(goal);
    System.out.println("Path: " + path);

    finder = factory.createAStarFinder(g, start, goal);
    System.out.println();
    System.out.println("Using A* algorithm");
    while (!finder.done())
    {
      finder.step();
    }
    System.out.println("Path length: " + finder.getDistance(goal));
    System.out.println("Closed set size: " + finder.closedSet().size());
    path = finder.getPath(goal);
    System.out.println("Path: " + path);
    
    BidirectionalGraphAnimation<PuzzleState> bfinder = factory.createBidirectionalAStarFinder(g, start, goal);    
    System.out.println();
    System.out.println("Using Bidirectional A*");
    while (!bfinder.done())
    {
      bfinder.step();
    }
    System.out.println("Path length " + bfinder.getCompleteDistance());
    System.out.println("Size of closed sets: " + (bfinder.closedSet().size() + bfinder.closedSetReverse().size()));
    path = bfinder.getCompletePath();
    System.out.println("Path: " + path);
  }
}
