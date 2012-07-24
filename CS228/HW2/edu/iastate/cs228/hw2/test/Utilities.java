package edu.iastate.cs228.hw2.test;

import java.util.Comparator;
import java.util.Random;

/**
 * @author brianreber
 *
 */
public class Utilities {

	private Utilities() { }
	
	public static <T> boolean isSorted(T[] arr, Comparator <? super T> comp) {
		
		for (int i = 1; i < arr.length; i++) {
			if (comp.compare(arr[i - 1], arr[i]) > 0)
				return false;
		}
		
		return true;
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
	public static Integer[] getRandomArrayOfIntegers(Random r, int size, int range) {
		Integer[] arr = new Integer[size];

		for (int i = 0; i < arr.length; i++) {
			arr[i] = r.nextInt(range);
		}

		return arr;
	}	
}
