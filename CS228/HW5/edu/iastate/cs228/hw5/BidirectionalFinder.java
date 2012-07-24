package edu.iastate.cs228.hw5;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import edu.iastate.cs228.hw5.api.BidirectionalGraphAnimation;
import edu.iastate.cs228.hw5.api.GraphAnimation;

/**
 * Implementation of a Bidirectional find. Alternates steps between
 * start and end search, and stops when it finds a meeting point.
 * 
 * @author Brian Reber
 */
public class BidirectionalFinder<E> implements BidirectionalGraphAnimation<E> 
{
	/**
	 * The search that starts from the starting vertex
	 */
	private GraphAnimation<E> start;
	/**
	 * The search that starts from the goal vertex
	 */
	private GraphAnimation<E> end;
	/**
	 * The vertex that was last visited
	 */
	private E lastAdded;
	/**
	 * Represents whether it is the starting search's turn
	 */
	private boolean startTurn;
	/**
	 * Represents whether we have found a meeting vertex yet
	 */
	private boolean isDone;
	
	/**
	 * Creates a BidirectionalFinder with the given searches starting from the starting
	 * vertex and the ending vertex
	 * 
	 * @param start
	 * The search starting at the starting vertex
	 * @param end
	 * The search starting at the goal vertex
	 */
	public BidirectionalFinder(GraphAnimation<E> start, GraphAnimation<E> end) 
	{
		isDone = false;
		startTurn = true;
		this.start = start;
		this.end = end;
	}
	
	/* (non-Javadoc)
	 * @see edu.iastate.cs228.hw5.api.GraphAnimation#step()
	 */
	@Override
	public E step() 
	{
		if (isDone) 
		{
			return null;
		}
		
		E ret;
		if (startTurn) 
		{
			ret = start.step();
			
			if (end.closedSet().contains(ret)) 
			{
				isDone = true;
			}
		} 
		else 
		{
			ret = end.step();
			
			if (start.closedSet().contains(ret)) 
			{
				isDone = true;
			}
		}
		startTurn = !startTurn;
		lastAdded = ret;
		return ret;
	}
	
	/* (non-Javadoc)
	 * @see edu.iastate.cs228.hw5.api.GraphAnimation#done()
	 */
	@Override
	public boolean done() 
	{
		return isDone;
	}
	
	/* (non-Javadoc)
	 * @see edu.iastate.cs228.hw5.api.GraphAnimation#getPath(java.lang.Object)
	 */
	@Override
	public List<E> getPath(E vertex) 
	{
		return start.getPath(vertex);
	}
	
	/* (non-Javadoc)
	 * @see edu.iastate.cs228.hw5.api.GraphAnimation#getDistance(java.lang.Object)
	 */
	@Override
	public int getDistance(E vertex) 
	{
		return start.getDistance(vertex);
	}
	
	/* (non-Javadoc)
	 * @see edu.iastate.cs228.hw5.api.GraphAnimation#getPredecessor(java.lang.Object)
	 */
	@Override
	public E getPredecessor(E vertex) 
	{
		return start.getPredecessor(vertex);
	}
	
	/* (non-Javadoc)
	 * @see edu.iastate.cs228.hw5.api.GraphAnimation#closedSet()
	 */
	@Override
	public Collection<E> closedSet() 
	{
		return start.closedSet();
	}
	
	/* (non-Javadoc)
	 * @see edu.iastate.cs228.hw5.api.GraphAnimation#openSet()
	 */
	@Override
	public Iterator<E> openSet() {
		return start.openSet();
	}
	
	/* (non-Javadoc)
	 * @see edu.iastate.cs228.hw5.api.BidirectionalGraphAnimation#getPathReverse(java.lang.Object)
	 */
	@Override
	public List<E> getPathReverse(E vertex) 
	{
		return end.getPath(vertex);
	}
	
	/* (non-Javadoc)
	 * @see edu.iastate.cs228.hw5.api.BidirectionalGraphAnimation#getDistanceReverse(java.lang.Object)
	 */
	@Override
	public int getDistanceReverse(E vertex) 
	{
		return end.getDistance(vertex);
	}
	
	/* (non-Javadoc)
	 * @see edu.iastate.cs228.hw5.api.BidirectionalGraphAnimation#getPredecessorReverse(java.lang.Object)
	 */
	@Override
	public E getPredecessorReverse(E vertex) 
	{
		return end.getPredecessor(vertex);
	}
	
	/* (non-Javadoc)
	 * @see edu.iastate.cs228.hw5.api.BidirectionalGraphAnimation#closedSetReverse()
	 */
	@Override
	public Collection<E> closedSetReverse() 
	{
		return end.closedSet();
	}
	
	/* (non-Javadoc)
	 * @see edu.iastate.cs228.hw5.api.BidirectionalGraphAnimation#openSetReverse()
	 */
	@Override
	public Iterator<E> openSetReverse() 
	{
		return end.openSet();
	}
	
	/* (non-Javadoc)
	 * @see edu.iastate.cs228.hw5.api.BidirectionalGraphAnimation#getCompletePath()
	 */
	@Override
	public List<E> getCompletePath() 
	{
		if (!done())
		{
			return new ArrayList<E>();
		}
		
		LinkedList<E> total = new LinkedList<E>();
		total.addAll(start.getPath(lastAdded));
		List<E> test = end.getPath(lastAdded);

		// We need to add the search from the goal vertex
		// in reverse in order to keep the correct ordering
		// from start node to end node
		for (int i = test.size() - 2; i >= 0; i--) 
		{
			total.add(test.get(i));
		}
		
		return total;
	}
	
	/* (non-Javadoc)
	 * @see edu.iastate.cs228.hw5.api.BidirectionalGraphAnimation#getCompleteDistance()
	 */
	@Override
	public int getCompleteDistance() 
	{
		if (!done()) 
		{
			return -1;
		}
		
		return start.getDistance(lastAdded) + end.getDistance(lastAdded);
	}
}
