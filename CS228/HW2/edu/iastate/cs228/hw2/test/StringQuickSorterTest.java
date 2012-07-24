package edu.iastate.cs228.hw2.test;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
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
public class StringQuickSorterTest {
	
	private String[] strings = {"CS228", "CS227", "Brian", "Reber", "Apple", "Zebra", "Test", "Brain", "BRIAN", "Java"};
	private QuickSorter<String> sorter;
	
	private Comparator<String> natural = new Comparator<String>() {
		@Override
		public int compare(String o1, String o2) {
			return o1.compareTo(o2);
		}
	};
	
	private Comparator<String> reverse = new Comparator<String>() {
		@Override
		public int compare(String o1, String o2) {
			return -o1.compareTo(o2);
		}
	};
	
	
	@Test
	public final void testSortNaturalOrderBasicStrategyNormalPartition() throws StackOverflowError {
		sorter = new ComparableQuickSorter<String>();
		sorter.setUseThreeWay(false);

		sorter.sort(strings);
		System.out.println(Arrays.toString(strings));
		assertTrue(Utilities.isSorted(strings, natural));
	}
	
	@Test
	public final void testSortNaturalOrderMedianStrategyNormalPartition() throws StackOverflowError {
		sorter = new ComparableQuickSorter<String>(new MedianStrategy<String>(3, 10, new Random(41)));
		sorter.setUseThreeWay(false);

		sorter.sort(strings);
		System.out.println(Arrays.toString(strings));
		assertTrue(Utilities.isSorted(strings, natural));
	}
	
	@Test
	public final void testSortNaturalOrderBasicStrategyThreeWayPartition() throws StackOverflowError {
		sorter = new ComparableQuickSorter<String>();
		sorter.setUseThreeWay(true);

		sorter.sort(strings);
		System.out.println(Arrays.toString(strings));
		assertTrue(Utilities.isSorted(strings, natural));
	}
	
	@Test
	public final void testSortNaturalOrderMedianStrategyThreeWayPartition() throws StackOverflowError {
		sorter = new ComparableQuickSorter<String>(new MedianStrategy<String>(3, 10, new Random(41)));
		sorter.setUseThreeWay(true);

		sorter.sort(strings);
		System.out.println(Arrays.toString(strings));
		assertTrue(Utilities.isSorted(strings, natural));
	}
	
	//REVERSE
	
	@Test
	public final void testSortReverseOrderBasicStrategyNormalPartition() throws StackOverflowError {
		sorter = new QuickSorter<String>(reverse);
		sorter.setUseThreeWay(false);

		sorter.sort(strings);
		System.out.println(Arrays.toString(strings));
		assertTrue(Utilities.isSorted(strings, reverse));
	}
	
	@Test
	public final void testSortReverseOrderMedianStrategyNormalPartition() throws StackOverflowError {
		sorter = new QuickSorter<String>(reverse, new MedianStrategy<String>(3, 10, new Random(41)));
		sorter.setUseThreeWay(false);

		sorter.sort(strings);
		System.out.println(Arrays.toString(strings));
		assertTrue(Utilities.isSorted(strings, reverse));
	}
	
	@Test
	public final void testSortReverseOrderBasicStrategyThreeWayPartition() throws StackOverflowError {
		sorter = new QuickSorter<String>(reverse);
		sorter.setUseThreeWay(true);
		
		sorter.sort(strings);
		System.out.println(Arrays.toString(strings));
		assertTrue(Utilities.isSorted(strings, reverse));
	}
	
	@Test
	public final void testSortReverseOrderMedianStrategyThreeWayPartition() throws StackOverflowError {
		sorter = new QuickSorter<String>(reverse, new MedianStrategy<String>(3, 10, new Random(41)));
		sorter.setUseThreeWay(true);

		sorter.sort(strings);
		System.out.println(Arrays.toString(strings));
		assertTrue(Utilities.isSorted(strings, reverse));
	}

}