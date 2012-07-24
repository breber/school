package edu.iastate.cs228.hw2;
import java.util.Comparator;

/**
 * This class encapsulates the configuration and instrumentation for a 
 * sorting utility based on the quicksort algorithm.
 * 
 * @author Implemented by Brian Reber
 */
public class QuickSorter<T>
{
	/**
	 * The Comparator to use to compare elements in the array
	 */
	private Comparator<T> comparator;
	/**
	 * The IPivotStrategy used to choose a pivot point 
	 */
	private IPivotStrategy<T> strategy;
	/**
	 * The maximum recursion depth achieved in the most recent call to sort
	 */
	private int recursionDepth;
	/**
	 * The number of swaps used in the most recent call to sort
	 */
	private int numSwaps;
	/**
	 * The number of element comparisons done in the most recent call to sort
	 */
	private int numComparisons;
	/**
	 * Boolean value representing whether three way partitioning is to be used
	 */
	private boolean useThreeWay;

	
	/**
	 * Constructs a QuickSorter that will sort an array in ascending order according
	 * to the given Comparator, using the default pivot selection strategy.
	 * @param comp Comparator to use for sorting
	 */
	public QuickSorter(Comparator<T> comp)
	{
		this(comp, new BasicStrategy<T>());
	}

	/**
	 * Constructs a QuickSorter that will sort an array in ascending order according
	 * to the given Comparator, using the given pivot selection strategy.
	 * @param comp Comparator to use for sorting
	 * @param strategy the pivot selection strategy
	 */
	public QuickSorter(Comparator<T> comp, IPivotStrategy<T> strategy)
	{
		this.comparator = comp;
		this.strategy = strategy;
		recursionDepth = 0;
		numSwaps = 0;
		numComparisons = 0;
		useThreeWay = false;
	}

	/**
	 * Sorts the given array using the quicksort algorithm.
	 * @param arr array to be sorted
	 */
	public void sort(T[] arr)
	{
		recursionDepth = 0;
		numSwaps = 0;
		numComparisons = 0;
		quickSortRec(arr, 0, arr.length - 1, 1);
	}

	/**
	 * Returns the maximum depth of recursion for the most recent
	 * call to sort().
	 * 
	 * @return maximum depth of recursion
	 */
	public int getMaxDepth()
	{
		return recursionDepth;
	}

	/**
	 * Returns the number of comparisons of array elements performed during
	 * the most recent call to sort().
	 * 
	 * @return number of comparisons performed
	 */
	public int getComparisons()
	{
		return numComparisons;
	}

	/**
	 * Returns the number of exchanges of array elements performed during the
	 * most recent call to sort().  This value includes an approximation
	 * of the number of exchanges performed during insertion sort operations.
	 * 
	 * @return number of exchanges of array elements
	 */
	public int getSwaps()
	{
		return numSwaps;
	}

	/**
	 * Sets this sorter to use three-way partitioning.
	 * 
	 * @param useThreeWayPartition true to use three-way partitioning, 
	 *   false to use normal partitioning
	 */
	public void setUseThreeWay(boolean useThreeWayPartition)
	{
		useThreeWay = useThreeWayPartition;
	}

	/**
	 * Returns true if this sorter is currently configured to use
	 * three-way partitioning, false otherwise.
	 * 
	 * @return true if three-way partitioning is being used, false otherwise
	 */
	public boolean getUseThreeWay()
	{
		return useThreeWay;
	}

	/**
	 * Sorts the subarray consisting of positions first through last.
	 * 
	 * @param arr array to be sorted
	 * @param first index of first position in subarray
	 * @param last index of last position in subarray
	 * @param depth depth of recursion prior to this call
	 */
	private void quickSortRec(T[] arr, int first, int last, int depth)
	{
		if (last < first + 1)
		{
			return;
		}

		int pivot = 0;
		if (last - first < strategy.minLength())
		{
			int tempComparisons = SortUtil.insertionSort(arr, first, last, comparator);
			numComparisons += tempComparisons;
			numSwaps += (last - first - 1 + tempComparisons) / 3;
			return;
		}
		if (useThreeWay)
		{
			Pair<Integer> pivotPoint = partitionThreeWay(arr, first, last);
			quickSortRec(arr, first, pivotPoint.getFirst() - 1, depth + 1);
			quickSortRec(arr, pivotPoint.getSecond() + 1, last, depth + 1);
		}
		else
		{
			pivot = partitionNormal(arr, first, last);
			quickSortRec(arr, first, pivot, depth + 1);
			quickSortRec(arr, pivot + 1, last, depth + 1);
		}

		if (depth + 1 > recursionDepth)
		{
			recursionDepth = depth + 1;
		}
	}

	/**
	 * Partitions the array using the normal 2 part strategy.
	 * 
	 * @param arr
	 * The array to partition
	 * @param start
	 * The starting index of the subarray to partition
	 * @param end
	 * The ending index of the subarray to partition
	 * @return
	 * The index at which the pivot element has been put
	 */
	public int partitionNormal(T[] arr, int start, int end) 
	{
		int indexPivot = strategy.indexOfPivotElement(arr, start, end, comparator);
		numComparisons += strategy.getComparisons();
		numSwaps += strategy.getSwaps();
		T pivot = arr[indexPivot];
		int left = start;
		int right = end;

		while (left < right) 
		{
			while (left < end && comparator.compare(arr[left], pivot) <= 0) 
			{
				numComparisons++;
				left++;
			}
			while (comparator.compare(arr[right], pivot) > 0)
			{
				numComparisons++;
				right--;
			}
			if (left == right && arr[right].equals(pivot))
			{
				right--;
			}
			if (left < right)
			{
				swap(arr, left, right);
			}
		}

		swap(arr, start, right);
		
		return right;
	}

	/**
	 * Partitions the array using the a 3 part strategy.
	 * 
	 * @param arr
	 * The array to partition
	 * @param start
	 * The starting index of the subarray to partition
	 * @param end
	 * The ending index of the subarray to partition
	 * @return
	 * The Pair of indices representing the portion of the subarray that
	 * is equal to the pivot
	 */
	public Pair<Integer> partitionThreeWay(T[] arr, int start, int end) 
	{
		T pivot = arr[strategy.indexOfPivotElement(arr, start, end, comparator)];
		numComparisons += strategy.getComparisons();
		numSwaps += strategy.getSwaps();
		int i = start;
		int firstPoint = start;
		int secondPoint = end;

		while (i <= secondPoint) 
		{
			if (comparator.compare(arr[i], pivot) < 0)
			{
				swap(arr, i, firstPoint);
				firstPoint++;
				i++;
				numComparisons++;
			}
			else if (comparator.compare(arr[i], pivot) > 0)
			{
				swap(arr, i, secondPoint);
				secondPoint--;
				numComparisons++;
			}
			else
			{
				i++;
			}
		}

		return new Pair<Integer>(firstPoint, secondPoint);
	}

	/**
	 * Swaps the elements at the given indices and increases the swap count
	 * for this QuickSorter
	 * 
	 * @param arr
	 * The array on which to do the swapping
	 * @param loc1
	 * The first index
	 * @param loc2
	 * The second index
	 */
	private void swap(T[] arr, int loc1, int loc2)
	{
		T temp = arr[loc1];
		arr[loc1] = arr[loc2];
		arr[loc2] = temp;
		numSwaps++;
	}
}
