package edu.iastate.cs228.hw3.test;

import java.util.ArrayList;
import java.util.ListIterator;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.iastate.cs228.hw3.PriorityList;

/**
 * Collection of JUnit tests for testing PriorityList implementation.
 * @author Anid Monsur, modifications and additions by Cody Hoover
 *
 */
public class PriorityListTest3 extends TestCase {
	PriorityList<String> list;

	@Before
	public void setUp() throws Exception {
		list = new PriorityList<String>(5);
		list.add("Alice");
		list.add("Bob");
		list.add("Carl");
		list.addWithPriority(4, "Dave");
	}

	@After
	public void tearDown() throws Exception {
		list = null;
	}

	//Checks that get() returns the correct value and does not modify the list.
	@Test
	public final void testgetTest() {
		assertEquals("Carl", list.get(2));
		assertEquals("Carl", list.get(2));
		assertEquals("Bob", list.get(1));
		assertEquals("Alice", list.get(0));
		assertEquals("Dave", list.get(3));
	}

	//Checks that contains() returns the correct value and does not modify the list.
	@Test
	public final void testcontainsTest() {
		assertTrue(list.contains("Bob"));
		assertTrue(list.contains("Alice"));
		assertTrue(list.contains("Carl"));
		assertTrue(list.contains("Dave"));
		assertFalse(list.contains("Alex"));
		assertFalse(list.contains(new Integer(5)));
		assertFalse(list.contains(null));
		list.add(null);
		assertTrue(list.contains(null));
		assertEquals("Carl", list.get(2));
		assertEquals("Carl", list.get(2));
		assertEquals("Bob", list.get(1));
	}

	//Checks that indexOf() returns the correct value and does not modify the list.
	@Test
	public final void testindexOfTest() {
		assertEquals(0, list.indexOf("Alice"));
		assertEquals(1, list.indexOf("Bob"));
		assertEquals(3, list.indexOf("Dave"));
		assertEquals(2, list.indexOf("Carl"));
		assertEquals(-1, list.indexOf("Jeff"));
		assertEquals(-1, list.indexOf(null));
		assertEquals(0, list.indexOf("Alice"));
		assertEquals(1, list.indexOf("Bob"));
		assertEquals(3, list.indexOf("Dave"));
		assertEquals(2, list.indexOf("Carl"));
		assertEquals(-1, list.indexOf("Jeff"));
		assertEquals(-1, list.indexOf(null));
	}

	//Tests that clear() properly clears a list, not modifying max.
	//Also tests that size() and isEmpty() return correct values.
	@Test
	public final void testclearAndSizeTest() {
		assertFalse(list.isEmpty());
		assertEquals(4, list.size());
		list.clear();
		assertTrue(list.isEmpty());
		assertEquals(0, list.size());
		assertEquals(4, list.getMaxPriority());
		list.add("Hi");
		assertFalse(list.isEmpty());
	}

	@Test
	public final void testmaxTest() {
		assertEquals(4, list.getMaxPriority());
	}

	
	@Test
	public final void testsizeAndAddPriorityTest() {
		assertEquals(3, list.sizeWithPriority(0));
		assertEquals(0, list.sizeWithPriority(1));
		assertEquals(0, list.sizeWithPriority(2));
		assertEquals(0, list.sizeWithPriority(3));
		assertEquals(1, list.sizeWithPriority(4));
		list.addWithPriority(0, "Hi");
		list.addWithPriority(0, ",");
		list.addWithPriority(0, "how");
		list.addWithPriority(1, "are");
		list.addWithPriority(1, "you");
		list.addWithPriority(1, "doing");
		list.addWithPriority(1, "on");
		list.addWithPriority(2, "this");
		list.addWithPriority(3, "228");
		list.addWithPriority(3, "assignment");
		list.addWithPriority(4, "?");
		assertEquals(6, list.sizeWithPriority(0));
		assertEquals(4, list.sizeWithPriority(1));
		assertEquals(1, list.sizeWithPriority(2));
		assertEquals(2, list.sizeWithPriority(3));
		assertEquals(2, list.sizeWithPriority(4));
		/*
		//This for-each loop is here so you can make sure
		//addWithPriority is properly adding to the list.
		for(String s : list)
		{
			System.out.println(s);
		}
		*/
		assertEquals("Alice", list.get(0));
		assertEquals("Bob", list.get(1));
		assertEquals("Carl", list.get(2));
		assertEquals("Hi", list.get(3));
		assertEquals("how", list.get(5));
		assertEquals("are", list.get(6));
		assertEquals("on", list.get(9));
		assertEquals("this", list.get(10));
		assertEquals("228", list.get(11));
		assertEquals("Dave", list.get(13));
		assertEquals("?", list.get(14));
	}

	@Test
	public final void testaddCollection() {
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add("X");
		arrayList.add("Y");
		arrayList.add("Z");
		list.addAll(arrayList);
		assertEquals(7, list.size());
		assertEquals(5, list.indexOf("Y"));
//		assertEquals(4, list.getPriority(5));
//		assertEquals(0, list.getPriority(2));
		list.addAll(1, arrayList);
		assertEquals("Bob", list.get(4));
		assertEquals("Dave", list.get(6));
		assertEquals("Z", list.get(9));
		assertEquals(10, list.size());
//		assertEquals(0, list.getPriority(5));
//		assertEquals(0, list.getPriority(2));
//		assertEquals(4, list.getPriority(6));
//		assertEquals(4, list.getPriority(9));
		
	}

	@Test
	public final void testsetTest() {
//		assertEquals(0, list.getPriority(0));
		list.set(0, "X");
//		assertEquals(0, list.getPriority(0));
		assertEquals(0, list.indexOf("X"));
		assertFalse(list.contains("Alice"));
		assertEquals(4, list.size());
		assertEquals(3, list.sizeWithPriority(0));
		list.set(3, "Carol");
		assertTrue(list.contains("Carol"));
		assertFalse(list.contains("Dave"));
//		assertEquals(4, list.getPriority(3));
		assertEquals(1, list.sizeWithPriority(4));
		
	}

	@Test
	public final void testremoveTest() {
		assertTrue(list.remove("Bob"));
		assertFalse(list.remove("Bob"));
		assertEquals(3, list.size());
//		assertEquals(4, list.getPriority(2));
		assertEquals(2, list.sizeWithPriority(0));
		assertEquals(1, list.sizeWithPriority(4));
		assertEquals("Carl", list.remove(1));
		assertEquals(2, list.size());
//		assertEquals(0, list.getPriority(0));
//		assertEquals(4, list.getPriority(1));
		assertEquals(1, list.sizeWithPriority(0));
		assertEquals(1, list.sizeWithPriority(4));
	}

	/**
	 * Tests clear, addWithPriority, contains, and sizeWithPriority functions.
	 */
	@Test
	public final void testaddTest() {
		list.clear();
		list.addWithPriority(3, "Max");
		list.addWithPriority(4, "Zach");
		assertTrue(list.contains("Zach"));
		assertEquals(0, list.sizeWithPriority(2));
		assertEquals(0, list.sizeWithPriority(1));
		assertEquals(0, list.sizeWithPriority(0));
		assertEquals(1, list.sizeWithPriority(4));
		list.add("John");
		assertEquals(2, list.sizeWithPriority(4));
		assertEquals(0, list.sizeWithPriority(2));
		assertEquals(0, list.sizeWithPriority(1));
		assertEquals(0, list.sizeWithPriority(0));
		assertEquals("Max", list.get(0));
		assertEquals("Zach", list.get(1));
		assertEquals("John", list.get(2));
		/*
		 * This for-each loop is to make sure things are being
		 * added to the list correctly.
		for(String s : list)
		{
			System.out.println(s);
		}
		*/
	}

	//Tests iteratorWithPriority.hasNext(), hasPrevious().
	@Test
	public final void testiteratorHasNextTest() {
		assertTrue(list.iteratorWithPriority(0).hasNext());
		assertFalse(list.iteratorWithPriority(1).hasNext());
		assertFalse(list.iteratorWithPriority(2).hasNext());
		assertFalse(list.iteratorWithPriority(3).hasNext());
		assertTrue(list.iteratorWithPriority(4).hasNext());
		ListIterator<String> iter = list.iteratorWithPriority(2);
		assertFalse(iter.hasNext());
		assertFalse(iter.hasPrevious());
		iter.add("Hi");
		assertFalse(iter.hasNext());
		assertTrue(iter.hasPrevious());
		assertEquals("Hi", iter.previous());
	}

//	/**
//	 * Performs tests with the listIterator functions.
//	 */
//	@Test
//	public final void testbasicIteratorTest() {
//		ListIterator<String> iter = list.listIterator();
//		assertFalse(iter.hasPrevious());
//		assertTrue(iter.hasNext());
//		assertEquals(0, iter.nextIndex());
//		assertEquals(-1, iter.previousIndex());
//		assertEquals(iter.next(), iter.previous());
//		iter.remove();
//		assertEquals(2, list.sizeWithPriority(0));
//		assertEquals(3, list.size());
//		assertFalse(list.contains("Alice"));
//		assertFalse(iter.hasPrevious());
//		assertTrue(iter.next().equals("Bob"));
//		assertEquals(1, iter.nextIndex());
//		iter.add("Billy");
//		assertEquals(2, iter.nextIndex());
//		assertEquals("Billy", iter.previous());
//		iter.next();
//		iter.next();
//		iter.remove();
//		assertTrue(iter.hasNext());
//		assertEquals("Carl", iter.next());
//		assertEquals("Dave", iter.next());
//		iter.remove();
//		assertFalse(list.contains("Dave"));
//		assertEquals(0, list.sizeWithPriority(4));
//	}

	/**
	 * Test listIterator for an empty list.
	 */
	@Test
	public final void testbasicIteratorTest2() {
		list.clear();
		ListIterator<String> iter = list.listIterator();
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
	public final void testadvancedIteratorTest3() {
		ListIterator<String> iter = list.listIterator(4);
		assertFalse(iter.hasNext());
		assertEquals("Dave", iter.previous());
		assertEquals(iter.nextIndex(), list.indexOf("Dave"));
	}

	/**
	 * Tests iteratorWithPriority. Elements with priority are added to the list
	 * to add complexity.
	 */
	@Test
	public final void testiteratorWithPriorityTest() {
		list.addWithPriority(3, "Max");
		list.addWithPriority(4, "Zach");
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add("X");
		arrayList.add("Y");
		arrayList.add("Z");
		list.addAll(3, arrayList);

		assertEquals(4, list.sizeWithPriority(3));
		ListIterator<String> iter = list.iteratorWithPriority(3);
		assertEquals(0, iter.nextIndex());
		assertFalse(iter.hasPrevious());
		assertEquals("X", iter.next());
		iter.next();
		iter.next();
		assertEquals("Max", iter.next());
		assertFalse(iter.hasNext());
		iter.add("Pat");
		assertFalse(iter.hasNext());
		assertEquals("Pat", iter.previous());
		assertEquals(5, list.sizeWithPriority(3));
		assertEquals(4, iter.nextIndex());
	}
	
	@Test
	public final void testgetPriorityTest(){
		list.clear();
		list.addWithPriority(0,"0");
		list.addWithPriority(1, "1");
		list.addWithPriority(1, "1");
		list.addWithPriority(1, "1");
		list.addWithPriority(0, "0");
		list.addWithPriority(4, "4");
		list.addWithPriority(3, "3");
//		assertEquals(0, list.getPriority(0));
//		assertEquals(0, list.getPriority(1));
//		assertEquals(1, list.getPriority(2));
//		assertEquals(1, list.getPriority(3));
//		assertEquals(1, list.getPriority(4));
//		assertEquals(3, list.getPriority(5));
//		assertEquals(4, list.getPriority(6));

	}
}