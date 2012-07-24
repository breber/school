package edu.iastate.cs228.hw5;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.iastate.cs228.hw5.api.Graph;
import edu.iastate.cs228.hw5.api.GraphAnimation;

/**
 * An abstraction of a Finder type.  To reduce code repetition, much of the code needed for a Finder
 * can be extracted out into this AbstractFinder.  This way, the concrete finder classes will only need
 * to implement a couple methods that are specific to the type of finds.
 * 
 * @author Brian Reber
 */
public abstract class AbstractFinder<E> implements GraphAnimation<E>
{
	/**
	 * The graph to be traversed.
	 */
	protected Graph<E> graph;

	/**
	 * The start node vertex.
	 */
	protected E start;

	/**
	 * The goal vertex (may be null).
	 */
	protected E goal;
	
	/**
	 * Predecessors of nodes discovered in the search.  Each node
	 * in the open set or closed set has an entry in this map.
	 * (The predecessor of the start node is null.)
	 */
	protected Map<E, E> predMap = new HashMap<E, E>();

	/**
	 * Vertices in the closed set.
	 */
	protected Set<E> closedSet = new HashSet<E>();

	/**
	 * Flag indicating that the algorithm has terminated, either
	 * by finding a goal node or by visiting all nodes reachable
	 * from the start.
	 */
	protected boolean done = false;
	
	/**
	 * Called from subclasses. Creates a new AbstractFinder given the 
	 * graph, the start vertex and the goal vertex.
	 * 
	 * @param graph
	 * The graph to search
	 * @param start
	 * The starting vertex
	 * @param goal
	 * The goal vertex
	 */
	public AbstractFinder(Graph<E> graph, E start, E goal)
	{
		this.graph = graph;
		this.start = start;
		this.goal = goal;
		predMap.put(start, null);
	}
	
	@Override
	public Collection<E> closedSet()
	{
		return Collections.unmodifiableCollection(closedSet);
	}

	@Override
	public boolean done()
	{
		return done;
	}
	
	@Override
	public E getPredecessor(E vertex)
	{
		return predMap.get(vertex);
	}
	
	@Override
	public List<E> getPath(E vertex)
	{
		E current = vertex;
		List<E> path = new LinkedList<E>();
		path.add(current);
		E pred = null;
		while ((pred = predMap.get(current)) != null)
		{
			path.add(0, pred);
			current = pred;
		}
		if (start.equals(path.get(0)))
		{
			return path;
		}
		else
		{
			// vertex is not reachable, return empty list
			return new LinkedList<E>();
		}
	}
}
