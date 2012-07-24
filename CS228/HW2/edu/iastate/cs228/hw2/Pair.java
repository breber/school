package edu.iastate.cs228.hw2;

/**
 * Represents a pair of Objects of the same type.
 * 
 * @author Brian Reber
 */
public class Pair<T> 
{
	/**
	 * The first element in the pair
	 */
	private T first;
	/**
	 * The second element in the pair
	 */
	private T second;
	
	/**
	 * Creates a new Pair with the 2 given items
	 *  
	 * @param first
	 * First element in the pair
	 * @param second
	 * Second element in the pair
	 */
	public Pair(T first, T second) 
	{
		this.first = first;
		this.second = second;
	}

	/**
	 * Gets the first element in the pair.
	 * 
	 * @return the first element in the pair
	 */
	public T getFirst() 
	{
		return first;
	}

	/**
	 * Sets the first element in the pair.
	 * 
	 * @param first 
	 * The item to set as the first element in the pair
	 */
	public void setFirst(T first) 
	{
		this.first = first;
	}

	/**
	 * Gets the second element in the pair.
	 * 
	 * @return the second element in the pair
	 */
	public T getSecond() 
	{
		return second;
	}

	/**
	 * Sets the second element in the pair.
	 * 
	 * @param second 
	 * The item to set as the second element in the pair
	 */
	public void setSecond(T second) 
	{
		this.second = second;
	}
}
