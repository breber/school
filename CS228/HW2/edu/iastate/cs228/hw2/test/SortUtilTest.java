package edu.iastate.cs228.hw2.test;

import static org.junit.Assert.*;

import java.util.Comparator;
import java.util.Random;

import org.junit.Test;

import edu.iastate.cs228.hw2.SortUtil;

/**
 * A test suite for the SortUtil class.
 * 
 * @author brianreber
 */
public class SortUtilTest {

	private int[] seeds = {5, 50, 35};
	private int[] sizes = {10, 20, 30};
	private int[] nums =  {5, 50, 30};
	
	@Test
	public final void testInsertionSortTArrayComparatorOfQsuperTNaturalOrder() {
		for (int i = 0; i < sizes.length; i++) {
			Integer[] arr = Utilities.getRandomArrayOfIntegers(new Random(seeds[i]), nums[i], sizes[i]);

			Comparator<Integer> comp = new Comparator<Integer>() {
				@Override
				public int compare(Integer o1, Integer o2) {
					return o1.compareTo(o2);
				}
			};

			SortUtil.insertionSort(arr);

			assertTrue(Utilities.isSorted(arr, comp));
		}
	}
	
	@Test
	public final void testInsertionSortTArrayNaturalOrder() {
		for (int i = 0; i < sizes.length; i++) {
			Integer[] arr = Utilities.getRandomArrayOfIntegers(new Random(seeds[i]), nums[i], sizes[i]);

			Comparator<Integer> comp = new Comparator<Integer>() {
				@Override
				public int compare(Integer o1, Integer o2) {
					return o1.compareTo(o2);
				}
			};

			SortUtil.insertionSort(arr, comp);

			assertTrue(Utilities.isSorted(arr, comp));
		}
	}
	
	@Test
	public final void testInsertionSortTArrayReverseOrder() {
		for (int i = 0; i < sizes.length; i++) {
			Integer[] arr = Utilities.getRandomArrayOfIntegers(new Random(seeds[i]), nums[i], sizes[i]);

			Comparator<Integer> comp = new Comparator<Integer>() {
				@Override
				public int compare(Integer o1, Integer o2) {
					return -o1.compareTo(o2);
				}
			};

			SortUtil.insertionSort(arr, comp);

			assertTrue(Utilities.isSorted(arr, comp));
		}
	}

	@Test
	public final void testInsertionSortTArrayIntIntComparatorOfQsuperTInNaturalOrder() {
		for (int i = 0; i < sizes.length; i++) {
			Integer[] arr = Utilities.getRandomArrayOfIntegers(new Random(seeds[i]), nums[i], sizes[i]);

			Comparator<Integer> comp = new Comparator<Integer>() {
				@Override
				public int compare(Integer o1, Integer o2) {
					return o1.compareTo(o2);
				}
			};

			SortUtil.insertionSort(arr, 0, arr.length - 1, comp);

			assertTrue(Utilities.isSorted(arr, comp));
		}
	}
	
	@Test
	public final void testInsertionSortTArrayIntIntComparatorOfQsuperTInReverseOrder() {
		for (int i = 0; i < sizes.length; i++) {
			Integer[] arr = Utilities.getRandomArrayOfIntegers(new Random(seeds[i]), nums[i], sizes[i]);

			Comparator<Integer> comp = new Comparator<Integer>() {
				@Override
				public int compare(Integer o1, Integer o2) {
					return -o1.compareTo(o2);
				}
			};

			SortUtil.insertionSort(arr, 0, arr.length - 1, comp);

			assertTrue(Utilities.isSorted(arr, comp));
		}
	}
}