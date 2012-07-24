package edu.iastate.cs228.hw5;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;

import edu.iastate.cs228.hw5.api.Graph;
import edu.iastate.cs228.hw5.api.Graph.Edge;

/**
 * An implementation of a Dijkstras search, as well as an A*
 * search, depending on a flag the user passes in.
 * 
 * @author Brian Reber
 */
public class DijkstrasAStarFinder<E> extends AbstractFinder<E> 
{
	/**
	 * A class that holds the score and dist values for a certain vertex
	 * 
	 * @author Brian Reber
	 */
	private class ScoreDist 
	{
		/**
		 * The score of the vertex
		 */
		public Integer score;
		/**
		 * The shortest found distance to this vertex 
		 */
		public Integer dist;
		
		/**
		 * Creates a new ScoreDist with the given score and distance
		 * 
		 * @param score
		 * The score of the vertex
		 * @param dist
		 * The shortest found distance to the vertex
		 */
		public ScoreDist(int score, int dist) 
		{
			this.score = score;
			this.dist = dist;
		}
	}

	/**
	 * The open set, which prioritizes the elements by their score
	 */
	private PriorityQueue<E> openSet = new PriorityQueue<E>(11, new Comparator<E>() 
		{
			@Override
			public int compare(E o1, E o2) {
				return distScore.get(o1).score.compareTo(distScore.get(o2).score);
			}
		}
	);

	/**
	 * Maps a vertex to an instance of ScoreDist used for distance measurements
	 * as well as sorting the priority queue
	 */
	private Map<E, ScoreDist> distScore = new HashMap<E, ScoreDist>();
	
	/**
	 * Indicates wether this is Dijkstras or A*
	 */
	private final boolean isDijkstras;

	/**
	 * Constructs a DijkstrasAStarFinder for the given graph and the
	 * given start and goal nodes.  The goal may be null.
	 * 
	 * @param graph the graph to be searched
	 * @param start the start node
	 * @param goal the goal node
	 * @param isDijkstras represents whether this search will be Dijkstras or not
	 */
	public DijkstrasAStarFinder(Graph<E> graph, E start, E goal, boolean isDijkstras) 
	{
		super(graph, start, goal);
		this.isDijkstras = isDijkstras;
		openSet.add(start);
		distScore.put(openSet.element(), new ScoreDist(0, 0));
	}

	/* (non-Javadoc)
	 * @see edu.iastate.cs228.hw5.api.GraphAnimation#step()
	 */
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

		E current = openSet.poll();

		if (!closedSet.contains(current)) 
		{
			closedSet.add(current);
			if (current.equals(goal)) 
			{
				done = true;
				return current;
			}

			Iterator<Edge<E>> iter = graph.getNeighbors(current);
			while (iter.hasNext())
			{
				Edge<E> edge = iter.next();
				E neighbor = edge.vertex;
				if (!closedSet.contains(neighbor)) 
				{
					int alt = distScore.get(current).dist + edge.weight; 
					if (!distScore.containsKey(neighbor) || alt < distScore.get(neighbor).dist) 
					{
						int score = alt;
						if (!isDijkstras) 
						{
							score += graph.h(neighbor, goal);
						}
						distScore.put(neighbor, new ScoreDist(score, alt));
						openSet.add(neighbor);
						predMap.put(neighbor, current);
					}
				}
			}
			return current;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.iastate.cs228.hw5.api.GraphAnimation#getDistance(java.lang.Object)
	 */
	@Override
	public int getDistance(E vertex) 
	{
		if (distScore.get(vertex) == null) 
		{
			return -1;
		}
		
		return distScore.get(vertex).dist;
	}

	/* (non-Javadoc)
	 * @see edu.iastate.cs228.hw5.api.GraphAnimation#openSet()
	 */
	@Override
	public Iterator<E> openSet() 
	{
		return openSet.iterator();
	}
}
