package edu.iastate.cs228.hw2.test;

import java.util.Comparator;
import java.util.Random;

import edu.iastate.cs228.hw2.ComparableQuickSorter;
import edu.iastate.cs228.hw2.QuickSorter;

public class SortMain {

	public static void main(String[] args) {
		int size = 1000000;
		int range = 10000;
		int seed = 150;

		long elapsed;

		Random rand = new Random((long) seed);
		Integer[] ints = getRandomArrayOfIntegers(rand, size, range);

		QuickSorter<Integer> sorter = new ComparableQuickSorter<Integer>();
		sorter.setUseThreeWay(false);
		elapsed = performSortAndGetTime(ints, sorter);
		System.out.println("Sorted = " + Utilities.isSorted(ints, new Positive<Integer>()));
		
		ints = getRandomArrayOfIntegers(rand, size, range);
		elapsed = performSortAndGetTime(ints, sorter);
		System.out.println("Basic Strategy, Normal Partitioning, " + elapsed + ", Num Comparisons " + sorter.getComparisons() + 
				", Num Swaps " + sorter.getSwaps() + ", Max Recursion Depth " + sorter.getMaxDepth());
		System.out.println("Sorted = " + Utilities.isSorted(ints, new Positive<Integer>()) + "\n");
	}
	
	/**
	 * Performs the search and returns the number of miliseconds it took to complete the sort.
	 * 
	 * @param arr
	 * The array to sort.
	 * @param sorter
	 * The QuickSorter used to perform the sort
	 * @return
	 * The number of milliseconds it took to complete the sort
	 */
	private static long performSortAndGetTime(Integer[] arr, QuickSorter<Integer> sorter) {
		long start = System.currentTimeMillis(); 
		try {
			sorter.sort(arr);
		} catch (StackOverflowError e) {
			System.err.println("There was a StackOverflowError. Max depth of recursion = " + sorter.getMaxDepth());
		}
//		System.out.println(Arrays.toString(arr));

		return System.currentTimeMillis() - start;
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
	private static Integer[] getRandomArrayOfIntegers(Random r, int size, int range) {
		Integer[] arr = new Integer[size];

		for (int i = 0; i < arr.length; i++) {
			arr[i] = r.nextInt(range);
		}

		return arr;
	}

}

class Positive<T extends Comparable<T>> implements Comparator<Integer> {

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Integer o1, Integer o2) {
		return o1.compareTo(o2);
	}
}

class Negative<T extends Comparable<T>> implements Comparator<Integer> {

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Integer o1, Integer o2) {
		return -(o1.compareTo(o2));
	}
}

///**
// * The main runner class for the Sorting algorithms.
// * 
// * Runs many different combinations of ing algorithms and settings
// * as defined in the spec.
// * 
// * @author brianreber
// */
//public class SortMain {
//
//	public static void main(String[] args) {
//		int size;
//		int range;
//		int seed;
//
//		long elapsed;
//		
//		if (args.length != 3) {
//			System.err.println(getUsage());
//			return;
//		}
//
//		try {
//			size = Integer.parseInt(args[0]);
//			range = Integer.parseInt(args[1]);
//			seed = Integer.parseInt(args[2]);
//
//			if (size <= 0) {
//				System.err.println(getUsage());
//				return;
//			}
//		} catch (NumberFormatException e) {
//			System.err.println(getUsage());
//			return;
//		}
//
//		Random rand = new Random((long) seed);
//		Integer[] ints = getRandomArrayOfIntegers(rand, size, range);
//
//		//STEP 1
//		System.out.println("STEP 1");
//		QuickSorter<Integer> sorter = new ComparableQuickSorter<Integer>();
//		sorter.setUseThreeWay(false);
//		elapsed = performSortAndGetTime(ints, sorter);
//		System.out.println("Basic Strategy, Normal Partitioning, " + elapsed + ", Num Comparisons " + sorter.getComparisons() + 
//				", Num Swaps " + sorter.getSwaps() + ", Max Recursion Depth " + sorter.getMaxDepth());
//		System.out.println("Sorted = " + Utilities.isSorted(ints, new Positive<Integer>()));
//		
//		ints = getRandomArrayOfIntegers(rand, size, range);
//		elapsed = performSortAndGetTime(ints, sorter);
//		System.out.println("Basic Strategy, Normal Partitioning, " + elapsed + ", Num Comparisons " + sorter.getComparisons() + 
//				", Num Swaps " + sorter.getSwaps() + ", Max Recursion Depth " + sorter.getMaxDepth());
//		System.out.println("Sorted = " + Utilities.isSorted(ints, new Positive<Integer>()) + "\n");
//		
//		
//		//STEP 2
//		System.out.println("STEP 2");
//		rand.setSeed(seed);
//		ints = getRandomArrayOfIntegers(rand, size, range);
//		sorter = new QuickSorter<Integer>(new Negative<Integer>());
//		sorter.setUseThreeWay(false);
//		elapsed = performSortAndGetTime(ints, sorter);
//		System.out.println("Basic Strategy, Normal Partitioning, " + elapsed + ", Num Comparisons " + sorter.getComparisons() + 
//				", Num Swaps " + sorter.getSwaps() + ", Max Recursion Depth " + sorter.getMaxDepth());
//		System.out.println("Sorted = " + Utilities.isSorted(ints, new Negative<Integer>()));
//
//		ints = getRandomArrayOfIntegers(rand, size, range);
//		elapsed = performSortAndGetTime(ints, sorter);
//		System.out.println("Basic Strategy, Normal Partitioning, " + elapsed + ", Num Comparisons " + sorter.getComparisons() + 
//				", Num Swaps " + sorter.getSwaps() + ", Max Recursion Depth " + sorter.getMaxDepth());
//		System.out.println("Sorted = " + Utilities.isSorted(ints, new Negative<Integer>()) + "\n");
//
//		
//		//STEP 3
//		System.out.println("STEP 3");
//		rand.setSeed(seed);
//		ints = getRandomArrayOfIntegers(rand, size, range);
//		rand.setSeed(seed);
//		sorter = new ComparableQuickSorter<Integer>(new MedianStrategy<Integer>(1, 10, rand));
//		sorter.setUseThreeWay(false);
//		elapsed = performSortAndGetTime(ints, sorter);
//		System.out.println("Median Strategy, Normal Partitioning, " + elapsed + ", Num Comparisons " + sorter.getComparisons() + 
//				", Num Swaps " + sorter.getSwaps() + ", Max Recursion Depth " + sorter.getMaxDepth());
//		System.out.println("Sorted = " + Utilities.isSorted(ints, new Positive<Integer>()));
//
//		rand.setSeed(seed);
//		ints = getRandomArrayOfIntegers(rand, size, range);
//		rand.setSeed(seed);
//		sorter = new ComparableQuickSorter<Integer>(new MedianStrategy<Integer>(1, 10, rand));
//		sorter.setUseThreeWay(false);
//		elapsed = performSortAndGetTime(ints, sorter);
//		System.out.println("Median Strategy, Normal Partitioning, " + elapsed + ", Num Comparisons " + sorter.getComparisons() + 
//				", Num Swaps " + sorter.getSwaps() + ", Max Recursion Depth " + sorter.getMaxDepth());
//		System.out.println("Sorted = " + Utilities.isSorted(ints, new Positive<Integer>()) + "\n");
//		
//		//STEP 4
//		System.out.println("STEP 4");
//		rand.setSeed(seed);
//		ints = getRandomArrayOfIntegers(rand, size, range);
//		sorter = new ComparableQuickSorter<Integer>();
//		sorter.setUseThreeWay(true);
//		elapsed = performSortAndGetTime(ints, sorter);
//		System.out.println("Basic Strategy, Three Way Partitioning, " + elapsed + ", Num Comparisons " + sorter.getComparisons() + 
//				", Num Swaps " + sorter.getSwaps() + ", Max Recursion Depth " + sorter.getMaxDepth());
//		System.out.println("Sorted = " + Utilities.isSorted(ints, new Positive<Integer>()));
//
//		rand.setSeed(seed);
//		ints = getRandomArrayOfIntegers(rand, size, range);
//		sorter = new ComparableQuickSorter<Integer>();
//		sorter.setUseThreeWay(true);
//		elapsed = performSortAndGetTime(ints, sorter);
//		System.out.println("Basic Strategy, Three Way Partitioning, " + elapsed + ", Num Comparisons " + sorter.getComparisons() + 
//				", Num Swaps " + sorter.getSwaps() + ", Max Recursion Depth " + sorter.getMaxDepth());
//		System.out.println("Sorted = " + Utilities.isSorted(ints, new Positive<Integer>()) + "\n");
//		
//		//STEP 5
//		System.out.println("STEP 5");
//		rand.setSeed(seed);
//		ints = getRandomArrayOfIntegers(rand, size, range);
//		rand.setSeed(seed);
//		sorter = new ComparableQuickSorter<Integer>(new MedianStrategy<Integer>(1, 10, rand));
//		sorter.setUseThreeWay(true);
//		elapsed = performSortAndGetTime(ints, sorter);
//		System.out.println("Median Strategy, Three Way Partitioning, " + elapsed + ", Num Comparisons " + sorter.getComparisons() + 
//				", Num Swaps " + sorter.getSwaps() + ", Max Recursion Depth " + sorter.getMaxDepth());
//		System.out.println("Sorted = " + Utilities.isSorted(ints, new Positive<Integer>()));
//
//		rand.setSeed(seed);
//		ints = getRandomArrayOfIntegers(rand, size, range);
//		rand.setSeed(seed);
//		sorter = new ComparableQuickSorter<Integer>(new MedianStrategy<Integer>(1, 10, rand));
//		sorter.setUseThreeWay(true);
//		elapsed = performSortAndGetTime(ints, sorter);
//		System.out.println("Median Strategy, Three Way Partitioning, " + elapsed + ", Num Comparisons " + sorter.getComparisons() + 
//				", Num Swaps " + sorter.getSwaps() + ", Max Recursion Depth " + sorter.getMaxDepth());
//		System.out.println("Sorted = " + Utilities.isSorted(ints, new Positive<Integer>()) + "\n");
//		
//		//STEP 6
//		System.out.println("STEP 6");
//		sorter = new ComparableQuickSorter<Integer>();
//		sorter.setUseThreeWay(false);
//		elapsed = performSortAndGetTime(ints, sorter);
//		System.out.println("Basic Strategy, Normal Partitioning, " + elapsed + ", Num Comparisons " + sorter.getComparisons() + 
//				", Num Swaps " + sorter.getSwaps() + ", Max Recursion Depth " + sorter.getMaxDepth());
//		System.out.println("Sorted = " + Utilities.isSorted(ints, new Positive<Integer>()));
//
//		elapsed = performSortAndGetTime(ints, sorter);
//		System.out.println("Basic Strategy, Normal Partitioning, " + elapsed + ", Num Comparisons " + sorter.getComparisons() + 
//				", Num Swaps " + sorter.getSwaps() + ", Max Recursion Depth " + sorter.getMaxDepth());
//		System.out.println("Sorted = " + Utilities.isSorted(ints, new Positive<Integer>()) + "\n");
//		
//		//STEP 7
//		System.out.println("STEP 7");
//		sorter.setUseThreeWay(true);
//		elapsed = performSortAndGetTime(ints, sorter);
//		System.out.println("Basic Strategy, Three Way Partitioning, " + elapsed + ", Num Comparisons " + sorter.getComparisons() + 
//				", Num Swaps " + sorter.getSwaps() + ", Max Recursion Depth " + sorter.getMaxDepth());
//		System.out.println("Sorted = " + Utilities.isSorted(ints, new Positive<Integer>()));
//
//		elapsed = performSortAndGetTime(ints, sorter);
//		System.out.println("Basic Strategy, Three Way Partitioning, " + elapsed + ", Num Comparisons " + sorter.getComparisons() + 
//				", Num Swaps " + sorter.getSwaps() + ", Max Recursion Depth " + sorter.getMaxDepth());
//		System.out.println("Sorted = " + Utilities.isSorted(ints, new Positive<Integer>()));
//		
//		//STEP 8
//		System.out.println("STEP 8");
//		rand.setSeed(seed);
//		sorter = new ComparableQuickSorter<Integer>(new MedianStrategy<Integer>(3, 10, rand));
//		sorter.setUseThreeWay(false);
//		elapsed = performSortAndGetTime(ints, sorter);
//		System.out.println("Basic Strategy, Normal Partitioning, " + elapsed + ", Num Comparisons " + sorter.getComparisons() + 
//				", Num Swaps " + sorter.getSwaps() + ", Max Recursion Depth " + sorter.getMaxDepth());
//		System.out.println("Sorted = " + Utilities.isSorted(ints, new Positive<Integer>()));
//
//		elapsed = performSortAndGetTime(ints, sorter);
//		System.out.println("Basic Strategy, Normal Partitioning, " + elapsed + ", Num Comparisons " + sorter.getComparisons() + 
//				", Num Swaps " + sorter.getSwaps() + ", Max Recursion Depth " + sorter.getMaxDepth());
//		System.out.println("Sorted = " + Utilities.isSorted(ints, new Positive<Integer>()));
//	}
//	
//	/**
//	 * Performs the search and returns the number of miliseconds it took to complete the sort.
//	 * 
//	 * @param arr
//	 * The array to sort.
//	 * @param sorter
//	 * The QuickSorter used to perform the sort
//	 * @return
//	 * The number of milliseconds it took to complete the sort
//	 */
//	private static long performSortAndGetTime(Integer[] arr, QuickSorter<Integer> sorter) {
//		long start = System.currentTimeMillis(); 
//		try {
//			sorter.sort(arr);
//		} catch (StackOverflowError e) {
//			System.err.println("There was a StackOverflowError. Max depth of recursion = " + sorter.getMaxDepth());
//		}
////		System.out.println(Arrays.toString(arr));
//
//		return System.currentTimeMillis() - start;
//	}
//
//	/**
//	 * Gets a random array of Integers.
//	 * 
//	 * @param r
//	 * The Random generator to use
//	 * @param size
//	 * The number of elements to create
//	 * @param range
//	 * The range of the values of the elements to put in the array
//	 * @return
//	 * The array of Random Integers
//	 */
//	private static Integer[] getRandomArrayOfIntegers(Random r, int size, int range) {
//		Integer[] arr = new Integer[size];
//
//		for (int i = 0; i < arr.length; i++) {
//			arr[i] = r.nextInt(range);
//		}
//
//		return arr;
//	}
//
//	/**
//	 * Gets the usage text for the main method
//	 * 
//	 * @return
//	 * The string to print out containing the usage information
//	 * for the main method
//	 */
//	private static String getUsage() {
//		return "Please provide the correct arguments.\n" +
//			"-size : number of elements in the test array\n" +
//			"-range : values to be generated for the test array will be in between 0 and range - 1\n" +
//			"-seed : seed for the random number generator for creating the test arrays";
//	}
//}
//
//class Positive<T extends Comparable<T>> implements Comparator<Integer> {
//
//	/* (non-Javadoc)
//	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
//	 */
//	@Override
//	public int compare(Integer o1, Integer o2) {
//		return o1.compareTo(o2);
//	}
//}
//
//class Negative<T extends Comparable<T>> implements Comparator<Integer> {
//
//	/* (non-Javadoc)
//	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
//	 */
//	@Override
//	public int compare(Integer o1, Integer o2) {
//		return -(o1.compareTo(o2));
//	}
//}
