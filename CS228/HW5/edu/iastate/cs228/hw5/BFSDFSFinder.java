package edu.iastate.cs228.hw5;

import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import edu.iastate.cs228.hw5.api.Graph;
import edu.iastate.cs228.hw5.api.Graph.Edge;

/**
 * An implementation of a Breadth-First search, as well as a Depth-First
 * search, depending on a flag the user passes in.
 * 
 * @author Brian Reber
 */
public class BFSDFSFinder<E> extends AbstractFinder<E>
{
	/**
	 * The open set (used as a FIFO queue for BFS, a LIFO stack for DFS).
	 */
	private Deque<E> openSet = new LinkedList<E>();
	
	/**
	 * Indicates wether this is a DFS or a BFS
	 */
	private final boolean isDFS;

	/**
	 * Constructs a BFSDFSFinder for the given graph and the
	 * given start and goal nodes.  The goal may be null.
	 * 
	 * @param graph the graph to be searched
	 * @param start the start node
	 * @param goal the goal node
	 * @param isDFS represents whether this search will be depth-first or not
	 */
	public BFSDFSFinder(Graph<E> graph, E start, E goal, boolean isDFS)
	{
		super(graph, start, goal);
		this.isDFS = isDFS;
		openSet.add(start);
	}

	@Override
	public Iterator<E> openSet()
	{
		// Wrap the open set in a read-only view
		return Collections.unmodifiableCollection(openSet).iterator();
	}
	
	@Override
	public E step()
	{
		if (openSet.isEmpty()) 
		{
			done = true;
		}
		if (done) 
		{
			return null;
		}
		
		E current = (isDFS) ? openSet.removeLast() : openSet.remove();
		closedSet.add(current);
		if (current.equals(goal))
		{
			done = true;
			return current;
		}
		
		Iterator<Edge<E>> iter = graph.getNeighbors(current);
		while (iter.hasNext())
		{
			E neighbor = iter.next().vertex;
			if (!neighbor.equals(start) && predMap.get(neighbor) == null)
			{
				openSet.add(neighbor);
				predMap.put(neighbor, current);
			}
		}
		return current;
	}

	/**
	 * Since the algorithm does not maintain a distance map, this
	 * method always returns 0 for reachable vertices.
	 */
	@Override
	public int getDistance(E vertex)
	{
		List<E> path = getPath(vertex);
		if (path.size() == 0)
		{
			return -1;
		}
		return 0;
	}
}