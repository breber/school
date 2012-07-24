package edu.iastate.cs228.hw5;

import edu.iastate.cs228.hw5.api.BidirectionalGraphAnimation;
import edu.iastate.cs228.hw5.api.Graph;
import edu.iastate.cs228.hw5.api.GraphAnimation;
import edu.iastate.cs228.hw5.api.PathFinderFactory;

/**
 * Implementation Choices:
 * In order to easily implement each of the algorithms, I felt it would work best
 * to have an abstract class that contained the common code for all the algorithms
 * including things like the closed set, get path etc.  Then I implemented 2 concrete
 * classes - one for BFS / DFS, and another for Dijkstras / A*.  This made the most
 * sense because the pairs of algorithms are very similar, and only require a simple if
 * statement to make the minor modification necessary to make it run the other algorithm.
 * <br /><br />
 * In the concrete classes, I use a boolean flag to represent which algorithm the current
 * instance is running.  This value gets passed in the constructor.
 * <br /><br />
 * For the bidirectional search, I implemented a single class that takes in 2 instances
 * of GraphAnimation, representing the search starting from the start and goal nodes.
 * 
 * @author Brian Reber
 */
public class PathFinderFactoryImpl implements PathFinderFactory
{
	@Override
	public <E> GraphAnimation<E> createAStarFinder(Graph<E> graph, E start, E goal)
	{
		return new DijkstrasAStarFinder<E>(graph, start, goal, false);
	}

	@Override
	public <E> GraphAnimation<E> createBFSFinder(Graph<E> graph, E start, E goal)
	{
		return new BFSDFSFinder<E>(graph, start, goal, false);
	}

	@Override
	public <E> GraphAnimation<E> createDFSFinder(Graph<E> graph, E start, E goal)
	{
		return new BFSDFSFinder<E>(graph, start, goal, true);
	}

	@Override
	public <E> GraphAnimation<E> createDijkstraFinder(Graph<E> graph, E start,
			E goal)
	{
		return new DijkstrasAStarFinder<E>(graph, start, goal, true);
	}

	@Override
	public <E> BidirectionalGraphAnimation<E> createBidirectionalAStarFinder(
			Graph<E> graph, E start, E goal)
	{
		return new BidirectionalFinder<E>(new DijkstrasAStarFinder<E>(graph, start, goal, false),
				new DijkstrasAStarFinder<E>(graph, goal, start, false));
	}

	@Override
	public <E> BidirectionalGraphAnimation<E> createBidirectionalBFSFinder(
			Graph<E> graph, E start, E goal)
	{
		return new BidirectionalFinder<E>(new BFSDFSFinder<E>(graph, start, goal, false),
				new BFSDFSFinder<E>(graph, goal, start, false));
	}

	@Override
	public <E> BidirectionalGraphAnimation<E> createBidirectionalDFSFinder(
			Graph<E> graph, E start, E goal)
	{
		return new BidirectionalFinder<E>(new BFSDFSFinder<E>(graph, start, goal, true),
				new BFSDFSFinder<E>(graph, goal, start, true));
	}

	@Override
	public <E> BidirectionalGraphAnimation<E> createBidirectionalDijkstraFinder(
			Graph<E> graph, E start, E goal)
	{
		return new BidirectionalFinder<E>(new DijkstrasAStarFinder<E>(graph, start, goal, true),
				new DijkstrasAStarFinder<E>(graph, goal, start, true));
	}
}
