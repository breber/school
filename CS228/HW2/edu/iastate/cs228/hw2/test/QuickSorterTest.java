package edu.iastate.cs228.hw2.test;

import static org.junit.Assert.assertTrue;

import java.util.Comparator;
import java.util.Random;

import org.junit.Test;

import edu.iastate.cs228.hw2.ComparableQuickSorter;
import edu.iastate.cs228.hw2.MedianStrategy;
import edu.iastate.cs228.hw2.QuickSorter;

/**
 * A test suite for the QuickSorter class.
 * Tests mny different strategies, sizes of arrays,
 * and partitioning methods.
 * 
 * @author Brian Reber
 */
public class QuickSorterTest {
	
	private int[] seeds = {5, 50, 35, 75, 45, 100, 500, 43};
	private int[] sizes = {10, 20, 30, 100, 1000, 10000, 100000, 1000000};
	private int[] nums =  {5, 50, 30, 250, 250, 5000, 75000, 100000};
	private QuickSorter<Integer> sorter;
	private Random rand;
	
	private Comparator<Integer> natural = new Comparator<Integer>() {
		@Override
		public int compare(Integer o1, Integer o2) {
			return o1.compareTo(o2);
		}
	};
	
	private Comparator<Integer> reverse = new Comparator<Integer>() {
		@Override
		public int compare(Integer o1, Integer o2) {
			return -o1.compareTo(o2);
		}
	};
	
	
	@Test
	public final void testSortNaturalOrderBasicStrategyNormalPartition() throws StackOverflowError {
		sorter = new ComparableQuickSorter<Integer>();
		sorter.setUseThreeWay(false);
		
		for (int i = 0; i < seeds.length; i++) {
			rand = new Random(seeds[i]);
			Integer[] arr = Utilities.getRandomArrayOfIntegers(rand, sizes[i], nums[i]);
			
			try {
				sorter.sort(arr);
				System.out.println("Comparisons: " + sorter.getComparisons() + " Swaps: " + sorter.getSwaps() + " Recursion Depth: " + sorter.getMaxDepth());
				assertTrue(Utilities.isSorted(arr, natural));
			} catch (StackOverflowError e) {
				throw new StackOverflowError("Failed in array of size " + sizes[i] + " with seed " + seeds[i] + " and range " + nums[i]);
			}
		}
	}
	
	@Test
	public final void testSortNaturalOrderMedianStrategyNormalPartition() throws StackOverflowError {
		
		for (int i = 0; i < seeds.length; i++) {
			sorter = new ComparableQuickSorter<Integer>(new MedianStrategy<Integer>(3, 10, new Random(seeds[i])));
			sorter.setUseThreeWay(false);
			
			rand = new Random(seeds[i]);
			Integer[] arr = Utilities.getRandomArrayOfIntegers(rand, sizes[i], nums[i]);
			
			try {
				sorter.sort(arr);
				System.out.println("Comparisons: " + sorter.getComparisons() + " Swaps: " + sorter.getSwaps() + " Recursion Depth: " + sorter.getMaxDepth());
				assertTrue(Utilities.isSorted(arr, natural));
			} catch (StackOverflowError e) {
				throw new StackOverflowError("Failed in array of size " + sizes[i] + " with seed " + seeds[i] + " and range " + nums[i]);
			}
		}
	}
	
	@Test
	public final void testSortNaturalOrderBasicStrategyThreeWayPartition() throws StackOverflowError {
		sorter = new ComparableQuickSorter<Integer>();
		sorter.setUseThreeWay(true);
		
		for (int i = 0; i < seeds.length; i++) {
			rand = new Random(seeds[i]);
			Integer[] arr = Utilities.getRandomArrayOfIntegers(rand, sizes[i], nums[i]);
			
			try {
				sorter.sort(arr);
				System.out.println("Comparisons: " + sorter.getComparisons() + " Swaps: " + sorter.getSwaps() + " Recursion Depth: " + sorter.getMaxDepth());
				assertTrue(Utilities.isSorted(arr, natural));
			} catch (StackOverflowError e) {
				throw new StackOverflowError("Failed in array of size " + sizes[i] + " with seed " + seeds[i] + " and range " + nums[i]);
			}
		}
	}
	
	@Test
	public final void testSortNaturalOrderMedianStrategyThreeWayPartition() throws StackOverflowError {
		
		for (int i = 0; i < seeds.length; i++) {
			sorter = new ComparableQuickSorter<Integer>(new MedianStrategy<Integer>(3, 10, new Random(seeds[i])));
			sorter.setUseThreeWay(true);
			
			rand = new Random(seeds[i]);
			Integer[] arr = Utilities.getRandomArrayOfIntegers(rand, sizes[i], nums[i]);
			
			try {
				sorter.sort(arr);
				System.out.println("Comparisons: " + sorter.getComparisons() + " Swaps: " + sorter.getSwaps() + " Recursion Depth: " + sorter.getMaxDepth());
				assertTrue(Utilities.isSorted(arr, natural));
			} catch (StackOverflowError e) {
				throw new StackOverflowError("Failed in array of size " + sizes[i] + " with seed " + seeds[i] + " and range " + nums[i]);
			}
		}
	}
	
	@Test
	public final void testSortNaturalOrderBasicStrategyNormalPartitionSorted() throws StackOverflowError {
		sorter = new ComparableQuickSorter<Integer>();
		sorter.setUseThreeWay(false);

		for (int i = 0; i < seeds.length; i++) {
			rand = new Random(seeds[i]);
			Integer[] arr = Utilities.getRandomArrayOfIntegers(rand, sizes[i], nums[i]);
			sorter.sort(arr);
			
			if (Utilities.isSorted(arr, natural)) {
				try {
					sorter.sort(arr);
					System.out.println("Comparisons: " + sorter.getComparisons() + " Swaps: " + sorter.getSwaps() + " Recursion Depth: " + sorter.getMaxDepth());
					assertTrue(Utilities.isSorted(arr, natural));
				} catch (StackOverflowError e) {
					throw new StackOverflowError("Failed in array of size " + sizes[i] + " with seed " + seeds[i] + " and range " + nums[i]);
				}
			}
		}
	}
	
	@Test
	public final void testSortNaturalOrderMedianStrategyNormalPartitionSorted() throws StackOverflowError {
		
		for (int i = 0; i < seeds.length; i++) {
			rand = new Random(seeds[i]);
			Integer[] arr = Utilities.getRandomArrayOfIntegers(rand, sizes[i], nums[i]);
			sorter = new ComparableQuickSorter<Integer>();
			sorter.setUseThreeWay(true);
			sorter.sort(arr);
			
			
			sorter = new ComparableQuickSorter<Integer>(new MedianStrategy<Integer>(3, 10, new Random(seeds[i])));
			sorter.setUseThreeWay(false);
			if (Utilities.isSorted(arr, natural)) {
				try {
					sorter.sort(arr);
					System.out.println("Comparisons: " + sorter.getComparisons() + " Swaps: " + sorter.getSwaps() + " Recursion Depth: " + sorter.getMaxDepth());
					assertTrue(Utilities.isSorted(arr, natural));
				} catch (StackOverflowError e) {
					throw new StackOverflowError("Failed in array of size " + sizes[i] + " with seed " + seeds[i] + " and range " + nums[i]);
				}
			}
		}
	}	
	
	@Test
	public final void testSortNaturalOrderBasicStrategyThreeWayPartitionSorted() throws StackOverflowError {
		sorter = new ComparableQuickSorter<Integer>();
		sorter.setUseThreeWay(true);

		for (int i = 0; i < seeds.length; i++) {
			rand = new Random(seeds[i]);
			Integer[] arr = Utilities.getRandomArrayOfIntegers(rand, sizes[i], nums[i]);
			sorter.sort(arr);
			
			if (Utilities.isSorted(arr, natural)) {
				try {
					sorter.sort(arr);
					System.out.println("Comparisons: " + sorter.getComparisons() + " Swaps: " + sorter.getSwaps() + " Recursion Depth: " + sorter.getMaxDepth());
					assertTrue(Utilities.isSorted(arr, natural));
				} catch (StackOverflowError e) {
					throw new StackOverflowError("Failed in array of size " + sizes[i] + " with seed " + seeds[i] + " and range " + nums[i]);
				}
			}
		}
	}
	
	@Test
	public final void testSortNaturalOrderMedianStrategyThreeWayPartitionSorted() throws StackOverflowError {
		
		for (int i = 0; i < seeds.length; i++) {
			rand = new Random(seeds[i]);
			Integer[] arr = Utilities.getRandomArrayOfIntegers(rand, sizes[i], nums[i]);
			sorter = new ComparableQuickSorter<Integer>();
			sorter.setUseThreeWay(true);
			sorter.sort(arr);
			
			sorter = new ComparableQuickSorter<Integer>(new MedianStrategy<Integer>(3, 10, new Random(seeds[i])));
			sorter.setUseThreeWay(true);
			
			if (Utilities.isSorted(arr, natural)) {
				try {
					sorter.sort(arr);
					System.out.println("Comparisons: " + sorter.getComparisons() + " Swaps: " + sorter.getSwaps() + " Recursion Depth: " + sorter.getMaxDepth());
					assertTrue(Utilities.isSorted(arr, natural));
				} catch (StackOverflowError e) {
					throw new StackOverflowError("Failed in array of size " + sizes[i] + " with seed " + seeds[i] + " and range " + nums[i]);
				}
			}
		}
	}

	
	//REVERSE
	
	@Test
	public final void testSortReverseOrderBasicStrategyNormalPartition() throws StackOverflowError {
		sorter = new QuickSorter<Integer>(reverse);
		sorter.setUseThreeWay(false);
		
		for (int i = 0; i < seeds.length; i++) {
			rand = new Random(seeds[i]);
			Integer[] arr = Utilities.getRandomArrayOfIntegers(rand, sizes[i], nums[i]);
			
			try {
				sorter.sort(arr);
				System.out.println("Comparisons: " + sorter.getComparisons() + " Swaps: " + sorter.getSwaps() + " Recursion Depth: " + sorter.getMaxDepth());
				assertTrue(Utilities.isSorted(arr, reverse));
			} catch (StackOverflowError e) {
				throw new StackOverflowError("Failed in array of size " + sizes[i] + " with seed " + seeds[i] + " and range " + nums[i]);
			}
		}
	}
	
	@Test
	public final void testSortReverseOrderMedianStrategyNormalPartition() throws StackOverflowError {
		
		for (int i = 0; i < seeds.length; i++) {
			sorter = new QuickSorter<Integer>(reverse, new MedianStrategy<Integer>(3, 10, new Random(seeds[i])));
			sorter.setUseThreeWay(false);
			
			rand = new Random(seeds[i]);
			Integer[] arr = Utilities.getRandomArrayOfIntegers(rand, sizes[i], nums[i]);
			
			try {
				sorter.sort(arr);
				System.out.println("Comparisons: " + sorter.getComparisons() + " Swaps: " + sorter.getSwaps() + " Recursion Depth: " + sorter.getMaxDepth());
				assertTrue(Utilities.isSorted(arr, reverse));
			} catch (StackOverflowError e) {
				throw new StackOverflowError("Failed in array of size " + sizes[i] + " with seed " + seeds[i] + " and range " + nums[i]);
			}
		}
	}
	
	@Test
	public final void testSortReverseOrderBasicStrategyThreeWayPartition() throws StackOverflowError {
		sorter = new QuickSorter<Integer>(reverse);
		sorter.setUseThreeWay(true);
		
		for (int i = 0; i < seeds.length; i++) {
			rand = new Random(seeds[i]);
			Integer[] arr = Utilities.getRandomArrayOfIntegers(rand, sizes[i], nums[i]);
			
			try {
				sorter.sort(arr);
				System.out.println("Comparisons: " + sorter.getComparisons() + " Swaps: " + sorter.getSwaps() + " Recursion Depth: " + sorter.getMaxDepth());
				assertTrue(Utilities.isSorted(arr, reverse));
			} catch (StackOverflowError e) {
				throw new StackOverflowError("Failed in array of size " + sizes[i] + " with seed " + seeds[i] + " and range " + nums[i]);
			}
		}
	}
	
	@Test
	public final void testSortReverseOrderMedianStrategyThreeWayPartition() throws StackOverflowError {
		
		for (int i = 0; i < seeds.length; i++) {
			sorter = new QuickSorter<Integer>(reverse, new MedianStrategy<Integer>(3, 10, new Random(seeds[i])));
			sorter.setUseThreeWay(true);
			
			rand = new Random(seeds[i]);
			Integer[] arr = Utilities.getRandomArrayOfIntegers(rand, sizes[i], nums[i]);
			
			try {
				sorter.sort(arr);
				System.out.println("Comparisons: " + sorter.getComparisons() + " Swaps: " + sorter.getSwaps() + " Recursion Depth: " + sorter.getMaxDepth());
				assertTrue(Utilities.isSorted(arr, reverse));
			} catch (StackOverflowError e) {
				throw new StackOverflowError("Failed in array of size " + sizes[i] + " with seed " + seeds[i] + " and range " + nums[i]);
			}
		}
	}
	
	@Test
	public final void testSortReverseOrderBasicStrategyNormalPartitionSorted() throws StackOverflowError {
		sorter = new QuickSorter<Integer>(reverse);
		sorter.setUseThreeWay(false);

		for (int i = 0; i < seeds.length; i++) {
			rand = new Random(seeds[i]);
			Integer[] arr = Utilities.getRandomArrayOfIntegers(rand, sizes[i], nums[i]);
			sorter.sort(arr);
			
			if (Utilities.isSorted(arr, reverse)) {
				try {
					sorter.sort(arr);
					System.out.println("Comparisons: " + sorter.getComparisons() + " Swaps: " + sorter.getSwaps() + " Recursion Depth: " + sorter.getMaxDepth());
					assertTrue(Utilities.isSorted(arr, reverse));
				} catch (StackOverflowError e) {
					throw new StackOverflowError("Failed in array of size " + sizes[i] + " with seed " + seeds[i] + " and range " + nums[i]);
				}
			}
		}
	}
	
	@Test
	public final void testSortReverseOrderMedianStrategyNormalPartitionSorted() throws StackOverflowError {
		
		for (int i = 0; i < seeds.length; i++) {
			rand = new Random(seeds[i]);
			Integer[] arr = Utilities.getRandomArrayOfIntegers(rand, sizes[i], nums[i]);
			sorter = new ComparableQuickSorter<Integer>();
			sorter.setUseThreeWay(true);
			sorter.sort(arr);
			
			sorter = new QuickSorter<Integer>(reverse, new MedianStrategy<Integer>(3, 10, new Random(seeds[i])));
			sorter.setUseThreeWay(false);
			
			if (Utilities.isSorted(arr, reverse)) {
				try {
					sorter.sort(arr);
					System.out.println("Comparisons: " + sorter.getComparisons() + " Swaps: " + sorter.getSwaps() + " Recursion Depth: " + sorter.getMaxDepth());
					assertTrue(Utilities.isSorted(arr, reverse));
				} catch (StackOverflowError e) {
					throw new StackOverflowError("Failed in array of size " + sizes[i] + " with seed " + seeds[i] + " and range " + nums[i]);
				}
			}
		}
	}	
	
	@Test
	public final void testSortReverseOrderBasicStrategyThreeWayPartitionSorted() throws StackOverflowError {
		sorter = new QuickSorter<Integer>(reverse);
		sorter.setUseThreeWay(true);

		for (int i = 0; i < seeds.length; i++) {
			rand = new Random(seeds[i]);
			Integer[] arr = Utilities.getRandomArrayOfIntegers(rand, sizes[i], nums[i]);
			sorter.sort(arr);
			
			if (Utilities.isSorted(arr, reverse)) {
				try {
					sorter.sort(arr);
					System.out.println("Comparisons: " + sorter.getComparisons() + " Swaps: " + sorter.getSwaps() + " Recursion Depth: " + sorter.getMaxDepth());
					assertTrue(Utilities.isSorted(arr, reverse));
				} catch (StackOverflowError e) {
					throw new StackOverflowError("Failed in array of size " + sizes[i] + " with seed " + seeds[i] + " and range " + nums[i]);
				}
			}
		}
	}
	
	@Test
	public final void testSortReverseOrderMedianStrategyThreeWayPartitionSorted() throws StackOverflowError {
		
		for (int i = 0; i < seeds.length; i++) {
			rand = new Random(seeds[i]);
			Integer[] arr = Utilities.getRandomArrayOfIntegers(rand, sizes[i], nums[i]);
			sorter = new ComparableQuickSorter<Integer>();
			sorter.setUseThreeWay(true);
			sorter.sort(arr);
			
			sorter = new QuickSorter<Integer>(reverse, new MedianStrategy<Integer>(3, 10, new Random(seeds[i])));
			sorter.setUseThreeWay(true);
			
			if (Utilities.isSorted(arr, reverse)) {
				try {
					sorter.sort(arr);
					System.out.println("Comparisons: " + sorter.getComparisons() + " Swaps: " + sorter.getSwaps() + " Recursion Depth: " + sorter.getMaxDepth());
					assertTrue(Utilities.isSorted(arr, reverse));
				} catch (StackOverflowError e) {
					throw new StackOverflowError("Failed in array of size " + sizes[i] + " with seed " + seeds[i] + " and range " + nums[i]);
				}
			}
		}
	}

}