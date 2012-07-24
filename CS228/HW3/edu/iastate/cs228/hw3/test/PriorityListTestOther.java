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
 * @author Anid Monsur
 *
 */
public class PriorityListTestOther extends TestCase {
	PriorityList<String> list;

	@Before
	public final void setUp() throws Exception {
		list = new PriorityList<String>(5);
		list.add("Alice");
		list.add("Bob");
		list.add("Carl");
		list.addWithPriority(4, "Dave");
	}

	@After
	public final void tearDown() throws Exception {
		list = null;
	}

	@Test
	public final void testgetTest() {
		assertEquals("Carl", list.get(2));
	}

	@Test
	public final void testcontainsTest() {
		assertTrue(list.contains("Bob"));
		assertFalse(list.contains("Alex"));
		assertFalse(list.contains(new Integer(5)));
		assertFalse(list.contains(null));
		list.add(null);
		assertTrue(list.contains(null));
	}

	@Test
	public final void testindexOfTest() {
		assertEquals(0, list.indexOf("Alice"));
		assertEquals(2, list.indexOf("Carl"));
		assertEquals(-1, list.indexOf("Jeff"));
	}

	@Test
	public final void testclearAndSizeTest() {
		assertFalse(list.isEmpty());
		assertEquals(4, list.size());
		list.clear();
		assertTrue(list.isEmpty());
		assertEquals(0, list.size());
	}

	@Test
	public final void testmaxTest() {
		assertEquals(4, list.getMaxPriority());
	}

	@Test
	public final void testsizePriorityTest() {
		assertEquals(3, list.sizeWithPriority(0));
		assertEquals(0, list.sizeWithPriority(1));
		assertEquals(0, list.sizeWithPriority(2));
		assertEquals(0, list.sizeWithPriority(3));
		assertEquals(1, list.sizeWithPriority(4));
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
		list.addAll(4, arrayList);
		assertEquals("X", list.get(4));
		assertEquals(10, list.size());
	}

	@Test
	public final void testsetTest() {
		list.set(0, "X");
		assertEquals(0, list.indexOf("X"));
		assertFalse(list.contains("Alice"));
		assertEquals(4, list.size());
		list.set(2, "Carol");
		assertTrue(list.contains("Carol"));
		assertFalse(list.contains("Carl"));
	}

	@Test
	public final void testremoveTest() {
		assertTrue(list.remove("Bob"));
		assertFalse(list.remove("Bob"));
		assertEquals(3, list.size());
		assertEquals("Carl", list.remove(1));
		assertEquals(2, list.size());
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
		assertEquals(1, list.sizeWithPriority(4));
		list.add("John");
		assertEquals(2, list.sizeWithPriority(4));
	}

	@Test
	public final void testiteratorHasNextTest() {
		assertTrue(list.iteratorWithPriority(0).hasNext());
		assertFalse(list.iteratorWithPriority(1).hasNext());
		assertFalse(list.iteratorWithPriority(2).hasNext());
		assertFalse(list.iteratorWithPriority(3).hasNext());
		assertTrue(list.iteratorWithPriority(4).hasNext());
	}

	/**
	 * Performs tests with the listIterator functions.
	 */
//	@Test
//	public final void testbasicIteratorTest() {
//		ListIterator<String> iter = list.listIterator();
//		assertFalse(iter.hasPrevious());
//		assertTrue(iter.hasNext());
//		assertEquals(0, iter.nextIndex());
//		assertEquals(-1, iter.previousIndex());
//		assertEquals(iter.next(), iter.previous());
//		iter.remove();
//		assertEquals(3, list.size());
//		assertFalse(list.contains("Alice"));
//		assertFalse(iter.hasPrevious());
//		assertEquals(0, iter.nextIndex());
//		assertTrue(iter.next().equals("Bob"));
//		assertEquals(1, iter.nextIndex());
//		iter.add("Billy");
//		assertEquals(2, iter.nextIndex());
//		assertEquals("Billy", iter.previous());
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
}