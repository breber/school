package edu.iastate.cs228.hw2;

import java.util.Comparator;
import java.util.Random;

/**
 * A strategy for choosing the pivot element for Quick Sort.
 * 
 * This algorithm chooses 'k' random indices in and then finds
 * the median element.
 * 
 * @author Brian Reber
 */
public class MedianStrategy<T> implements IPivotStrategy<T> 
{
	
	/**
	 * The number of indices to randomly choose
	 */
	private int kVal;
	/**
	 * The minimum length of array this strategy should be performed on
	 */
	private int minLength;
	/**
	 * The random generator used to choose the random indieces
	 */
	private Random rand;
	/**
	 * The number of comparisons done by calling indexOfPivotElement
	 */
	private int numComparisons;
	/**
	 * The number of swap done by calling indexOfPivotElement
	 */
	private int numSwaps;
	
	/**
	 * Creates a Median Strategy with the given 'k' value.
	 *  
	 * @param k
	 * The number of random indices to choose
	 */
	public MedianStrategy(int k)
	{
		this(k, k, new Random());
	}
	
	/**
	 * Creates a Median Strategy with the given 'k' value and the
	 * given minLength. The minLength is the shortest subarray this
	 * strategy should be used on.
	 * 
	 * @param k
	 * The number of random indices to choose
	 * @param minLength
	 * The minimum length of the array this strategy should be used on
	 */
	public MedianStrategy(int k, int minLength)
	{
		this(k, minLength, new Random());
	}
	
	/**
	 * Creates a Median Strategy with the given 'k' value, the given 
	 * minimum length of a subarray this strategy should be used on, and
	 * a seed value for the random generator.
	 * 
	 * @param k
	 * The number of random indices to choose
	 * @param minLength
	 * The minimum length of the array this strategy should be used on
	 * @param rand
	 * The Random generator to use
	 */
	public MedianStrategy(int k, int minLength, Random rand)
	{
		if (k < 1 || k % 2 == 0)
		{
			throw new IllegalArgumentException("K should be greater than or equal to 1 and not an even number");
		}
		
		if (minLength < k)
		{
			throw new IllegalArgumentException("minLength should be greater than or equal to k");
		}
		
		this.kVal = k;
		this.minLength = minLength;
		this.rand = rand;
		this.numComparisons = 0;
		this.numSwaps = 0;
	}

	/* (non-Javadoc)
	 * @see edu.iastate.cs228.hw2.IPivotStrategy#indexOfPivotElement(T[], int, int, java.util.Comparator)
	 */
	@Override
	public int indexOfPivotElement(final T[] arr, int first, int last, final Comparator<? super T> comp) 
	{
		numComparisons = 0;
		numSwaps = 0;
		Integer[] indices = new Integer[kVal];
		
		for (int i = 0; i < kVal; i++)
		{
			indices[i] = rand.nextInt(last - first) + first;
		}
		
		Comparator<Integer> c = new Comparator<Integer>() 
		{
			@Override
			public int compare(Integer o1, Integer o2) 
			{
				numComparisons++;
				return comp.compare(arr[o1], arr[o2]);
			}
		};
		
		int tempComparisons = SortUtil.insertionSort(indices, 0, indices.length - 1, c);
		numComparisons += tempComparisons;
		numSwaps += (last - first - 1 + tempComparisons) / 3;
		
		return indices[indices.length / 2];
	}
	
	/* (non-Javadoc)
	 * @see edu.iastate.cs228.hw2.IPivotStrategy#minLength()
	 */
	@Override
	public int minLength() 
	{
		return minLength;
	}

	/* (non-Javadoc)
	 * @see edu.iastate.cs228.hw2.IPivotStrategy#getComparisons()
	 */
	@Override
	public int getComparisons() 
	{
		return numComparisons;
	}

	/* (non-Javadoc)
	 * @see edu.iastate.cs228.hw2.IPivotStrategy#getSwaps()
	 */
	@Override
	public int getSwaps() 
	{
		return numSwaps;
	}
}