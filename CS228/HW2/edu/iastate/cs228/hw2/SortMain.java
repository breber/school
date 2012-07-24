package edu.iastate.cs228.hw2;

import java.util.Comparator;
import java.util.Random;

/**
 * The main runner class for the Sorting algorithms.
 * 
 * Runs many different combinations of ing algorithms and settings
 * as defined in the spec.
 * 
 * @author Brian Reber
 */
public class SortMain 
{
	/**
	 * A Comparator that compares two items using natural ordering
	 */
	private static Comparator<Integer> natural = new Comparator<Integer>() 
	{
		@Override
		public int compare(Integer o1, Integer o2) {
			return o1.compareTo(o2);
		}
	};
	
	/**
	 * A Comparator that compares two items using reverse ordering
	 */
	private static Comparator<Integer> reverse = new Comparator<Integer>() 
	{
		@Override
		public int compare(Integer o1, Integer o2) {
			return -o1.compareTo(o2);
		}
	};
	
	
	/**
	 * The main method.  Runs many different strategies and partitioning
	 * methods to sort an array of random ints
	 *  
	 * @param args
	 */
	public static void main(String[] args) 
	{
		int sizeOfArray;
		int rangeOfNumbers;
		long seed;

		long elapsed;
		
		try 
		{
			if (args.length != 3) 
			{
				throw new IllegalArgumentException("Wrong number of arguments");
			}
			
			sizeOfArray = Integer.parseInt(args[0]);
			rangeOfNumbers = Integer.parseInt(args[1]);
			seed = Long.parseLong(args[2]);
		} 
		catch (NumberFormatException e) 
		{
			System.out.println(getUsage());
			return;
		} 
		catch (IllegalArgumentException e) 
		{
			System.out.println(getUsage());
			return;
		}

		Random rand = new Random(seed);
		Integer[] ints = null;
		QuickSorter<Integer> sorter;
		
		long start;
		
		//STEP 1
		System.out.println("STEP 1");
		for (int i = 0; i < 2; i++)
		{
			rand = new Random(seed);
			sorter = new ComparableQuickSorter<Integer>();
			ints = getRandomArrayOfIntegers(rand, sizeOfArray, rangeOfNumbers);
			start = System.currentTimeMillis(); 
			try 
			{
				sorter.sort(ints);
			} 
			catch (StackOverflowError e) 
			{
				System.out.println("There was a StackOverflowError. Max depth of recursion = " + sorter.getMaxDepth());
				continue;
			}
			elapsed = System.currentTimeMillis() - start;
			System.out.println("Basic Strategy, " + (sorter.getUseThreeWay() ? "Three Way":"Normal") + 
					" Partitioning, " + elapsed + " ms, Num Comparisons " + sorter.getComparisons() + 
					", Num Swaps " + sorter.getSwaps() + ", Max Recursion Depth " + sorter.getMaxDepth());
		}
		
		//STEP 2
		System.out.println("STEP 2");
		for (int i = 0; i < 2; i++)
		{
			rand.setSeed(seed);
			ints = getRandomArrayOfIntegers(rand, sizeOfArray, rangeOfNumbers);
			sorter = new QuickSorter<Integer>(reverse);
			start = System.currentTimeMillis(); 
			try 
			{
				sorter.sort(ints);
			} 
			catch (StackOverflowError e) 
			{
				System.out.println("There was a StackOverflowError. Max depth of recursion = " + sorter.getMaxDepth());
				continue;
			}
			elapsed = System.currentTimeMillis() - start;
			System.out.println("Basic Strategy, " + (sorter.getUseThreeWay() ? "Three Way":"Normal") + 
					" Partitioning, " + elapsed + " ms, Num Comparisons " + sorter.getComparisons() + 
					", Num Swaps " + sorter.getSwaps() + ", Max Recursion Depth " + sorter.getMaxDepth());
		}
		
		//STEP 3
		System.out.println("STEP 3");
		for (int i = 0; i < 2; i++)
		{
			rand.setSeed(seed);
			ints = getRandomArrayOfIntegers(rand, sizeOfArray, rangeOfNumbers);
			rand.setSeed(seed);
			sorter = new ComparableQuickSorter<Integer>(new MedianStrategy<Integer>(1, 10, rand));
			start = System.currentTimeMillis(); 
			try 
			{
				sorter.sort(ints);
			} 
			catch (StackOverflowError e) 
			{
				System.out.println("There was a StackOverflowError. Max depth of recursion = " + sorter.getMaxDepth());
				continue;
			}
			elapsed = System.currentTimeMillis() - start;
			System.out.println("Median Strategy, " + (sorter.getUseThreeWay() ? "Three Way":"Normal") + 
					" Partitioning, " + elapsed + " ms, Num Comparisons " + sorter.getComparisons() + 
					", Num Swaps " + sorter.getSwaps() + ", Max Recursion Depth " + sorter.getMaxDepth());
		}
		
		//STEP 4
		System.out.println("STEP 4");
		for (int i = 0; i < 2; i++)
		{
			rand.setSeed(seed);
			ints = getRandomArrayOfIntegers(rand, sizeOfArray, rangeOfNumbers);
			sorter = new ComparableQuickSorter<Integer>();
			sorter.setUseThreeWay(true);
			start = System.currentTimeMillis(); 
			try 
			{
				sorter.sort(ints);
			} 
			catch (StackOverflowError e) 
			{
				System.out.println("There was a StackOverflowError. Max depth of recursion = " + sorter.getMaxDepth());
				continue;
			}
			elapsed = System.currentTimeMillis() - start;
			System.out.println("Basic Strategy, " + (sorter.getUseThreeWay() ? "Three Way":"Normal") + 
					" Partitioning, " + elapsed + " ms, Num Comparisons " + sorter.getComparisons() + 
					", Num Swaps " + sorter.getSwaps() + ", Max Recursion Depth " + sorter.getMaxDepth());
		}
		
		//STEP 5
		System.out.println("STEP 5");
		for (int i = 0; i < 2; i++)
		{
			rand.setSeed(seed);
			ints = getRandomArrayOfIntegers(rand, sizeOfArray, rangeOfNumbers);
			rand.setSeed(seed);
			sorter = new ComparableQuickSorter<Integer>(new MedianStrategy<Integer>(1, 10, rand));
			sorter.setUseThreeWay(true);
			start = System.currentTimeMillis(); 
			try 
			{
				sorter.sort(ints);
			} 
			catch (StackOverflowError e) 
			{
				System.out.println("There was a StackOverflowError. Max depth of recursion = " + sorter.getMaxDepth());
				continue;
			}
			elapsed = System.currentTimeMillis() - start;
			System.out.println("Median Strategy, " + (sorter.getUseThreeWay() ? "Three Way":"Normal") + 
					" Partitioning, " + elapsed + " ms, Num Comparisons " + sorter.getComparisons() + 
					", Num Swaps " + sorter.getSwaps() + ", Max Recursion Depth " + sorter.getMaxDepth());
		}
		
		//STEP 6
		System.out.println("STEP 6");
		for (int i = 0; i < 2; i++)
		{
			//Verify the array is sorted before running step 6
			if (!isSorted(ints, natural))
			{
				sorter = new ComparableQuickSorter<Integer>();
				sorter.setUseThreeWay(true);
				sorter.sort(ints);
			}
			
			sorter = new ComparableQuickSorter<Integer>();
			start = System.currentTimeMillis(); 
			try 
			{
				sorter.sort(ints);
			} 
			catch (StackOverflowError e) 
			{
				System.out.println("There was a StackOverflowError. Max depth of recursion = " + sorter.getMaxDepth());
				continue;
			}
			elapsed = System.currentTimeMillis() - start;
			System.out.println("Basic Strategy, " + (sorter.getUseThreeWay() ? "Three Way":"Normal") + 
					" Partitioning, " + elapsed + " ms, Num Comparisons " + sorter.getComparisons() + 
					", Num Swaps " + sorter.getSwaps() + ", Max Recursion Depth " + sorter.getMaxDepth());
		}
		
		//STEP 7
		System.out.println("STEP 7");
		for (int i = 0; i < 2; i++)
		{
			//Verify the array is sorted before running step 7
			if (!isSorted(ints, natural))
			{
				sorter = new ComparableQuickSorter<Integer>();
				sorter.setUseThreeWay(true);
				sorter.sort(ints);
			}
			
			sorter = new ComparableQuickSorter<Integer>();
			sorter.setUseThreeWay(true);
			start = System.currentTimeMillis(); 
			try 
			{
				sorter.sort(ints);
			} 
			catch (StackOverflowError e) 
			{
				System.out.println("There was a StackOverflowError. Max depth of recursion = " + sorter.getMaxDepth());
				continue;
			}
			elapsed = System.currentTimeMillis() - start;
			System.out.println("Basic Strategy, " + (sorter.getUseThreeWay() ? "Three Way":"Normal") + 
					" Partitioning, " + elapsed + " ms, Num Comparisons " + sorter.getComparisons() + 
					", Num Swaps " + sorter.getSwaps() + ", Max Recursion Depth " + sorter.getMaxDepth());
		}
		
		//STEP 8
		System.out.println("STEP 8");
		for (int i = 0; i < 2; i++)
		{
			//Verify the array is sorted before running step 8
			if (!isSorted(ints, natural))
			{
				sorter = new ComparableQuickSorter<Integer>();
				sorter.setUseThreeWay(true);
				sorter.sort(ints);
			}
			
			rand.setSeed(seed);
			sorter = new ComparableQuickSorter<Integer>(new MedianStrategy<Integer>(3, 10, rand));
			start = System.currentTimeMillis(); 
			try 
			{
				sorter.sort(ints);
			} 
			catch (StackOverflowError e) 
			{
				System.out.println("There was a StackOverflowError. Max depth of recursion = " + sorter.getMaxDepth());
				continue;
			}
			elapsed = System.currentTimeMillis() - start;
			System.out.println("Median Strategy, " + (sorter.getUseThreeWay() ? "Three Way":"Normal") + 
					" Partitioning, " + elapsed + " ms, Num Comparisons " + sorter.getComparisons() + 
					", Num Swaps " + sorter.getSwaps() + ", Max Recursion Depth " + sorter.getMaxDepth());
		}
	}
	
	/**
	 * Gets a random array of Integers.
	 * 
	 * @param r
	 * The Random generator to use
	 * @param size
	 * The number of elements to create
	 * @param range
	 * The range of the values of the elements to put in the array
	 * @return
	 * The array of Random Integers
	 */
	private static Integer[] getRandomArrayOfIntegers(Random r, int size, int range) 
	{
		Integer[] arr = new Integer[size];

		for (int i = 0; i < arr.length; i++) 
		{
			arr[i] = r.nextInt(range);
		}

		return arr;
	}

	/**
	 * Checks if the array is sorted using the given comparator
	 * 
	 * @param arr
	 * The array to check
	 * @param comp
	 * The comparator to use to compare elements
	 * @return
	 * true if the array is sorted, false otherwise
	 */
	private static <T> boolean isSorted(T[] arr, Comparator <? super T> comp) 
	{
		for (int i = 1; i < arr.length; i++) 
		{
			if (comp.compare(arr[i - 1], arr[i]) > 0)
				return false;
		}
		
		return true;
	}
	
	/**
	 * Gets the usage text for the main method
	 * 
	 * @return
	 * The string to print out containing the usage information
	 * for the main method
	 */
	private static String getUsage() 
	{
		return "Please provide the correct arguments.\n" +
			"-size : number of elements in the test array\n" +
			"-range : values to be generated for the test array will be in between 0 and range - 1\n" +
			"-seed : seed for the random number generator for creating the test arrays";
	}
}