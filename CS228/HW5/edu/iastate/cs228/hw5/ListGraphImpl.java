
package edu.iastate.cs228.hw5;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import edu.iastate.cs228.hw5.api.ListGraph;

/**
 * Graph implementation based on adjacency lists.
 * 
 * @author Brian Reber
 */
public class ListGraphImpl<E> implements ListGraph<E>
{
	/**
	 * The Map of Vertices to edges
	 */
	private Map<E, Set<Edge<E>>> edgeSets = new HashMap<E, Set<Edge<E>>>();

	@Override
	public boolean addEdge(E u, E v, int weight)
	{
		Set<Edge<E>> edges = edgeSets.get(u);
		for (Edge<E> cur : edges) 
		{
			if (cur.vertex.equals(v) && cur.weight != weight)
			{
				cur.weight = weight;
				return true;
			}
			else if (cur.vertex.equals(v))
			{
				return false;
			}
		}

		// If we got here, then we didn't already have the edge in the Set
		edges.add(new Edge<E>(v, weight));
		return true;
	}

	@Override
	public boolean addVertex(E vertex)
	{
		if (!edgeSets.containsKey(vertex))
		{
			edgeSets.put(vertex, new HashSet<Edge<E>>());
			return true;
		}
		else
		{
			return false;	
		}
	}

	@Override
	public Iterator<E> vertices()
	{
		return edgeSets.keySet().iterator();
	}

	@Override
	public Iterator<edu.iastate.cs228.hw5.api.Graph.Edge<E>> getNeighbors(E vertex)
	{
		return Collections.unmodifiableCollection(edgeSets.get(vertex)).iterator();
	}

	@Override
	public int h(E current, E goal)
	{
		return 0;
	}
}
