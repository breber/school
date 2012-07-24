package edu.iastate.cs228.hw3.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import junit.framework.TestCase;

import org.junit.Test;

import edu.iastate.cs228.hw3.PriorityList;

/**
 * Test suite for PriorityList using Strings as the type.
 * 
 * @author Brian Reber
 */
public class PriorityListTest extends TestCase {
	
	private PriorityList<String> list;

	/**
	 * Test method for {@link edu.iastate.cs228.hw3.PriorityList#addWithPriority(int, java.lang.Object)}.
	 */
	@Test
	public final void testAddWithPriority() {
		list = new PriorityList<String>(3);
		list.addWithPriority(1, "Test");
		list.addWithPriority(2, "Brian");
		list.addWithPriority(0, "Reber");
		list.addWithPriority(1, "123456");
		list.addWithPriority(0, "CS228");
		list.addWithPriority(2, "LinkedList");
		list.addWithPriority(0, "ArrayList");

		int i = 0;
		String[] vals = {"Reber", "CS228", "ArrayList", "Test", "123456", "Brian", "LinkedList"};
		ListIterator<String> iter = list.listIterator();
		while (iter.hasNext()) {
			String temp = iter.next();
			if (temp != null) {
				assertTrue(temp.equals(vals[i]));
			}
			i++;
		}
	}
	
	/**
	 * Test method for {@link edu.iastate.cs228.hw3.PriorityList#addWithPriority(int, java.lang.Object)}.
	 */
	@Test
	public final void testAddWithPriorityIteratorStartsAtCorrectNode() {
		list = new PriorityList<String>(3);
		list.addWithPriority(1, "Test");
		list.addWithPriority(2, "Brian");
		list.addWithPriority(0, "Reber");
		list.addWithPriority(1, "123456");
		list.addWithPriority(0, "CS228");
		list.addWithPriority(2, "LinkedList");
		list.addWithPriority(0, "ArrayList");

		ListIterator<String> iter = list.iteratorWithPriority(0);
		assertTrue("Iterator with priority 0 should start at Reber", iter.next().equals("Reber"));
		
		iter = list.iteratorWithPriority(1);
		assertTrue("Iterator with priority 1 should start at Test", iter.next().equals("Test"));
		
		iter = list.iteratorWithPriority(2);
		assertTrue("Iterator with priority 2 should start at Brian", iter.next().equals("Brian"));
	}

	/**
	 * Test method for {@link edu.iastate.cs228.hw3.PriorityList#addWithPriority(int, java.lang.Object)}.
	 */
	@Test
	public final void testAddWithPriorityOutsideOfPrioritySize() {
		boolean fail = false;
		list = new PriorityList<String>(2);
		try {
			list.addWithPriority(-1, "Test");
			fail = true;
		} catch (IllegalArgumentException e) {	} 
		try {
			list.addWithPriority(2, "Brian");
			fail = true;
		} catch (IllegalArgumentException e) {	}
		try {
			list.addWithPriority(3, "Brian");
			fail = true;
		} catch (IllegalArgumentException e) {	}

		assertFalse(fail);
	}

	/**
	 * Test method for {@link edu.iastate.cs228.hw3.PriorityList#removeWithPriority(int)}.
	 */
	@Test
	public final void testRemoveWithPriority() {
		list = new PriorityList<String>(3);
		list.addWithPriority(1, "Test");
		list.addWithPriority(2, "Brian");
		list.addWithPriority(0, "Reber");
		list.addWithPriority(1, "123456");

		assertTrue("Size of Priority 1 should be 2", list.sizeWithPriority(1) == 2);
		list.removeWithPriority(1);
		assertTrue("Size of Priority 1 after removal should be 1", list.sizeWithPriority(1) == 1);

		int i = 0;
		String[] vals = {"Reber", "123456", "Brian"};
		ListIterator<String> iter = list.listIterator();
		while (iter.hasNext()) {
			String temp = iter.next();
			if (temp != null) {
				assertTrue(temp.equals(vals[i]));
			}
			i++;
		}
		
		iter = list.iteratorWithPriority(1);
		assertTrue(iter.next().equals("123456"));
	}

	/**
	 * Test method for {@link edu.iastate.cs228.hw3.PriorityList#removeWithPriority(int)}.
	 */
	@Test
	public final void testRemoveWithPriorityInvalidPriorities() {
		list = new PriorityList<String>(3);
		list.addWithPriority(1, "Test");
		list.addWithPriority(2, "Brian");
		list.addWithPriority(0, "Reber");
		list.addWithPriority(1, "123456");

		boolean fail = false;
		try {
			list.removeWithPriority(-1);
			fail = true;
		} catch (IllegalArgumentException e) {	} 
		try {
			list.removeWithPriority(3);
			fail = true;
		} catch (IllegalArgumentException e) {	}
		try {
			list.removeWithPriority(4);
			fail = true;
		} catch (IllegalArgumentException e) {	}

		assertFalse(fail);
	}

	/**
	 * Test method for {@link edu.iastate.cs228.hw3.PriorityList#removeWithPriority(int)}.
	 */
	@Test
	public final void testRemoveWithPriorityEmptySublist() {
		list = new PriorityList<String>(4);
		list.addWithPriority(1, "Test");
		list.addWithPriority(2, "Brian");
		list.addWithPriority(0, "Reber");
		list.addWithPriority(1, "123456");

		boolean fail = false;
		try {
			list.removeWithPriority(3);
			fail = true;
		} catch (NoSuchElementException e) {	} 

		assertFalse(fail);
	}

	/**
	 * Test method for {@link edu.iastate.cs228.hw3.PriorityList#sizeWithPriority(int)}.
	 */
	@Test
	public final void testSizeWithPriority() {
		list = new PriorityList<String>(4);
		list.addWithPriority(1, "Test");
		list.addWithPriority(2, "Brian");
		list.addWithPriority(0, "Reber");
		list.addWithPriority(1, "123456");

		assertTrue(list.sizeWithPriority(0) == 1);
		assertTrue(list.sizeWithPriority(1) == 2);
		assertTrue(list.sizeWithPriority(2) == 1);
		assertTrue(list.sizeWithPriority(3) == 0);
	}

	/**
	 * Test method for {@link edu.iastate.cs228.hw3.PriorityList#sizeWithPriority(int)}.
	 */
	@Test
	public final void testSizeWithPriorityInvalidPriorities() {
		list = new PriorityList<String>(3);
		list.addWithPriority(1, "Test");
		list.addWithPriority(2, "Brian");
		list.addWithPriority(0, "Reber");
		list.addWithPriority(1, "123456");

		boolean fail = false;
		try {
			list.sizeWithPriority(-1);
			fail = true;
		} catch (IllegalArgumentException e) {	} 
		try {
			list.sizeWithPriority(3);
			fail = true;
		} catch (IllegalArgumentException e) {	}
		try {
			list.sizeWithPriority(4);
			fail = true;
		} catch (IllegalArgumentException e) {	}

		assertFalse(fail);
	}

	/**
	 * Test method for {@link edu.iastate.cs228.hw3.PriorityList#iteratorWithPriority(int)}.
	 */
	@Test
	public final void testIteratorWithPriority() {
		list = new PriorityList<String>(3);
		list.addWithPriority(1, "Test");
		list.addWithPriority(2, "Brian");
		list.addWithPriority(0, "Reber");
		list.addWithPriority(1, "123456");

		ListIterator<String> iter = list.iteratorWithPriority(0);
		assertTrue(iter.hasNext());
		assertFalse(iter.hasPrevious());
		assertTrue(iter.nextIndex() == 0);
		assertTrue(iter.previousIndex() == -1);

		iter.next();

		assertFalse(iter.hasNext());
		assertTrue(iter.hasPrevious());
		assertTrue(iter.nextIndex() == 1);
		assertTrue(iter.previousIndex() == 0);

		try {
			iter.next();
			assertTrue(false);
		} catch (NoSuchElementException e) {
			assertTrue(true);
		}

		//New Priority
		iter = list.iteratorWithPriority(1);
		assertTrue(iter.hasNext());
		assertFalse(iter.hasPrevious());
		assertTrue(iter.nextIndex() == 0);
		assertTrue(iter.previousIndex() == -1);

		iter.next();

		assertTrue(iter.hasNext());
		assertTrue(iter.hasPrevious());
		assertTrue(iter.nextIndex() == 1);
		assertTrue(iter.previousIndex() == 0);

		iter.next();

		assertFalse(iter.hasNext());
		assertTrue(iter.hasPrevious());
		assertTrue(iter.nextIndex() == 2);
		assertTrue(iter.previousIndex() == 1);

		try {
			iter.next();
			assertTrue(false);
		} catch (NoSuchElementException e) {
			assertTrue(true);
		}
	}

	/**
	 * Test method for {@link edu.iastate.cs228.hw3.PriorityList#iteratorWithPriority(int)}.
	 */
	@Test
	public final void testIteratorWithPriorityRemove() {
		String temp = "";
		list = new PriorityList<String>(3);
		list.addWithPriority(1, "Test");
		list.addWithPriority(2, "Brian");
		list.addWithPriority(0, "Reber");
		list.addWithPriority(1, "123456");
		list.addWithPriority(1, "789");

		ListIterator<String> iter = list.iteratorWithPriority(1);
		assertTrue(iter.hasNext());
		assertFalse(iter.hasPrevious());
		assertTrue(iter.nextIndex() == 0);
		assertTrue(iter.previousIndex() == -1);

		temp = iter.next();
		assertTrue(temp.equals("Test"));
		assertTrue(iter.hasNext());
		assertTrue(iter.hasPrevious());
		assertTrue(iter.nextIndex() == 1);
		assertTrue(iter.previousIndex() == 0);

		iter.remove();

		assertTrue(iter.hasNext());
		assertFalse(iter.hasPrevious());
		assertTrue(iter.nextIndex() == 0);
		assertTrue(iter.previousIndex() == -1);

		temp = iter.next();
		assertTrue(temp.equals("123456"));

		assertTrue(iter.hasNext());
		assertTrue(iter.hasPrevious());
		assertTrue(iter.nextIndex() == 1);
		assertTrue(iter.previousIndex() == 0);

		try {
			iter.next();
			iter.next();
			assertTrue(false);
		} catch (NoSuchElementException e) {
			assertTrue(true);
		}
	}
	
	/**
	 * Test method for {@link edu.iastate.cs228.hw3.PriorityList#iteratorWithPriority(int)}.
	 */
	@Test
	public final void testIteratorWithPriorityInvalidPriorities() {
		list = new PriorityList<String>(3);
		list.addWithPriority(1, "Test");
		list.addWithPriority(2, "Brian");
		list.addWithPriority(0, "Reber");
		list.addWithPriority(1, "123456");

		boolean fail = false;
		try {
			list.iteratorWithPriority(-1);
			fail = true;
		} catch (IllegalArgumentException e) {	} 
		try {
			list.iteratorWithPriority(3);
			fail = true;
		} catch (IllegalArgumentException e) {	}
		try {
			list.iteratorWithPriority(4);
			fail = true;
		} catch (IllegalArgumentException e) {	}

		assertFalse(fail);
	}
	
	/**
	 * Test method for {@link edu.iastate.cs228.hw3.PriorityList#getMaxPriority()}.
	 */
	@Test
	public final void testGetMaxPriority() {
		list = new PriorityList<String>(3);

		assertTrue("Max priority should be 2, but is " + list.getMaxPriority(), list.getMaxPriority() == 2);
	}

	/**
	 * Test method for {@link edu.iastate.cs228.hw3.PriorityList#size()}.
	 */
	@Test
	public final void testSize() {
		list = new PriorityList<String>(3);
		list.add("Test");
		list.add("Brian");
		list.add("Reber");
		list.add("123456");

		assertTrue(list.get(0).equals("Test"));
		assertTrue(list.get(1).equals("Brian"));
		assertTrue(list.get(2).equals("Reber"));
		assertTrue(list.get(3).equals("123456"));
	}

	/**
	 * Test method for {@link edu.iastate.cs228.hw3.PriorityList#isEmpty()}.
	 */
	@Test
	public final void testIsEmpty() {
		list = new PriorityList<String>(1);
		assertTrue(list.isEmpty());
		list.add("Test");
		list.add("Brian");
		list.add("Reber");
		list.add("123456");
		assertFalse(list.isEmpty());
	}

	/**
	 * Test method for {@link edu.iastate.cs228.hw3.PriorityList#contains(java.lang.Object)}.
	 */
	@Test
	public final void testContains() {
		list = new PriorityList<String>(1);
		list.add("Test");
		list.add("Brian");
		list.add("Reber");
		list.add("123456");

		assertTrue("List should contain string \"Brian\"", list.contains("Brian"));
		assertFalse("List should not contain string \"Computer\"", list.contains("Computer"));
	}

	/**
	 * Test method for {@link edu.iastate.cs228.hw3.PriorityList#contains(java.lang.Object)}.
	 */
	@Test
	public final void testContainsObjectOfWrongType() {
		list = new PriorityList<String>(1);
		list.add("Test");
		list.add("Brian");
		list.add("Reber");
		list.add("123456");
		list.add(null);

		assertFalse("List does not contain Integer(5)", list.contains(new Integer(5)));
	}

	/**
	 * Test method for {@link edu.iastate.cs228.hw3.PriorityList#contains(java.lang.Object)}.
	 */
	@Test
	public final void testContainsNull() {
		list = new PriorityList<String>(1);
		list.add("Test");
		list.add("Brian");
		list.add("Reber");
		list.add("123456");
		list.add(null);

		assertTrue("Null is in the list", list.contains(null));
	}

	/**
	 * Test method for {@link edu.iastate.cs228.hw3.PriorityList#iterator()}.
	 */
	@Test
	public final void testIterator() {
		list = new PriorityList<String>(3);
		list.add("Test");

		Iterator<String> iter = list.iterator();
		assertTrue(iter.hasNext());
		assertTrue(iter.next().equals("Test"));
		assertFalse(iter.hasNext());
		
		list.add("Brian");
		list.add("Reber");

		iter = list.iterator();
		assertTrue(iter.hasNext());
		assertTrue(iter.next().equals("Test"));
		iter.remove();
		assertTrue(iter.next().equals("Brian"));

		iter = list.iterator();
		assertTrue(iter.hasNext());
		assertTrue(iter.next().equals("Brian"));
	}
	
	/**
	 * Test method for {@link edu.iastate.cs228.hw3.PriorityList#add(java.lang.Object)}.
	 */
	@Test
	public final void testAddE() {
		list = new PriorityList<String>(3);
		list.add("Test");

		ListIterator<String> iter = list.iteratorWithPriority(0);
		assertTrue("Adding item to empty list should set it to priority 0", iter.next().equals("Test"));

		list.addWithPriority(2, "Brian");
		list.add("Reber");

		iter = list.iteratorWithPriority(2);
		assertTrue("Adding item to list with the last element priority = 2 should set it to priority 2", iter.next().equals("Brian"));
		assertTrue("Adding item to list with the last element priority = 2 should set it to priority 2", iter.next().equals("Reber"));

		list.addWithPriority(1, "Test1");
		list.add("Final");

		iter = list.iteratorWithPriority(2);
		assertTrue("Adding item to list with the last element priority = 2 should set it to priority 2", iter.next().equals("Brian"));
		assertTrue("Adding item to list with the last element priority = 2 should set it to priority 2", iter.next().equals("Reber"));
		assertTrue("Adding item to list with the last element priority = 2 should set it to priority 2", iter.next().equals("Final"));
	}

	/**
	 * Test method for {@link edu.iastate.cs228.hw3.PriorityList#remove(java.lang.Object)}.
	 */
	@Test
	public final void testRemoveObject() {
		list = new PriorityList<String>(3);
		list.add("Test");
		list.add("Brian");
		list.add("Reber");
		list.add("CS228");
		list.add("123456");
		list.add("ArrayList");
		
		boolean temp = list.remove("Brian");
		
		assertTrue("Revmoving item \"Reber\" should be true, but is " + temp, temp);
		assertTrue(list.get(2).equals("CS228"));
		
		temp = list.remove("ArrayList");
		assertTrue("Revmoving item \"ArrayList\" should be true, but is " + temp, temp);
		
		temp = list.remove("PriorityList");
		assertFalse("Revmoving item \"PriorityList\" should be false, but is " + temp, temp);
	}
	
	/**
	 * Test method for {@link edu.iastate.cs228.hw3.PriorityList#remove(java.lang.Object)}.
	 */
	@Test
	public final void testRemoveObjectWrongType() {
		list = new PriorityList<String>(3);
		list.add("Test");
		list.add("Brian");
		list.add("Reber");
		list.add("CS228");
		list.add("123456");
		list.add("ArrayList");
		
		boolean temp = list.remove(new Integer(5));
		
		assertFalse("Revmoving Integer(5) should be false, but is " + temp, temp);
		assertTrue(list.get(2).equals("Reber"));
	}

	/**
	 * Test method for {@link edu.iastate.cs228.hw3.PriorityList#addAll(java.util.Collection)}.
	 */
	@Test
	public final void testAddAllCollectionOfQextendsE() {
		String[] expected = {"Test", "Brian", "Reber", "123456", "ArrayList", "CS228", "LinkedList"};
		list = new PriorityList<String>(1);
		list.add("Test");
		list.add("Brian");
		list.add("Reber");
		list.add("123456");

		Collection<String> strings = new ArrayList<String>();
		strings.add("ArrayList");
		strings.add("CS228");
		strings.add("LinkedList");

		list.addAll(strings);

		int i = 0;
		ListIterator<String> iter = list.listIterator();
		while (iter.hasNext()) {
			String temp = iter.next();
			if (temp != null) {
				assertTrue(temp.equals(expected[i]));
			}
			i++;
		}
	}

	/**
	 * Test method for {@link edu.iastate.cs228.hw3.PriorityList#addAll(int, java.util.Collection)}.
	 */
	@Test
	public final void testAddAllIntCollectionOfQextendsE() {
		String[] expected = {"Test", "Brian", "ArrayList", "CS228", "LinkedList", "Reber", "123456"};
		list = new PriorityList<String>(1);
		list.add("Test");
		list.add("Brian");
		list.add("Reber");
		list.add("123456");

		Collection<String> strings = new ArrayList<String>();
		strings.add("ArrayList");
		strings.add("CS228");
		strings.add("LinkedList");

		list.addAll(2, strings);

		int i = 0;
		ListIterator<String> iter = list.listIterator();
		while (iter.hasNext()) {
			String temp = iter.next();
			if (temp != null) {
				assertTrue(temp.equals(expected[i]));
			}
			i++;
		}
	}
	
	/**
	 * Test method for {@link edu.iastate.cs228.hw3.PriorityList#addAll(int, java.util.Collection)}.
	 */
	@Test
	public final void testAddAllIntCollectionOfQextendsEExtremeIndex() {
		list = new PriorityList<String>(1);
		list.add("Test");
		list.add("Brian");
		list.add("Reber");
		list.add("123456");

		Collection<String> strings = new ArrayList<String>();
		strings.add("ArrayList");
		strings.add("CS228");
		strings.add("LinkedList");
		
		try {
			list.addAll(-1, strings);
			assertTrue("Index -1 should not insert item in array", false);
		} catch (IndexOutOfBoundsException e) {
			assertTrue(true);
		}
		try {
			list.addAll(5, strings);
			assertTrue("Index 5 should not insert item in array", false);
		} catch (IndexOutOfBoundsException e) {
			assertTrue(true);
		}
		try {
			list.addAll(4, strings);
			assertTrue("Index 4 (list.size()) should insert item in array", list.size() == 7);
		} catch (Exception e) {
			assertTrue(false);
		}
	}

	/**
	 * Test method for {@link edu.iastate.cs228.hw3.PriorityList#clear()}.
	 */
	@Test
	public final void testClear() {
		list = new PriorityList<String>(1);
		assertTrue(list.isEmpty());
		list.add("Test");
		list.add("Brian");
		list.add("Reber");
		list.add("123456");
		assertFalse(list.isEmpty());
		list.clear();
		assertTrue(list.isEmpty());
	}

	/**
	 * Test method for {@link edu.iastate.cs228.hw3.PriorityList#get(int)}.
	 */
	@Test
	public final void testGet() {
		list = new PriorityList<String>(1);
		list.add("Test");
		list.add("Brian");
		list.add("Reber");
		list.add("123456");
		list.add("CS228");
		list.add("LinkedList");
		list.add("ArrayList");
		list.add("PriorityList");

		String[] strings = {"Test", "Brian", "Reber", "123456", "CS228", "LinkedList", "ArrayList", "PriorityList"};
		
		for (int i = 0; i < list.size(); i++) {
			String temp = list.get(i);
			assertTrue(temp.equals(strings[i]));
		}
	}

	/**
	 * Test method for {@link edu.iastate.cs228.hw3.PriorityList#set(int, java.lang.Object)}.
	 */
	@Test
	public final void testSet() {
		list = new PriorityList<String>(1);
		list.add("Test");
		list.add("Brian");
		list.add("Reber");
		list.add("123456");

		list.set(1, "CS228");
		
		assertTrue(list.get(1).equals("CS228"));
		
		list.set(0, "LinkedList");
		assertTrue(list.get(0).equals("LinkedList"));
	}
	
	/**
	 * Test method for {@link edu.iastate.cs228.hw3.PriorityList#set(int, java.lang.Object)}.
	 */
	@Test
	public final void testSetExtremeIndex() {
		list = new PriorityList<String>(1);
		list.add("Test");
		list.add("Brian");
		list.add("Reber");
		list.add("123456");
		
		try {
			list.set(-1, "CS228");
			assertTrue("Index -1 should not set item in array", false);
		} catch (IndexOutOfBoundsException e) {
			assertTrue(true);
		}
		try {
			list.set(5, "CS228");
			assertTrue("Index 5 should not set item in array", false);
		} catch (IndexOutOfBoundsException e) {
			assertTrue(true);
		}
		try {
			list.set(4, "CS228");
			assertTrue("Index 4 (list.size()) should not set item in array", false);
		} catch (IndexOutOfBoundsException e) {
			assertTrue(true);
		}
	}

	/**
	 * Test method for {@link edu.iastate.cs228.hw3.PriorityList#add(int, java.lang.Object)}.
	 */
	@Test
	public final void testAddIntE() {
		list = new PriorityList<String>(3);
		list.add("A");
		list.add("B");
		list.add("C");
		list.addWithPriority(1, "D");
		list.add(2, "X");
		
		ListIterator<String> iter = list.iteratorWithPriority(0);
		assertTrue(iter.next().equals("A"));
		assertTrue(iter.next().equals("B"));
		assertTrue(iter.next().equals("X"));
		assertTrue(iter.next().equals("C"));
		
		
		list = new PriorityList<String>(3);
		list.add("A");
		list.add("B");
		list.add("C");
		list.addWithPriority(1, "D");
		list.add(3, "X");
		
		iter = list.iteratorWithPriority(1);
		assertTrue(iter.next().equals("X"));
		assertTrue(iter.next().equals("D"));
	}
	
	/**
	 * Test method for {@link edu.iastate.cs228.hw3.PriorityList#add(int, java.lang.Object)}.
	 */
	@Test
	public final void testAddIntEZeroIndex() {
		list = new PriorityList<String>(3);
		list.add("A");
		list.add("B");
		list.add("C");
		list.addWithPriority(1, "D");
		list.add(0, "X");
		
		ListIterator<String> iter = list.iteratorWithPriority(0);
		assertTrue(iter.next().equals("X"));
		assertTrue(iter.next().equals("A"));
		assertTrue(iter.next().equals("B"));
		assertTrue(iter.next().equals("C"));
		
		
		list = new PriorityList<String>(3);
		list.addWithPriority(1, "A");
		list.add(0, "X");
		
		iter = list.iteratorWithPriority(1);
		assertTrue(iter.next().equals("X"));
	}
	
	/**
	 * Test method for {@link edu.iastate.cs228.hw3.PriorityList#add(int, java.lang.Object)}.
	 */
	@Test
	public final void testAddIntEExtremeIndex() {
		list = new PriorityList<String>(3);
		list.add("A");
		list.add("B");
		list.add("C");
		list.addWithPriority(1, "D");
		try {
			list.add(5, "X");
			fail("Adding at index greater than size should fail");
		} catch (IndexOutOfBoundsException e) {
			assertTrue(true);
		}
		
		try {
			list.add(-1, "X");
			fail("Adding at index less than 0 should fail");
		} catch (IndexOutOfBoundsException e) {
			assertTrue(true);
		}
		
		try {
			list.add(4, "X");
			assertTrue(true);
		} catch (IndexOutOfBoundsException e) {
			fail("Adding at index = size() should work");
		}
	}

	/**
	 * Test method for {@link edu.iastate.cs228.hw3.PriorityList#remove(int)}.
	 */
	@Test
	public final void testRemoveInt() {
		list = new PriorityList<String>(3);
		list.add("Test");
		list.add("Brian");
		list.add("Reber");
		list.add("CS228");
		list.add("123456");
		list.add("ArrayList");
		
		String temp = list.remove(2);
		
		assertTrue("Revmoving item at index 1 should be \"Reber\", but is " + temp, temp.equals("Reber"));
		assertTrue(list.get(2).equals("CS228"));
		
		temp = list.remove(4);
		
		assertTrue("Revmoving item at index 4 should be \"ArrayList\", but is " + temp, temp.equals("ArrayList"));
	}
	
	/**
	 * Test method for {@link edu.iastate.cs228.hw3.PriorityList#remove(int)}.
	 */
	@Test
	public final void testRemoveIntExtremeIndex() {
		list = new PriorityList<String>(3);
		list.add("Test");
		list.add("Brian");
		list.add("Reber");
		list.add("CS228");
		list.add("123456");
		list.add("ArrayList");
		
		try {
			list.remove(-1);
			fail("Should not be able to remove item at index -1");
		} catch (IndexOutOfBoundsException e) {
			assertTrue(true);
		}
		
		try {
			list.remove(list.size());
			fail("Should not be able to remove item at index size()");
		} catch (IndexOutOfBoundsException e) {
			assertTrue(true);
		}
		
		try {
			list.remove(list.size() + 1);
			fail("Should not be able to remove item at index size() + 1");
		} catch (IndexOutOfBoundsException e) {
			assertTrue(true);
		}
	}

	/**
	 * Test method for {@link edu.iastate.cs228.hw3.PriorityList#indexOf(java.lang.Object)}.
	 */
	@Test
	public final void testIndexOf() {
		list = new PriorityList<String>(3);
		list.add("Test");
		list.add("Brian");
		list.add("Reber");
		
		assertTrue(list.indexOf("Test") == 0);
		assertTrue(list.indexOf("Brian") == 1);
		assertTrue(list.indexOf("Reber") == 2);
		
		assertTrue(list.indexOf("NOTINLIST") == -1);
	}
	
	/**
	 * Test method for {@link edu.iastate.cs228.hw3.PriorityList#indexOf(java.lang.Object)}.
	 */
	@Test
	public final void testIndexOfWrongType() {
		list = new PriorityList<String>(3);
		list.add("Test");
		list.add("Brian");
		list.add("Reber");
		
		assertTrue("Integer(5) should not be in list of Strings", list.indexOf(new Integer(5)) == -1);
	}

	/**
	 * Test method for {@link edu.iastate.cs228.hw3.PriorityList#listIterator()}.
	 */
	@Test
	public final void testListIteratorPrevious() {
		list = new PriorityList<String>(3);
		list.add("Test");
		list.add("Brian");
		list.add("Reber");
		
		ListIterator<String> iter = list.listIterator();
		iter.next();
		iter.next();
		
		assertTrue(iter.hasPrevious());
		assertTrue(iter.previous().equals("Brian"));
		
		iter.remove();
		assertTrue(iter.hasNext());
		assertTrue(iter.next().equals("Reber"));
	}
	
	/**
	 * Test method for {@link edu.iastate.cs228.hw3.PriorityList#listIterator()}.
	 */
	@Test
	public final void testListIteratorPreviousSet() {
		list = new PriorityList<String>(3);
		list.add("Test");
		list.add("Brian");
		list.add("Reber");
		
		ListIterator<String> iter = list.listIterator();
		iter.next();
		iter.next();
		
		assertTrue(iter.hasPrevious());
		assertTrue(iter.previous().equals("Brian"));
		
		iter.set("BRIAN");
		assertTrue(iter.next().equals("BRIAN"));
	}

	/**
	 * Test method for {@link edu.iastate.cs228.hw3.PriorityList#listIterator()}.
	 */
	@Test
	public final void testListIteratorNextSet() {
		list = new PriorityList<String>(3);
		list.add("Test");
		list.add("Brian");
		list.add("Reber");
		
		ListIterator<String> iter = list.listIterator();
		iter.next();
		iter.next();
		
		iter.set("BRIAN");
		assertTrue(iter.previous().equals("BRIAN"));
	}
	
	/**
	 * Test method for {@link edu.iastate.cs228.hw3.PriorityList#listIterator()}.
	 */
	@Test
	public final void testListIteratorSetIllegalState() {
		list = new PriorityList<String>(3);
		list.add("Test");
		
		ListIterator<String> iter = list.listIterator();
		
		try {
			iter.set("FAIL");
			fail("Should not be able to set before next or previous called");
		} catch (IllegalStateException e) {
			assertTrue(true);
		}
	}
	
	/**
	 * Test method for {@link edu.iastate.cs228.hw3.PriorityList#addWithPriority(int, java.lang.Object)}.
	 */
	@Test
	public final void testListIteratorAdd() {
		list = new PriorityList<String>(4);
		list.addWithPriority(0, "A");
		list.addWithPriority(0, "B");
		list.addWithPriority(0, "C");
		list.addWithPriority(1, "D");
		list.addWithPriority(3, "E");
		list.addWithPriority(3, "F");

		int i = 0;
		String[] vals = {"A", "B", "C", "X", "D", "E", "F"};
		
		ListIterator<String> iter = list.listIterator();
		iter.next();
		iter.next();
		iter.next();
		
		iter.add("X");
		
		iter = list.listIterator();
		
		while (iter.hasNext()) {
			String temp = iter.next();
			if (temp != null) {
				assertTrue("iter.next() should be " + vals[i] + " but was " + temp, temp.equals(vals[i]));
			}
			i++;
		}
		
		iter = list.iteratorWithPriority(0);
		i = 0;
		while (iter.hasNext()) {
			String temp = iter.next();
			if (temp != null) {
				assertTrue("iter.next() should be " + vals[i] + " but was " + temp, temp.equals(vals[i]));
			}
			i++;
		}
		
		iter = list.iteratorWithPriority(1);
		i = 4;
		while (iter.hasNext()) {
			String temp = iter.next();
			if (temp != null) {
				assertTrue("iter.next() should be " + vals[i] + " but was " + temp, temp.equals(vals[i]));
			}
			i++;
		}
	}
	
	/**
	 * Test method for {@link edu.iastate.cs228.hw3.PriorityList#listIterator(int)}.
	 */
	@Test
	public final void testListIteratorInt() {
		list = new PriorityList<String>(3);
		list.add("Test");
		list.add("Brian");
		list.add("Reber");
		list.add("CS228");
		list.add("123456");
		list.add("ArrayList");
		
		ListIterator<String> iter = list.listIterator(3);
		assertTrue(iter.hasNext());
		assertTrue(iter.next().equals("CS228"));
	}
	
	/**
	 * Test method for {@link edu.iastate.cs228.hw3.PriorityList#listIterator(int)}.
	 */
	@Test
	public void testListIteratorIntExtremeIndex() {
		list = new PriorityList<String>(3);
		list.add("Test");
		list.add("Brian");
		list.add("Reber");
		list.add("CS228");
		list.add("123456");
		list.add("ArrayList");
		
		ListIterator<String> iter = null;
		
		try {
			iter = list.listIterator(-1);
			fail("Should not be able to create iterator starting at index -1");
		} catch (IndexOutOfBoundsException e) {
			assertTrue(true);
		}
		
		try {
			iter = list.listIterator(7);
			fail("Should not be able to create iterator starting at index 7");
		} catch (IndexOutOfBoundsException e) {
			assertTrue(true);
		}
		
		iter = list.listIterator(6);
		assertFalse(iter.hasNext());
		assertEquals(iter.previous(), "ArrayList");
	}
	
	/**
	 * Test method for {@link edu.iastate.cs228.hw3.PriorityList#listIterator(int)}.
	 */
	@Test
	public void testListIteratorIntExtremeIndexAddIterator() {
		list = new PriorityList<String>(3);
		ListIterator<String> iter = list.listIterator();
		
		iter.add("Test");
		iter.add("Brian");
		iter.add("Reber");
		iter.add("CS228");
		iter.add("123456");
		iter.add("ArrayList");
		
		iter = list.listIterator(6);
		assertFalse(iter.hasNext());
		assertEquals(iter.previous(), "ArrayList");
	}
	
	/**
	 * Test method for {@link edu.iastate.cs228.hw3.PriorityList#listIterator(int)}.
	 */
	@Test
	public void testListIteratorIntExtremeIndexAddInt() {
		list = new PriorityList<String>(3);
		ListIterator<String> iter = null;
		list.add("Test");
		list.add(0, "Brian");
		list.add("Reber");
		list.add(3, "CS228");
		list.add(2, "123456");
		list.add(4, "ArrayList");
		
		iter = list.listIterator(6);
		assertFalse(iter.hasNext());
		assertEquals(iter.previous(), "CS228");
	}
	
	/**
	 * Test method for {@link edu.iastate.cs228.hw3.PriorityList#listIterator(int)}.
	 */
	@Test
	public void testListIteratorIntExtremeIndexRemove() {
		list = new PriorityList<String>(3);
		list.add("Test");
		list.add("Brian");
		list.add("Reber");
		list.add("CS228");
		list.add("123456");
		list.add("ArrayList");
		
		ListIterator<String> iter = null;		
		iter = list.listIterator(3);
		assertTrue(iter.next().equals("CS228"));
		assertTrue(iter.previous().equals("CS228"));
		iter.remove();
		
		String[] vals = {"Test", "Brian", "Reber", "123456", "ArrayList"};
		int i = 0;
		Iterator<String> iter1 = list.iterator();
		while (iter1.hasNext()) {
			String temp = iter1.next();
			if (temp != null) {
				assertTrue("iter1.next() should be " + vals[i] + " but was " + temp, temp.equals(vals[i]));
			}
			i++;
		}
	}
	
	/**
	 * Test method for {@link edu.iastate.cs228.hw3.PriorityList#listIterator(int)}.
	 */
	@Test
	public void testListIteratorIntExtremeIndexRemoveEndList() {
		list = new PriorityList<String>(3);
		list.add("Test");
		list.add(0, "Brian");
		list.add("Reber");
		list.add(3, "CS228");
		list.add(2, "123456");
		list.add(4, "ArrayList");
		
		ListIterator<String> iter = list.listIterator(6);
		assertFalse(iter.hasNext());
		try {
			iter.remove();
			fail("Should not be able to call remove on an iterator before calling add or remove");
		} catch (IllegalStateException e) {
			assertTrue(true);
		}
		
		iter.previous();
		iter.remove();
		
		String[] vals = new String[]{"Brian", "Test", "123456", "Reber", "ArrayList"};
		int i = 0;
		Iterator<String> iter1 = list.iterator();
		while (iter1.hasNext()) {
			String temp = iter1.next();
			if (temp != null) {
				assertTrue("iter1.next() should be " + vals[i] + " but was " + temp, temp.equals(vals[i]));
			}
			i++;
		}
	}
}