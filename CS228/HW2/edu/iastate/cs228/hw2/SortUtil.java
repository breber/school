package edu.iastate.cs228.hw2;
import java.util.Comparator;

/**
 * Utilities for sorting arrays of objects using the insertion
 * sort algorithm.
 * 
 * @author Implemented by Brian Reber
 */
public class SortUtil
{
	/**
	 * Sorts the given array using the insertion sort algorithm. 
	 * The array is sorted in ascending order according to the given 
	 * Comparator.
	 * 
	 * @param arr the array to be sorted
	 * @param comp the Comparator to use for ordering
	 */
	public static <T> void insertionSort(T[] arr, Comparator< ? super T> comp)
	{
		insertionSort(arr, 0, arr.length - 1, comp);
	}

	/**
	 * Sorts the given array using the insertion sort algorithm.
	 * The array is sorted in ascending order according to the 
	 * natural ordering of type T.
	 * 
	 * @param arr the array to be sorted
	 */
	public static <T extends Comparable<? super T>> void insertionSort(T[] arr)
	{
		Comparator<T> comp = new Comparator<T>() 
		{
			@Override
			public int compare(T o1, T o2) 
			{
				return o1.compareTo(o2);
			}
		};
		
		insertionSort(arr, comp);
	}

	/**
	 * Sorts the subarray between first and last (inclusive) 
	 * using the insertion sort algorithm.  The subarray
	 * is sorted in ascending order according to the given Comparator.
	 * 
	 * @param arr the array to be sorted
	 * @param comp the Comparator to use for ordering
	 * @param first the index of the first element in the subarray (inclusive)
	 * @param last the index of the last element in the subarray to sort (inclusive)
	 * @return the number of comparisons of array elements
	 *   performed during this call
	 */
	public static <T> int insertionSort(T[] arr, int first, int last,
			Comparator<? super T> comp)
	{
		int countComparisons = 0;
		
		//For each position in the array, we want to insert it into the correct place
		for (int i = first; i <= last; i++) 
		{
			//Keep a copy of the current position so we don't write over it
			T temp = arr[i];
			
			int position = i - 1;
			
			//Go through each position earlier in the array than the current position until
			//we find one that is "less" than the current position.  Along the way, we move
			//the cells that are greater up one position
			while (position >= first && comp.compare(arr[position], temp) > 0) 
			{
				arr[position + 1] = arr[position];
				countComparisons++;
				position--;
			}
			
			//count the last compare that doesn't get counted
			countComparisons++;
			
			arr[position + 1] = temp;
		}
		
		return countComparisons;
	}
}