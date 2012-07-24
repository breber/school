package edu.iastate.cs228.hw2.test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import edu.iastate.cs228.hw2.MedianStrategy;
import edu.iastate.cs228.hw2.Pair;
import edu.iastate.cs228.hw2.QuickSorter;
import edu.iastate.cs228.hw2.SortUtil;

public class Assignment2Tests 
{
	Random generator = new Random();
	Integer[] arr = new Integer[100000];
	int seed = 57;
	
	Comparator<Integer> comp = new Comparator<Integer>()
	{
		@Override
		public int compare(Integer lhs, Integer rhs)
		{
			return lhs - rhs;
		}
	};
	
	@Before
	public void setUp()
	{
		generator.setSeed(seed);
		for(int i = 0; i< arr.length; i++)
		{
			arr[i] = generator.nextInt(50000);
		}
	}
	
	/**
	 * Test insertion sorting
	 */
	@Test
	public void insertionSortTest()
	{
		generator.setSeed(seed);
		Integer[] insertSortTestArray = new Integer[500];
		for(int i = 0; i< insertSortTestArray.length; i++)
		{
			insertSortTestArray[i] = generator.nextInt(750);
		}
		
		
		SortUtil.insertionSort(insertSortTestArray, 0, insertSortTestArray.length - 1, comp);
		
		for(int i = 1; i < insertSortTestArray.length; i++)
		{
			assertTrue(comp.compare(insertSortTestArray[i-1], insertSortTestArray[i]) <= 0);
		}
	}
	
	/**
	 * Test insertion sort comparison counter
	 */
	@Test
	public void insertionSortCompsTest()
	{
		generator.setSeed(seed);
		Integer[] insertSortTestArray = new Integer[10];
		for(int i = 0; i< insertSortTestArray.length; i++)
		{
			insertSortTestArray[i] = generator.nextInt(30);
		}
		
		int insertSortComps = SortUtil.insertionSort(insertSortTestArray, 0, insertSortTestArray.length - 1, comp);
		assertEquals(27,insertSortComps);
		
		for(int i = 1; i < insertSortTestArray.length; i++)
		{
			assertTrue(comp.compare(insertSortTestArray[i-1], insertSortTestArray[i]) <= 0);
		}
	}
	
	
	@Test
	public void normalPartitionTest()
	{
		//TODO
	}
	
	/**
	 * Test the postcondition of threeWayPartition 
	 */
	@Test
	public void threeWayPartitionTest()
	{			
		QuickSorter<Integer> sorter = new QuickSorter<Integer>(comp);
		Pair<Integer> subArray = sorter.partitionThreeWay(arr , 0 , arr.length-1);
		Integer pivot = arr[subArray.getFirst()];
		
		for(int i = 0; i < subArray.getFirst(); i++)
		{
			assertTrue(comp.compare(arr[i] , pivot) < 0);
		}
		
		for(int i = subArray.getFirst(); i < subArray.getSecond(); i++)
		{
			assertEquals(comp.compare(arr[i], pivot), 0);
		}
		
		for(int i = subArray.getSecond() + 1; i < arr.length; i++)
		{
			assertTrue(comp.compare(arr[i], pivot) > 0);
		}
	}
	
	/**
	 * Test quicksort with basic strategy and normal partitioning
	 */
	@Test 
	public void quickSortNormalBasic()
	{
		QuickSorter<Integer> sorter = new QuickSorter<Integer>(comp);	
		
		try
		{
			sorter.sort(arr);
		}
		catch(StackOverflowError e){}
		
		for(int i = 1; i < arr.length; i++)
		{
			assertTrue(comp.compare(arr[i-1], arr[i]) <= 0);
		}
	}
	
	/**
	 * Test quicksort with basic strategy and three-way partitioning
	 */
	@Test
	public void quickSortThreeWayBasic()
	{
		QuickSorter<Integer> sorter = new QuickSorter<Integer>(comp);
		sorter.setUseThreeWay(true);
		
		try
		{
			sorter.sort(arr);
		}
		catch(StackOverflowError e){}
		
		for(int i = 1; i < arr.length; i++)
		{
			assertTrue(comp.compare(arr[i-1], arr[i]) <= 0);
		}
	}
	
	/**
	 * Test quicksort with median strategy and normal partitioning
	 */
	@Test
	public void quickSortNormalMedian()
	{
		MedianStrategy<Integer> strat = new MedianStrategy<Integer>(7);
		QuickSorter<Integer> sorter = new QuickSorter<Integer>(comp, strat);
		
		try
		{
			sorter.sort(arr);
		}
		catch(StackOverflowError e){}
		
		for(int i = 1; i < arr.length; i++)
		{
			assertTrue(comp.compare(arr[i-1], arr[i]) <= 0);
		}
	}
	
	/**
	 * Test quicksort with median strategy and three-way partitioning
	 */
	@Test
	public void quickSortThreeWayMedian()
	{
		MedianStrategy<Integer> strat = new MedianStrategy<Integer>(7);
		QuickSorter<Integer> sorter = new QuickSorter<Integer>(comp, strat);
		sorter.setUseThreeWay(true);
		
		try
		{
			sorter.sort(arr);
		}
		catch(StackOverflowError e){}
		
		for(int i = 1; i < arr.length; i++)
		{
			assertTrue(comp.compare(arr[i-1], arr[i]) <= 0);
		}
	}
	
	
}