package edu.iastate.cs228.hw3.test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.ListIterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.iastate.cs228.hw3.PriorityList;

public class PriorityListTest2 {
	
	PriorityList<Integer> list;
	
	@Before
	public void setUp() throws Exception {
		list = new PriorityList<Integer>(1);
		list.add(0);
		list.add(1);
		list.add(2);
		list.add(4);
		list.add(5);
		list.add(3,3);
	}

	@After
	public void tearDown() throws Exception {
		list = null;
	}
	
	@Test
	public void getTest() {
		assertEquals((Integer) 0, list.get(0));
		assertEquals((Integer)1, list.get(1));
		assertEquals((Integer)2, list.get(2));
		assertEquals((Integer)3, list.get(3));
		assertEquals((Integer)4, list.get(4));
		assertEquals((Integer)5, list.get(5));
	}

	@Test
	public void containsTest() {
		assertTrue(list.contains((Integer) 1));
		assertTrue(list.contains((Integer) 4));
		assertFalse(list.contains(new Integer(7)));
		assertFalse(list.contains(null));
		assertFalse(list.contains("1"));
		list.add(null);
		assertTrue(list.contains(null));
	}
	
	@Test
	public void indexOfTest() {
		assertEquals(0, list.indexOf((Integer) 0));
		assertEquals(5, list.indexOf((Integer) 5));
		assertEquals(-1,list.indexOf((Integer) 71));
	}
	
	@Test
	public void clearAndSizeTest() {
		assertFalse(list.isEmpty());
		assertEquals(6, list.size());
		list.clear();
		assertTrue(list.isEmpty());
		assertEquals(0, list.size());
	}
	
	@Test
	public void maxTest() {
		assertEquals(0, list.getMaxPriority());
	}
	
	@Test
	public void addCollection() {
		ArrayList<Integer> arrayList = new ArrayList<Integer>();
		arrayList.add(6);
		arrayList.add(7);
		arrayList.add(8);
		list.addAll(arrayList);
		assertEquals(9, list.size());
		assertEquals(7, list.indexOf(7));
		//list.addAll(4, arrayList);
		assertEquals((Integer) 8, list.get(8));
		//assertEquals(10, list.size());
	}
	
	@Test
	public void setTest() {
		list.set((Integer) 0, 6);
		assertEquals(0, list.indexOf(6));
		assertFalse(list.contains("Alice"));
		assertEquals(6, list.size());
		list.set((Integer) 2, 7);
		assertTrue(list.contains(7));
		assertFalse(list.contains(8));
	}
	
    @Test(expected=IndexOutOfBoundsException.class)
    public void testIndexOutOfBoundsException() {
    	list.remove(8);
    }
	
	@Test
	public void removeTest() {
		assertTrue(list.remove((Integer) 0));
		assertFalse(list.remove((Integer) 0));
		assertEquals(5, list.size());
		assertEquals((Integer) 1, list.remove(0));
		assertEquals(4, list.size());
	}
	
	/**
	 * Performs tests with the listIterator functions.
	 */
	@Test
	public void basicIteratorTest() {
		ListIterator<Integer> iter = list.listIterator();
		assertFalse(iter.hasPrevious());
		assertTrue(iter.hasNext());
		assertEquals(0, iter.nextIndex());
		assertEquals(-1, iter.previousIndex());
		assertEquals(iter.next(), iter.previous());
		iter.remove();
		assertEquals(5, list.size());
		assertFalse(list.contains(0));
		assertFalse(iter.hasPrevious());
		assertTrue(iter.next().equals(1));
		assertEquals(1, iter.nextIndex());
		iter.add((Integer) 0);
		assertEquals(2, iter.nextIndex());
		assertEquals((Integer) 0, iter.previous());
	}

	/**
	 * Test listIterator for an empty list.
	 */
	@Test
	public void basicIteratorTest2() {
		list.clear();
		ListIterator<Integer> iter = list.listIterator();
		assertFalse(iter.hasNext());
		assertFalse(iter.hasPrevious());
		assertEquals(0, iter.nextIndex());
		assertEquals(-1, iter.previousIndex());
	}

	/**
	 * Tests listIterator when cursor is initially placed at the end of the
	 * list.
	 */
	@Test
	public void advancedIteratorTest3() {
		ListIterator<Integer> iter = list.listIterator(6);
		assertFalse(iter.hasNext());
		assertEquals((Integer)5, iter.previous());
		assertEquals(iter.nextIndex(), list.indexOf((Integer) 5));
	}

}