package edu.iastate.cs228.hw3;
import java.util.ListIterator;

/**
 * Operations for a list that is partitioned into sublists associated with 
 * integer priorities.
 */
public interface IPriorityList<E>
{
	/**
	 * Adds the element at the end of the sublist for the
	 * given priority.
	 * @param priority priority of the sublist in which to add the item
	 * @param item the item to be added
	 * @throws IllegalArgumentException
	 *   if the priority is less than zero or greater than
	 *   the maximum priority 
	 */
	void addWithPriority(int priority, E item);

	/**
	 * Removes and returns the element at the beginning of 
	 * the sublist for the given priority.
	 * @param priority priority of the sublist from which to remove an item
	 * @return the element at the head of the sublist for the given priority
	 * @throws NoSuchElementException
	 *   if the sublist for the given priority is empty
	 * @throws IllegalArgumentException
	 *   if the priority is less than zero or greater than
	 *   the maximum priority 
	 */
	E removeWithPriority(int priority);

	/**
	 * Returns the number of elements in the sublist for the given priority.
	 * @param priority priority of the sublist
	 * @return number of elements in the indicated sublist
	 * @throws IllegalArgumentException
	 *   if the priority is less than zero or greater than
	 *   the maximum priority 
	 */
	int sizeWithPriority(int priority);

	/**
	 * Returns a ListIterator for the sublist for the given priority.
	 * @param priority priority of the sublist
	 * @return iterator for the sublist of given priority
	 * @throws IllegalArgumentException
	 *   if the priority is less than zero or greater than
	 *   the maximum priority 
	 */
	ListIterator<E> iteratorWithPriority(int priority);

	/**
	 * Returns the maximum priority level.  
	 * @return the maximum priority level.
	 */
	int getMaxPriority();
}