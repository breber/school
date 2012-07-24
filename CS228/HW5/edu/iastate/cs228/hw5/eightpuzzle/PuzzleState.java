package edu.iastate.cs228.hw5.eightpuzzle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import edu.iastate.cs228.hw5.api.Graph;

/**
 * Class representing a possible state of a "sliding square"
 * puzzle such as the 9-puzzle or 15-puzzle. Instances
 * of this class form the nodes of an implicit graph in 
 * which adjacent states are found using the neighbors()
 * method.
 */
public class PuzzleState
{
  
  private static final int WIDTH = 3;
  private static final int HEIGHT = 3;
  private static final int SIZE = WIDTH * HEIGHT;
  
  /**
   * Configuration of the grid, by rows.
   */
  private int[] s;
  
  /**
   * Constructs a PuzzleState with the given initial state.
   * @param a
   */
  public PuzzleState(int[] a)
  {
    s = a;
  }
  
  /**
   * Returns a PuzzleState representing the usual goal state
   * (1, 2, 3, ... in top row, etc.)
   * @return
   */
  public static PuzzleState goalState()
  {
    int[] s = new int[SIZE];
    for (int i = 0; i < SIZE - 1; ++i)
    {
      s[i] = i + 1;
    }
    s[SIZE - 1] = 0;
    return new PuzzleState(s);
  }
  
  /**
   * Returns the states that can be reached from this state in a single move.
   * @return
   */
  public List<Graph.Edge<PuzzleState>> neighbors()
  {
    ArrayList<Graph.Edge<PuzzleState>> ret = new ArrayList<Graph.Edge<PuzzleState>>();
    int i = pos();
    if (i >= WIDTH)
    {
      int[] newArray = Arrays.copyOf(s, SIZE);
      swap(newArray, i, i - WIDTH);
      ret.add(new Graph.Edge<PuzzleState>(new PuzzleState(newArray), 1));
    }
    if (i < WIDTH * (HEIGHT - 1))
    {
      int[] newArray = Arrays.copyOf(s, SIZE);
      swap(newArray, i, i + WIDTH);
      ret.add(new Graph.Edge<PuzzleState>(new PuzzleState(newArray), 1));
    }
    if (i % WIDTH != 0)
    {
      int[] newArray = Arrays.copyOf(s, SIZE);
      swap(newArray, i, i - 1);
      ret.add(new Graph.Edge<PuzzleState>(new PuzzleState(newArray), 1));
    }
    if (i % WIDTH != WIDTH - 1)
    {
      int[] newArray = Arrays.copyOf(s, SIZE);
      swap(newArray, i, i + 1);
      ret.add(new Graph.Edge<PuzzleState>(new PuzzleState(newArray), 1));
    }
    return ret;
  }
  
  /**
   * Determines whether this state is the goal state.
   */
  public boolean isGoal()
  {
    for (int i = 0; i < SIZE - 1; ++i)
    {
      if (s[i] != i + 1) return false;
    }
    return s[SIZE - 1] == 0;
  }
  
 
  /**
   * Returns the sum of the manhattan distances of each
   * value from its location in the goal state.
   * @return
   */
  public int score()
  {
    int ret = 0;
    int[] positions = new int[SIZE];
    for (int i = 0; i < SIZE; ++i)
    {
      positions[s[i]] = i;
    }
    
    for (int i = 0; i < SIZE; ++i)
    {
      int index = i;
      if (i == 0) index = SIZE;
      int pos = positions[i];
      int x = pos % WIDTH;
      int y = pos / WIDTH;
      int targetX = (index - 1) % WIDTH;
      int targetY = (index - 1) / WIDTH;
      int dist = Math.abs(x - targetX) + Math.abs(y - targetY);
      ret += dist;
    }
    return ret;
  }
  
  @Override
  public boolean equals(Object obj)
  {
    if (!(obj instanceof PuzzleState))
    {
      return false;
    }
    return Arrays.equals(this.s, ((PuzzleState) obj).s); 
  }
  
  /**
   * Returns a randomly generated, solvable state.
   * @return
   */
  public static PuzzleState randomState()
  {
    int[] startConfig = null;
    do
    {
      startConfig = randomPermutation(WIDTH * HEIGHT);

    } while (!isSolvable(startConfig));
    return new PuzzleState(startConfig);
  }
  
  @Override
  public int hashCode()
  {
    int ret = 1;
    for (int i = 0; i < SIZE; ++i)
    {
      ret = ret * 31 + s[i];
    }
    return ret;
  }
  
  @Override
  public String toString()
  {
    return Arrays.toString(s);
  }
  
  /**
   * Exchange two elements of the given array.
   * @param a
   * @param i
   * @param j
   */
  private static void swap(int[] a, int i, int j)
  {
    int temp = a[i];
    a[i] = a[j];
    a[j] = temp;
  }
  
  /**
   * Returns the position of the zero (empty square) in this state.
   * @return
   */
  private int pos()
  {
    for (int i = 0; i < SIZE; ++i)
    {
      if (s[i] == 0) return i;
    }
    throw new IllegalStateException("No zero in State: " + s.toString());
  }
  
  /**
   * Returns a random permutation of integers 0 through size - 1
   * @param size
   * @return
   */
  private static int[] randomPermutation(int size)
  {
    Random gen = new Random();
    int[] ret = new int[size];
    for (int i = 0; i < size; ++i)
    {
      ret[i] = i;
    }
    for (int i = size - 1; i > 0; --i)
    {
      int index = gen.nextInt(i + 1);
      int temp = ret[index];
      ret[index] = ret[i];
      ret[i] = temp;
    }
    return ret;
  }
  
  /**
   * Determines whether the given state is solvable.
   * @param p
   * @return
   */
  public static boolean isSolvable(int[] p)
  {
    boolean permutationIsEven = isEven(p);
    boolean scoreIsEven = (scoreOfEmptySquare(p)) % 2 == 0;
    return (permutationIsEven && scoreIsEven) || (!permutationIsEven && !scoreIsEven);
  }
  
  /**
   * Determines whether the given permutation is even.
   * @param p
   * @return
   */
  private static boolean isEven(int[] p)
  {
    int[] indexOf = new int[p.length];
    for (int i = 1; i < p.length; ++i)
    {
      indexOf[i] = i - 1;
    }
    indexOf[0] = p.length - 1;
    boolean[] seen = new boolean[p.length];
    int evenCycleCount = 0;
    while (true)
    {
      int currentCycleLength = 1;
      
      // find next element of ordered list we haven't seen
      int i = 0;
      while (i < p.length && seen[indexOf[i]])
      {
        ++i;
      }
      if (i == p.length)
      {
        break;
      }
      int start = i;
      //System.out.println("start " + start);
      seen[indexOf[i]] = true;
      int current = p[indexOf[i]];
      while (current != start)
      {
        //System.out.println("current " + current);
        ++currentCycleLength;
        seen[indexOf[current]] = true;
        current = p[indexOf[current]];
      }
      //System.out.println("length " + currentCycleLength);
      if (currentCycleLength % 2 == 0)
      {
        ++evenCycleCount;
      }
    }
    
    return evenCycleCount % 2 == 0;
  }
  
  /**
   * Returns the manhattan distance of the zero (empty square)
   * from the lower right corner.
   * @param s
   * @return
   */
  private static int scoreOfEmptySquare(int[] s)
  {
    int pos = SIZE;
    for (int i = 0; i < SIZE; ++i)
    {
      if (s[i] == 0)
      {
        pos = i;
        break;
      }
    }
    int x = pos % WIDTH;
    int y = pos / WIDTH;
    int targetX = WIDTH - 1;
    int targetY = HEIGHT - 1;
    return Math.abs(x - targetX) + Math.abs(y - targetY);
  }

}
