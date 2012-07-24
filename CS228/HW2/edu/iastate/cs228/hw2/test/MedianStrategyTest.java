package edu.iastate.cs228.hw2.test;
import static org.junit.Assert.assertEquals;

import java.util.Comparator;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import edu.iastate.cs228.hw2.MedianStrategy;


public class MedianStrategyTest
{
	Random rand;
	Integer[] medianTestArray;
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
		rand = new Random(42);
		medianTestArray = new Integer[50];
		for(int i = 0; i < 50; i++)
		{
			medianTestArray[i] = rand.nextInt(100);
		}
	}
	/**
	 * Test MedianStrategy.indexofPivotElement return values
	 */
	@Test
	public void PivotElementTest()
	{	
		MedianStrategy<Integer> strat = new MedianStrategy<Integer>(5,5,rand);
		int median = strat.indexOfPivotElement(medianTestArray, 8, 32, comp);
		assertEquals(18, median);		

		strat = new MedianStrategy<Integer>(3,7,rand);
		median = strat.indexOfPivotElement(medianTestArray, 16, 45, comp);		
		assertEquals(26, median);	
	}

	/**
	 * Test medianStrategy swaps and comps
	 */
	@Test
	public void swapsAndComps()
	{
		MedianStrategy<Integer> strat = new MedianStrategy<Integer>(5,5,rand);
		strat.indexOfPivotElement(medianTestArray, 8, 32, comp);
		assertEquals(6,strat.getComparisons());
		assertEquals(3,strat.getSwaps());

		strat = new MedianStrategy<Integer>(3,7,rand);
		strat.indexOfPivotElement(medianTestArray, 16, 45, comp);	
		assertEquals(3,strat.getComparisons());
		assertEquals(1,strat.getSwaps());
	}

}